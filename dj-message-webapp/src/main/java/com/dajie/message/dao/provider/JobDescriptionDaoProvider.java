package com.dajie.message.dao.provider;

public class JobDescriptionDaoProvider {

	public String selectDescriptionByIds(String ids){
		StringBuilder builder = new StringBuilder();
		builder.append("select id,description,createTime from job_description where id in("+ids+")");
		return builder.toString();
	}
}
