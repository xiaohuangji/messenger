package com.dajie.message.dao;

import com.dajie.message.annotation.database.DBBean;
import com.dajie.message.model.push.PushStatsInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * Created by wills on 4/25/14.
 */
@DBBean
public interface PushStatsInfoDAO {

    @Select("select * from push_stats_info order by id desc limit 10")
    public List<PushStatsInfo> getLatestPushStats();

    @Delete("delete from push_stats_info where updateTime < #{updateTime}")
    public int deleteOldData(@Param("updateTime")Date updateTime);
}
