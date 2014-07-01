package com.dajie.message.model.job;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Job的基本信息,用于数据库表的存储
 * @author xinquan.guan
 *
 */
public class JobModel {

	/**
	 * job的id
	 */
	private int jobId;
	
	/**
	 * hr用户信息
	 */
	private int userId;
	
	/**
	 * 公司id
	 */
	private int corpId;
	
	/**
	 * 公司名称
	 */
	private String corpName;
	
	/**
	 * 创建时间
	 */
	private Date createDate;
	
	/**
	 * 修改时间
	 */
	private Date updateDate;
	
	/**
	 * 开始时间
	 */
	private Date startDate;
	
	/**
	 * 终止时间
	 */
	private Date endDate;
	/**
	 * 描述
	 */
	@JsonIgnore
	private String descriptionIds;
	
	@JsonIgnore
	private String oldJid;
	
	/**
	 * 猎头
	 */
	private int hunter;
	
	
	public String getOldJid() {
		return oldJid;
	}

	public void setOldJid(String oldJid) {
		this.oldJid = oldJid;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * 地理信息
	 */
	@JsonIgnore
	private String poiUids;
	

	/**
	 * 工作时间
	 */
	private int workExperience;
	
	/**
	 * 
	 */
	private int industry;
	
	/**
	 * 学位
	 */
	private int educationLevel;
	
	/**
	 * 职位名称
	 */
	private String positionName;
	
	/**
	 * 起始薪资
	 */
	private int salaryStart;
	/**
	 * 终止薪资
	 */
	private int salaryEnd;
	
	/**
	 * 工作类型
	 */
	private String jobType;
	
	/**
	 * 状态
	 */
	private int status;
	
	
	
	public int getWorkExperience() {
		return workExperience;
	}

	public void setWorkExperience(int workExperience) {
		this.workExperience = workExperience;
	}

	public int getEducationLevel() {
		return educationLevel;
	}

	public void setEducationLevel(int educationLevel) {
		this.educationLevel = educationLevel;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public int getSalaryStart() {
		return salaryStart;
	}

	public void setSalaryStart(int salaryStart) {
		this.salaryStart = salaryStart;
	}

	public int getSalaryEnd() {
		return salaryEnd;
	}

	public void setSalaryEnd(int salaryEnd) {
		this.salaryEnd = salaryEnd;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
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



	public String getDescriptionIds() {
		return descriptionIds;
	}

	public void setDescriptionIds(String descriptionIds) {
		this.descriptionIds = descriptionIds;
	}

	public String getPoiUids() {
		return poiUids;
	}

	public void setPoiUids(String poiUids) {
		this.poiUids = poiUids;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}


	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public int getIndustry() {
		return industry;
	}

	public void setIndustry(int industry) {
		this.industry = industry;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getHunter() {
		return hunter;
	}

	public void setHunter(int hunter) {
		this.hunter = hunter;
	}


}
