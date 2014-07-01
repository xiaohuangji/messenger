package com.dajie.message.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dajie.common.config.ConfigurationManager;
import com.dajie.common.dubbo.tolerance.Idempotent;
import com.dajie.message.constants.returncode.AccountResultCode;
import com.dajie.message.constants.returncode.CommonResultCode;
import com.dajie.message.enums.GenderEnum;
import com.dajie.message.enums.PlatformEnum;
import com.dajie.message.enums.SmsSendTypeEnum;
import com.dajie.message.enums.UserStatus;
import com.dajie.message.mcp.model.UserPassport;
import com.dajie.message.mcp.service.IPassportService;
import com.dajie.message.model.PlatformMap;
import com.dajie.message.model.user.DajieConnectResult;
import com.dajie.message.model.user.DajieUserInfo;
import com.dajie.message.model.user.ExtUserInfo;
import com.dajie.message.model.user.UserBase;
import com.dajie.message.model.user.UserCareer;
import com.dajie.message.model.user.UserEducation;
import com.dajie.message.model.wrapper.LoginReturn;
import com.dajie.message.service.IAccountService;
import com.dajie.message.service.IPersonMapObjectService;
import com.dajie.message.service.IPlatformMapService;
import com.dajie.message.service.ISmsSendService;
import com.dajie.message.service.ISystemService;
import com.dajie.message.service.IUserProfileService;
import com.dajie.message.service.cache.CacheKeyPrefix;
import com.dajie.message.service.cache.IRedisCache;
import com.dajie.message.util.AccountFieldValidator;
import com.dajie.message.util.SMSUtil;
import com.dajie.message.util.SecurityUtils;
import com.dajie.message.util.StringUtil;
import com.dajie.word.filter.model.FilterWord.FilterLevel;
import com.dajie.word.filter.service.KeywordFilterService;

@Component("accountService")
public class AccountServiceImpl implements IAccountService {

	// 单个用户一天尝试发短信验证码的限制
	private static final int VERIFY_CODE_RETRY_TIME = 10;

	// 万能验证码，开发和测试可用
	private static final String VERIFY_CODE_UNIVERSAL = "1024";

	// 开发环境
	private static final String DEV_ENV = "dev";

	// Test环境
	private static final String TEST_ENV = "test";

	// 运行时环境
	private static final String RUN_TIME_ENV = ConfigurationManager
			.getInstance().getEnvName().toLowerCase();

	@Autowired
	private IRedisCache redisCache;

	@Autowired
	private ISmsSendService smsSendService;

	@Autowired
	private IUserProfileService userProfileService;

	@Autowired
	private DJAccountService djAccountService;

	@Autowired
	private IPlatformMapService platformMapService;

	@Autowired
	private IPlatformProcessFactory platformProcessFactory;

	@Autowired
	private IPersonMapObjectService personMapObjectService;

	@Autowired
	private IPassportService passportService;

	@Autowired
	private ISystemService systemService;

	@Autowired
	private KeywordFilterService keywordFilterService;

	private static final Logger logger = Logger
			.getLogger(AccountServiceImpl.class);

	@Override
	@Idempotent(value = false)
	public LoginReturn register(String mobile, String verifyCode,
			String password) {
		int returnCode = CommonResultCode.OP_FAIL;

		// 校验验证码
		boolean codeValid = validateMobileCode(mobile,
				SmsSendTypeEnum.REGISTER, verifyCode);
		if (!codeValid) {
			return new LoginReturn(AccountResultCode.PARAM_VERIFI_CODE_ERROR);
		}

		// 参数校验
		boolean isMobile = AccountFieldValidator.isPhoneNumber(mobile);
		if (!isMobile) {
			return new LoginReturn(AccountResultCode.PARAM_MOBILE_INVALID);
		}

		// 输入正确，清除验证码
		cleanVerifyCode(mobile, SmsSendTypeEnum.REGISTER);
		boolean isMobileUsed = isMobileUsed(mobile);
		if (isMobileUsed) {
			returnCode = AccountResultCode.REG_MOBILE_IS_USED;
		} else {
			// int userId = idSequenceService.getNextUserId();
			String salt = SecurityUtils.getRandomSalt();
			String pwdMD5 = SecurityUtils.flavorPwdMD5(password, salt);
			UserBase userBase = new UserBase();
			// userBase.setUserId(userId);
			userBase.setMobile(mobile);
			userBase.setPassword(pwdMD5);
			userBase.setSalt(salt);
			userBase.setCreateTime(new Date());
			if (!isMobileUsed(mobile)) {
				boolean isSuccess = initialNewUser(userBase) > 0;
				if (isSuccess) {
					logger.info("register success.userId = "
							+ userBase.getUserId() + ",mobile=" + mobile);
					return constructSuccessLoginView(userBase);
				}
			} else {
				logger.info("register : has be registered may be two request ,mobile = "
						+ mobile);
				returnCode = AccountResultCode.REG_MOBILE_IS_USED;
			}
		}
		return new LoginReturn(returnCode);
	}

