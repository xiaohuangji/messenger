package com.dajie.message.model.user;

import java.io.Serializable;

/**
 * Created by John on 5/29/14.
 */
public class UserEmailVerify implements Serializable {

	private static final long serialVersionUID = -2683776179560595108L;

	/**
	 * userId
	 */
	private int userId;

	/**
	 * 目前公司
	 */
	private String corpName;

	/**
	 * email
	 */
	private String email;

	public UserEmailVerify() {
	}

	public UserEmailVerify(int userId, String corpName, String email) {
		this.userId = userId;
		this.corpName = corpName;
		this.email = email;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UserEmailVerify))
			return false;
		UserEmailVerify other = (UserEmailVerify) obj;
		if (corpName == null) {
			if (other.corpName != null)
				return false;
		} else if (!corpName.equals(other.corpName))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

}
