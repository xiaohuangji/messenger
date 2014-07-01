package com.dajie.message.model.system;

import java.util.Date;

/**
 * Created by wills on 4/15/14.
 */
public class PrivacySetting {

    public static final int CHAT_DISABLE=0;

    public static final int CHAT_FRIEND=1;

    public static final int CHAT_EVERYONE=2;

    private int userId;
    /**
     * 同事是否可见
     */
    private int colleagueVisibility=1;

    /**
     * 是否可见
     */
    private int visibility=1;

    /**
     * 接受聊天对象
     */
    private int chatNotification=CHAT_EVERYONE;

    private Date createTime=new Date();

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getColleagueVisibility() {
        return colleagueVisibility;
    }

    public void setColleagueVisibility(int colleagueVisibility) {
        this.colleagueVisibility = colleagueVisibility;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getChatNotification() {
        return chatNotification;
    }

    public void setChatNotification(int chatNotification) {
        this.chatNotification = chatNotification;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
