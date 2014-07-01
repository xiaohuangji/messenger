package com.dajie.message.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.dajie.message.annotation.database.DBBean;

/**
 * 
 * @author li.hui
 * 
 */
@DBBean
public interface IdSequenceDAO {
	static final String TABLE = "id_sequence";

	@Select("select id from " + TABLE + " where type=#{type}")
	public long getCurId(@Param("type") int type);

	@Update("update " + TABLE + " set id = #{id} WHERE type = #{type}")
	public int updateToDB(@Param("id") long id, @Param("type") int type);

}
