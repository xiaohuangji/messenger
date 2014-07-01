package com.dajie.message.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.dajie.message.enums.GenderEnum;
import com.dajie.message.enums.PlatformEnum;
import com.dajie.message.model.user.ExtUserInfo;
import com.renren.api.AccessToken;
import com.renren.api.AccessToken.Type;
import com.renren.api.DefaultRennExecutor;
import com.renren.api.RennException;
import com.renren.api.RennExecutor;
import com.renren.api.mapper.ObjectMapper;
import com.renren.api.service.Image;
import com.renren.api.service.ImageSize;
import com.renren.api.service.Sex;
import com.renren.api.service.User;
import com.renren.api.service.UserService;

@Component("renrenProcessor")
public class RenrenProcessor extends PlatformProcessor {

	public RenrenProcessor() {
		super(PlatformEnum.RENREN);
	}

	private static final Logger LOGGER = Logger
			.getLogger(RenrenProcessor.class);
	RennExecutor executor = new DefaultRennExecutor();
	ObjectMapper mapper = new ObjectMapper();

	public long getRenrenId(String token) {
		AccessToken accessToken = new AccessToken(Type.Bearer, token, null,
				null, null);
		UserService userService = new UserService(executor, accessToken, mapper);
		long renrenId = 0L;
		try {
			User user = userService.getUserLogin();
			renrenId = user.getId();
		} catch (Exception e) {
			LOGGER.error("get renren user id exception.", e);
		}
		return renrenId;
	}

	public User getRenrenUser(String token, long renrenId) {
		AccessToken accessToken = new AccessToken(Type.Bearer, token, null,
				null, null);
		UserService userService = new UserService(executor, accessToken, mapper);
		User user = null;
		try {
			user = userService.getUser(renrenId);
		} catch (RennException e) {
			LOGGER.error("get renren user info exception.", e);
		}
		return user;
	}

	@Override
	ExtUserInfo getExtUserBase(String accessToken) {
		long renrenId = getRenrenId(accessToken);
		User user = getRenrenUser(accessToken, renrenId);
		if (null == user) {
			return null;
		} else {
			ExtUserInfo extUserBase = new ExtUserInfo();
			// baseInfo
			List<Image> imageList = user.getAvatar();
			Image largeImage = null;
			Image tinyImage = null;
			for (Image img : imageList) {
				if (img.getSize() == ImageSize.LARGE) {
					largeImage = img;
				} else if (img.getSize() == ImageSize.TINY) {
					tinyImage = img;
				}
			}
			String avatar = (largeImage == null && tinyImage == null) ? null
					: ((largeImage != null) ? largeImage.getUrl() : tinyImage
							.getUrl());

			GenderEnum genderEnum = GenderEnum.UNKNOWN;
			if (user.getBasicInformation() != null) {
				genderEnum = user.getBasicInformation().getSex() == Sex.MALE ? GenderEnum.MALE
						: GenderEnum.FEMALE;
			}
			extUserBase.setExtUserId(user.getId() + "");
			extUserBase.setAvatar(avatar);
			extUserBase.setEmail("");
			extUserBase.setName(user.getName());
			extUserBase.setGender(genderEnum);
			return extUserBase;
		}

	}

	@Override
	String getExtUserId(String accessToken) {
		long renrenId = getRenrenId(accessToken);
		return renrenId > 0 ? renrenId + "" : "";
	}

}
