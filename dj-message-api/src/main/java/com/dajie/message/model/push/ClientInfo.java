package com.dajie.message.model.push;

import java.util.Date;

/**
 * Created by wills on 3/13/14.
 * 一个clientId表示一部手机
 * appId表示应用
 * userId表示应用内用户标识
 * 一个clientId可对应多个appId
 *
 * clientId1,NULL,NULL ------连接成功不入库
 * clientId2,NULL,NULL
 *
 * clientId1,appId1,user1  -----clientId1订阅appId1成功，已登录
 * clientId1,appId2,user1  -----clientId1订阅appId2成功，已登录
 * clientId1,appId3,NULL   -----clientId1订阅appId3成功，未登录
 * clientId2,appId3,NULL
 */
public class ClientInfo {

    private String clientId;

    private String appId;

    private String userId;

    private int tagId;

    private Date createTime=new Date();

    private Date updateTime;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

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

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
