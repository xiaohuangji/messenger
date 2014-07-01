package com.dajie.message.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.dajie.message.annotation.database.DBBean;

/**
 * Created by John on 4/16/14.
 */
@DBBean
public interface FriendsDAO {

	static final String TABLE = "friends";

	@Insert("replace into " + TABLE
			+ " (userId, friendId) values (#{userId}, #{friendId})")
	boolean addFriend(@Param("userId") int userId,
			@Param("friendId") int friendId);

	@Select("select friendId from " + TABLE + " where userId = #{userId}")
	List<Integer> getAllFriends(@Param("userId") int userId);

	/** 分页获取 **/
	@Select("select friendId from " + TABLE
			+ " where userId = #{userId} limit #{start}, #{pageSize}")
	List<Integer> getFriends(@Param("userId") int userId,
			@Param("start") int start, @Param("pageSize") int pageSize);

	@Delete("delete from " + TABLE
			+ " where userId = #{userId} and friendId = #{friendId}")
	boolean delFriend(@Param("userId") int userId,
			@Param("friendId") int friendId);

	@Select("select count(userId) from " + TABLE
			+ " where userId = #{userId} and friendId = #{friendId}")
	int isFriend(@Param("userId") int userId,
			@Param("friendId") int friendId);

}

