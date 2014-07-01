package com.dajie.push.netty.filter;

import com.dajie.push.netty.channel.NettyChannel;

/**
 * Created by wills on 5/4/14.
 */
public interface IFilter {

    public boolean filter(NettyChannel channel,String destUserId,String payload);
}
