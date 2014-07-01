package com.dajie.push.storage.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by wills on 5/4/14.
 */
public interface BlackListDAO {

    @Select("select count(*) from blacklist where userId=#{userId} and blockId=#{blockId}")
    public int isBlocked(@Param("userId")int userId,@Param("blockId")int blockId);

}
