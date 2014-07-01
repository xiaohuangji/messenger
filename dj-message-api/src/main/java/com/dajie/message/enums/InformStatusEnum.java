package com.dajie.message.enums;

public enum InformStatusEnum implements CodedEnum {
	NO_PROCESS(0),PROCESS(1);

	private int code;
	private InformStatusEnum(int code){
		this.code = code;
	}
	
	@Override
	public int getCode() {	
		return code;
	}

}