	@Override
	@Idempotent(value = false)
	public int completeRegister(int _userId, String name, int gender,
			String avatar, String avatarMask) {
		//空格符处理
		name = StringUtil.trim(name);
		// check input name
		boolean isNameValid = AccountFieldValidator.nameValidate(name);
		if (!isNameValid) {
			logger.info("complete register input name invalid:" + name);
			return AccountResultCode.PARAM_NAME_INVALID;
		}
		boolean isKeywordValid = keywordFilterService.checkByLevel(name,
				FilterLevel.FIRST);
		if (!isKeywordValid) {
			logger.info("complete register input name contains sensitive word: "
					+ name);
			return AccountResultCode.PARAM_NAME_SENSITIVE;
		}
		// process gender
		GenderEnum genderEnum = GenderEnum.parse(gender);
		gender = genderEnum.getCode();

		// update userbase
		UserBase userBase = userProfileService.getUserBase(_userId);
		boolean isCompleteRegInfo = isCompleteRegInfo(userBase);
		userBase.setName(name);
		userBase.setGender(gender);
		userBase.setAvatar(avatar);
		userBase.setAvatarMask(avatarMask);
		boolean updateReturn = userProfileService.completeRegInfo(_userId,
				name, gender, avatar, avatarMask);
		if (updateReturn) {
			logger.info("complete register userId=" + _userId + ",name=" + name
					+ ",gender=" + gender + ",avatar=" + avatar
					+ ",avatarMask=" + avatarMask);

			// elastic search point
			// complete reg理论上只会被成功调用一次
			if (!isCompleteRegInfo) {
				// 注册完成时增加一个搜索用户的节点
				logger.info("elastic search add new person map Obejct with out location.");
				personMapObjectService.addNewPersonMapObject(userBase, 0, 0);
			}
			// 已完成注册，再调用，客户端非正常行为
			else {
				// 更新一个用户节点
				logger.info("elastic search update a person map Obejct.");
				personMapObjectService.updatePersonMapObject(userBase
						.getUserId());
			}

		}
		return updateReturn ? CommonResultCode.OP_SUCC
				: CommonResultCode.OP_FAIL;
	}

	@Override
	@Idempotent(value = false)
	public LoginReturn login(String mobile, String password) {
		int returnCode = CommonResultCode.OP_FAIL;
		// get user by mobile,
		UserBase userBase = userProfileService.getUserBaseByMobile(mobile);
		if (null == userBase) {
			returnCode = AccountResultCode.LOGIN_ACCOUNT_NOT_EXIST;
		} else {
			String passwordMD5 = SecurityUtils.flavorPwdMD5(password,
					userBase.getSalt());
			if (null == passwordMD5
					|| !passwordMD5.equals(userBase.getPassword())) {
				returnCode = AccountResultCode.LOGIN_PASSWORD_ERROR;
			} else {
				int userStatus = userBase.getStatus();
				if (userStatus == UserStatus.FREEZE.getCode()) {
					returnCode = AccountResultCode.LOGIN_ACCOUNT_FREEZE;
				} else if (userStatus == UserStatus.NORMAL.getCode()) {
					return constructSuccessLoginView(userBase);
				}
			}
		}
		return new LoginReturn(returnCode);
	}

	@Override
	@Idempotent(value = false)
	public LoginReturn loginWithDJ(String account, String password) {
		int returnCode = CommonResultCode.OP_FAIL;
		DajieConnectResult dajieConnectResult = djAccountService.loginWithDJ(
				account, password);
		logger.info("login with dj return :code = "
				+ dajieConnectResult.getCode() + ",dajieUid="
				+ dajieConnectResult.getUid());
		if (dajieConnectResult.getCode() != 0) {
			returnCode = dajieConnectResult.getCode();
		} else {
			int djUserId = dajieConnectResult.getUid();
			// 是否已登陆过
			PlatformMap platformMap = platformMapService.getPlatformMap(
					PlatformEnum.DAJIE, djUserId + "");
			// 第一次登陆
			if (null == platformMap) {
				ExtUserInfo extUserBase = djAccountService
						.getBaseInfoFromDJ(djUserId);
				LoginReturn loginReturn = new LoginReturn(
						AccountResultCode.LOGIN_DJ_ACCOUNT_NOT_REG);
				loginReturn.setAvatar(extUserBase.getAvatar());
				loginReturn.setGender(extUserBase.getGender().getCode());
				loginReturn.setName(extUserBase.getName());
				logger.info("unregister dajie user login.dajieUid="
						+ dajieConnectResult.getUid());
				return loginReturn;
			}
			// 已是app用户
			else {
				int userId = platformMap.getUserId();
				UserBase userBase = userProfileService.getUserBase(userId);
				if (userBase != null) {
					int userStatus = userBase.getStatus();
					if (userStatus == UserStatus.FREEZE.getCode()) {
						returnCode = AccountResultCode.LOGIN_ACCOUNT_FREEZE;
					} else if (userStatus == UserStatus.NORMAL.getCode()) {
						logger.info("dajie login suc .dajieUid="
								+ dajieConnectResult.getUid());
						return constructSuccessLoginView(userBase);
					}
				}
			}
		}
		return new LoginReturn(returnCode);
	}

