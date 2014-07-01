package com.dajie.message.dao;

import com.dajie.message.annotation.database.DBBean;
import com.dajie.message.model.push.ClientInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by wills on 2/14/14.
 */

@DBBean
public interface ClientInfoDAO {

    static final String TABLE="push_client_info";

    @Select("select count(clientId) from "+TABLE+" where appId=#{appId}")
    public int getClientInfoCount(@Param("appId")String appId);


    @Select("select *  from "+TABLE+" where appId=#{appId} order by id desc limit #{offset},#{limit}")
    public List<ClientInfo> getClientInfos(@Param("appId")String appId,@Param("offset")int offset,@Param("limit")int limit);


    @Delete("delete from "+TABLE+" where appId=#{appId} and userId=#{userId} and clientId=#{clientId}")
    public int removeClientInfo(@Param("appId")String appId,@Param("userId")String userId,@Param("clientId")String clientId);
}
