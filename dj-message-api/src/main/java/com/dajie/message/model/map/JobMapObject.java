/**
 * 
 */
package com.dajie.message.model.map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * @author tianyuan.zhu
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobMapObject extends BaseMapObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private int jobId;
	
	private int authorId;
	
	private String authorName;
	
	private String authorPosition;
	
	private String authorAvatar;
	
	private String companyId;
	
	private String companyName;
	
	private String tag;
	
	private int salaryMin;
	
	private int salaryMax;
	
	private int experience;
	
	private int education;
	
	private String jobType;
	
	private long startTime;
	
	private long endTime;
	
	private int industry;
	
	private int status;

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getSalaryMin() {
		return salaryMin;
	}

	public void setSalaryMin(int salaryMin) {
		this.salaryMin = salaryMin;
	}

	public int getSalaryMax() {
		return salaryMax;
	}

	public void setSalaryMax(int salaryMax) {
		this.salaryMax = salaryMax;
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


	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAuthorPosition() {
		return authorPosition;
	}

	public void setAuthorPosition(String authorPosition) {
		this.authorPosition = authorPosition;
	}

	public String getAuthorAvatar() {
		return authorAvatar;
	}

	public void setAuthorAvatar(String authorAvatar) {
		this.authorAvatar = authorAvatar;
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


}