	@Override
	@Idempotent(value = false)
	public LoginReturn loginWith3rdPlatform(String accessToken, int type) {
		PlatformEnum platformEnum = PlatformEnum.parse(type);
		PlatformProcessor platformProcessor = null;
		try {
			platformProcessor = platformProcessFactory
					.getPlatformProcessor(platformEnum);
		} catch (Exception e) {
			return new LoginReturn(AccountResultCode.UNKNOWN_PLATFORM);
		}
		String platformUid = platformProcessor.getExtUserId(accessToken);
		if (StringUtil.isEmpty(platformUid)) {
			return new LoginReturn(AccountResultCode.INVALID_TOKEN);
		}
		PlatformMap platformMap = platformMapService.getPlatformMap(
				platformEnum, platformUid);
		if (platformMap == null) {
			LoginReturn loginReturn = new LoginReturn(
					AccountResultCode.LOGIN_3rd_ACCOUNT_NOT_REG);
			ExtUserInfo extUserInfo = platformProcessor
					.getExtUserBase(accessToken);
			if (extUserInfo != null) {
				loginReturn.setName(extUserInfo.getName());
				loginReturn.setAvatar(extUserInfo.getAvatar());
				loginReturn.setGender(extUserInfo.getGender().getCode());
			} else {
				logger.info("get 3rd platform info get null,platformType="
						+ type + ",accessToken=" + accessToken);
			}
			return loginReturn;
		} else {
			int userId = platformMap.getUserId();
			// refresh accesstoken,成功与否不会影响用户登陆。登陆时已验证token有效性
			refreshToken(userId, accessToken, type, false);
			UserBase userBase = userProfileService.getUserBase(userId);

			if (userBase != null) {
				int userStatus = userBase.getStatus();
				if (userStatus == UserStatus.FREEZE.getCode()) {
					return new LoginReturn(
							AccountResultCode.LOGIN_ACCOUNT_FREEZE);
				} else if (userStatus == UserStatus.NORMAL.getCode()) {
					logger.info("login by 3rd suc");
					return constructSuccessLoginView(userBase);
				}
			}
			return new LoginReturn(CommonResultCode.OP_FAIL);
		}

	}

	@Override
	public int sendSmsCode(String mobile, int type) {
		SmsSendTypeEnum typeEnum = SmsSendTypeEnum.parse(type);
		if (typeEnum == SmsSendTypeEnum.UNKNOWN) {
			return AccountResultCode.SMS_SEND_UNKNOWN_TYPE;
		}

		// check input
		if (!AccountFieldValidator.isPhoneNumber(mobile)) {
			logger.info("sendSmsCode---mobile is not a valid number:" + mobile);
			return AccountResultCode.PARAM_MOBILE_INVALID;
		}

		// send type prehandle
		boolean isMobileUsed = isMobileUsed(mobile);
		if (typeEnum == SmsSendTypeEnum.RESET_PWD && !isMobileUsed) {
			return AccountResultCode.SMS_SEND_NOT_REG_MOBILE;
		}

		// check and incr sms send count
		Integer sendCount = redisCache.get(
				CacheKeyPrefix.MOBILE_SMS_SEND_COUNT, mobile, Integer.class);
		if (sendCount == null) {
			logger.info("updateMobileCodeCount mobile=" + mobile
					+ " , nowCount=" + 1);
			redisCache.set(CacheKeyPrefix.MOBILE_SMS_SEND_COUNT, mobile,
					Integer.valueOf(1), 60 * 60 * 24);
		} else if (sendCount > VERIFY_CODE_RETRY_TIME) {
			logger.info("sendSmsCode---send count has out of limit,mobile="
					+ mobile);
			return AccountResultCode.SMS_SEND_RETRY_OUT_LIMIT;
		} else {
			logger.info("updateMobileCodeCount mobile=" + mobile
					+ " , nowCount=" + (sendCount + 1));
			redisCache.incr(CacheKeyPrefix.MOBILE_SMS_SEND_COUNT, mobile);
		}

		// get sms code and send it
		try {
			// 构建短信下发内容
			String code = getMobileCode(mobile, typeEnum);
			logger.info("sendVerifyMessage code=" + code);
			// 发送短信
			int sendReturn = smsSendService.smsSendVerifyCode(mobile, code);
			if (sendReturn > 0) {
				return CommonResultCode.OP_SUCC;
			} else {
				return CommonResultCode.OP_FAIL;
			}
		} catch (Exception e) {
			logger.error("AccountService sendVerifyMessage error! ", e);
			return CommonResultCode.OP_FAIL;
		}
	}

	@Override
	@Idempotent(value = false)
	public int modifyPasswordByMobile(String mobile, String newPassword,
			String verifyCode) {
		boolean isCodeValid = validateMobileCode(mobile,
				SmsSendTypeEnum.RESET_PWD, verifyCode);
		if (!isCodeValid) {
			return AccountResultCode.PARAM_VERIFI_CODE_ERROR;
		}

		UserBase userBase = userProfileService.getUserBaseByMobile(mobile);
		cleanVerifyCode(mobile, SmsSendTypeEnum.RESET_PWD);
		if (null == userBase) {
			logger.info("modify password by mobile---this mobile is not a user in app,mobile="
					+ mobile);
			return AccountResultCode.MOD_PASSWORD_USER_NOT_EXIST;
		} else {
			String salt = userBase.getSalt();
			salt = StringUtil.isNotEmpty(salt) ? salt : SecurityUtils
					.getRandomSalt();
			String pwdMD5 = SecurityUtils.flavorPwdMD5(newPassword, salt);
			boolean isSuccessful = userProfileService.updatePassword(
					userBase.getUserId(), pwdMD5, salt);
			logger.info("update password "
					+ (isSuccessful ? "success" : "fail") + "  mobile = "
					+ mobile);
			if (isSuccessful) {
				return CommonResultCode.OP_SUCC;
			} else {
				return CommonResultCode.OP_FAIL;
			}
		}

	}

