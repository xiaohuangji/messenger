package com.dajie.push.netty.pushpool;

import io.netty.channel.Channel;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wills on 3/14/14.
 */
public class PushEvent {

    private Channel channel;

    private String clientId;

    private int msgId;

    private String topic;

    private String payload;

    private AtomicInteger retryTime=new AtomicInteger(0);

    public String getPushKey(){
        return clientId+msgId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public int getAndIncrRetryTime() {
        return retryTime.getAndIncrement();
    }

    public int getRetryTime(){
        return retryTime.get();
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
