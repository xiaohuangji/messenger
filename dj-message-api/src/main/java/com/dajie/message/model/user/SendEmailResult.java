package com.dajie.message.model.user;

import java.io.Serializable;

public class SendEmailResult implements Serializable {

	private static final long serialVersionUID = -8930485594008664138L;

	/**
	 * 0：验证邮件发送成功  1：验证邮件发送失败
	 */
	private int status;

	/**
	 * 验证邮件中的url（只在dev和test环境下暴露）
	 */
	private String verifyUrl;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getVerifyUrl() {
		return verifyUrl;
	}

	public void setVerifyUrl(String verifyUrl) {
		this.verifyUrl = verifyUrl;
	}

}
