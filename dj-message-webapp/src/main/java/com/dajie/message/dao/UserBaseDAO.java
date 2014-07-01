package com.dajie.message.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import com.dajie.message.annotation.database.DBBean;
import com.dajie.message.model.user.UserBase;

/**
 * 
 * @author li.hui
 * 
 */
@DBBean
public interface UserBaseDAO {

	static final String TABLE = "user_base";

	static String select_sql = "select userId,password,salt,name,gender,avatar,avatarMask,email,mobile,birth,type,status,feature,audit,createTime,lastLogin from "
			+ TABLE + " ";

	@Insert("insert into "
			+ TABLE
			+ " (password,salt,name,gender,avatar,avatarMask,email,mobile,createTime) "
			+ "values(#{password},#{salt},#{name},#{gender},#{avatar},#{avatarMask},#{email},#{mobile},#{createTime})")
	@SelectKey(keyProperty = "userId", resultType = Integer.class, before = false, statement = "SELECT LAST_INSERT_ID() AS userId")
	public int insert(UserBase userBase);

	@Select(select_sql + "where userId = #{userId}")
	public UserBase getUserBaseById(@Param("userId") int userId);

	@Select(select_sql + "where mobile = #{mobile}")
	public UserBase getUserBaseByMobile(@Param("mobile") String mobile);

	@Update("update " + TABLE
			+ " set password = #{password},salt=#{salt} where userId=#{userId}")
	public int updatePwd(@Param("userId") int userId,
			@Param("password") String password, @Param("salt") String salt);

	@Update("update " + TABLE + " set name = #{name} where userId = #{userId}")
	public int updateName(@Param("userId") int userId,
			@Param("name") String name);

	@Update("update " + TABLE
			+ " set gender = #{gender} where userId = #{userId}")
	public int updateGender(@Param("userId") int userId,
			@Param("gender") int gender);

	@Update("update "
			+ TABLE
			+ " set avatar = #{avatar}, avatarMask = #{avatarMask} where userId = #{userId}")
	public int updateAvatar(@Param("userId") int userId,
			@Param("avatar") String avatar,
			@Param("avatarMask") String avatarMask);

	@Update("update "
			+ TABLE
			+ " set name = #{name},gender = #{gender},avatar=#{avatar},avatarMask=#{avatarMask}  where userId = #{userId}")
	public int updateRequiredInfo(@Param("userId") int userId,
			@Param("name") String name, @Param("gender") int gender,
			@Param("avatar") String avatar,
			@Param("avatarMask") String avatarMask);

	@Update("update " + TABLE
			+ " set mobile = #{mobile} where userId = #{userId}")
	public int updateMobile(@Param("userId") int userId,
			@Param("mobile") String mobile);

	@Update("update "
			+ TABLE
			+ " set mobile = #{mobile},password=#{password},salt=#{salt} where userId = #{userId}")
	public int updateMobileAndPassword(@Param("userId") int userId,
			@Param("mobile") String mobile, @Param("password") String password,
			@Param("salt") String salt);

	@Delete("delete from " + TABLE + " where userId=#{userId}")
	public int deleteUserBase(@Param("userId") int userId);

	@Update("update " + TABLE
			+ " set status = #{status} where userId = #{userId}")
	public int updateUserStatus(@Param("userId") int userId,
			@Param("status") int status);

	@Update("update " + TABLE
			+ " set audit = #{audit} where userId = #{userId}")
	public int updateUserAuditResult(@Param("userId") int userId,
			@Param("audit") int audit);

	@Select("select audit from " + TABLE + " where userId = #{userId}")
	public Integer getUserAduit(@Param("userId") int userId);

	@Update("update " + TABLE
			+ " set lastLogin = #{lastLogin} where userId = #{userId}")
	public int updateLastLogin(@Param("userId") int userId,
			@Param("lastLogin") Date lastLogin);
}
