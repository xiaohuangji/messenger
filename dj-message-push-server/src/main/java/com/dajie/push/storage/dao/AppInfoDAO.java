package com.dajie.push.storage.dao;

import com.dajie.message.model.push.AppInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by wills on 3/17/14.
 */
public interface AppInfoDAO {

    @Select("select * from push_apikey where apiKey=#{apiKey} and secretKey=#{secretKey}")
    public AppInfo getAppInfo(@Param("apiKey")String apiKey,@Param("secretKey")String secretKey);
}
