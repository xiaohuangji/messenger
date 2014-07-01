package com.dajie.message.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dajie.common.file.service.FileService;
import com.dajie.core.profile.model.Education;
import com.dajie.core.profile.model.UserDetail;
import com.dajie.core.profile.model.WorkExperience;
import com.dajie.core.profile.service.ProfileService;
import com.dajie.corp.api.service.CorpService;
import com.dajie.corp.info.model.CorpEmployee;
import com.dajie.infra.user.model.UserBase;
import com.dajie.infra.user.service.UserBaseService;
import com.dajie.message.constants.returncode.AccountResultCode;
import com.dajie.message.constants.returncode.CommonResultCode;
import com.dajie.message.enums.GenderEnum;
import com.dajie.message.model.user.DajieConnectResult;
import com.dajie.message.model.user.DajieUserInfo;
import com.dajie.message.model.user.ExtUserInfo;
import com.dajie.message.util.AccountFieldValidator;
import com.dajie.message.util.ConfigUtil;
import com.dajie.message.util.StringUtil;

@Component("djAccountService")
public class DJAccountService {
	private Logger logger = LoggerFactory.getLogger(DJAccountService.class);

	@Autowired
	private UserBaseService userService;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private CorpService corpService;

	private final Set<String> defaultAvatarSet = new HashSet<String>();

	@PostConstruct
	public void init() {
		List<String> defaultAvatarList = Arrays.asList(ConfigUtil.getInstance()
				.getConfig("default_avatar").split(","));
		if (null != defaultAvatarList) {
			for (String avatar : defaultAvatarList) {
				if (StringUtil.isNotEmpty(avatar)) {
					defaultAvatarSet.add(avatar);
				}
			}
		}
		logger.info("init default avatar set : " + defaultAvatarSet.toString());
	}

	/**
	 * 登陆大街帐号，正确登陆返回dajie的uid,异常则返回负数的异常码
	 * 
	 * @param account
	 * @param password
	 * @return
	 */
	public DajieConnectResult loginWithDJ(String account, String password) {
		DajieConnectResult dajieConnectResult = new DajieConnectResult(
				AccountResultCode.DJ_ACCOUNT_CONNECT_FAIL);
		try {
			UserBase userBase = userService.login(account, password);
			// 未正常返回
			if (userBase == null) {
				boolean isDJAccountExist = isDJAccountExist(account);
				if (isDJAccountExist) {
					dajieConnectResult
							.setCode(AccountResultCode.DJ_ACCOUNT_PASSWORD_ERR);
				} else {
					dajieConnectResult
							.setCode(AccountResultCode.DJ_ACCOUNT_NOT_EXIST);
				}
			} else {
				// 大街的正常用户
				if (!userBase.isDeleted() && !userBase.isDisabled()
						&& !userBase.isCanceled()) {
					dajieConnectResult.setCode(CommonResultCode.OP_SUCC);
					dajieConnectResult.setUid(userBase.getUid());
				} else {
					logger.info("dajie user is unnormal");
					if (userBase.isDeleted() || userBase.isCanceled()) {
						logger.info("dajie account is delete..");
						dajieConnectResult
								.setCode(AccountResultCode.DJ_ACCOUNT_NOT_EXIST);
					} else if (userBase.isDisabled()) {
						logger.info("dajie account is disable");
						dajieConnectResult
								.setCode(AccountResultCode.DJ_ACCOUNT_DISABLE);
					}
				}
			}
		} catch (Exception e) {
			logger.error("login with dajie error", e);
		}
		return dajieConnectResult;
	}

