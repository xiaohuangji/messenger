package com.dajie.messageadmin.model;

/**
 * Created by wills on 5/12/14.
 */

public class FeedbackOption{
    public Integer userId;
    public String mobile;
    public String startTime;
    public String endTime;
    public Integer status;
    public Integer system;
    public String  systemVersion;
    public String mobileBrand;
    public String mobileModel;
    public String mobileResolution;
    public String channel;
    public String clientVersion;

    public Integer getUserId() {
        return userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setSystem(Integer system) {
        this.system = system;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public void setMobileBrand(String mobileBrand) {
        this.mobileBrand = mobileBrand;
    }

    public void setMobileModel(String mobileModel) {
        this.mobileModel = mobileModel;
    }

    public void setMobileResolution(String mobileResolution) {
        this.mobileResolution = mobileResolution;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getSystem() {
        return system;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public String getMobileBrand() {
        return mobileBrand;
    }

    public String getMobileModel() {
        return mobileModel;
    }

    public String getMobileResolution() {
        return mobileResolution;
    }

    public String getChannel() {
        return channel;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public FeedbackOption(Integer userId, String mobile, String startTime, String endTime, Integer status, Integer system, String systemVersion, String mobileBrand, String mobileModel, String mobileResolution, String channel, String clientVersion) {
        this.userId = userId;
        this.mobile = mobile;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.system = system;
        this.systemVersion = systemVersion;
        this.mobileBrand = mobileBrand;
        this.mobileModel = mobileModel;
        this.mobileResolution = mobileResolution;
        this.channel = channel;
        this.clientVersion = clientVersion;
    }

    @Override
    public String toString() {
        return "FeedbackOption{" +
                "userId=" + userId +
                ", mobile='" + mobile + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status=" + status +
                ", system=" + system +
                ", systemVersion='" + systemVersion + '\'' +
                ", mobileBrand='" + mobileBrand + '\'' +
                ", mobileModel='" + mobileModel + '\'' +
                ", mobileResolution='" + mobileResolution + '\'' +
                ", channel='" + channel + '\'' +
                ", clientVersion='" + clientVersion + '\'' +
                '}';
    }
}