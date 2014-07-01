package com.dajie.message.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dajie.message.enums.GenderEnum;
import com.dajie.message.enums.PlatformEnum;
import com.dajie.message.model.user.ExtUserInfo;
import com.dajie.message.util.StringUtil;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.Avatar;
import com.qq.connect.javabeans.qzone.UserInfoBean;

@Component("tecentQQProcessor")
public class TecentQQProcessor extends PlatformProcessor {

	private static Logger logger = Logger.getLogger(TecentQQProcessor.class);

	public TecentQQProcessor() {
		super(PlatformEnum.TECENT_QQ);
	}

	public String getOpenId(String accessToken) {
		OpenID openID = new OpenID(accessToken);
		String openId = "";
		try {
			openId = openID.getUserOpenID();
		} catch (QQConnectException e) {
			logger.error("tecent QQ connect get openID error", e);
		}
		return openId;
	}

	public UserInfoBean getUserInfo(String accessToken, String openId) {
		UserInfo userInfo = new UserInfo(accessToken, openId);
		UserInfoBean userInfoBean = null;
		try {
			userInfoBean = userInfo.getUserInfo();
		} catch (QQConnectException e) {
			logger.error("tecent QQ connect get getUserInfo error", e);
		}
		return userInfoBean;
	}

	@Override
	ExtUserInfo getExtUserBase(String accessToken) {
		String openId = getOpenId(accessToken);
		UserInfoBean userInfo = getUserInfo(accessToken, openId);
		if (null == userInfo) {
			return null;
		} else {
			ExtUserInfo extUserInfo = new ExtUserInfo();
			Avatar avatar = userInfo.getAvatar();
			String avatarUrl = avatar.getAvatarURL100();
			if (!StringUtil.isEmpty(avatarUrl)) {
				extUserInfo.setAvatar(avatarUrl);
			}
			extUserInfo.setName(userInfo.getNickname());
			extUserInfo.setExtUserId(openId);
			extUserInfo.setEmail("");
			String gender = userInfo.getGender();
			GenderEnum genderEnum = GenderEnum.UNKNOWN;
			if ("男".equals(gender)) {
				genderEnum = GenderEnum.MALE;
			} else if ("女".equals(gender)) {
				genderEnum = GenderEnum.FEMALE;
			}
			extUserInfo.setGender(genderEnum);

			return extUserInfo;
		}
	}

	@Override
	String getExtUserId(String accessToken) {
		return getOpenId(accessToken);
	}

}
