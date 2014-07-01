package com.dajie.push.netty.channel;

import io.netty.channel.Channel;

/**
 * Created by wills on 3/13/14.
 */
public interface IChannelManager {

    /**
     * android
     */
    public static int CONN_ANDROID=1;

    /**
     * ios
     */
    public static int CONN_IOS=2;

    /**
     * server-side
     */
    public static int CONN_SERVER=3;

    public void addChannel(NettyChannel nettyChannel);

    public void removeChannel(Channel channel);

    public void bindUserIdToChannel(String clientId,String userId);

    public NettyChannel getChannel(Channel channel);

    public NettyChannel getChannel(String clientId);

    public ConnectionStats getConnStats();
}