	@Override
	@Idempotent(value = false)
	public LoginReturn registerByDJ(String _appId, String account,
			String password, String name, int gender, String avatar,
			String avatarMask) {
		int returnCode = CommonResultCode.OP_FAIL;

		//空格符处理
		name = StringUtil.trim(name);
		// check input
		boolean isNameValid = AccountFieldValidator.nameValidate(name);
		if (!isNameValid) {
			logger.info("register by dj input name invalid:" + name);
			returnCode = AccountResultCode.PARAM_NAME_INVALID;
			return new LoginReturn(returnCode);
		}
		boolean isKeywordValid = keywordFilterService.checkByLevel(name,
				FilterLevel.FIRST);
		if (!isKeywordValid) {
			logger.info("register by dj input name contains sensitive word: "
					+ name);
			return new LoginReturn(AccountResultCode.PARAM_NAME_SENSITIVE);
		}
		DajieConnectResult dajieConnectResult = djAccountService.loginWithDJ(
				account, password);
		if (dajieConnectResult.getCode() != 0) {
			return new LoginReturn(dajieConnectResult.getCode());

		} else {
			int djUserId = dajieConnectResult.getUid();
			PlatformMap platformMap = platformMapService.getPlatformMap(
					PlatformEnum.DAJIE, djUserId + "");
			if (null != platformMap) {
				logger.info("this dajie account has register");
				returnCode = AccountResultCode.REG_DJ_IS_USED;
			} else {
				// reg new user by dj
				UserBase userBase = new UserBase();
				// user input info
				userBase.setAvatar(avatar);
				userBase.setAvatarMask(avatarMask);
				userBase.setName(name);
				userBase.setGender(gender);
				userBase.setCreateTime(new Date());

				// extract from dajie
				ExtUserInfo extUserInfo = djAccountService
						.getBaseInfoFromDJ(djUserId);
				if (extUserInfo != null) {
					userBase.setEmail(extUserInfo.getEmail());
					userBase.setBirth(extUserInfo.getBirth());
				}

				// get user id
				// int userId = idSequenceService.getNextUserId();
				// userBase.setUserId(userId);

				platformMap = new PlatformMap();
				// platformMap.setUserId(userId);
				platformMap.setPlatformUid(djUserId + "");
				platformMap.setPlatformType(PlatformEnum.DAJIE.getCode());
				platformMap.setSource(_appId);
				platformMap.setCreateTime(new Date());
				boolean initResult = initialNewUser(userBase, platformMap);
				if (initResult) {
					logger.info("register by dj suc.dajie uid = "
							+ platformMap.getPlatformUid() + ",userId="
							+ platformMap.getUserId());
					return constructSuccessLoginView(userBase);
				}
			}
		}
		return new LoginReturn(returnCode);
	}

	@Override
	public int importDJUser(int djUid) {
		PlatformMap platformMap = platformMapService.getPlatformMap(
				PlatformEnum.DAJIE, djUid + "");
		if (null != platformMap) {
			logger.info("has import dajie user, this dajie account has registered dajieUid="
					+ djUid);
			return platformMap.getUserId();
		} else {
			// reg new user by dj
			UserBase userBase = new UserBase();

			// extract from dajie
			ExtUserInfo extUserInfo = djAccountService.getBaseInfoFromDJ(djUid);
			if (extUserInfo != null) {
				userBase.setEmail(extUserInfo.getEmail());
				userBase.setBirth(extUserInfo.getBirth());
				userBase.setAvatar(extUserInfo.getAvatar());
				// 遮罩头像与原图一样
				userBase.setAvatarMask(extUserInfo.getAvatar());
				userBase.setName(extUserInfo.getName());
				userBase.setGender(extUserInfo.getGender().getCode());
			}

			userBase.setCreateTime(new Date());

			// get user id
			// int userId = idSequenceService.getNextUserId();
			// userBase.setUserId(userId);

			platformMap = new PlatformMap();
			// platformMap.setUserId(userId);
			platformMap.setPlatformUid(djUid + "");
			platformMap.setPlatformType(PlatformEnum.DAJIE.getCode());
			platformMap.setSource("Dajie import");
			platformMap.setCreateTime(new Date());
			boolean initResult = initialNewUser(userBase, platformMap);
			if (initResult) {
				logger.info("register by dj suc.dajie uid = "
						+ platformMap.getPlatformUid() + ",userId="
						+ platformMap.getUserId());
				return userBase.getUserId();
			} else {
				logger.info("register by dj failed.dajie uid = "
						+ platformMap.getPlatformUid());
				return CommonResultCode.OP_FAIL;
			}
		}
	}

