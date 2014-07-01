package com.dajie.message.model.job;

import com.dajie.message.model.user.UserBase;

public class SimpleJobInfo extends JobModel{

	private String hrName;
	
	private String hrAvatar;
	
	private int gender;
	
	private int expire;

	public SimpleJobInfo(JobModel jobModel,UserBase userBase) {
		this.setGender(userBase.getGender());
		this.setHrAvatar(userBase.getAvatar());
		this.setHrName(userBase.getName());
		
		this.setCorpId(jobModel.getCorpId());
		this.setCorpName(jobModel.getCorpName());
		this.setCreateDate(jobModel.getCreateDate());
		this.setEducationLevel(jobModel.getEducationLevel());
		this.setEndDate(jobModel.getEndDate());
		this.setJobId(jobModel.getJobId());
		this.setJobType(jobModel.getJobType());
		this.setPositionName(jobModel.getPositionName());
		this.setSalaryEnd(jobModel.getSalaryEnd());
		this.setSalaryStart(jobModel.getSalaryStart());
		this.setStartDate(jobModel.getStartDate());
		this.setUpdateDate(jobModel.getUpdateDate());
		this.setUserId(jobModel.getUserId());
		this.setWorkExperience(jobModel.getWorkExperience());
		this.setStatus(jobModel.getStatus());
		if(jobModel.getEndDate().getTime() < 10){//没有设置过期时间，永远不过期
			this.setExpire(0);
		}else{
			if(jobModel.getEndDate().getTime() - System.currentTimeMillis() > 0){//设置了过期时间
				this.setExpire(0);
			}else{
				this.setExpire(1);
			}
		}
		//this.setExpire(jobModel.getEndDate().getTime());
		
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



	public int getGender() {
		return gender;
	}



	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}



	
}
