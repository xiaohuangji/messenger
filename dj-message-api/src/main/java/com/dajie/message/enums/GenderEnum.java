package com.dajie.message.enums;

public enum GenderEnum implements CodedEnum {
	UNKNOWN(0), MALE(1), FEMALE(2);

	private int code;

	@Override
	public int getCode() {
		return code;
	}

	private GenderEnum(int code) {
		this.code = code;
	}

	public static GenderEnum parse(int code) {
		for (GenderEnum gender : GenderEnum.values()) {
			if (gender.getCode() == code) {
				return gender;
			}
		}
		return UNKNOWN;
	}
}
