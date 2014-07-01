package com.dajie.message.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.dajie.message.annotation.database.DBBean;

import java.util.Date;
import java.util.List;

/**
 * Created by John on 6/5/14.
 */
@DBBean
public interface UserStatisticsDAO {

	static final String TABLE = "user_statistics";

	@Select("select profileVisit from " + TABLE + " where userId = #{userId}")
	Integer getProfileVisit(@Param("userId") int userId);

	@Insert("insert into " + TABLE
			+ " (userId, createDate) values (#{userId}, #{createDate})")
	int insertStatistics(@Param("userId") int userId,
			@Param("createDate") Date createDate);

	@Update("update " + TABLE
			+ " set profileVisit = profileVisit + 1 where userId = #{userId}")
	int incProfileVisit(@Param("userId") int userId);

	@Select("select userId from "
			+ TABLE
			+ " where updateDate > #{updateDate} order by profileVisit desc limit 30")
	List<Integer> getHotUsers(@Param("updateDate") Date updateDate);
}