	@Override
	@Idempotent(value = false)
	public LoginReturn registerBy3rdPlatform(String _appId, String accessToken,
			int type, String name, int gender, String avatar, String avatarMask) {

		//空格符处理
		name = StringUtil.trim(name);
		// check name
		boolean isNameValid = AccountFieldValidator.nameValidate(name);
		if (!isNameValid) {
			logger.info("register by 3rd platform input name invalid: " + name);
			return new LoginReturn(AccountResultCode.PARAM_NAME_INVALID);
		}
		boolean isKeywordValid = keywordFilterService.checkByLevel(name,
				FilterLevel.FIRST);
		if (!isKeywordValid) {
			logger.info("register by 3rd platform input name contains sensitive word: "
					+ name);
			return new LoginReturn(AccountResultCode.PARAM_NAME_SENSITIVE);
		}

		// process gender
		GenderEnum genderEnum = GenderEnum.parse(gender);
		gender = genderEnum.getCode();

		PlatformEnum platformEnum = PlatformEnum.parse(type);
		PlatformProcessor platformProcessor = null;
		try {
			platformProcessor = platformProcessFactory
					.getPlatformProcessor(platformEnum);
		} catch (Exception e) {
			return new LoginReturn(AccountResultCode.UNKNOWN_PLATFORM);
		}
		String platformUid = platformProcessor.getExtUserId(accessToken);
		if (StringUtil.isEmpty(platformUid)) {
			return new LoginReturn(AccountResultCode.INVALID_TOKEN);
		}
		boolean isUsed = platformMapService.isAccountUsed(platformEnum,
				platformUid);
		if (!isUsed) {
			// reg an account by 3rd platform account
			// int userId = idSequenceService.getNextUserId();

			UserBase userBase = new UserBase();
			userBase.setAvatar(avatar);
			userBase.setAvatarMask(avatarMask);
			userBase.setName(name);
			userBase.setGender(gender);
			userBase.setCreateTime(new Date());
			// userBase.setUserId(userId);

			PlatformMap platformMap = new PlatformMap();
			platformMap.setCreateTime(new Date());
			platformMap.setAccessToken(accessToken);
			platformMap.setPlatformType(platformEnum.getCode());
			platformMap.setPlatformUid(platformUid);
			platformMap.setSource(_appId);
			// platformMap.setUserId(userId);

			boolean initResult = initialNewUser(userBase, platformMap);
			if (initResult) {
				logger.info("register by 3rd suc .accesstoken = " + accessToken
						+ ",type=" + type + ",userId="
						+ platformMap.getUserId());
				return constructSuccessLoginView(userBase);
			}
		} else {
			return new LoginReturn(AccountResultCode.REG_3rd_ACCOUNT_IS_USED);
		}
		return new LoginReturn(CommonResultCode.OP_FAIL);
	}

	/**
	 * 获取验证码，没有则生成并保存
	 * 
	 * @param mobile
	 * @param typeEnum
	 * @return
	 */
	private String getMobileCode(String mobile, SmsSendTypeEnum typeEnum) {
		String prefix = getSmsCodeCachePrefix(typeEnum);

		// 从mobileVerifyCodeCache中获取验证码
		String mobileCode = redisCache.get(prefix, mobile, String.class);
		logger.info("getMobileCode---mobile=" + mobile + " ,code=" + mobileCode);

		if (NumberUtils.toInt(mobileCode) > 0) {
			return mobileCode;
		} else {
			mobileCode = SMSUtil.getPhoneCode();
			redisCache.set(prefix, mobile, mobileCode, 60 * 10);
			logger.info("updateMobileCode mobile=" + mobile + " ,code="
					+ mobileCode);
			return mobileCode;
		}
	}

	/**
	 * 校验验证码
	 * 
	 * @param mobile
	 * @param typeEnum
	 *            验证码业务类型
	 * @param code
	 * @return
	 */
	@Override
	public boolean validateMobileCode(String mobile, SmsSendTypeEnum typeEnum,
			String code) {
		if (StringUtil.isEmpty(mobile) || StringUtil.isEmpty(code)) {
			logger.info("validate phone code input empty.");
			return false;
		}
		// 非线上环境,加上万能验证码
		if (TEST_ENV.equals(RUN_TIME_ENV) || DEV_ENV.equals(RUN_TIME_ENV)) {
			logger.info("current env: " + RUN_TIME_ENV);
			if (code.equals(VERIFY_CODE_UNIVERSAL)) {
				return true;
			}
		}

		// 从Cache中获取验证码
		String prefix = getSmsCodeCachePrefix(typeEnum);
		String mobileCode = redisCache.get(prefix, mobile, String.class);
		logger.info("getMobileCode mobile=" + mobile + " ,code=" + mobileCode);

		return code.equals(mobileCode);

	}

	private String getSmsCodeCachePrefix(SmsSendTypeEnum typeEnum) {
		String prefix = "";
		if (typeEnum == SmsSendTypeEnum.REGISTER) {
			prefix = CacheKeyPrefix.MOBILE_SMS_CODE_REG;
		} else if (typeEnum == SmsSendTypeEnum.RESET_PWD) {
			prefix = CacheKeyPrefix.MOBILE_SMS_CODE_RESET_PWD;
		}
		return prefix;
	}

