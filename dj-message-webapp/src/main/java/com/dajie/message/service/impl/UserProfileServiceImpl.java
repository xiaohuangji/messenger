package com.dajie.message.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dajie.message.dao.FriendsDAO;
import com.dajie.message.dao.SchoolLabelDAO;
import com.dajie.message.dao.UserBaseDAO;
import com.dajie.message.dao.UserCareerDAO;
import com.dajie.message.dao.UserEducationDAO;
import com.dajie.message.dao.UserLabelDAO;
import com.dajie.message.enums.UserStatus;
import com.dajie.message.mcp.service.IPassportService;
import com.dajie.message.model.user.SimpleUserView;
import com.dajie.message.model.user.UserBase;
import com.dajie.message.model.user.UserCareer;
import com.dajie.message.model.user.UserEducation;
import com.dajie.message.model.user.UserLabel;
import com.dajie.message.service.IPersonMapObjectService;
import com.dajie.message.service.IPushService;
import com.dajie.message.service.IUserProfileService;
import com.dajie.message.service.mq.GoudaVerifyEvent;
import com.dajie.message.service.mq.GoudaVerifyType;
import com.dajie.message.service.worker.GoudaVerifyPutter;
import com.dajie.message.util.EmojiFilterUtil;
import com.dajie.message.util.StringUtil;

/**
 * Created by John on 4/29/14.
 */
@Component("userProfileService")
public class UserProfileServiceImpl implements IUserProfileService {

	private static Logger logger = LoggerFactory
			.getLogger(UserProfileServiceImpl.class);

	@Autowired
	private UserBaseDAO userBaseDAO;

	@Autowired
	private UserEducationDAO userEducationDAO;

	@Autowired
	private UserCareerDAO userCareerDAO;

	@Autowired
	private SchoolLabelDAO schoolLabelDAO;

	@Autowired
	private UserLabelDAO userLabelDAO;

	@Autowired
	private FriendsDAO friendsDAO;

	@Autowired
	private IPassportService passportService;

	@Autowired
	private IPersonMapObjectService personMapObjectService;

	@Autowired
	private IPushService pushService;
	
	@Autowired
	private GoudaVerifyPutter goudaVerifyPutter;

	@Override
	public int createUserBase(UserBase userBase) {
		int daoResult = userBaseDAO.insert(userBase);
		if (daoResult > 0 && !StringUtils.isEmpty(userBase.getName())) {
			auditUserName(userBase.getUserId(), userBase.getName());
		}
		return daoResult;
	}

	/**
	 * Audit userName
	 */
	private void auditUserName(int userId, String name) {
		try {
			GoudaVerifyEvent nameVerify = new GoudaVerifyEvent();
			nameVerify.setVerifyType(GoudaVerifyType.VERIFY_NAME.getCode());
			nameVerify.setId(userId);
			nameVerify.setUserId(userId);
			nameVerify.setUserName(name);
			nameVerify.setUpdateDate(System.currentTimeMillis());
			goudaVerifyPutter.addVerifyEvent(nameVerify);
		} catch (Exception e) {
			logger.error("GoudaVerifyPutter add nameVerifyEvent failed", e);
		}
	}

	@Override
	public UserBase getUserBase(int userId) {
		return userBaseDAO.getUserBaseById(userId);
	}

	@Override
	public boolean updateName(int userId, String name) {
		int daoResult = userBaseDAO.updateName(userId, name);
		if (daoResult > 0) {
			clearUserNameAudit(userId);
			personMapObjectService.updatePersonMapObject(userId);
			sendUserUpdate(userId);
			auditUserName(userId, name);
		}
		return daoResult > 0;
	}

	private void sendUserUpdate(int userId) {
		List<Integer> friends = friendsDAO.getAllFriends(userId);
		if (friends != null) {
			for (int toId : friends) {
				try {
					pushService.sendUserUpdate(userId, toId);
				} catch (Exception e) {
					logger.info("pushService sendUserUpdate failed: userId "
							+ userId, e);
				}
			}
		}
	}

	@Override
	public boolean updateGender(int userId, int gender) {
		int daoResult = userBaseDAO.updateGender(userId, gender);
		if (daoResult > 0) {
			personMapObjectService.updatePersonMapObject(userId);
		}
		return daoResult > 0;
	}

	@Override
	public boolean updateAvatar(int userId, String avatar, String avatarMask) {
		int daoResult = userBaseDAO.updateAvatar(userId, avatar, avatarMask);
		if (daoResult > 0) {
			personMapObjectService.updatePersonMapObject(userId);
			sendUserUpdate(userId);
		}
		return daoResult > 0;
	}

	@Override
	public boolean createUserEducation(UserEducation userEducation) {
		int daoResult = userEducationDAO.insert(userEducation);
		if (daoResult > 0) {
			personMapObjectService.updatePersonMapObject(userEducation
					.getUserId());
			auditUserEducation(userEducation);
		}
		return daoResult > 0;
	}

