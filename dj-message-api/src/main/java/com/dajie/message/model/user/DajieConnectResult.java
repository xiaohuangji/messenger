package com.dajie.message.model.user;

public class DajieConnectResult {
	// 返回码
	private int code;

	// dajie userId
	private int uid;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public DajieConnectResult(int code) {
		this.code = code;
	}
}