	@Override
	public boolean isMobileUsed(String mobile) {
		UserBase userBase = userProfileService.getUserBaseByMobile(mobile);
		return userBase != null;
	}

	@Override
	public int bindAccount(String _appId, int _userId, String accessToken,
			int type) {
		PlatformEnum platformEnum = PlatformEnum.parse(type);
		PlatformProcessor platformProcessor = null;
		try {
			platformProcessor = platformProcessFactory
					.getPlatformProcessor(platformEnum);
		} catch (Exception e) {
			return AccountResultCode.UNKNOWN_PLATFORM;
		}
		String platformUid = platformProcessor.getExtUserId(accessToken);
		if (StringUtil.isEmpty(platformUid)) {
			return AccountResultCode.INVALID_TOKEN;
		}
		boolean isUsed = platformMapService.isAccountUsed(platformEnum,
				platformUid);
		if (isUsed) {
			return AccountResultCode.BIND_3rd_ACCOUNT_USED;
		} else {
			boolean hasBind = platformMapService.hasBind(_userId, platformEnum);
			if (hasBind) {
				return AccountResultCode.BIND_HAS_BIND;
			} else {
				// to bind
				int bindResult = platformMapService.bind3rdPlatformMap(_appId,
						_userId, platformEnum, platformUid, accessToken);
				if (bindResult > 0) {
					return CommonResultCode.OP_SUCC;
				} else {
					return CommonResultCode.OP_FAIL;
				}
			}
		}
	}

	@Override
	public int reload3rdAccount(String _appId, int _userId, String accessToken,
			int type) {
		logger.info("reload3rdAccount: userId=" + _userId + ",accesstoken="
				+ accessToken + ",type=" + type);
		PlatformEnum platformEnum = PlatformEnum.parse(type);
		PlatformProcessor platformProcessor = null;
		try {
			platformProcessor = platformProcessFactory
					.getPlatformProcessor(platformEnum);
		} catch (Exception e) {
			return AccountResultCode.UNKNOWN_PLATFORM;
		}

		String platformUid = platformProcessor.getExtUserId(accessToken);
		if (StringUtil.isEmpty(platformUid)) {
			return AccountResultCode.INVALID_TOKEN;
		}

		boolean isUsed = platformMapService.isAccountUsed(platformEnum,
				platformUid);
		PlatformMap platformMap = platformMapService
				.getPlatformMapByUserIdAndType(_userId, platformEnum);

		// platformUid这个帐号被用了，且不是UserId本身这个人用的。提示已被使用
		boolean isUsedBySelf = null != platformMap
				&& platformUid.equals(platformMap.getPlatformUid());
		if (isUsed && !isUsedBySelf) {
			return AccountResultCode.BIND_3rd_ACCOUNT_USED;
		} else {
			//绑定
			if (platformMap == null) {
				logger.info("reload 3rd account: no this type 3rd account bind, bind a new account.platformUid="
						+ platformUid);
				int bindResult = platformMapService.bind3rdPlatformMap(_appId,
						_userId, platformEnum, platformUid, accessToken);
				return bindResult > 0 ? CommonResultCode.OP_SUCC
						: CommonResultCode.OP_FAIL;
			} else {
				String hasBindPlatformUid = platformMap.getPlatformUid();
				//同一个第三方帐号，刷token
				if (hasBindPlatformUid.equals(platformUid)) {
					logger.info("reload 3rd account: refresh token");
					refreshToken(_userId, accessToken, type, false);
					return CommonResultCode.OP_SUCC;
				} 
				//换一个第三方帐号，更新第三方帐号
				else {
					logger.info("reload 3rd account: update has bind account,pre platformUid="
							+ hasBindPlatformUid
							+ ",bind platformUid = "
							+ platformUid);
					int updateResult = platformMapService
							.updateByUserIdAndType(_userId, hasBindPlatformUid,
									accessToken, platformEnum);
					return updateResult > 0 ? CommonResultCode.OP_SUCC
							: CommonResultCode.OP_FAIL;
				}
			}
		}
	}

	// 更新用户的第三方token
	/**
	 * 
	 * @param _userId
	 * @param accessToken
	 * @param type
	 * @param isCheck
	 *            是否验证token的效性
	 * @return
	 */
	private int refreshToken(int _userId, String accessToken, int type,
			boolean isCheck) {
		PlatformEnum platformEnum = PlatformEnum.parse(type);
		PlatformProcessor platformProcessor = null;
		try {
			platformProcessor = platformProcessFactory
					.getPlatformProcessor(platformEnum);
		} catch (Exception e) {
			return AccountResultCode.UNKNOWN_PLATFORM;
		}
		if (isCheck) {
			String extUserId = platformProcessor.getExtUserId(accessToken);
			if (StringUtil.isEmpty(extUserId)) {
				return AccountResultCode.INVALID_TOKEN;
			}
		}
		int result = platformMapService.refreshToken(_userId, accessToken,
				platformEnum);
		return result > 0 ? CommonResultCode.OP_SUCC : CommonResultCode.OP_FAIL;

	}

