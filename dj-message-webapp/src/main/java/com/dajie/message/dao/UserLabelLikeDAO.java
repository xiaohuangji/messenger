package com.dajie.message.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.dajie.message.annotation.database.DBBean;

@DBBean
public interface UserLabelLikeDAO {

	static final String TABLE = "user_label_like";

	@Insert("replace into " + TABLE
			+ " (userId, labelId) values (#{userId}, #{labelId})")
	public int addLike(@Param("userId") int userId,
			@Param("labelId") int labelId);

	/** 只删本人的 **/
	@Delete("delete from " + TABLE + " where userId = #{userId} and labelId = #{labelId}")
	public int delLike(@Param("userId") int userId, @Param("labelId") int labelId);

	@Select("select count(userId) from " + TABLE
			+ " where labelId = #{labelId}")
	public int getLikeCounts(@Param("labelId") int labelId);

	@Select("select count(userId) from " + TABLE
			+ " where userId = #{userId} and labelId = #{labelId}")
	public int isLiked(@Param("userId") int userId,
			@Param("labelId") int labelId);

}
