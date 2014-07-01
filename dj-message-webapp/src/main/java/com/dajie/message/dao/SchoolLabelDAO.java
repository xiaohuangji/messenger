package com.dajie.message.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.dajie.message.annotation.database.DBBean;
import com.dajie.message.model.user.SchoolLabel;

@DBBean
public interface SchoolLabelDAO {
	
	static final String TABLE = "school_label";
	
	@Select("select school, label from " + TABLE)
	public List<SchoolLabel> get();
	
	@Select("select label from " + TABLE + " where school = #{school}")
	public String getLabel(@Param("school") String school);

}
