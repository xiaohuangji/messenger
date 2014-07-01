package com.dajie.message.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import weibo4j.Account;
import weibo4j.Users;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

import com.dajie.message.enums.GenderEnum;
import com.dajie.message.enums.PlatformEnum;
import com.dajie.message.model.user.ExtUserInfo;

@Component("sinaWeiboProcessor")
public class SinaWeiboProcessor extends PlatformProcessor {

	public SinaWeiboProcessor() {
		super(PlatformEnum.SINA_WEIBO);
	}

	private static Logger logger = Logger.getLogger(SinaWeiboProcessor.class);

	public String getUid(String accessToken) {
		Account account = new Account();
		account.setToken(accessToken);
		try {
			JSONObject uid = account.getUid();
			logger.info("sina weibo getUid return:" + uid);
			return uid.getString("uid");
		} catch (WeiboException e) {
			logger.error("get sinaweibo uid error cause by weiboException", e);
			return null;
		} catch (JSONException e) {
			logger.error("get sinaweibo uid json exception", e);
			return null;
		}
	}

	public User getUser(String accessToken, String uid) {
		Users um = new Users();
		um.client.setToken(accessToken);
		try {
			User user = um.showUserById(uid);
			logger.info("sina weibo get user:" + user.toString());
			return user;
		} catch (WeiboException e) {
			logger.error("get sinaweibo user error", e);
			return null;
		}
	}

	@Override
	ExtUserInfo getExtUserBase(String accessToken) {
		String uid = getUid(accessToken);
		User user = getUser(accessToken, uid);
		if (null == user) {
			return null;
		} else {
			ExtUserInfo extUserInfo = new ExtUserInfo();
			extUserInfo.setAvatar(user.getAvatarLarge());
			extUserInfo.setEmail("");
			extUserInfo.setExtUserId(uid);
			extUserInfo.setName(user.getName());
			GenderEnum genderEnum = GenderEnum.UNKNOWN;
			String gender = user.getGender();
			if ("m".equals(gender)) {
				genderEnum = GenderEnum.MALE;
			} else if ("f".equals(gender)) {
				genderEnum = GenderEnum.FEMALE;
			}
			extUserInfo.setGender(genderEnum);
			return extUserInfo;
		}

	}

	@Override
	String getExtUserId(String accessToken) {
		return getUid(accessToken);
	}



}
