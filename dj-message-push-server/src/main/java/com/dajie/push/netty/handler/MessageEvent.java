package com.dajie.push.netty.handler;

import com.dajie.push.mqtt.message.AbstractMessage;
import com.lmax.disruptor.EventFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by wills on 3/12/14.
 */
public class MessageEvent {

    private AbstractMessage abstractMessage;

    private Channel channel;

    public MessageEvent(){

    }
    public MessageEvent(Channel channel, AbstractMessage abstractMessage) {
        this.abstractMessage = abstractMessage;
        this.channel=channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public AbstractMessage getAbstractMessage() {
        return abstractMessage;
    }

    public void setAbstractMessage(AbstractMessage abstractMessage) {
        this.abstractMessage = abstractMessage;
    }
}
