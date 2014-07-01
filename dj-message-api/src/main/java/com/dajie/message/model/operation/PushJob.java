package com.dajie.message.model.operation;

import java.util.Date;
import java.util.List;

/**
 * Created by wills on 5/8/14.
 */
public class PushJob {

    /**
     * quartz中的jobkey
     */
    private String pushJobKey;


    private List<Integer> userIds;

    private String content;

    private Date triggerDate;

    /**
     * 分片任务，标识是否是最后一片
     */
    private boolean last=false;

    public String getPushJobKey() {
        return pushJobKey;
    }

    public void setPushJobKey(String pushJobKey) {
        this.pushJobKey = pushJobKey;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTriggerDate() {
        return triggerDate;
    }

    public void setTriggerDate(Date triggerDate) {
        this.triggerDate = triggerDate;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
}
