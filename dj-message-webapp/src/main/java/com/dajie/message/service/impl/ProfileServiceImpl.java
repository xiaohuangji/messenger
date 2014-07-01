package com.dajie.message.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dajie.common.config.ConfigurationManager;
import com.dajie.common.email.enums.EmailCategoryEnum;
import com.dajie.common.email.model.EmailEntry;
import com.dajie.common.email.service.EmailService;
import com.dajie.common.framework.WebHolder;
import com.dajie.corp.api.enums.CorpInfoItem;
import com.dajie.corp.api.service.CorpService;
import com.dajie.corp.enums.common.CorpResult;
import com.dajie.corp.enums.common.SourceTypeEnum;
import com.dajie.corp.info.model.CorpBase;
import com.dajie.message.constants.returncode.AccountResultCode;
import com.dajie.message.constants.returncode.CommonResultCode;
import com.dajie.message.dao.BlackListDAO;
import com.dajie.message.dao.UserEmailVerifyDAO;
import com.dajie.message.dao.UserLabelDAO;
import com.dajie.message.dao.UserLabelLikeDAO;
import com.dajie.message.dao.UserStatisticsDAO;
import com.dajie.message.mcp.passport.AES;
import com.dajie.message.mcp.passport.AzDGCrypt;
import com.dajie.message.model.MCPInteger;
import com.dajie.message.model.user.LabelView;
import com.dajie.message.model.user.ProfileView;
import com.dajie.message.model.user.SendEmailResult;
import com.dajie.message.model.user.UserBase;
import com.dajie.message.model.user.UserCareer;
import com.dajie.message.model.user.UserEducation;
import com.dajie.message.model.user.UserEmailVerify;
import com.dajie.message.model.user.UserLabel;
import com.dajie.message.model.user.VerificationView;
import com.dajie.message.service.IJobService;
import com.dajie.message.service.IProfileService;
import com.dajie.message.service.IPushService;
import com.dajie.message.service.IUserProfileService;
import com.dajie.message.service.mq.GoudaVerifyEvent;
import com.dajie.message.service.mq.GoudaVerifyType;
import com.dajie.message.service.worker.GoudaVerifyPutter;
import com.dajie.message.util.AccountFieldValidator;
import com.dajie.message.util.EmojiFilterUtil;
import com.dajie.message.util.StringUtil;
import com.dajie.word.filter.model.FilterWord.FilterLevel;
import com.dajie.word.filter.service.KeywordFilterService;

/**
 * component name use messageProfileService, "profileService" used by
 * 
 * @see{com.dajie.core.profile.service.ProfileService bean
 * 
 * @author John
 * 
 */
@Component("messageProfileService")
public class ProfileServiceImpl implements IProfileService {

	private static Logger logger = LoggerFactory
			.getLogger(ProfileServiceImpl.class);

	private static final int MYSELF = 1;
	private static final int FRIEND = 2;

	// 大街帐号 goudajob@163.com 密码112233 uid30154278
	public static final int GOUDA_UID = 30154278;

	private static final int EMAIL_LIMIT = 10;

	private static final String API_DOMAIN;

	private static final String env = ConfigurationManager.getInstance()
			.getEnvName();

	static {
		if (env.equalsIgnoreCase("dev")) {
			API_DOMAIN = "http://192.168.27.47:8556";
		} else if (env.equalsIgnoreCase("test")) {
			API_DOMAIN = "http://192.168.27.57:8556";
		} else if (env.equalsIgnoreCase("pre_release")) {
			API_DOMAIN = "http://10.10.64.105:8556";
		} else {
			API_DOMAIN = "http://www.goudajob.com";
		}
	}

	private static final String VERIFY_API_URL = API_DOMAIN + "/mail/check";

	private static final int LABEL_LENGTH_LIMIT = 400;

	private static final int LABEL_LENGTH_LIMIT_ERROR = -2;

	private static final int CONTENT_SENSITIVE = -3;

	@Autowired
	private UserStatisticsDAO userStatisticsDAO;

	@Autowired
	private UserLabelDAO userLabelDAO;

	@Autowired
	private UserLabelLikeDAO userLabelLikeDAO;

	@Autowired
	private BlackListDAO blackListDAO;

	@Autowired
	private UserEmailVerifyDAO userEmailVerifyDAO;

	@Autowired
	private IUserProfileService userProfileService;

	@Autowired
	private CorpService corpService;

	@Autowired
	private IJobService jobService;

	@Autowired
	private IPushService pushService;

