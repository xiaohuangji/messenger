package com.dajie.push.storage.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.dajie.message.model.system.UserDeviceToken;

public interface UserDeviceTokenDAO {

	@Select("select id,userId,token,type,createTime,updateTime from user_ios_token where userId=#{userId}")
	public UserDeviceToken getDeviceToken(@Param("userId") int userId);

}
