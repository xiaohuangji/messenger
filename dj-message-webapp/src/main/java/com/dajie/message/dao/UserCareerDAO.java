package com.dajie.message.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.dajie.message.annotation.database.DBBean;
import com.dajie.message.model.user.UserCareer;

@DBBean
public interface UserCareerDAO {

	static final String TABLE = "user_career";

	@Insert("insert into "
			+ TABLE
			+ " (userId, positionName, positionType, corpName, corpId, verification, industry, experience, education) values"
			+ " (#{uc.userId}, #{uc.positionName}, #{uc.positionType}, #{uc.corpName}, #{uc.corpId}, #{uc.verification}, #{uc.industry}, #{uc.experience}, #{uc.education})")
	public int insert(@Param("uc") UserCareer userCareer);

	@Select("select userId, positionName, positionType, corpName, corpId, verification, industry, experience, education from "
			+ TABLE + " where userId = #{userId}")
	public UserCareer getUserCareer(@Param("userId") int userId);

	@Select("select corpName from " + TABLE + " where userId = #{userId}")
	public String getCorpName(@Param("userId") int userId);

	@Select("select verification from " + TABLE + " where userId = #{userId}")
	public Integer getVerification(@Param("userId") int userId);

	@Update("update "
			+ TABLE
			+ " set positionName = #{uc.positionName}, positionType = #{uc.positionType}, corpName = #{uc.corpName}, corpId = #{uc.corpId}, verification = #{uc.verification}, industry = #{uc.industry},"
			+ " experience = #{uc.experience}, education = #{uc.education} where userId = #{uc.userId}")
	public int update(@Param("uc") UserCareer userCareer);

	@Update("update " + TABLE
			+ " set verification = #{verification} where userId = #{userId}")
	public int updateVerification(@Param("userId") int userId,
			@Param("verification") int verification);

}
