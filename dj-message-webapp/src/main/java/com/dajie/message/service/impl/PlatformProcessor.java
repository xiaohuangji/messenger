package com.dajie.message.service.impl;

import com.dajie.message.enums.PlatformEnum;
import com.dajie.message.model.user.ExtUserInfo;

public abstract class PlatformProcessor {

	private PlatformEnum platformType;

	public PlatformEnum getPlatformType() {
		return platformType;
	}

	public PlatformProcessor(PlatformEnum platformType) {
		this.platformType = platformType;
	}

	abstract ExtUserInfo getExtUserBase(String accessToken);

	abstract String getExtUserId(String accessToken);

}
