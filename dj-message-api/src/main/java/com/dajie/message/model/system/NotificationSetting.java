package com.dajie.message.model.system;

import java.util.Date;

/**
 * Created by wills on 5/4/14.
 */
public class NotificationSetting {

    private int userId;

    private int newMessage=1;

    private int sound=1;

    private int vibration=0;

    private int nightNoDisturbance=0;

    private Date createTime=new Date();

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(int newMessage) {
        this.newMessage = newMessage;
    }

    public int getSound() {
        return sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }

    public int getVibration() {
        return vibration;
    }

    public void setVibration(int vibration) {
        this.vibration = vibration;
    }

    public int getNightNoDisturbance() {
        return nightNoDisturbance;
    }

    public void setNightNoDisturbance(int nightNoDisturbance) {
        this.nightNoDisturbance = nightNoDisturbance;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
