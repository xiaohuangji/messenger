package com.dajie.push.storage.dao;

import com.dajie.message.model.push.PushInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by wills on 2/11/14.
 */

public interface PushInfoDAO {

    @Select("select id,appId,userId,msgId,payload from push_push_info where appId=#{appId} and userId=#{userId} and status=0 ")
    public List<PushInfo> getPushInfosByUserId(@Param("appId")String appId,@Param("userId")String userId);

    @Insert("insert into push_push_info (appId,userId,fromUserId,upMsgId,msgId,payload,createTime) values (#{appId},#{userId},#{fromUserId},#{upMsgId},#{msgId},#{payload},#{createTime})")
    public int addPushInfo(PushInfo pushInfo);

    @Update("update push_push_info set status=#{status} where userId=#{userId} and msgId=#{msgId}")
    public int updatePushInfoStatus(@Param("userId")String userId, @Param("msgId")int msgId, @Param("status")int status);

    @Update("delete from push_push_info where userId=#{userId} and msgId=#{msgId}")
    public int deletePushInfoStatus(@Param("userId")String userId, @Param("msgId")int msgId);

    @Select("select count(1) from push_push_info where userId=#{userId} and upMsgId=#{upMsgId}")
    public int isExisted(@Param("userId")String userId,@Param("upMsgId")long upMsgId);

    @Update("<script>" +
            "update push_push_info set status=#{status} where id in " +
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            " </script>")
    public int updatePushInfoStatusById(@Param("ids")List<Long> ids,@Param("status")int status);

    @Update("<script>" +
            "delete from push_push_info where id in " +
            "<foreach item='item' index='index' collection='ids' open='(' separator=',' close=')'>" +
            "#{item}" +
            "</foreach>" +
            " </script>")
    public int deletePushInfoStatusById(@Param("ids")List<Long> ids);

    @Update("update push_push_info set status=#{status} where appId=#{appId} and userId=#{userId} and msgId=#{msgId}")
    public int updatePushInfoStatusByAppId(@Param("appId")String appId, @Param("userId")String userId ,@Param("msgId")int msgId, @Param("status")int status);

}
