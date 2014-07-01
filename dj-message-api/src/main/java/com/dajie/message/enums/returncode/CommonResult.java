package com.dajie.message.enums.returncode;

public enum CommonResult implements ReturnCodeEnum {
	OP_SUCC(0, "success"),
	OP_FAIL(-1, "fail");

	private int code;

	private String msg;

	private CommonResult(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String getMsg() {
		return msg;
	}
}
