package com.dajie.message.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;

import com.dajie.message.annotation.database.DBBean;
import com.dajie.message.dao.provider.JobDescriptionDaoProvider;
import com.dajie.message.model.job.JobDescriptionModel;

@DBBean
public interface JobDescriptionDAO {

	static final String JOB_DESCRIPTION_TABLE = "job_description";
	
	
	@Insert("insert into "+JOB_DESCRIPTION_TABLE+"(description,createTime,md5) values(#{description},#{createDate},#{md5Description})")
	@SelectKey(keyProperty="id",resultType=Integer.class,before=false,statement="SELECT LAST_INSERT_ID() AS id")
	public void insertDescription(JobDescriptionModel model);
	
	@Select("select id,description,createTime from "+JOB_DESCRIPTION_TABLE+" where description=#{description}")
	@Results({
		@Result(property="createDate",column="createTime")
	})
	public List<JobDescriptionModel> selectDescription(String description);
	
	@Select("select id from "+JOB_DESCRIPTION_TABLE+" where md5=#{md5}")
	public Integer selectDescriptionByMd5(@Param("md5")byte[] descriptionMd5);
	
	@SelectProvider(type=JobDescriptionDaoProvider.class,method="selectDescriptionByIds")
	public List<JobDescriptionModel> selectDescriptionsByIds(String ids);
	
	@Select("select * from "+JOB_DESCRIPTION_TABLE + " where id=#{id}")
	public JobDescriptionModel selectDescriptionById(Integer descriptionId);
	
}
