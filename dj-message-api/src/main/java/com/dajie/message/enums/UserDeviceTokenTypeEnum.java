package com.dajie.message.enums;

public enum UserDeviceTokenTypeEnum implements CodedEnum {
	UNKNOWN(-1), APPLE(0), ANDROID(1);

	private int code;

	public int getCode() {
		return code;
	}

	private UserDeviceTokenTypeEnum(int code) {
		this.code = code;
	}

	public static UserDeviceTokenTypeEnum parse(int code) {
		for (UserDeviceTokenTypeEnum tokenTypeEnum : UserDeviceTokenTypeEnum
				.values()) {
			if (tokenTypeEnum.getCode() == code) {
				return tokenTypeEnum;
			}
		}
		return UNKNOWN;
	}
}
