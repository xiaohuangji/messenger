package com.dajie.message.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.dajie.message.annotation.database.DBBean;
import com.dajie.message.model.job.JobDescriptionRelationInfo;

@DBBean
public interface JobDescriptionUserRelationDao {

	public static final String JOB_DESCRIPTION_USER_RELATION_TABLE = "job_description_user";
	
	@Insert("insert into "+JOB_DESCRIPTION_USER_RELATION_TABLE+"(userId,descriptionId,createDate) values(#{userId},#{descriptionId},#{createDate})")
	public void addRelation(@Param("userId")int userId,@Param("descriptionId")int descriptionId,@Param("createDate")Date createDate);
	
	@Select("select id,descriptionId,userId,createDate from "+JOB_DESCRIPTION_USER_RELATION_TABLE+" where userId=#{userId} and createDate < #{timestamp} order by createDate desc limit 0,#{pageSize}")
	public List<JobDescriptionRelationInfo> selectRelationByUserId(@Param("userId")int userId,@Param("timestamp")Date timestamp,@Param("pageSize")int pageSize);
	
	@Delete("delete from "+JOB_DESCRIPTION_USER_RELATION_TABLE + " where userId = #{userId} and descriptionId=#{descriptionId}")
	public void deleteRelation(@Param("userId")int userId,@Param("descriptionId")int descriptionId);
	
	@Select("select id,descriptionId,userId,createDate from "+JOB_DESCRIPTION_USER_RELATION_TABLE+" where userId=#{userId} and descriptionId=#{descriptionId}")
	public JobDescriptionRelationInfo selectRelationByUserIdAndDesrId(@Param("userId")int userId,@Param("descriptionId")int descriptionId);
}