	@Autowired
	private KeywordFilterService keywordFilterService;

	@Autowired
	private GoudaVerifyPutter goudaVerifyPutter;

	@Override
	public ProfileView get(int _userId, int hostId) {
		ProfileView userProfile = new ProfileView();
		UserBase userBase = userProfileService.getUserBase(hostId);
		UserEducation userEducation = userProfileService
				.getUserEducation(hostId);
		UserCareer userCareer = userProfileService.getUserCareer(hostId);
		List<UserLabel> ubList = userProfileService.getLabels(hostId);

		List<LabelView> labels = new ArrayList<LabelView>();

		for (UserLabel userLabel : ubList) {
			LabelView view = new LabelView(userLabel);
			view.setLikeCounts(userLabelLikeDAO.getLikeCounts(view.getId()));
			view.setIsliked(userLabelLikeDAO.isLiked(_userId, view.getId()));
			labels.add(view);
		}
		userProfile.setLabels(labels);

		int blocked = blackListDAO.isBlocked(hostId, _userId);
		int block = blackListDAO.isBlocked(_userId, hostId);
		userProfile.setBlocked(2 * block + blocked);

		if (userBase != null) {
			userProfile.setUserId(userBase.getUserId());
			userProfile.setName(userBase.getName());
			userProfile.setGender(userBase.getGender());
			userProfile.setAvatar(userBase.getAvatar());
			userProfile.setAvatarMask(userBase.getAvatarMask());
			userProfile.setEmail(userBase.getEmail());
			userProfile.setMobile(userBase.getMobile());
		}

		if (_userId == hostId) {
			userProfile.setRelationship(MYSELF);
		} else if (userProfileService.isFriend(_userId, hostId)) {
			userProfile.setRelationship(FRIEND);
		} else { // Hide origin picture
			userProfile.setAvatar(null);
		}

		if (userEducation != null) {
			userProfile.setMajor(userEducation.getMajor());
			userProfile.setSchool(userEducation.getSchool());
			userProfile.setDegree(userEducation.getDegree());
		}
		if (userCareer != null) {
			userProfile.setPositionName(userCareer.getPositionName());
			userProfile.setPositionType(userCareer.getPositionType());
			userProfile.setCorpName(userCareer.getCorpName());
			Integer verification = userCareer.getVerification();
			if ((verification != null && verification == 1)
					|| _userId != hostId) {
				userProfile.setVerification(verification);
			} else {
				VerificationView view = getLatestVerification(hostId);
				userProfile.setVerification(view.getVerification() < 0 ? 0 : view.getVerification());
			}
			userProfile.setIndustry(userCareer.getIndustry());
			userProfile.setExperience(userCareer.getExperience());
			userProfile.setEducation(userCareer.getEducation());
		}
		incProfileVisit(hostId);
		return userProfile;
	}

	/**
	 * TODO Use redisCache to increment
	 */
	private void incProfileVisit(int hostId) {
		synchronized (this) {
			if (userStatisticsDAO.incProfileVisit(hostId) == 1) {
				return;
			} else {
				userStatisticsDAO.insertStatistics(hostId, new Date());
				userStatisticsDAO.incProfileVisit(hostId);
			}
		}
	}

	@Override
	public int modifyName(int _userId, String name) {
		// 空格符处理
		name = StringUtil.trim(name);
		/**
		 * check input name
		 */
		boolean isNameValid = AccountFieldValidator.nameValidate(name);
		if (!isNameValid) {
			logger.info("modify name:" + name);
			return AccountResultCode.PARAM_NAME_INVALID;
		}
		boolean isKeywordValid = keywordFilterService.checkByLevel(name,
				FilterLevel.FIRST);
		if (!isKeywordValid) {
			logger.info("modify name contains sensitive word: " + name);
			return AccountResultCode.PARAM_NAME_SENSITIVE;
		}

		return userProfileService.updateName(_userId, name) ? CommonResultCode.OP_SUCC
				: CommonResultCode.OP_FAIL;
	}

	@Override
	public int modifyGender(int _userId, int gender) {
		return userProfileService.updateGender(_userId, gender) ? CommonResultCode.OP_SUCC
				: CommonResultCode.OP_FAIL;
	}

	@Override
	public int modifyAvatar(int _userId, String avatar, String avatarMask) {
		return userProfileService.updateAvatar(_userId, avatar, avatarMask) ? CommonResultCode.OP_SUCC
				: CommonResultCode.OP_FAIL;
	}

