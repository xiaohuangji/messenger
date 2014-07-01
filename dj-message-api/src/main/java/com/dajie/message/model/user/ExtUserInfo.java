package com.dajie.message.model.user;

import java.util.Date;

import com.dajie.message.enums.GenderEnum;

public class ExtUserInfo {

	// base info
	private String extUserId;

	private String name;

	private GenderEnum gender;

	private String avatar;

	private String email;

	private Date birth;

	public String getExtUserId() {
		return extUserId;
	}

	public void setExtUserId(String extUserId) {
		this.extUserId = extUserId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GenderEnum getGender() {
		return gender;
	}

	public void setGender(GenderEnum gender) {
		this.gender = gender;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

}
