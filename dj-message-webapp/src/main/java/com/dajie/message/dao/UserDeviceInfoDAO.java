package com.dajie.message.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.dajie.message.annotation.database.DBBean;
import com.dajie.message.model.system.UserDeviceInfo;

@DBBean
public interface UserDeviceInfoDAO {
	static final String TABLE = "user_device_info";

	static final String SELECT_SQL = "select id,userId,system,systemVersion,mobileBrand,mobileModel,channel,clientVersion,mobileResolution,createTime from "
			+ TABLE;

	@Insert("replace into "
			+ TABLE
			+ " (userId,system,systemVersion,mobileBrand,mobileModel,channel,clientVersion,mobileResolution,createTime)"
			+ " values(#{userDeviceInfo.userId},#{userDeviceInfo.system},#{userDeviceInfo.systemVersion},#{userDeviceInfo.mobileBrand},#{userDeviceInfo.mobileModel},#{userDeviceInfo.channel},#{userDeviceInfo.clientVersion},#{userDeviceInfo.mobileResolution},#{userDeviceInfo.createTime})")
	public int replaceDeviceInfo(
			@Param("userDeviceInfo") UserDeviceInfo userDeviceInfo);

	@Select(SELECT_SQL + " where userId = #{userId}")
	public UserDeviceInfo getDeviceInfo(@Param("userId") int userId);
}