	@Override
	public int modifyEducation(int _userId, String major, String school,
			int degree) {

		boolean daoResult = false;

		boolean isKeywordValid = keywordFilterService.checkByLevel(
				StringUtils.defaultString(major), FilterLevel.FIRST)
				&& keywordFilterService.checkByLevel(
						StringUtils.defaultString(school), FilterLevel.FIRST);
		if (!isKeywordValid) {
			logger.info("modify major or school contains sensitive word: "
					+ school + ", " + major);
			return AccountResultCode.PARAM_NAME_SENSITIVE;
		}

		/**
		 * To do optimization: 1. Load school_label table to a map on start 2.
		 * using insert on duplicate key
		 * 
		 */
		String label = userProfileService.getSchoolLabel(school);

		UserEducation oldUserEducation = userProfileService
				.getUserEducation(_userId);
		UserEducation newUserEducation = new UserEducation(_userId, major,
				school, label, degree);
		if (oldUserEducation == null) {
			daoResult = userProfileService
					.createUserEducation(newUserEducation);
		} else {
			if (!StringUtils.defaultString(oldUserEducation.getSchool())
					.equals(school)
					|| !StringUtils.defaultString(oldUserEducation.getMajor())
							.equals(major)) {
				userProfileService.clearUserEducationAudit(_userId);
			}
			daoResult = userProfileService
					.updateUserEducation(newUserEducation);
		}
		return daoResult ? CommonResultCode.OP_SUCC : CommonResultCode.OP_FAIL;

	}

	@Override
	public int modifyCareer(int _userId, String positionName, int positionType,
			String corpName, int corpId, int industry, int experience,
			int education) {

		boolean daoResult = false;

		boolean isKeywordValid = keywordFilterService.checkByLevel(
				StringUtils.defaultString(positionName), FilterLevel.FIRST)
				&& keywordFilterService.checkByLevel(
						StringUtils.defaultString(corpName), FilterLevel.FIRST);
		if (!isKeywordValid) {
			logger.info("modify corpName or positionName contains sensitive word: "
					+ corpName + ", " + positionName);
			return AccountResultCode.PARAM_NAME_SENSITIVE;
		}

		CorpBase corpBase = corpService.getCorpBaseByName(corpName);
		if (corpBase == null) {
			userProfileService.updateVerification(_userId, 0);
			jobService.changeJobStatusToInformByUserId(_userId);
			corpBase = new CorpBase();
			corpBase.setName(corpName);
			corpBase.setSrcType(SourceTypeEnum.GOUDA_APP.getType());
			CorpResult corpResult = corpService.addCorpBase(corpBase);
			logger.info("ModifyCareer:corpService.addCorpBase(" + corpName
					+ "):" + corpResult);
		} else {
			corpId = corpBase.getId();
		}
		UserCareer newUserCareer = new UserCareer(_userId, positionName,
				positionType, corpName, corpId, industry, experience, education);
		UserCareer oldUserCareer = userProfileService.getUserCareer(_userId);
		if (oldUserCareer == null) {
			daoResult = userProfileService.createUserCareer(newUserCareer);
		} else {
			String oldCorpName = oldUserCareer.getCorpName();
			int oldCorpId = oldUserCareer.getCorpId();
			if (oldCorpId == corpId
					|| StringUtils.defaultString(corpName).equalsIgnoreCase(
							oldCorpName)) {
				newUserCareer.setVerification(oldUserCareer.getVerification());
			} else {
				jobService.changeJobStatusToInformByUserId(_userId);
				userEmailVerifyDAO.delUserEmailVerifyByUserId(_userId);
			}

			if (!StringUtils.defaultString(corpName).equals(oldCorpName)
					|| !StringUtils.defaultString(positionName).equals(
							oldUserCareer.getPositionName())) {
				userProfileService.clearUserCareerAudit(_userId);
			}
			daoResult = userProfileService.updateUserCareer(newUserCareer);
		}
		return daoResult ? CommonResultCode.OP_SUCC : CommonResultCode.OP_FAIL;
	}

