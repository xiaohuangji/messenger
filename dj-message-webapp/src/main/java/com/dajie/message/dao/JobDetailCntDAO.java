package com.dajie.message.dao;

import com.dajie.message.annotation.database.DBBean;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;


@DBBean
public interface JobDetailCntDAO {

	public static final String JOB_DETAIL_CNT_TABLE= "job_detail_cnt";
	
	@Insert("insert into "+JOB_DETAIL_CNT_TABLE+"(jobId,cnt,createDate) values(#{jobId},#{cnt},#{createDate})")
	public int insertJobDetailCnt(@Param("jobId")int jobId,@Param("cnt")int cnt,@Param("createDate")Date createDate);
	
	@Select("select cnt from "+JOB_DETAIL_CNT_TABLE+" where jobId=#{jobId}")
	public Integer selectJobDetailCnt(int jobId);
	
	@Update("update "+JOB_DETAIL_CNT_TABLE+" set cnt=#{cnt} where jobId=#{jobId}")
	public int updateJobDetailCnt(@Param("jobId")int jobId,@Param("cnt")int cnt);

    @Select("select jobId from "+JOB_DETAIL_CNT_TABLE+" where createDate > #{createDate} order by cnt desc limit 30")
    public List<Integer> getHotJobs(@Param("createDate")Date createDate);

	
}
