package com.dajie.message.enums;

public enum PlatformEnum implements CodedEnum {
	UNKNOWN(0),

	DAJIE(1),

	SINA_WEIBO(2),

	TECENT_QQ(3),

	RENREN(4);

	private PlatformEnum(int code) {
		this.code = code;
	}

	private int code;

	public int getCode() {
		return code;
	}

	public static PlatformEnum parse(int code) {
		for (PlatformEnum type : PlatformEnum.values()) {
			if (type.getCode() == code) {
				return type;
			}
		}
		return UNKNOWN;
	}
}
