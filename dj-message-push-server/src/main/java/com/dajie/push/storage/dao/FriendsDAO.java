package com.dajie.push.storage.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by John on 4/16/14.
 */
public interface FriendsDAO {

	static final String TABLE = "friends";

	@Select("select count(userId) from " + TABLE
			+ " where userId = #{userId} and friendId = #{friendId}")
	int isFriend(@Param("userId") int userId,
                 @Param("friendId") int friendId);

}
