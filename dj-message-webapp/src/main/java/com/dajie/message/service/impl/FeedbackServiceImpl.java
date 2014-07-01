package com.dajie.message.service.impl;

import com.dajie.message.constants.returncode.CommonResultCode;
import com.dajie.message.dao.FeedbackDAO;
import com.dajie.message.model.operation.Feedback;
import com.dajie.message.service.IFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by wills on 5/8/14.
 */
@Component("feedbackService")
public class FeedbackServiceImpl implements IFeedbackService {

    @Autowired
    private FeedbackDAO feedbackDAO;

    private static final Date TIME1970=new Date(0);

    @Override
    public List<Feedback> getFeedback(Integer userId, String mobile, Date startTime, Date endTime, Integer status,
                                      Integer system, String systemVersion, String mobileBrand, String mobileModel,
                                      String channel, String clientVersion, String mobileResolution, int pageSize, int pageNum)
    {
        int offset=pageSize*(pageNum-1);
        int limit=pageSize;

        return feedbackDAO.getFeedback(userId, mobile, startTime==null?TIME1970:startTime, endTime==null?new Date():endTime, status, system, systemVersion, mobileBrand, mobileModel, channel, clientVersion, mobileResolution, offset, limit);
    }

    @Override
    public int getCount(Integer userId, String mobile, Date startTime, Date endTime, Integer status, Integer system, String systemVersion, String mobileBrand, String mobileModel, String channel, String clientVersion, String mobileResolution) {
        return feedbackDAO.getCount(userId, mobile, startTime==null?TIME1970:startTime, endTime==null?new Date():endTime, status, system, systemVersion, mobileBrand, mobileModel, channel, clientVersion, mobileResolution);
    }

    @Override
    public int updateStatus(int id, int status) {
        int result= feedbackDAO.updateStatus(id,status);
        return result>0? CommonResultCode.OP_SUCC:CommonResultCode.OP_FAIL;
    }

    @Override
    public int reply(int id, String reply) {
        int result= feedbackDAO.updateReply(id, reply);
        return result>0? CommonResultCode.OP_SUCC:CommonResultCode.OP_FAIL;
    }
}
