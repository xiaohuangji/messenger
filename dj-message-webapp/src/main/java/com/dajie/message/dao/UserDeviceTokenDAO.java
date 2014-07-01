package com.dajie.message.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.dajie.message.annotation.database.DBBean;

import java.util.Date;

@DBBean
public interface UserDeviceTokenDAO {

	@Insert("replace into user_device_token (userId,token,type,createTime) values (#{userId},#{token},0,#{createTime})")
	public int bindIosToken(@Param("userId")int userId,@Param("token")String token,@Param("createTime")Date createTime);
	
	@Insert("replace into user_device_token (userId,token,type,createTime) values (#{userId},#{token},1,#{createTime})")
	public int bindAndroidToken(@Param("userId")int userId,@Param("token")String token,@Param("createTime")Date createTime);
}
