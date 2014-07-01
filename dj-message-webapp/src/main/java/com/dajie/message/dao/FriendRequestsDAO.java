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
public interface FriendRequestsDAO {
	
	static final String TABLE = "friend_requests";
	
	@Insert("replace into " + TABLE + " (userId, toId) values (#{userId}, #{toId})")
	boolean sendFriendRequest(@Param("userId") int userId, @Param("toId") int toId);
	
	@Select("select userId from " + TABLE + " where toId = #{userId}")
	List<Integer> getAllFriendRequests(@Param("userId") int userId);
	
	/** 分页获取 **/
	@Select("select userId from " + TABLE + " where toId = #{userId} limit #{start}, #{pageSize}")
	List<Integer> getFriendRequests(@Param("userId") int userId, @Param("start") int start, @Param("pageSize") int pageSize);
	
	@Delete("delete from " + TABLE + " where userId = #{fromId} and toId = #{userId}")
	boolean delFriendRequest(@Param("userId") int userId, @Param("fromId") int fromId);

}
