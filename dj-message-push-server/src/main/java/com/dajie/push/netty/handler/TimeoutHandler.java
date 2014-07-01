package com.dajie.push.netty.handler;

import com.dajie.push.netty.channel.IChannelManager;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wills on 3/12/14.
 */
public class TimeoutHandler extends ChannelHandlerAdapter {

    private static final Logger LOGGER= LoggerFactory.getLogger(TimeoutHandler.class);

    private IChannelManager channelManager;

    public TimeoutHandler(IChannelManager channelManager) {
        this.channelManager = channelManager;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleState e=((IdleStateEvent)evt).state();
            if(e==IdleState.READER_IDLE){
                LOGGER.info("[channel] timeout happen, close it and remove it from channelManager");
                channelManager.removeChannel(ctx.channel());
                ctx.close();
            }
        }

    }
}
