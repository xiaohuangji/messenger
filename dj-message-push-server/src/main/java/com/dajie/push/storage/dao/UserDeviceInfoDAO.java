package com.dajie.push.storage.dao;

import com.dajie.message.model.system.UserDeviceInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


public interface UserDeviceInfoDAO {
	static final String TABLE = "user_device_info";

	static final String SELECT_SQL = "select userId,system,systemVersion,mobileBrand,mobileModel,channel,clientVersion,mobileResolution,createTime from "
			+ TABLE;

	@Select(SELECT_SQL + " where userId = #{userId}")
	public UserDeviceInfo getDeviceInfo(@Param("userId") int userId);
}