	public ExtUserInfo getBaseInfoFromDJ(int djUid) {
		ExtUserInfo extUserBase = new ExtUserInfo();
		extUserBase.setExtUserId(djUid + "");
		UserBase userBase = userService.getUserByUid(djUid);
		if (!userBase.isDeleted() && !userBase.isDisabled()
				&& !userBase.isCanceled()) {
			String name = userBase.getName();
			String avatar = userBase.getAvatar();
			if (!defaultAvatarSet.contains(avatar)) {
				avatar = FileService.url(avatar, FileService.LARGE);
			} else {
				avatar = "";
			}
			String email = "";
			GenderEnum genderEnum = GenderEnum.UNKNOWN;
			UserDetail detail = profileService.getUserDetailByUid(userBase
					.getUid());
			if (detail != null) {
				genderEnum = GenderEnum.parse(detail.getGender());
				email = detail.getEmail();
			}
			extUserBase.setAvatar(avatar);
			extUserBase.setEmail(email);
			extUserBase.setGender(genderEnum);
			extUserBase.setName(name);
		}
		return extUserBase;
	}

	public DajieUserInfo getDajieUserInfo(int djUid) {
		DajieUserInfo dajieUserInfo = new DajieUserInfo();

		// dajie work experiences info，user last one
		List<WorkExperience> experiences = profileService
				.getWorkExperienceListByUid(djUid);
		if (experiences != null && experiences.size() > 0) {
			Collections.sort(experiences);
			WorkExperience lastExperience = experiences.get(0);
			if (lastExperience != null) {
				String position = lastExperience.getPosition();
				int industry = null != lastExperience.getPositionIndustry() ? lastExperience
						.getPositionIndustry() : 0;
				int jobType = null != lastExperience.getPositionFunction() ? lastExperience
						.getPositionFunction() : 0;
				logger.info("dj_user_info,dj_uid+" + djUid + " : postion="
						+ position + ",jobType=" + jobType + ",industry="
						+ industry);

				String cId = lastExperience.getCid();
				String corp = lastExperience.getCorpName();
				logger.info("dj_user_info,dj_uid+" + djUid + " : corp=" + corp
						+ ",cid=" + cId);

				lastExperience.getEndDate();
				if (StringUtil.isNotEmpty(cId)) {
					try {
						Integer corpId = corpService.getCorpIdByCid(cId);
						if (corpId != null) {
							dajieUserInfo.setCorpId(corpId);
							dajieUserInfo.setCorpName(corp);
							boolean isVerifyEmployee = false;
							CorpEmployee corpEmployee = corpService
									.getCorpEmployeeByCorpIdUid(corpId, djUid);
							if (corpEmployee != null) {
								isVerifyEmployee = corpEmployee
										.getAuthenticate() == 1;
							}
							dajieUserInfo.setVerifyEmployee(isVerifyEmployee);
						} else {
							logger.info("get corpId by cid return null");
						}
					} catch (NumberFormatException e) {
						logger.info("get corpId by cid return not a number.");
					} catch (Exception e) {
						logger.info("get corpId by cid exception");
					}
				}

				dajieUserInfo.setPositionName(position);
				dajieUserInfo.setIndustry(industry);
				dajieUserInfo.setPositionType(jobType);
			}
		}

		// dajie education info,use highest degree
		Education education = profileService.getHighestEducationByUid(djUid);
		if (null != education) {
			String major = education.getMajorName();
			String school = education.getSchoolName();
			int degree = education.getDegree();

			dajieUserInfo.setMajor(major);
			dajieUserInfo.setSchool(school);
			dajieUserInfo.setDegree(degree);
		}

		// get work expirence year from user profile
		UserDetail userDetail = profileService.getUserDetailByUid(djUid);
		int workYear = userDetail.getWorkYears();
		dajieUserInfo.setWorkYear(workYear);
		return dajieUserInfo;
	}

	private boolean isDJAccountExist(String account) {
		UserBase userBase = null;
		if (StringUtil.isEmail(account)) {
			userBase = userService.getUserByEmail(account);
		}
		if (AccountFieldValidator.isPhoneNumber(account)) {
			userBase = userService.getUserByPhone(account);
		}
		if (userBase != null) {
			return true;
		}
		return false;
	}

}
