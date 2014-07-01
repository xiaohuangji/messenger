package com.dajie.push.storage.dao;

import com.dajie.message.model.operation.Feedback;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by wills on 5/8/14.
 */

public interface FeedbackDAO {

    @Insert("insert into operation_feedback (userId,content,reply,status,createTime,mobile,system,systemVersion,mobileBrand,mobileModel,channel,clientVersion,mobileResolution) values (" +
            "#{userId},#{content},#{reply},#{status},#{createTime},#{mobile},#{system},#{systemVersion},#{mobileBrand},#{mobileModel},#{channel},#{clientVersion},#{mobileResolution})")
    public void insertFeedback(Feedback feedback);

    @Select("select mobile from user_base where userId=#{userId}")
    public String getMobileByUserId(@Param("userId")int userId);
}
