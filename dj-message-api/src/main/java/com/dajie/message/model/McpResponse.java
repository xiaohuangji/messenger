package com.dajie.message.model;

import java.io.Serializable;

import com.dajie.message.constants.returncode.CommonResultCode;

public class McpResponse<T> implements Serializable {

	private static final long serialVersionUID = 9164640925985064235L;

	private int code;

	private T result;

	public McpResponse(T result) {
		this.code = CommonResultCode.OP_SUCC;
		this.result = result;
	}

	public McpResponse(int code, T result) {
		this.code = code;
		this.result = result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

}
