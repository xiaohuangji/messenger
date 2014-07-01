package com.dajie.message.model.operation;

import java.util.Date;

/**
 * Created by wills on 5/8/14.
 */
public class Feedback {

    public static final int STATUS_PENDING=1;

    public static final int STATUS_HAVEHANDLED=2; //已处理

    public static final int STATUS_DELETED=9;//已删除

    private int id;

    private int userId;

    private Date createTime;

    private Date updateTime;

    private int status=STATUS_PENDING;

    private String content;

    private String reply;

    //手机号
    private String mobile;

    // 用户手机系统平台
    private int system;

    // 手机平台版本
    private String systemVersion;

    // 手机品牌
    private String mobileBrand;

    // 手机型号
    private String mobileModel;

    // 渠道
    private String channel;

    // 客户端版本
    private String clientVersion;

    // 手机分辨率
    private String mobileResolution;

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getSystem() {
        return system;
    }

    public void setSystem(int system) {
        this.system = system;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getMobileBrand() {
        return mobileBrand;
    }

    public void setMobileBrand(String mobileBrand) {
        this.mobileBrand = mobileBrand;
    }

    public String getMobileModel() {
        return mobileModel;
    }

    public void setMobileModel(String mobileModel) {
        this.mobileModel = mobileModel;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getMobileResolution() {
        return mobileResolution;
    }

    public void setMobileResolution(String mobileResolution) {
        this.mobileResolution = mobileResolution;
    }
}
