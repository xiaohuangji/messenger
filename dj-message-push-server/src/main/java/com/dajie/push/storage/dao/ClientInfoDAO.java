package com.dajie.push.storage.dao;


import com.dajie.message.model.push.ClientInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by wills on 2/14/14.
 */

public interface ClientInfoDAO {

    @Insert("replace into push_client_info (clientId,appId,userId) values (#{clientId},#{appId},#{userId})")
    public int updateClientInfo(ClientInfo clientInfo);

    @Select("select clientId from push_client_info where appId=#{appId} and userId=#{userId} order by id desc limit 1")
    public String getClientId(@Param("appId")String appId,@Param("userId")String userId);

    @Select("select clientId from push_client_info where userId=#{userId} order by id desc limit 1")
    public String getClientIdByUserId(@Param("userId")String userId);

    @Select("select clientId from push_client_info where appId=#{appId} and userId=''")
    public List<String> getClientIds(@Param("appId")String appId);

    @Delete("delete from push_client_info where clientId=#{clientId} and appId=#{appId} and userId=#{userId}")
    public int deleteClientInfo(@Param("clientId")String clientId,@Param("appId")String appId,@Param("userId")String userId);

}