	@Override
	public int bindDJAccount(String _appId, int _userId, String account,
			String password) {
		DajieConnectResult dajieConnectResult = djAccountService.loginWithDJ(
				account, password);
		if (dajieConnectResult.getCode() != 0) {
			return dajieConnectResult.getCode();
		} else {
			PlatformEnum platformEnum = PlatformEnum.DAJIE;
			String platformUid = dajieConnectResult.getUid() + "";
			boolean isUsed = platformMapService.isAccountUsed(platformEnum,
					platformUid);
			if (isUsed) {
				return AccountResultCode.BIND_3rd_ACCOUNT_USED;
			} else {
				boolean hasBind = platformMapService.hasBind(_userId,
						platformEnum);
				if (hasBind) {
					return AccountResultCode.BIND_HAS_BIND;
				} else {
					// to bind
					int bindResult = platformMapService.bind3rdPlatformMap(
							_appId, _userId, platformEnum, platformUid, null);
					if (bindResult > 0) {
						logger.info("bind dajie suc.dajiue uid = "
								+ platformUid + ",userId=" + _userId);
						return CommonResultCode.OP_SUCC;
					} else {
						return CommonResultCode.OP_FAIL;
					}
				}
			}
		}
	}

	@Override
	public int unbindAccount(int _userId, int type) {
		PlatformEnum platformEnum = PlatformEnum.parse(type);
		if (platformEnum == PlatformEnum.UNKNOWN) {
			return AccountResultCode.UNKNOWN_PLATFORM;
		}
		List<PlatformMap> maps = platformMapService
				.getPlatformMapsByUserId(_userId);
		int bindAccountCnt = null == maps ? 0 : maps.size();
		if (bindAccountCnt == 0) {
			return AccountResultCode.UNBIND_NOT_EXIST;
		}
		//
		else if (bindAccountCnt == 1) {
			boolean isMobileUser = userProfileService.isMobileUser(_userId);
			// 解绑后将不能登陆鸟
			if (!isMobileUser) {
				return AccountResultCode.UNBIND_LAST_ONE;
			}
		}
		int delCount = platformMapService.unbind3rdPlatformMap(_userId,
				platformEnum);
		if (delCount > 0) {
			return CommonResultCode.OP_SUCC;
		} else {
			return AccountResultCode.UNBIND_NOT_EXIST;

		}
	}

	@Override
	public int logout(int _userId) {
		passportService.removeTicket(_userId);
		return systemService.bindIosToken(_userId, "");
	}

	@Override
	public int changeMobile(int _userId, String verifyCode, String mobile,
			String password) {
		boolean isVerifyCodeValid = validateMobileCode(mobile,
				SmsSendTypeEnum.RESET_MOBILE, verifyCode);
		if (!isVerifyCodeValid) {
			return AccountResultCode.PARAM_VERIFI_CODE_ERROR;
		}
		boolean isMobileValid = AccountFieldValidator.isPhoneNumber(mobile);
		if (!isMobileValid) {
			return AccountResultCode.PARAM_MOBILE_INVALID;
		}
		boolean isMobileUsed = isMobileUsed(mobile);
		if (isMobileUsed) {
			return AccountResultCode.CHANGE_MOBILE_IS_USED;
		}
		// 如果增加手机号的同时，设置登陆密码
		if (!StringUtil.isEmpty(password)) {

			String salt = SecurityUtils.getRandomSalt();
			String pwdMD5 = SecurityUtils.flavorPwdMD5(password, salt);
			boolean updateResult = userProfileService.updateMobileAndPassword(
					_userId, mobile, pwdMD5, salt);
			return updateResult ? CommonResultCode.OP_SUCC
					: CommonResultCode.OP_FAIL;

		} else {
			boolean updateResult = userProfileService.updateMobile(_userId,
					mobile);
			return updateResult ? CommonResultCode.OP_SUCC
					: CommonResultCode.OP_FAIL;
		}
	}

	@Override
	public Map<String, Map<String, String>> getBindInfo(int _userId) {
		List<PlatformMap> platformMaps = platformMapService
				.getPlatformMapsByUserId(_userId);
		Map<String, Map<String, String>> bindMap = new HashMap<String, Map<String, String>>();
		if (platformMaps != null) {
			for (PlatformMap platform : platformMaps) {
				String bindKey = PlatformEnum.parse(platform.getPlatformType())
						.name();
				Map<String, String> bindValue = new HashMap<String, String>();
				bindValue.put("uid", platform.getPlatformUid());
				bindValue.put("token", platform.getAccessToken());
				bindMap.put(bindKey, bindValue);
			}
		}
		return bindMap;
	}

	// 初始化新用户,手机注册过程
	private int initialNewUser(UserBase userBase) {
		return userProfileService.createUserBase(userBase);

	}

