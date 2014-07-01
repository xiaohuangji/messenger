package com.dajie.message.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.dajie.message.annotation.database.DBBean;

@DBBean
public interface UserCustomizationDAO {

	@Insert("replace into user_customization (userId,customization) values (#{userId},#{customization})")
	public int setCustomization(@Param("userId")int userId,@Param("customization")String customization);
	
	@Insert("select customization from user_customization where userId=#{userId}")
	public String getCustomization(@Param("userId")int userId);
}
