package com.dajie.push.netty.handler;

import com.dajie.push.netty.channel.ConnectionStats;
import com.dajie.push.netty.stats.PushStats;
import io.netty.channel.Channel;

/**
 * Created by wills on 3/12/14.
 */
public interface IMqttMessageHandler {

    /**
     * mqtt message handler
     * @param event
     */
    public void handleMqttMessage(MessageEvent event);

    /**
     * remove channle from channelManager
     * @param channel
     */
    public void removeChannel(Channel channel);

    /**
     * get statistic
     * @return
     */
    public PushStats getPushStats();

    public ConnectionStats getConnStats();

    public PoolStats getPoolStats();

    /**
     * 将统计数据刷新到DB
     * @return
     */
    public int flushPushStatsToDB();

}
