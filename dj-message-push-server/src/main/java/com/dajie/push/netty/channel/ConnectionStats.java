package com.dajie.push.netty.channel;

import com.dajie.push.utils.JsonUtil;

/**
 * connection status and statistics
 * @see com.dajie.push.netty.channel.IChannelManager
 * Created by wills on 4/2/14.
 */
public class ConnectionStats {

    private int ios;

    private int android;

    private int server;

    public ConnectionStats(int ios, int android,int server) {
        this.ios = ios;
        this.android = android;
        this.server=server;
    }

    public int getServer() {
        return server;
    }

    public void setServer(int server) {
        this.server = server;
    }

    public int getIos() {
        return ios;
    }

    public void setIos(int ios) {
        this.ios = ios;
    }

    public int getAndroid() {
        return android;
    }

    public void setAndroid(int android) {
        this.android = android;
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
