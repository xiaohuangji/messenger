package com.dajie.message.model.operation;

import java.util.Date;

/**
 * Created by wills on 5/8/14.
 * 与数据库对应的model,包含过滤条件字段
 */
public class PushJobRaw {

    public static final int STATUS_NORMAL=1;

    public static final int STATUS_CANCELD=2;

    public static final int STATUS_FINISHED=3;

    public static final int STATUS_INVALID=4;

    private int id;

    private String content;

    private Date triggerDate;

    private int filterIsVerified;

    private String filterJobType;

    private String filterIndustry;

    private int filterGender;

    private String filterCity;

    private String filterClientVersion;

    private int filterMobileOs=0;

    private String operator;

    private int userCount;

    private int status=STATUS_NORMAL;

    private String filterDesc;

    private Date createTime=new Date();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getFilterIsVerified() {
        return filterIsVerified;
    }

    public void setFilterIsVerified(int filterIsVerified) {
        this.filterIsVerified = filterIsVerified;
    }

    public String getFilterJobType() {
        return filterJobType;
    }

    public void setFilterJobType(String filterJobType) {
        this.filterJobType = filterJobType;
    }

    public String getFilterIndustry() {
        return filterIndustry;
    }

    public void setFilterIndustry(String filterIndustry) {
        this.filterIndustry = filterIndustry;
    }

    public int getFilterGender() {
        return filterGender;
    }

    public void setFilterGender(int filterGender) {
        this.filterGender = filterGender;
    }

    public String getFilterCity() {
        return filterCity;
    }

    public void setFilterCity(String filterCity) {
        this.filterCity = filterCity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getFilterDesc() {
        return filterDesc;
    }

    public void setFilterDesc(String filterDesc) {
        this.filterDesc = filterDesc;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getFilterMobileOs() {
        return filterMobileOs;
    }

    public void setFilterMobileOs(int filterMobileOs) {
        this.filterMobileOs = filterMobileOs;
    }

    public String getFilterClientVersion() {
        return filterClientVersion;
    }

    public void setFilterClientVersion(String filterClientVersion) {
        this.filterClientVersion = filterClientVersion;
    }
}
