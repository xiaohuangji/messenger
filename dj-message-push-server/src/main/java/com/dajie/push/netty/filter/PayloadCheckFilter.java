package com.dajie.push.netty.filter;

import com.dajie.message.constants.GoudaConstant;
import com.dajie.message.model.operation.Feedback;
import com.dajie.message.model.system.UserDeviceInfo;
import com.dajie.push.netty.channel.NettyChannel;
import com.dajie.push.storage.dao.FeedbackDAO;
import com.dajie.push.storage.dao.UserDeviceInfoDAO;
import com.dajie.push.utils.JsonUtil;
import com.dajie.push.utils.StatisticLogger;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

/**
 * Created by wills on 5/9/14.
 * 内容检测过滤器
 */
public class PayloadCheckFilter implements IFilter {
    
    private static final Logger LOGGER= LoggerFactory.getLogger(PayloadCheckFilter.class);

    private FeedbackDAO feedbackDAO;

    private UserDeviceInfoDAO userDeviceInfoDAO;

    public void setFeedbackDAO(FeedbackDAO feedbackDAO) {
        this.feedbackDAO = feedbackDAO;
    }

    public void setUserDeviceInfoDAO(UserDeviceInfoDAO userDeviceInfoDAO) {
        this.userDeviceInfoDAO = userDeviceInfoDAO;
    }


    private void insertFeedback(int userId,String payload){
        Feedback feedback=new Feedback();
        UserDeviceInfo userDeviceInfo=userDeviceInfoDAO.getDeviceInfo(userId);
        try{
            if(userDeviceInfo!=null){
                BeanUtils.copyProperties(feedback,userDeviceInfo);
            }
        }
        catch(Exception e){
            LOGGER.error("copy userDeviceInfo to feedback error,",e);
        }
        feedback.setMobile(feedbackDAO.getMobileByUserId(userId));
        feedback.setUserId(userId);
        feedback.setContent(payload);
        feedback.setCreateTime(new Date());
        feedbackDAO.insertFeedback(feedback);
    }

    @Override
    public boolean filter(NettyChannel channel, String destUserId, String payload) {
        try{
            Map<String,Object> jsonMap=JsonUtil.jsonToMap(payload);
            //Integer userId=(Integer)jsonMap.get("to");
            int contentType=(Integer)jsonMap.get("contentType");
            //print statics info
            StatisticLogger.printChatLogger(channel,destUserId,contentType);

            if(Integer.valueOf(destUserId).equals(GoudaConstant.ASSISTANT_ID)){
                String userId=channel.getUserId();
                if(userId!=null){
                    String content=(String)((Map)(jsonMap.get("content"))).get("text");
                    insertFeedback(Integer.valueOf(channel.getUserId()),content);
                    LOGGER.info("receive feedback message,userId:"+userId);
                }
                return false;
            }
            return true;
        }
        catch(Exception e){
            LOGGER.warn("msg payload parse error,",e);
            return false;
        }
    }
}