	@Override
	public SendEmailResult verifyCorpEmail(int _userId, String corpName,
			String email) {

		SendEmailResult result = new SendEmailResult();

		/**
		 * Store the latest changed user email verify request
		 */
		UserEmailVerify userEmailVerify = new UserEmailVerify(_userId,
				"", email);	
		userEmailVerifyDAO.addUserEmailVerify(userEmailVerify);

		/**
		 * Send an email to check if the email address exists
		 */
		String name = "";
		UserBase userBase = userProfileService.getUserBase(_userId);
		if (userBase != null) {
			name = StringUtils.defaultString(userBase.getName());
		}
		try {
			String verifyUrl = genVerifyUrl(userEmailVerify);
			/**
			 * DEV and TEST environment
			 */
			if (env.equalsIgnoreCase("dev") || env.equalsIgnoreCase("test")
					|| env.equalsIgnoreCase("pre_release")) {
				result.setVerifyUrl(verifyUrl);
			}
			logger.info("VerifyCorpEmail:sendEmail(" + email + "), verifyUrl:"
					+ verifyUrl);
			sendEmail(email, name, verifyUrl);
		} catch (Exception e) {
			logger.error("用户认证邮件发送失败", e);
			result.setStatus(1);
		}

		return result;

	}

	private String genVerifyUrl(UserEmailVerify userEmailVerify) {
		String verifyToken = genVerifyToken(userEmailVerify);
		String genUrlTime = AzDGCrypt.encrypt(String.valueOf(System.currentTimeMillis()),String.valueOf(GOUDA_UID));
		return VERIFY_API_URL + "?userId=" + userEmailVerify.getUserId()
				+ "&token=" + verifyToken + "&sig=" + genUrlTime;
	}

	private String genVerifyToken(UserEmailVerify userEmailVerify) {
		String jsonString = JSONObject.fromObject(userEmailVerify).toString();
		String encryptText = null;
		AES.setKey(String.valueOf(GOUDA_UID));
		AES.encryptForURL(jsonString.trim());
		encryptText = AES.getEncryptedString();
		return encryptText;
	}

	private void sendEmail(String email, String name, String verifiUrl) {
		EmailService emailService = WebHolder.getInstance().getInjector()
				.getInstance(EmailService.class);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("name", name);
		data.put("url", verifiUrl);
		EmailEntry emailEntry = new EmailEntry(EmailCategoryEnum.BASE,
				"gouda_verification", email, GOUDA_UID, data);
		emailService.sendWithLimit(EmailCategoryEnum.BASE, emailEntry,
				EMAIL_LIMIT);
	}

	@Override
	public VerificationView getLatestVerification(int _userId) {
		VerificationView view = new VerificationView();
		UserEmailVerify emailVerify = userEmailVerifyDAO
				.getUserEmailVerify(_userId);
		UserCareer userCareer = userProfileService.getUserCareer(_userId);
		view.setCorpName(userCareer.getCorpName());
		if (emailVerify != null) {			
			view.setEmail(emailVerify.getEmail());
			/** 
			 * We have sent the verifyEmail, but user did not click the link
			 */
			view.setStatus(1);
		}

		int verification = userProfileService.getVerification(_userId);

		logger.info("[GetLastestVerification] userId:" + _userId
				+ ", verification:" + verification);
		logger.info("[GetLastestVerification] emailVerify:"
				+ JSONObject.fromObject(emailVerify).toString());

		view.setVerification(verification);
		if (verification == 1 || emailVerify == null
				|| userEmailVerifyDAO.getVerifyDate(emailVerify) == null) {
			/**
			 * The userEmailVerify is failed by expired
			 * 
			 * @see CorpEmailVerifyJob
			 */
			if (emailVerify == null && userProfileService.isVerifyFailed(_userId)) {
				view.setStatus(-1);
			}
			return view;
		}

		/** 
		 * We have sent the verifyEmail, and user has clicked the link
		 */
		view.setStatus(2);
		/**
		 * Get latest verification
		 */
		String corpName = userCareer.getCorpName();
		/**
		 * Check corpName exists or not
		 */
		CorpBase corpBase = corpService.getCorpBaseByName(corpName);
		if (corpBase == null) {
			view.setVerification(0);
			return view;
		}
		/**
		 * Check emailDomain exists or not
		 */
		corpBase = corpService.getCorpBaseById(corpBase.getId(),
				CorpInfoItem.EmailDomain);
		String email = emailVerify.getEmail();
		String emailDomain = email.substring(email.indexOf('@') + 1);
		List<String> emailDomainList = corpBase.getEmailDomains();
		if (emailDomainList != null && emailDomainList.contains(emailDomain)) {
			userProfileService.updateVerification(_userId, 1);
			logger.info("[GetLastestVerification] userId:" + _userId
					+ ", email:" + email + ", pushMessage: verify passed");
			try {
				pushService.sendTextMessage(_userId, "恭喜你，你已经通过认证，可以开始发布机会啦");
			} catch (Exception e) {
				logger.error("push verify success failed", e);
			}
			userEmailVerifyDAO.delUserEmailVerify(emailVerify);
			view.setVerification(1);
			return view;
		} else {
			view.setVerification(0);
			return view;
		}
	}

