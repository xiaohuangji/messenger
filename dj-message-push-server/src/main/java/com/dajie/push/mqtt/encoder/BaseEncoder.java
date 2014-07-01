package com.dajie.push.mqtt.encoder;

import com.dajie.push.mqtt.message.AbstractMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author wills
 */
abstract class BaseEncoder<T extends AbstractMessage> {
    abstract protected void encode(ChannelHandlerContext chc, T msg, ByteBuf bb);
}
