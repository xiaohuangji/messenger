package com.dajie.push.mqtt.encoder;

import com.dajie.push.mqtt.message.AbstractMessage;
import com.dajie.push.mqtt.message.PingRespMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author wills
 */
class PingRespEncoder extends BaseEncoder<PingRespMessage> {

    @Override
    protected void encode(ChannelHandlerContext chc, PingRespMessage msg, ByteBuf out) {
        out.writeByte(AbstractMessage.PINGRESP << 4).writeByte(0);
    }
}
