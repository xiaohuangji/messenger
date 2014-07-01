package com.dajie.message.dao;


import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.dajie.message.annotation.database.DBBean;
import com.dajie.message.model.job.JobThirdPartInfo;
import com.dajie.message.model.job.JobThirdPartModel;


@DBBean
public interface JobThirdPartDAO {

	public static final String TABLE = "job_third_part";
	
	@Insert("insert into "+TABLE + "(jid,cid,uid,corp_name,title,name,profession,industry,kind,recruit_type,is_intern,"
			+ "department,city,head_count,salary,welfare,corpad,type,status,post_status,end_date,validity,start_date,cv_eng,"
			+ " update_date,create_date,corp_quality,corp_scale,corp_rank,intro,email,project_tag,salary_end,internship_days,"
			+ "internship_period,position_exper,position_function,position_industry,display_type,poi,feature,address,experience,degree) values("
			+ "#{jid},#{cid},#{uid},#{corp_name},#{title},#{name},#{profession},#{industry},#{kind},#{recruit_type},#{is_intern},"
			+ "#{department},#{city},#{head_count},#{salary},#{welfare},#{corpad},#{type},#{status},#{post_status},#{end_date},#{validity},#{start_date},#{cv_eng},"
			+ "#{update_date},#{create_date},#{corp_quality},#{corp_scale},#{corp_rank},#{intro},#{email},#{project_tag},#{salary_end},#{internship_days},"
			+ "#{internship_period},#{position_exper},#{position_function},#{position_industry},#{display_type},#{poi},#{feature},#{address},#{experience},#{degree}"
			+ ""
			+ ")" )
	public int insertJob(JobThirdPartModel job);
	
	@Select("select jid from " + TABLE + " where jid=#{jid}")
	public List<String> selectJid(String jid);
	
	@Delete("delete from "+ TABLE+" where jid=#{jid}")
	public int deleteJobThirdPartInfo(String jid);
	
	@Select("select name,department,profession,position_industry,position_function,salary,salary_end,experience,degree,poi,feature,intro,create_date,welfare,corp_name from "
			+ TABLE + " where corp_name = #{corp_name} and create_date < #{timestamp} order by update_date desc limit 0,#{pageSize}")
	@Results({
		@Result(property="positionIndustry",column="position_industry"),
		@Result(property="positionFunction",column="position_function"),
		@Result(property="salaryEnd",column="salary_end"),
		@Result(property="createDate",column="create_date"),
		@Result(property="corpName",column="corp_name")
	})
	public List<JobThirdPartInfo> selectJobThirdPartInfo(@Param("corp_name")String corpName,@Param("timestamp")Date timestamp,@Param("pageSize")int pageSize);
	
	@Select("select seq,name,department,profession,position_industry,position_function,salary,salary_end,experience,degree,poi,feature,intro,create_date,welfare,corp_name,head_count from "
			+ TABLE + " where uid = #{userId} and type=100 order by create_date desc limit #{seq},#{pageSize}")
	@Results({
		@Result(property="positionIndustry",column="position_industry"),
		@Result(property="positionFunction",column="position_function"),
		@Result(property="salaryEnd",column="salary_end"),
		@Result(property="createDate",column="create_date"),
		@Result(property="corpName",column="corp_name"),
		@Result(property="headCount",column="head_count")
	})	
	public List<JobThirdPartInfo> selectDaJieThirdPartInfoByUserId(@Param("userId")int userId,@Param("seq")int seq,@Param("pageSize")int pageSize);
	
	@Select("select seq,name,department,profession,position_industry,position_function,salary,salary_end,experience,degree,poi,feature,intro,create_date,welfare,corp_name,head_count from "
			+ TABLE + " where corp_name = #{corp_name} and type=0 order by create_date desc limit #{seq},#{pageSize}")
	@Results({
		@Result(property="positionIndustry",column="position_industry"),
		@Result(property="positionFunction",column="position_function"),
		@Result(property="salaryEnd",column="salary_end"),
		@Result(property="createDate",column="create_date"),
		@Result(property="corpName",column="corp_name"),
		@Result(property="headCount",column="head_count")
	})
	public List<JobThirdPartInfo> selectThreeSiteJobThirdPartInfo(@Param("corp_name")String corpName,@Param("seq")int seq,@Param("pageSize")int pageSize);

	
}
