package com.dajie.message.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.dajie.message.annotation.database.DBBean;
import com.dajie.message.dao.provider.JobDaoProvider;
import com.dajie.message.dao.provider.JobDescriptionDaoProvider;
import com.dajie.message.model.job.JobInformModel;
import com.dajie.message.model.job.JobModel;
/**
 * 
 * @author xinquan.guan
 *
 */

@DBBean
public interface JobDAO {

	static final String JOB_TABLE = "job_table";
	
	
	@Insert("insert into "+JOB_TABLE +"("
			+ "userId,positionName,jobType,corpName,corpId,salaryStart,salaryEnd,workExperience,educationLevel,industry,"
			+ "descriptionIds,poiUids,status,startDate,endDate,update_date,createDate,oldJid,hunter)"+"values("
			+ "#{userId},#{positionName},#{jobType},#{corpName},#{corpId},#{salaryStart},#{salaryEnd},#{workExperience},#{educationLevel},#{industry},"
			+ "#{descriptionIds},#{poiUids},#{status},#{startDate},#{endDate},#{updateDate},#{createDate},#{oldJid},#{hunter})")
	@SelectKey(keyProperty="jobId",resultType=Integer.class,before=false,statement="SELECT LAST_INSERT_ID() AS jobId")
	public void insertJob(JobModel model);
	
	@Select("select jobId,userId,positionName,jobType,corpName,corpId,salaryStart,salaryEnd,workExperience,educationLevel,industry,descriptionIds,poiUids,status,startDate,endDate,update_date,createDate,hunter from "
			+ JOB_TABLE + " where userId = #{userId} and createDate < #{timestamp} order by createDate desc limit 0,#{pageSize}")
	@Results({
		@Result(property="updateDate",column="update_date")
	})
	public List<JobModel> selectJobModelByUserId(@Param("userId")int userId,@Param("timestamp")Date timestamp,@Param("pageSize")int pageSize);
	
	@Select("select jobId,userId,positionName,jobType,corpName,corpId,salaryStart,salaryEnd,workExperience,educationLevel,industry,descriptionIds,poiUids,status,startDate,endDate,update_date,hunter,createDate from "
			+ JOB_TABLE + " where jobId = #{jobId}")
	@Results({
		@Result(property="updateDate",column="update_date")
	})
	public JobModel selectJobModelByJobId(int jobId);
	
	@Delete("delete from "+JOB_TABLE + " where jobId = #{jobId}")
	public void deleteJobModel(int jobId);
	
	@Update("update "+JOB_TABLE+" set poiUids=#{poiUids} where jobId=#{jobId}")
	public int updateJobPoiUids(@Param("jobId") int jobId,@Param("poiUids")String poiUids);
	
	@Update("update "+JOB_TABLE+" set userId=#{userId},positionName=#{positionName},jobType=#{jobType},"
			+ "corpName=#{corpName},corpId=#{corpId},salaryStart=#{salaryStart},"
			+ "salaryEnd=#{salaryEnd},workExperience=#{workExperience},"
			+ "educationLevel=#{educationLevel},industry=#{industry},descriptionIds=#{descriptionIds},"
			+ "poiUids=#{poiUids},startDate=#{startDate},endDate=#{endDate},update_date=#{updateDate},status=#{status},hunter=#{hunter} where jobId=#{jobId}")
	public int updateJobModel(JobModel model);

	@Select("select oldJid from "+ JOB_TABLE + " where oldJid=#{jid}")
	public List<String> selectOldJid(String jid);
	
	@SelectProvider(type=JobDaoProvider.class,method="selectJobByJobIds")
	public List<JobModel> selectJobModelByJobIds(String ids);
	
}
