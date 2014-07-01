package com.dajie.push.netty.channel;

import io.netty.channel.Channel;

import java.util.Date;

/**
 * Created by wills on 3/13/14.
 */
public class NettyChannel {

    private Channel channel;

    private String clientId;

    private String apiKey;

    private String secretKey;

    private String userId;

    private Date connTime;

    public NettyChannel(Channel channel,String clientId,String apiKey,String secretKey) {
        this.channel = channel;
        this.clientId=clientId;
        this.apiKey=apiKey;
        this.secretKey=secretKey;
        this.connTime=new Date();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Date getConnTime() {
        return connTime;
    }

    public void setConnTime(Date connTime) {
        this.connTime = connTime;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
