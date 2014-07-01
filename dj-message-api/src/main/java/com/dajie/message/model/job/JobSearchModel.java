package com.dajie.message.model.job;

import java.util.List;


/**
 * 向search中添加的信息
 * @author xinquan.guan
 *
 */
public class JobSearchModel extends JobModel{
	
	/**
	 * 地理信息唯一标识
	 */
	private  String currentUid;
	
	/**
	 * 描述
	 */
	private List<JobDescriptionModel> descriptions;
	

	public JobSearchModel(JobModel model,List<JobDescriptionModel> descriptions,String currentUid) {
		
		this.setCorpId(model.getCorpId());
		this.setCorpName(model.getCorpName());
		this.setCreateDate(model.getCreateDate());
		this.setDescriptions(descriptions);
		this.setCurrentUid(currentUid);
		this.setDescriptionIds(model.getDescriptionIds());
		this.setEducationLevel(model.getEducationLevel());
		this.setStartDate(model.getStartDate());
		this.setEndDate(model.getEndDate());
		this.setJobId(model.getJobId());
		this.setJobType(model.getJobType());
		this.setPositionName(model.getPositionName());
		this.setPoiUids(model.getPoiUids());
		this.setSalaryEnd(model.getSalaryEnd());
		this.setSalaryStart(model.getSalaryStart());
		this.setUpdateDate(model.getUpdateDate());
		this.setUserId(model.getUserId());
		this.setWorkExperience(model.getWorkExperience());
		
	}
	
public JobSearchModel(JobModel model,List<JobDescriptionModel> descriptions) {
		
		this.setCorpId(model.getCorpId());
		this.setCorpName(model.getCorpName());
		this.setCreateDate(model.getCreateDate());
		this.setDescriptions(descriptions);
		this.setDescriptionIds(model.getDescriptionIds());
		this.setEducationLevel(model.getEducationLevel());
		this.setStartDate(model.getStartDate());
		this.setEndDate(model.getEndDate());
		this.setJobId(model.getJobId());
		this.setJobType(model.getJobType());
		this.setPositionName(model.getPositionName());
		this.setPoiUids(model.getPoiUids());
		this.setSalaryEnd(model.getSalaryEnd());
		this.setSalaryStart(model.getSalaryStart());
		this.setUpdateDate(model.getUpdateDate());
		this.setUserId(model.getUserId());
		this.setWorkExperience(model.getWorkExperience());
		
	}
	
	public JobSearchModel() {
	}

	public List<JobDescriptionModel> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<JobDescriptionModel> descriptions) {
		this.descriptions = descriptions;
	}

	public String getCurrentUid() {
		return currentUid;
	}

	public void setCurrentUid(String currentUid) {
		this.currentUid = currentUid;
	}
}
