package com.dajie.push.storage.dao;

import com.dajie.message.model.push.PushStatsInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * Created by wills on 4/25/14.
 */
public interface PushStatsInfoDAO {

    @Select("select * from push_stats_info where serverName=#{serverName} order by id desc limit 1")
    public PushStatsInfo getLatestPushStats(@Param("serverName")String serverName);

    @Insert("insert into push_stats_info (serverName,iosConn,androidConn,serverConn,fromIos,fromAndroid,fromServer,toIos,toAndroid,toServer,createTime) values (" +
            "#{serverName},#{iosConn},#{androidConn},#{serverConn},#{fromIos},#{fromAndroid},#{fromServer},#{toIos},#{toAndroid},#{toServer},#{createTime})")
    public int InsertLatestPushStats(PushStatsInfo stats);
}
