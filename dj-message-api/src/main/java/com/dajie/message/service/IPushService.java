package com.dajie.message.service;

import com.dajie.message.model.message.GoudaMessageBuilder;

/**
 * Created by wills on 3/24/14.
 */
public interface IPushService {

    public boolean sendPush(GoudaMessageBuilder goudaMessageBuilder);

    public boolean sendCardRequest(int fromId,int toId);

    public boolean sendCardAgree(int fromId,int toId);

    public boolean sendTextMessage(int toId,String content);

    public boolean sendLikeLabel(int fromId,int toId,String labelName);

    public boolean sendUnFriend(int fromId,int toId);

    public boolean sendUserUpdate(int fromId,int toId);

}
