package com.dajie.message.model.user;

import java.io.Serializable;

/**
 * Created by John on 4/15/14.
 */
public class UserCareer implements Serializable {

	private static final long serialVersionUID = 7004301955870017806L;

	/**
	 * userId
	 */
	private int userId;

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
	 * 公司Id
	 */
	private int corpId;

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

	public UserCareer() {
	}

	public UserCareer(int userId, String positionName, int positionType, 
			String corpName, int corpId, int industry, int experience, int education) {
		this.userId = userId;
		this.positionName = positionName;
		this.positionType = positionType;
		this.corpName = corpName;
		this.corpId = corpId;
		this.industry = industry;
		this.experience = experience;
		this.education = education;		
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public int getCorpId() {
		return corpId;
	}

	public void setCorpId(int corpId) {
		this.corpId = corpId;
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

}
