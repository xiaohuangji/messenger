package com.dajie.message.model.user;

import java.io.Serializable;

/**
 * Created by John on 5/29/14.
 */
public class VerificationView implements Serializable {

	private static final long serialVersionUID = -4877559666375475833L;

	/**
	 * 认证状态
	 */
	private int verification;
	
	/**
	 * 最近一次认证时填写的公司
	 */
	private String corpName;

	/**
	 * 最近一次认证时填写的email
	 */
	private String email;
	
	/**
	 * 认证提交的状态码 0.没有发验证邮件 1.已发邮件，但未点击；2.邮件已点击，等待审核
	 */
	private int status;
	
	public int getVerification() {
		return verification;
	}

	public void setVerification(int verification) {
		this.verification = verification;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
