package com.dajie.message.dao;

import com.dajie.message.annotation.database.DBBean;
import com.dajie.message.model.push.AppInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by wills on 3/17/14.
 */
@DBBean
public interface AppInfoDAO {

    static final String TABLE="push_apikey";

    @Select("select * from "+TABLE+" where apiKey=#{apiKey} and secretKey=#{secretKey}")
    public AppInfo getAppInfo(@Param("apiKey") String apiKey, @Param("secretKey") String secretKey);

    @Select("select appId from "+TABLE)
    public List<String> getAllAppId();
}