	@Override
	public MCPInteger addLabel(int _userId, String content) {
		UserLabel userLabel = new UserLabel();
		userLabel.setUserId(_userId);

		MCPInteger ret = new MCPInteger();
		ret.setRet(CommonResultCode.OP_FAIL);

		if (content.length() > LABEL_LENGTH_LIMIT) {
			ret.setRet(LABEL_LENGTH_LIMIT_ERROR);
		} else if (content != null
				&& !keywordFilterService.checkByLevel(content,
						FilterLevel.FIRST)) {
			ret.setRet(CONTENT_SENSITIVE);
		} else {
			userLabel.setContent(EmojiFilterUtil.emojiFilter(content));
			if (userLabelDAO.addLabel(userLabel) == 1) {
				ret.setRet(userLabel.getId());
				auditUserLabel(_userId, userLabel.getId(),
						userLabel.getContent());
			}
		}
		return ret;
	}
	
	@Override
	public MCPInteger modifyLabel(int _userId, int labelId, String content) {
		UserLabel userLabel = new UserLabel();
		userLabel.setUserId(_userId);
		userLabel.setId(labelId);
		
		MCPInteger ret = new MCPInteger();
		ret.setRet(CommonResultCode.OP_FAIL);

		if (content.length() > LABEL_LENGTH_LIMIT) {
			ret.setRet(LABEL_LENGTH_LIMIT_ERROR);
		} else if (content != null
				&& !keywordFilterService.checkByLevel(content,
						FilterLevel.FIRST)) {
			ret.setRet(CONTENT_SENSITIVE);
		} else {
			userLabel.setContent(EmojiFilterUtil.emojiFilter(content));
			if (userLabelDAO.modifyLabel(userLabel) == 1) {
				ret.setRet(userLabel.getId());
				auditUserLabel(_userId, userLabel.getId(),
						userLabel.getContent());
			}
		}
		return ret;
	}

	/**
	 * Audit userLabel
	 */
	private void auditUserLabel(int _userId, int labelId, String content) {
		try {
			GoudaVerifyEvent labelVerify = new GoudaVerifyEvent();
			labelVerify.setVerifyType(GoudaVerifyType.VERIFY_LABEL.getCode());
			labelVerify.setId(labelId);
			labelVerify.setUserId(_userId);
			labelVerify.setUserName(userProfileService.getUserBase(_userId)
					.getName());
			labelVerify.setLabelContent(content);
			labelVerify.setUpdateDate(System.currentTimeMillis());
			goudaVerifyPutter.addVerifyEvent(labelVerify);
		} catch (Exception e) {
			logger.error("GoudaVerifyPutter add labelVerifyEvent failed", e);
		}

	}

	@Override
	public int delLabel(int _userId, int labelId) {
		int daoResult = userLabelDAO.delLabel(_userId, labelId);
		if (daoResult > 0)
			return CommonResultCode.OP_SUCC;
		else if (daoResult == 0)
			return AccountResultCode.HAS_DELETED;
		else
			return CommonResultCode.OP_FAIL;
	}

	@Override
	public int likeLabel(int _userId, int labelId) {
		UserLabel userLabel = userProfileService.getLabelById(labelId);
		if (userLabel == null) {
			return AccountResultCode.HAS_DELETED;
		}
		try {
			if (userLabelLikeDAO.addLike(_userId, labelId) == 1) {
				try {
					pushService.sendLikeLabel(_userId, userLabel.getUserId(),
							userLabel.getContent());
				} catch (Exception e) {
					logger.error("push failed:" + _userId
							+ JSONObject.fromObject(userLabel).toString(), e);
				}
				return CommonResultCode.OP_SUCC;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return CommonResultCode.OP_FAIL;
		}
		return CommonResultCode.OP_FAIL;

	}

	@Override
	public int unlikeLabel(int _userId, int labelId) {
		if (userLabelDAO.labelIsExist(labelId) == 0) {
			return AccountResultCode.HAS_DELETED;
		}
		return userLabelLikeDAO.delLike(_userId, labelId) == 1 ? CommonResultCode.OP_SUCC
				: CommonResultCode.OP_FAIL;
	}
	

}
