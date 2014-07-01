package com.dajie.message.dao.provider;

public class JobDaoProvider {
	
	public String selectJobByJobIds(String ids){
		StringBuilder builder = new StringBuilder();
		builder.append("select jobId,userId,positionName,jobType,corpName,corpId,salaryStart,salaryEnd,workExperience,educationLevel,industry,descriptionIds,poiUids,status,startDate,endDate,update_date,hunter from "
				+ "job_table" + " where jobId in("+ids+")");
		return builder.toString();
	}
}
