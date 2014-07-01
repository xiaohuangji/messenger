package com.dajie.message.enums;

public enum UserStatus implements CodedEnum {
	NORMAL(0), FREEZE(-1);

	private int code;

	private UserStatus(int code) {
		this.code = code;
	}

	@Override
	public int getCode() {
		return code;
	}

	public static UserStatus parse(int code) {
		for (UserStatus status : UserStatus.values()) {
			if (status.getCode() == code) {
				return status;
			}
		}
		return NORMAL;
	}

}
