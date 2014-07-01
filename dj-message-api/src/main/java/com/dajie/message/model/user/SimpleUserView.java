package com.dajie.message.model.user;

import java.io.Serializable;

/**
 * Created by John on 4/29/14.
 */
public class SimpleUserView implements Serializable {

	private static final long serialVersionUID = 7746768626746193160L;

	/**
	 * userId
	 */
	private int userId;

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
	 * 专业
	 */
	private String major;
	
	/**
	 * 学校
	 */
	private String school;
	
	/**
	 * 学位
	 */
	private int degree;

	/**
	 * 职位名称
	 */
	private String positionName;
	
	/**
	 * 目前公司
	 */
	private String corpName;
	
	/**
	 * 工作经验
	 */
	private int experience;
	
	/**
	 * 最高学历
	 */
	private int education;

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
	
	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
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

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public int getEducation() {
		return education;
	}

	public void setEducation(int education) {
		this.education = education;
	}

}
