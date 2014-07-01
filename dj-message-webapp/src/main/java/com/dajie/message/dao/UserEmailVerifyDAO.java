package com.dajie.message.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.dajie.message.annotation.database.DBBean;
import com.dajie.message.model.user.UserEmailVerify;

/**
 * Created by John on 5/29/14.
 */
@DBBean
public interface UserEmailVerifyDAO {

	static final String TABLE = "user_email_verify";

	@Insert("replace into "
			+ TABLE
			+ " (userId, email) values (#{uev.userId}, #{uev.email})")
	int addUserEmailVerify(@Param("uev") UserEmailVerify userEmailVerify);

	@Select("select userId, email from " + TABLE
			+ " where userId = #{userId}")
	UserEmailVerify getUserEmailVerify(@Param("userId") int userId);

	@Update("update "
			+ TABLE
			+ " set verifyDate = #{verifyDate} where userId = #{uev.userId} and email = #{uev.email}")
	int updateVerifyDate(@Param("verifyDate") Date verifyDate,
			@Param("uev") UserEmailVerify userEmailVerify);

	@Select("select userId, email from " + TABLE
			+ " where verifyDate is not null and verifyDate < #{twoDaysAgo}")
	List<UserEmailVerify> getUserEmailVerifyTwoDaysAgo(
			@Param("twoDaysAgo") Date twoDaysAgo);

	@Select("select userId, email from " + TABLE
			+ " where verifyDate is not null and verifyDate > #{twoDaysAgo}")
	List<UserEmailVerify> getUserEmailVerifyAfterTwoDaysAgo(
			@Param("twoDaysAgo") Date twoDaysAgo);

	@Delete("delete from "
			+ TABLE
			+ " where userId = #{uev.userId} and email = #{uev.email}")
	int delUserEmailVerify(@Param("uev") UserEmailVerify userEmailVerify);
	
	@Delete("delete from "
			+ TABLE
			+ " where userId = #{userId}")
	int delUserEmailVerifyByUserId(@Param("userId") int userId);

	@Select("select verifyDate from "
			+ TABLE
			+ " where userId = #{uev.userId} and email = #{uev.email}")
	Date getVerifyDate(@Param("uev") UserEmailVerify userEmailVerify);

}
