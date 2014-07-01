package com.dajie.message.enums;

public enum SmsSendTypeEnum implements CodedEnum {
	UNKNOWN(0), REGISTER(1), RESET_PWD(2),RESET_MOBILE(3);

	private int code;

	private SmsSendTypeEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static SmsSendTypeEnum parse(int code) {
		for (SmsSendTypeEnum type : SmsSendTypeEnum.values()) {
			if (type.getCode() == code) {
				return type;
			}
		}
		return UNKNOWN;
	}
}
