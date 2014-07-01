package com.dajie.message.mcp.model;

public class ErrorModel {
	private int code;
	private String msg;
	

	public ErrorModel(int code, String msg) {
		this.setCode(code);
		this.msg = msg;
	}

	public ErrorModel() {
	}

	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
