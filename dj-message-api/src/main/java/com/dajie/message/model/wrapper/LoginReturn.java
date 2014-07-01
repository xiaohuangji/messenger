package com.dajie.message.model.wrapper;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class LoginReturn {
	// 返回码
	private int code;

	// 票
	private String t;

	private String secretKey;

	// 基本信息
	private int userId;

	private String name;

	private int gender;

	private String avatar;

	private String avatarMask;

	private String mobile;

	private String email;

	private Date lastLogin;

	// 公司信息
	private String positionName;

	private String corpName;

	private int verification;

	private int industry;

	// 学校信息
	private String major;

	private String school;

	private String label;

	private int degree;

	private int positionType;

	// 第三方绑定信息
	private Map<String, Map<String, String>> bindMap;

	public LoginReturn(int code) {
		this.code = code;
	}

	public LoginReturn() {
	};

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public int getVerification() {
		return verification;
	}

	public void setVerification(int verification) {
		this.verification = verification;
	}

	public int getIndustry() {
		return industry;
	}

	public void setIndustry(int industry) {
		this.industry = industry;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public Map<String, Map<String, String>> getBindMap() {
		return bindMap;
	}

	public void setBindMap(Map<String, Map<String, String>> bindMap) {
		this.bindMap = bindMap;
	}

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public int getPositionType() {
		return positionType;
	}

	public void setPositionType(int positionType) {
		this.positionType = positionType;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

}