	/**
	 * Audit userEducation
	 */
	private void auditUserEducation(UserEducation userEducation) {
		try {
			GoudaVerifyEvent educationVerify = new GoudaVerifyEvent();
			educationVerify.setVerifyType(GoudaVerifyType.VERIFY_EDUCATION.getCode());
			educationVerify.setId(userEducation.getUserId());
			educationVerify.setUserId(userEducation.getUserId());
			educationVerify.setUserName(userBaseDAO.getUserBaseById(
					userEducation.getUserId()).getName());
			educationVerify.setSchoolName(userEducation.getSchool());
			educationVerify.setMajorName(userEducation.getMajor());
			educationVerify.setUpdateDate(System.currentTimeMillis());
			goudaVerifyPutter.addVerifyEvent(educationVerify);
		} catch (Exception e) {
			logger.error("GoudaVerifyPutter add educationVerifyEvent failed", e);
		}
	}

	@Override
	public UserEducation getUserEducation(int userId) {
		return userEducationDAO.getUserEducation(userId);
	}

	@Override
	public boolean updateUserEducation(UserEducation userEducation) {
		int daoResult = userEducationDAO.update(userEducation);
		if (daoResult > 0) {
			personMapObjectService.updatePersonMapObject(userEducation
					.getUserId());
			auditUserEducation(userEducation);
		}
		return daoResult > 0;
	}

	@Override
	public boolean createUserCareer(UserCareer userCareer) {
		int daoResult = userCareerDAO.insert(userCareer);
		if (daoResult > 0) {
			personMapObjectService
					.updatePersonMapObject(userCareer.getUserId());
			auditUserCareer(userCareer);
		}
		return daoResult > 0;
	}

	/**
	 * Audit userCareer
	 */
	private void auditUserCareer(UserCareer userCareer) {
		try {			
			GoudaVerifyEvent careerVerify = new GoudaVerifyEvent();
			careerVerify.setVerifyType(GoudaVerifyType.VERIFY_WORK.getCode());
			careerVerify.setId(userCareer.getUserId());
			careerVerify.setUserId(userCareer.getUserId());
			careerVerify.setUserName(userBaseDAO.getUserBaseById(
					userCareer.getUserId()).getName());
			careerVerify.setCorpName(userCareer.getCorpName());
			careerVerify.setPositionName(userCareer.getPositionName());
			careerVerify.setUpdateDate(System.currentTimeMillis());
			goudaVerifyPutter.addVerifyEvent(careerVerify);
		} catch (Exception e) {
			logger.error("GoudaVerifyPutter add careerVerifyEvent failed", e);
		}
	}

	@Override
	public UserCareer getUserCareer(int userId) {
		return userCareerDAO.getUserCareer(userId);
	}

	@Override
	public boolean updateUserCareer(UserCareer userCareer) {
		int daoResult = userCareerDAO.update(userCareer);
		if (daoResult > 0) {
			personMapObjectService
					.updatePersonMapObject(userCareer.getUserId());
			auditUserCareer(userCareer);

		}
		return daoResult > 0;
	}

	@Override
	public int getVerification(int userId) {
		Integer verification = userCareerDAO.getVerification(userId);
		if (verification == null) {
			return 0;
		} else {
			return verification;
		}
	}

	@Override
	public boolean isVerified(int userId) {
		return getVerification(userId) == 1;
	}

	@Override
	public boolean isVerifyFailed(int userId) {
		return getVerification(userId) == -1;
	}

	@Override
	public boolean updateVerification(int userId, int verification) {
		if (userCareerDAO.updateVerification(userId, verification) == 1) {
			personMapObjectService.updatePersonMapObject(userId);
			return true;
		}
		return false;
	}

	@Override
	public String getSchoolLabel(String school) {
		return schoolLabelDAO.getLabel(school);
	}

	@Override
	public UserLabel getLabelById(int labelId) {
		UserLabel userLabel = userLabelDAO.getLabelById(labelId);
		if (userLabel != null) {
			userLabel.setContent(EmojiFilterUtil.recoverToEmoji(userLabel
					.getContent()));
		}
		return userLabel;
	}

	@Override
	public List<UserLabel> getLabels(int userId) {
		List<UserLabel> userLabels = userLabelDAO.getLabels(userId);
		if (userLabels != null) {
			for (UserLabel userLabel : userLabels) {
				userLabel.setContent(EmojiFilterUtil.recoverToEmoji(userLabel
						.getContent()));
			}
		}
		return userLabels;
	}

	@Override
	public boolean isFriend(int userId, int friendId) {
		return friendsDAO.isFriend(userId, friendId) > 0 ? true : false;
	}

