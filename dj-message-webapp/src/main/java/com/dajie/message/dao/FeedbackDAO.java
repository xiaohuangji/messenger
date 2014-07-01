package com.dajie.message.dao;

import com.dajie.message.annotation.database.DBBean;
import com.dajie.message.model.operation.Feedback;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * Created by wills on 5/8/14.
 */
@DBBean
public interface FeedbackDAO {

    @Select("<script>" +
            "select * from operation_feedback where "+
            "<if test=\"userId!=null and userId!=''\"> userId=#{userId} and </if>"+
            "<if test=\"mobile!=null and mobile!=''\"> mobile=#{mobile} and </if>"+
            "<if test=\"status!=null and status!=0\"> status=#{status} and </if>"+
            "<if test=\"system!=null and system!=0\"> system=#{system} and </if>"+
            "<if test=\"systemVersion!=null and systemVerion!=''\"> systemVersion=#{systemVersion} and </if>"+
            "<if test=\"mobileBrand!=null and mobileBrand!=''\"> mobileBrand=#{mobileBrand} and </if>"+
            "<if test=\"mobileModel!=null and mobileModel!=''\"> mobileModel=#{mobileModel} and </if>"+
            "<if test=\"channel!=null and channel!=''\"> channel=#{channel} and </if>"+
            "<if test=\"clientVersion!=null and clientVersion!=''\"> clientVersion=#{clientVersion} and </if>"+
            "<if test=\"mobileResolution!=null and mobileResolution!=''\"> mobileResolution=#{mobileResolution} and </if>"+
            "<![CDATA[createTime < #{endTime} and createTime > #{startTime} and status!=9]]> "+
            "order by id desc limit #{offset},#{limit}"+
            "</script>")
    public List<Feedback> getFeedback(@Param("userId")Integer userId, @Param("mobile")String mobile,@Param("startTime")Date startTime, @Param("endTime")Date endTime, @Param("status")Integer status,
                                      @Param("system")Integer system, @Param("systemVersion")String systemVersion, @Param("mobileBrand")String mobileBrand, @Param("mobileModel")String mobileModel,
                                      @Param("channel")String channel, @Param("clientVersion")String clientVersion, @Param("mobileResolution")String mobileResolution, @Param("offset")int offset,@Param("limit")int limit);


    @Select("<script>" +
            "select count(1) from operation_feedback where "+
            "<if test=\"userId!=null and userId!=''\"> userId=#{userId} and </if>"+
            "<if test=\"mobile!=null and mobile!=''\"> mobile=#{mobile} and </if>"+
            "<if test=\"status!=null and status!=0\"> status=#{status} and </if>"+
            "<if test=\"system!=null and system!=0\"> system=#{system} and </if>"+
            "<if test=\"systemVersion!=null and systemVerion!=''\"> systemVersion=#{systemVersion} and </if>"+
            "<if test=\"mobileBrand!=null and mobileBrand!=''\"> mobileBrand=#{mobileBrand} and </if>"+
            "<if test=\"mobileModel!=null and mobileModel!=''\"> mobileModel=#{mobileModel} and </if>"+
            "<if test=\"channel!=null and channel!=''\"> channel=#{channel} and </if>"+
            "<if test=\"clientVersion!=null and clientVersion!=''\"> clientVersion=#{clientVersion} and </if>"+
            "<if test=\"mobileResolution!=null and mobileResolution!=''\"> mobileResolution=#{mobileResolution} and </if>"+
            "<![CDATA[createTime < #{endTime} and createTime > #{startTime} and status!=9]]> "+
            "</script>")
    public int getCount(@Param("userId")Integer userId, @Param("mobile")String mobile,@Param("startTime")Date startTime, @Param("endTime")Date endTime, @Param("status")Integer status,
                        @Param("system")Integer system, @Param("systemVersion")String systemVersion, @Param("mobileBrand")String mobileBrand, @Param("mobileModel")String mobileModel,
                        @Param("channel")String channel, @Param("clientVersion")String clientVersion, @Param("mobileResolution")String mobileResolution);


    @Update("update operation_feedback set status=#{status} where id=#{id}")
    public int updateStatus(@Param("id")int id,@Param("status")int status);

    @Update("update operation_feedback set reply=#{reply} , status=2 where id=#{id}")
    public int updateReply(@Param("id")int id,@Param("reply")String reply);
}
