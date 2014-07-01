package com.dajie.message.model.job;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JobThirdPartInfo {

	private String name;
	
	private String positionIndustry;
	
	private String positionFunction;
	
	private String salary;
	
	private String salaryEnd;
	
	private int experience;
	
	private int degree;
	
	@JsonIgnore
	private String poi;
	
	private String feature;
	
	private String intro;
	
	private String corpName;
	/**
	 * 主站定义的poi
	 */
	private JobThirdPartPOI jpoi; 
	
	private Date createDate;
	
	private String welfare;
	
	private Integer seq;
	
	private String profession;
	
	private String department;
	
	private String headCount;

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPositionIndustry() {
		return positionIndustry;
	}

	public void setPositionIndustry(String positionIndustry) {
		this.positionIndustry = positionIndustry;
	}

	public String getPositionFunction() {
		return positionFunction;
	}

	public void setPositionFunction(String positionFunction) {
		this.positionFunction = positionFunction;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getSalaryEnd() {
		return salaryEnd;
	}

	public void setSalaryEnd(String salaryEnd) {
		this.salaryEnd = salaryEnd;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public String getPoi() {
		return poi;
	}

	public void setPoi(String poi) {
		if(poi != null && !"".equals(poi)){
			ObjectMapper m = new ObjectMapper();
			try {
				this.setJpoi(m.readValue(poi, JobThirdPartPOI.class));
			} catch (Exception e) {
			}
		}
		this.poi = poi;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getWelfare() {
		return welfare;
	}

	public void setWelfare(String welfare) {
		this.welfare = welfare;
	}

	public JobThirdPartPOI getJpoi() {
		return jpoi;
	}

	public void setJpoi(JobThirdPartPOI jpoi) {
		this.jpoi = jpoi;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getHeadCount() {
		return headCount;
	}

	public void setHeadCount(String headCount) {
		this.headCount = headCount;
	}
	
}