	@Override
	public SimpleUserView getSimpleUserView(int userId) {
		UserBase baseInfo = null;
		UserEducation educationInfo = null;
		UserCareer careerInfo = null;

		SimpleUserView user = new SimpleUserView();
		baseInfo = getUserBase(userId);
		educationInfo = getUserEducation(userId);
		careerInfo = getUserCareer(userId);
		if (baseInfo != null) {
			user.setUserId(userId);
			user.setName(baseInfo.getName());
			user.setGender(baseInfo.getGender());
			user.setAvatar(baseInfo.getAvatar());
			user.setAvatarMask(baseInfo.getAvatarMask());
			if (educationInfo != null) {
				user.setMajor(educationInfo.getMajor());
				user.setSchool(educationInfo.getSchool());
				user.setDegree(educationInfo.getDegree());
			}
			if (careerInfo != null) {
				user.setPositionName(careerInfo.getPositionName());
				user.setCorpName(careerInfo.getCorpName());
				user.setExperience(careerInfo.getExperience());
				user.setEducation(careerInfo.getEducation());
			}

		}
		return user;
	}

	@Override
	public UserBase getUserBaseByMobile(String mobile) {
		return userBaseDAO.getUserBaseByMobile(mobile);
	}

	@Override
	public boolean updatePassword(int userId, String password, String salt) {
		return userBaseDAO.updatePwd(userId, password, salt) > 0;
	}

	@Override
	public boolean isMobileUser(int userId) {
		UserBase userBase = getUserBase(userId);
		String mobile = userBase.getMobile();
		String password = userBase.getPassword();
		if (StringUtil.isNotEmpty(mobile) && StringUtil.isNotEmpty(password)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean completeRegInfo(int userId, String name, int gender,
			String avatar, String avatarMask) {
		int daoResult = userBaseDAO.updateRequiredInfo(userId, name, gender,
				avatar, avatarMask);
		if (daoResult > 0 && !StringUtils.isEmpty(name)) {
			auditUserName(userId, name);
		}
		return daoResult > 0;
	}

	@Override
	public boolean updateMobileAndPassword(int userId, String mobile,
			String password, String salt) {
		return userBaseDAO.updateMobileAndPassword(userId, mobile, password,
				salt) > 0;
	}

	@Override
	public boolean updateMobile(int userId, String mobile) {
		return userBaseDAO.updateMobile(userId, mobile) > 0;
	}

	@Override
	public boolean freezeUser(int userId) {
		passportService.removeTicket(userId);
		return updateUserStatus(userId, UserStatus.FREEZE.getCode());
	}

	@Override
	public boolean unfreezeUser(int userId) {
		return updateUserStatus(userId, UserStatus.NORMAL.getCode());
	}

	private boolean updateUserStatus(int userId, int status) {
		return userBaseDAO.updateUserStatus(userId, status) > 0;
	}

	/**
	 * 用户资料审核结果 (支持按位或) 1. 名字未通过
	 */
	@Override
	public int auditUserNameNotPass(int userId) {
		Integer audit = userBaseDAO.getUserAduit(userId);
		if (audit == null || (audit & 0x0001) == 1)
			return 0;
		else {
			userBaseDAO.updateUserAuditResult(userId, (audit | 0x0001));
			personMapObjectService.updatePersonMapObject(userId);
			return 1;
		}
	}

	@Override
	public int clearUserNameAudit(int userId) {
		Integer audit = userBaseDAO.getUserAduit(userId);
		if (audit == null || (audit & 0x0001) == 0)
			return 0;
		else {
			userBaseDAO.updateUserAuditResult(userId, (audit & 0x0006));
			personMapObjectService.updatePersonMapObject(userId);
			return 1;
		}
	}

	/**
	 * 用户资料审核结果 (支持按位或) 2.教育背景未通过
	 */
	@Override
	public int auditUserEducationNotPass(int userId) {
		Integer audit = userBaseDAO.getUserAduit(userId);
		if (audit == null || (audit & 0x0002) == 1)
			return 0;
		else {
			userBaseDAO.updateUserAuditResult(userId, (audit | 0x0002));
			personMapObjectService.updatePersonMapObject(userId);
			return 1;
		}
	}

	@Override
	public int clearUserEducationAudit(int userId) {
		Integer audit = userBaseDAO.getUserAduit(userId);
		if (audit == null || (audit & 0x0002) == 0)
			return 0;
		else {
			userBaseDAO.updateUserAuditResult(userId, (audit & 0x0005));
			personMapObjectService.updatePersonMapObject(userId);
			return 1;
		}
	}

	/**
	 * 用户资料审核结果 (支持按位或) 4.职业背景未通过
	 */
	@Override
	public int auditUserCareerNotPass(int userId) {
		Integer audit = userBaseDAO.getUserAduit(userId);
		if (audit == null || (audit & 0x0004) == 1)
			return 0;
		else {
			userBaseDAO.updateUserAuditResult(userId, (audit | 0x0004));
			personMapObjectService.updatePersonMapObject(userId);
			return 1;
		}
	}

	@Override
	public int clearUserCareerAudit(int userId) {
		Integer audit = userBaseDAO.getUserAduit(userId);
		if (audit == null || (audit & 0x0004) == 0)
			return 0;
		else {
			userBaseDAO.updateUserAuditResult(userId, (audit & 0x0003));
			personMapObjectService.updatePersonMapObject(userId);
			return 1;
		}
	}

	@Override
	public boolean updateLastLogin(int userId) {
		return userBaseDAO.updateLastLogin(userId, new Date()) > 0;
	}

}
