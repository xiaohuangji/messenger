package com.dajie.message.model.user;

import java.io.Serializable;
import java.util.Date;

public class UserBase implements Serializable {

	private static final long serialVersionUID = 2985068143079306930L;

	/**
	 * userId
	 */
	private int userId;

	/**
	 * 密码
	 */
	private transient String password;

	/**
	 * 盐
	 */
	private transient String salt;

	/**
	 * 名字
	 */
	private String name;

	/**
	 * 性别
	 */
	private int gender;

	/**
	 * 头像url
	 */
	private String avatar;

	/**
	 * 遮罩过的新头像url
	 */
	private String avatarMask;

	/**
	 * email
	 */
	private String email;

	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 生日
	 */
	private Date birth;

	/**
	 * 用户类型
	 */
	private int type;

	/**
	 * 用户状态
	 */
	private int status;

	/**
	 * 用户特征
	 */
	private int feature;

	/**
	 * 用户资料审核结果 (支持按位或) 1. 名字未通过 2.教育背景未通过 4.职业背景未通过 8.自定义标签未通过
	 */
	private int audit;

	/**
	 * 用户注册时间
	 */
	private Date createTime;

	/**
	 * 用户最近一次登录时间
	 */
	private Date lastLogin;
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAvatarMask() {
		return avatarMask;
	}

	public void setAvatarMask(String avatarMask) {
		this.avatarMask = avatarMask;
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

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getFeature() {
		return feature;
	}

	public void setFeature(int feature) {
		this.feature = feature;
	}

	public int getAudit() {
		return audit;
	}

	public void setAudit(int audit) {
		this.audit = audit;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastLogin() {
		return lastLogin;
	}
	
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	
}
