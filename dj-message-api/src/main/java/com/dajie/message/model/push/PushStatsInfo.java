package com.dajie.message.model.push;

import java.util.Date;

/**
 * Created by wills on 4/25/14.
 */
public class PushStatsInfo {

    private String serverName;

    private int iosConn=0;

    private int androidConn=0;;

    private int serverConn=0;

    private int fromIos=0;

    private int fromAndroid=0;

    private int fromServer=0;

    private int toIos=0;

    private int toAndroid=0;

    private int toServer=0;

    private Date createTime=new Date();

    private Date updateTime;

    public PushStatsInfo(String serverName) {
        this.serverName = serverName;
    }

    public PushStatsInfo() {
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getIosConn() {
        return iosConn;
    }

    public void setIosConn(int iosConn) {
        this.iosConn = iosConn;
    }

    public int getAndroidConn() {
        return androidConn;
    }

    public void setAndroidConn(int androidConn) {
        this.androidConn = androidConn;
    }

    public int getServerConn() {
        return serverConn;
    }

    public void setServerConn(int serverConn) {
        this.serverConn = serverConn;
    }

    public int getFromIos() {
        return fromIos;
    }

    public void setFromIos(int fromIos) {
        this.fromIos = fromIos;
    }

    public int getFromAndroid() {
        return fromAndroid;
    }

    public void setFromAndroid(int fromAndroid) {
        this.fromAndroid = fromAndroid;
    }

    public int getFromServer() {
        return fromServer;
    }

    public void setFromServer(int fromServer) {
        this.fromServer = fromServer;
    }

    public int getToIos() {
        return toIos;
    }

    public void setToIos(int toIos) {
        this.toIos = toIos;
    }

    public int getToAndroid() {
        return toAndroid;
    }

    public void setToAndroid(int toAndroid) {
        this.toAndroid = toAndroid;
    }

    public int getToServer() {
        return toServer;
    }

    public void setToServer(int toServer) {
        this.toServer = toServer;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
