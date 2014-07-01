package com.dajie.message.model.user;

public class DajieUserInfo extends ExtUserInfo {

	// work info
	private String positionName;

	private String corpName;

	private int corpId;

	private int industry;

	private boolean isVerifyEmployee;

	private int workYear;

	private int positionType;

	// education
	private String school;

	private String major;

	private int degree;

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

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public int getCorpId() {
		return corpId;
	}

	public void setCorpId(int corpId) {
		this.corpId = corpId;
	}

	public int getIndustry() {
		return industry;
	}

	public void setIndustry(int industry) {
		this.industry = industry;
	}

	public boolean isVerifyEmployee() {
		return isVerifyEmployee;
	}

	public void setVerifyEmployee(boolean isVerifyEmployee) {
		this.isVerifyEmployee = isVerifyEmployee;
	}

	public int getWorkYear() {
		return workYear;
	}

	public void setWorkYear(int workYear) {
		this.workYear = workYear;
	}

	public int getPositionType() {
		return positionType;
	}

	public void setPositionType(int positionType) {
		this.positionType = positionType;
	}

}
