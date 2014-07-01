package com.dajie.message.service;

import com.dajie.message.model.operation.Feedback;

import java.util.Date;
import java.util.List;

/**
 * Created by wills on 5/8/14.
 */
public interface IFeedbackService {

    public List<Feedback> getFeedback(Integer userId,String mobile,Date startTime,Date endTime,Integer status,Integer system,
                                      String systemVersion,String mobileBrand,String mobileModel,String channel,String clientVersion,
                                      String mobileResolution,int pageSize,int pageNum);

    public int getCount(Integer userId,String mobile,Date startTime,Date endTime,Integer status,Integer system,
                        String systemVersion,String mobileBrand,String mobileModel,String channel,String clientVersion,
                        String mobileResolution);

    public int updateStatus(int id,int status);

    public int reply(int id,String reply);
}
