package com.dajie.message.dao;

import com.dajie.message.annotation.database.DBBean;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * Created by wills on 5/4/14.
 */
@DBBean
public interface BlackListDAO {

    @Insert("insert into blacklist (userId,blockId,createTime) values (#{userId},#{blockId},#{createTime})")
    public int add(@Param("userId")int userId,@Param("blockId")int blockId,@Param("createTime")Date createTime);

    @Delete("delete from blacklist where userId=#{userId} and blockId=#{blockId}")
    public int remove(@Param("userId")int userId,@Param("blockId")int blockId);
    
    @Select("select count(userId) from blacklist where userId=#{userId} and blockId=#{blockId}")
	public int isBlocked(@Param("userId") int userId,@Param("blockId") int blockId);

    @Select("select blockId from blacklist where userId=#{userId}")
    public List<Integer> getList(@Param("userId")int userId);
}
