package com.dajie.push.netty.stats;

import com.dajie.push.utils.JsonUtil;

/**
 * Created by wills on 4/2/14.
 */
public class PushStats {

    private int fromServer;

    public int fromAndroid;

    public int fromIos;

    public int toAndroid;

    public int toIos;

    public int toServer;

    public PushStats(int fromServer, int fromAndroid, int fromIos, int toServer,int toAndroid, int toIos) {
        this.fromServer = fromServer;
        this.fromAndroid = fromAndroid;
        this.fromIos = fromIos;
        this.toAndroid = toAndroid;
        this.toIos = toIos;
        this.toServer=toServer;
    }

    public int getFromServer() {
        return fromServer;
    }

    public void setFromServer(int fromServer) {
        this.fromServer = fromServer;
    }

    public int getFromAndroid() {
        return fromAndroid;
    }

    public void setFromAndroid(int fromAndroid) {
        this.fromAndroid = fromAndroid;
    }

    public int getFromIos() {
        return fromIos;
    }

    public void setFromIos(int fromIos) {
        this.fromIos = fromIos;
    }

    public int getToAndroid() {
        return toAndroid;
    }

    public void setToAndroid(int toAndroid) {
        this.toAndroid = toAndroid;
    }

    public int getToIos() {
        return toIos;
    }

    public void setToIos(int toIos) {
        this.toIos = toIos;
    }

    public int getToServer() {
        return toServer;
    }

    public void setToServer(int toServer) {
        this.toServer = toServer;
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
