package com.dajie.message.model.job;

import java.util.Date;
import java.util.List;

import com.dajie.message.model.map.PoiInfoObject;
import com.dajie.message.model.user.UserBase;
import com.dajie.message.model.user.UserCareer;

/**
 * job 详细信息
 * @author xinquan.guan
 *
 */
public class JobInfo extends JobModel{
	
	/**
	 * 是否过期
	 */
	private boolean overDeadLine;
	
	
	private String hrName;
	
	private String hrAvatar;
	
	private int hrGender;
	
	private String hrCorp;
	
	private String hrTitle;
	
	
	
	
	public String getHrCorp() {
		return hrCorp;
	}

	public void setHrCorp(String hrCorp) {
		this.hrCorp = hrCorp;
	}

	public String getHrTitle() {
		return hrTitle;
	}

	public void setHrTitle(String hrTitle) {
		this.hrTitle = hrTitle;
	}

	/**
	 * 地图上的点信息
	 */
	private List<PoiInfoObject> poiInfoObject;
	
	/**
	 * job 描述信息
	 */
	private List<JobDescriptionModel> jobDescriptionModels;
	
	public JobInfo(JobModel model,UserBase userBase,UserCareer career,List<PoiInfoObject> poiInfoObject,List<JobDescriptionModel> jobDescriptionModel) {
		this.setPoiInfoObject(poiInfoObject);
		this.setHrGender(userBase.getGender());
		this.setHrAvatar(userBase.getAvatar());
		this.setHrName(userBase.getName());
		
		if(model.getEndDate() != null){
			if( System.currentTimeMillis() > model.getEndDate().getTime())
				this.setOverDeadLine(true);
		}
		else
			this.setOverDeadLine(false);
		this.setJobDescriptionModels(jobDescriptionModel);
		
		this.setCorpId(model.getCorpId());
		this.setCorpName(model.getCorpName());
		this.setCreateDate(model.getCreateDate());
		//this.setDescriptionIds(model.getDescriptionIds());
		this.setEducationLevel(model.getEducationLevel());
		this.setStartDate(model.getStartDate());
		this.setEndDate(model.getEndDate());
		this.setJobId(model.getJobId());
		this.setJobType(model.getJobType());
		//this.setPoiUids(poiUids);
		this.setPositionName(model.getPositionName());
		this.setSalaryEnd(model.getSalaryEnd());
		this.setSalaryStart(model.getSalaryStart());
		this.setUpdateDate(model.getUpdateDate());
		this.setUserId(model.getUserId());
		this.setWorkExperience(model.getWorkExperience());
		this.setIndustry(model.getIndustry());
		if(career != null){
			this.setHrCorp(career.getCorpName());
			this.setHrTitle(career.getPositionName());
		}
		
		this.setHunter(model.getHunter());
		this.setStatus(model.getStatus());
		if(model.getEndDate() != null){
			this.setOverDeadLine(model.getEndDate().getTime() > new Date().getTime() ? false : true);
		}
	}

	public String getHrName() {
		return hrName;
	}

	public void setHrName(String hrName) {
		this.hrName = hrName;
	}

	public String getHrAvatar() {
		return hrAvatar;
	}

	public void setHrAvatar(String hrAvatar) {
		this.hrAvatar = hrAvatar;
	}

	public int getHrGender() {
		return hrGender;
	}

	public void setHrGender(int hrGender) {
		this.hrGender = hrGender;
	}

	public JobInfo() {
		// TODO Auto-generated constructor stub
	}
	
	
	public boolean isOverDeadLine() {
		return overDeadLine;
	}

	public void setOverDeadLine(boolean overDeadLine) {
		this.overDeadLine = overDeadLine;
	}

	public List<JobDescriptionModel> getJobDescriptionModels() {
		return jobDescriptionModels;
	}

	public void setJobDescriptionModels(List<JobDescriptionModel> jobDescriptionModels) {
		this.jobDescriptionModels = jobDescriptionModels;
	}

	public List<PoiInfoObject> getPoiInfoObject() {
		return poiInfoObject;
	}

	public void setPoiInfoObject(List<PoiInfoObject> poiInfoObject) {
		this.poiInfoObject = poiInfoObject;
	}
	
	
}
