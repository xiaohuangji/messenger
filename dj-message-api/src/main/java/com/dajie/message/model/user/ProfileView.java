package com.dajie.message.model.user;

import java.io.Serializable;
import java.util.List;

/**
 * Created by John on 4/15/14.
 */
public class ProfileView implements Serializable {

	private static final long serialVersionUID = -9099825181996382185L;

	/**
	 * 查看者与资料所属人的关系，0表示陌生人，1表示本人，2表示好友
	 */
	private int relationship;

	/**
	 * 查看者与资料所属人的关系，是否被拉黑或把对方拉黑
	 */
	private int blocked;

	/**
	 * 用户id
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
	 * email
	 */
	private String email;

	/**
	 * 手机号
	 */
	private String mobile;

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
	 * 职位类型
	 */
	private int positionType;

	/**
	 * 目前公司
	 */
	private String corpName;

	/**
	 * 认证状态
	 */
	private int verification;

	/**
	 * 所在行业
	 */
	private int industry;

	/**
	 * 工作经验
	 */
	private int experience;

	/**
	 * 最高学历
	 */
	private int education;

	/**
	 * 用户的自定义标签
	 */
	private List<LabelView> labels;

	public int getRelationship() {
		return relationship;
	}

	public void setRelationship(int relationship) {
		this.relationship = relationship;
	}

	public int getBlocked() {
		return blocked;
	}

	public void setBlocked(int blocked) {
		this.blocked = blocked;
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

	public int getPositionType() {
		return positionType;
	}

	public void setPositionType(int positionType) {
		this.positionType = positionType;
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

	public List<LabelView> getLabels() {
		return labels;
	}

	public void setLabels(List<LabelView> labels) {
		this.labels = labels;
	}

}
