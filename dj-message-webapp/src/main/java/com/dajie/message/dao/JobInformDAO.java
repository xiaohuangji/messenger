package com.dajie.message.dao;


import com.dajie.message.annotation.database.DBBean;
import com.dajie.message.model.job.JobInformModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

@DBBean
public interface JobInformDAO {

	static final String INFORM_TABLE = "inform_job_table";
	
	@Insert("insert into "+INFORM_TABLE +" ("
			+ "informUserId,jobId,jobUserId,description,status,positionName,updateTime,createTime)"+"values("
			+ "#{informUserId},#{jobId},#{jobUserId},#{description},#{status},#{positionName},#{updateTime},#{createTime})")
	public int insertJobInform(JobInformModel model);


    @Select("<script>" +
            "select * from "+INFORM_TABLE+" where "+
            "<if test=\"userId!=null and userId!=''\"> informUserId=#{userId} and </if>"+
            "<if test=\"jobName!=null and jobName!=''\"> positionName like #{jobName} and </if>"+
            "<![CDATA[createTime < #{endTime} and createTime > #{startTime}]]> "+
            "order by id desc limit #{offset},#{limit}"+
            "</script>")
    public List<JobInformModel> getInformedJobs(@Param("userId")Integer userId,@Param("jobName")String jobName,@Param("startTime")Date startTime,@Param("endTime")Date endTime,@Param("offset")Integer offset,@Param("limit")Integer limit);


    @Select("select count(1) from "+INFORM_TABLE+"")
    public int getCount();

    @Update("update "+INFORM_TABLE+" set status=#{status} where id=#{id}")
    public int updateStatus(@Param("id")int id,@Param("status")int status);

    @Update("update "+INFORM_TABLE+" set status=#{status} where jobId=#{jobId} and status=0")
    public int updateStatusByJobId(@Param("jobId")int jobId,@Param("status")int status);
    
    @Select("select id,informUserId,positionName,jobId,jobUserId,description,status,updateTime,createTime from "+INFORM_TABLE+" where jobId=#{jobId}")
    public JobInformModel selectJobInfoModel(@Param("jobId")int jobId);

    @Select("select id,informUserId,positionName,jobId,jobUserId,description,status,updateTime,createTime from "+INFORM_TABLE+" where id=#{id}")
    public JobInformModel getInformJobById(@Param("id")int id);
}
