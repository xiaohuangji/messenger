package com.dajie.message.model.user;

import java.io.Serializable;

/**
 * Created by John on 6/10/14.
 */
public class ContactView implements Serializable {

	private static final long serialVersionUID = -2784507481707031346L;

	/**
	 * 大街Uid
	 */
	private String platformUid;

	/**
	 * 名字
	 */
	private String name;

	/**
	 * email
	 */
	private String email;

	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 当天勾搭该用户的人数
	 */
	private int personCount;

	public String getPlatformUid() {
		return platformUid;
	}

	public void setPlatformUid(String platformUid) {
		this.platformUid = platformUid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public int getPersonCount() {
		return personCount;
	}

	public void setPersonCount(int personCount) {
		this.personCount = personCount;
	}

}