	// 初始化新用户，第三方平台登陆
	private boolean initialNewUser(UserBase userBase, PlatformMap platformMap) {
		try {
			int userbaseInsert = userProfileService.createUserBase(userBase);
			if (userbaseInsert > 0) {
				// 设置platform uid
				int userId = userBase.getUserId();
				platformMap.setUserId(userId);
				int platformMapInsert = platformMapService
						.insertPlatformMap(platformMap);
				if (platformMapInsert > 0) {
					// dajie register extract dajie info
					if (platformMap.getPlatformType() == PlatformEnum.DAJIE
							.getCode()) {
						int djUid = NumberUtils.toInt(platformMap
								.getPlatformUid());
						if (djUid > 0) {
							try {
								DajieUserInfo dajieUserInfo = djAccountService
										.getDajieUserInfo(djUid);

								// use dajie work info initial user career info
								UserCareer userCareer = new UserCareer();
								userCareer.setCorpId(dajieUserInfo.getCorpId());
								userCareer.setCorpName(dajieUserInfo
										.getCorpName());
								userCareer.setIndustry(dajieUserInfo
										.getIndustry());
								userCareer.setPositionName(dajieUserInfo
										.getPositionName());
								userCareer.setUserId(userBase.getUserId());
								userCareer.setVerification(dajieUserInfo
										.isVerifyEmployee() ? 1 : 0);
								userCareer.setExperience(dajieUserInfo
										.getWorkYear());
								userCareer.setEducation(dajieUserInfo
										.getDegree());
								userCareer.setPositionType(dajieUserInfo
										.getPositionType());
								userProfileService.createUserCareer(userCareer);

								// user dajie education info initial user
								// education
								// info
								UserEducation userEducation = new UserEducation();
								String school = dajieUserInfo.getSchool();
								userEducation.setDegree(dajieUserInfo
										.getDegree());
								String label = userProfileService
										.getSchoolLabel(school);
								label = null == label ? "" : label;
								userEducation.setLabel(label);
								userEducation
										.setMajor(dajieUserInfo.getMajor());
								userEducation.setSchool(school);
								userEducation.setUserId(userBase.getUserId());
								userProfileService
										.createUserEducation(userEducation);
							} catch (Exception e) {
								logger.error(
										"extract info from dajie cause exception. but--just extra error,so init will continue.",
										e);
							}
						}
					}
					// elastic search point
					personMapObjectService
							.addNewPersonMapObject(userBase, 0, 0);
					return true;
				}
				// if userbase insert suc,platform insert error.this user will
				// register next time.
				else {
					// or delete userbase
					logger.error("init a new user which platform reg,insert platform_map error");
				}
			} else {
				logger.error("init a new user which platform reg,insert user base return <=0");
			}
		} catch (Exception e) {
			logger.error("initial a new user error", e);
		}
		return false;
	}

	private LoginReturn constructSuccessLoginView(UserBase userBase) {
		if (userBase == null) {
			return null;
		}
		/**
		 * add last login time
		 */
		userProfileService.updateLastLogin(userBase.getUserId());

		LoginReturn loginReturn = new LoginReturn();

		// user ticket
		loginReturn.setCode(CommonResultCode.OP_SUCC);
		UserPassport userPassport = getTicketByUserId(userBase.getUserId());
		loginReturn.setT(userPassport.getTicket());
		loginReturn.setSecretKey(userPassport.getUserSecretKey());

		// userbase
		loginReturn.setUserId(userBase.getUserId());
		loginReturn.setAvatar(userBase.getAvatar());
		loginReturn.setAvatarMask(userBase.getAvatarMask());
		loginReturn.setName(userBase.getName());
		loginReturn.setGender(userBase.getGender());
		loginReturn.setMobile(userBase.getMobile());
		loginReturn.setEmail(userBase.getEmail());
		//返回的是上次登陆时间(本次登陆时间调用方已知，返回没有意义)
		loginReturn.setLastLogin(userBase.getLastLogin());

		// 工作和教育信息
		UserCareer userCareer = userProfileService.getUserCareer(userBase
				.getUserId());
		UserEducation userEducation = userProfileService
				.getUserEducation(userBase.getUserId());
		if (userCareer != null) {
			loginReturn.setPositionName(userCareer.getPositionName());
			loginReturn.setCorpName(userCareer.getCorpName());
			loginReturn.setIndustry(userCareer.getIndustry());
			loginReturn.setVerification(userCareer.getVerification());
			loginReturn.setPositionType(userCareer.getPositionType());
		}
		if (userEducation != null) {
			loginReturn.setMajor(userEducation.getMajor());
			loginReturn.setSchool(userEducation.getSchool());
			loginReturn.setLabel(userEducation.getLabel());
			loginReturn.setDegree(userEducation.getDegree());
		}
		// bind info
		Map<String, Map<String, String>> bindMap = getBindInfo(userBase
				.getUserId());
		loginReturn.setBindMap(bindMap);
		return loginReturn;

	}

	private void cleanVerifyCode(String mobile, SmsSendTypeEnum typeEnum) {
		String prefix = getSmsCodeCachePrefix(typeEnum);
		redisCache.del(prefix, mobile);
		logger.info("valid verify code return true,clear verify code in redis");
	}

	private boolean isCompleteRegInfo(UserBase userBase) {
		if (userBase == null) {
			return false;
		} else {
			String name = userBase.getName();
			String avatar = userBase.getAvatar();
			boolean isRegInfoComplete = StringUtil.isNotEmpty(name)
					&& StringUtil.isNotEmpty(avatar);
			return isRegInfoComplete;
		}
	}

	private UserPassport getTicketByUserId(int userId) {
		UserPassport userPassport = new UserPassport();
		userPassport.setUserId(userId);
		userPassport.setCreateTime(System.currentTimeMillis());
		passportService.createPassport(userPassport);
		return userPassport;
	}

}
