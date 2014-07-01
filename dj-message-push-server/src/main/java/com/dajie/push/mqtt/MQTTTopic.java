package com.dajie.push.mqtt;

/**
 * Created by wills on 3/13/14.
 */
public class MQTTTopic {

    private String appId;

    private String userId;

    /**
     * when publishmessage's topic has flag, It means the message comes from other node.
     * just valid the in distributed system.
     */
    private String flag;

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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
