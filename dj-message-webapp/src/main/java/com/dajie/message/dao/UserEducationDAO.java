package com.dajie.message.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.dajie.message.annotation.database.DBBean;
import com.dajie.message.model.user.UserEducation;

@DBBean
public interface UserEducationDAO {

	static final String TABLE = "user_education";

	@Insert("insert into "
			+ TABLE
			+ " (userId, major, school, label, degree) values (#{ue.userId}, #{ue.major}, #{ue.school}, #{ue.label}, #{ue.degree})")
	public int insert(@Param("ue") UserEducation userEducation);

	@Select("select userId, major, school, label, degree from " + TABLE
			+ " where userId = #{userId}")
	public UserEducation getUserEducation(@Param("userId") int userId);
	
	@Select("select school from " + TABLE + " where userId = #{userId}")
	public String getSchool(@Param("userId") int userId);

	@Update("update "
			+ TABLE
			+ " set major = #{ue.major}, school = #{ue.school}, label = #{ue.label}, degree = #{ue.degree} where userId = #{ue.userId}")
	public int update(@Param("ue") UserEducation userEducation);

	@Update("update " + TABLE
			+ " set label = #{label} where userId = #{userId}")
	public int modifyLabel(@Param("userId") int userId,
			@Param("label") String label);

}
