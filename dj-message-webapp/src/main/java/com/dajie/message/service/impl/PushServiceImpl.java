package com.dajie.message.service.impl;

import com.dajie.framework.config.EnvironmentEnum;
import com.dajie.framework.config.impl.DefaultConfigManager;
import com.dajie.message.constants.GoudaConstant;
import com.dajie.message.model.message.*;
import com.dajie.message.model.user.SimpleUserView;
import com.dajie.message.service.IPushService;
import com.dajie.message.service.IUserProfileService;
import com.dajie.push.client.PushClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by wills on 4/22/14.
 */
@Component("pushService")
public class PushServiceImpl implements IPushService {

    private static final Logger LOGGER= LoggerFactory.getLogger(PushServiceImpl.class);
    //String clientId,String appId,String apiKey,String secretKey

    private static PushClient pushClient;

    static{
        EnvironmentEnum environment = DefaultConfigManager.getInstance().getEnvironmentEnum();
        pushClient=PushClient.getInstance(GoudaConstant.GOUDA_APPID, GoudaConstant.PUSH_APIKEY, GoudaConstant.PUSH_SECRETKEY,environment.getName());

    }

    @Autowired
    private IUserProfileService userProfileService;

    @Override
    public boolean sendPush(GoudaMessageBuilder goudaMessageBuilder) {
        boolean result=false;
        try{
            result=pushClient.sendPush(String.valueOf(goudaMessageBuilder.getTo()),goudaMessageBuilder.getMessage());
        }catch(Exception e){
            LOGGER.warn("send push fail,",e);
        }
        return result;
    }

    @Override
    public boolean sendCardRequest(int fromId,int toId){
        SimpleUserView userView=userProfileService.getSimpleUserView(fromId);
        GoudaMessageBuilder goudaMessageBuilder=new GoudaMessageBuilder().from(fromId).to(toId).content(new CardRequest(userView));
        return sendPush(goudaMessageBuilder);
    }

    @Override
    public boolean sendCardAgree(int fromId,int toId){
        SimpleUserView userView=userProfileService.getSimpleUserView(fromId);
        GoudaMessageBuilder goudaMessageBuilder=new GoudaMessageBuilder().from(fromId).to(toId).content(new CardAgree(userView));
        return sendPush(goudaMessageBuilder);
    }

    @Override
    public boolean sendTextMessage(int toId, String content) {
        GoudaMessageBuilder goudaMessageBuilder=new GoudaMessageBuilder().from(GoudaConstant.ASSISTANT_ID).to(toId).content(new Text(content));
        return sendPush(goudaMessageBuilder);
    }

    @Override
    public boolean sendLikeLabel(int fromId, int to,String labelName) {
        //产品说要以小助手名义发送。
        //暂时保留fromId接口。
        SimpleUserView userView=userProfileService.getSimpleUserView(fromId);
        GoudaMessageBuilder goudaMessageBuilder=new GoudaMessageBuilder().from(GoudaConstant.ASSISTANT_ID).to(to).content(new LikeLabel(userView,labelName));
        return sendPush(goudaMessageBuilder);
    }

    @Override
    public boolean sendUnFriend(int fromId,int toId){
        GoudaMessageBuilder goudaMessageBuilder=new GoudaMessageBuilder().from(fromId).to(toId).content(new UnFriend());
        return sendPush(goudaMessageBuilder);
    }

    @Override
    public boolean sendUserUpdate(int fromId,int toId){
        GoudaMessageBuilder goudaMessageBuilder=new GoudaMessageBuilder().from(fromId).to(toId).content(new UserUpdate());
        return sendPush(goudaMessageBuilder);
    }
}
