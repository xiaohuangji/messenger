package com.dajie.message.model.push;

import java.util.Date;

/**
 * Created by wills on 3/13/14.
 */
public class PushInfo {

    private long id;

    private String appId;

    private String userId;

    private long upMsgId;

    private int msgId;

    private String fromUserId;

    private String payload;

    private int status=0;

    private Date createTime=new Date();

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUpMsgId() {
        return upMsgId;
    }

    public void setUpMsgId(long upMsgId) {
        this.upMsgId = upMsgId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
