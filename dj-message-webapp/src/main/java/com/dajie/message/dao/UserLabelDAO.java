package com.dajie.message.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import com.dajie.message.annotation.database.DBBean;
import com.dajie.message.model.user.UserLabel;

@DBBean
public interface UserLabelDAO {

	static final String TABLE = "user_label";

	@Insert("insert into " + TABLE
			+ " (userId, content) values (#{userId}, #{content})")
	@SelectKey(keyProperty = "id", resultType = Integer.class, before = false, statement = "select last_insert_id() as id")
	int addLabel(UserLabel userLabel);
	
	@Update("update " + TABLE + " set content = #{content} where userId = #{userId} and id = #{id}")
	int modifyLabel(UserLabel userLabel);

	@Select("select count(id) from " + TABLE + " where id = #{id}")
	int labelIsExist(@Param("id") int id);

	@Select("select id, userId, content from " + TABLE + " where id = #{id}")
	UserLabel getLabelById(@Param("id") int id);

	/** 只删本人的 **/
	@Delete("delete from " + TABLE + " where userId = #{userId} and id = #{id}")
	int delLabel(@Param("userId") int userId, @Param("id") int id);

	@Select("select id, userId, content from " + TABLE
			+ " where userId = #{userId}")
	List<UserLabel> getLabels(@Param("userId") int userId);
	
}
