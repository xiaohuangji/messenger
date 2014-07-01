package com.dajie.message.dao;

import com.dajie.message.annotation.database.DBBean;
import com.dajie.message.model.operation.PushJobRaw;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by wills on 5/8/14.
 */
@DBBean
public interface PushJobDAO {

    @Insert("insert into operation_push_job (content,triggerDate,filterIsVerified,filterJobType,filterIndustry,filterGender,filterCity,filterClientVersion,filterMobileOs,status,operator,filterDesc,createTime) values" +
            " (#{content},#{triggerDate},#{filterIsVerified},#{filterJobType},#{filterIndustry},#{filterGender},#{filterCity},#{filterClientVersion},#{filterMobileOs},#{status},#{operator},#{filterDesc},#{createTime})")
    @SelectKey(keyProperty="id",resultType=Integer.class,before=false,statement="SELECT LAST_INSERT_ID() AS id")
    public int insertPushJob(PushJobRaw pushJobRaw);

    @Update("update operation_push_job set userCount=#{userCount} where id=#{id}")
    public int updateUserCount(@Param("id")int id,@Param("userCount")int userCount);

    @Select("select userCount from operation_push_job where id=#{id}")
    public int getUserCount(@Param("id")int id);

    @Select("select id,content,triggerDate,filterIsVerified,filterJobType,filterIndustry,filterGender,filterCity,userCount,status,operator,filterDesc from operation_push_job order by id desc limit #{offset},#{limit}")
    public List<PushJobRaw> getPushJob(@Param("offset")int offset,@Param("limit")int limit);

    @Update("update operation_push_job set status=#{status} where id=#{id}")
    public int changePushJobStatus(@Param("id")int id,@Param("status")int status);

    @Select("select count(id) from operation_push_job")
    public int getPushJobCount();

}
