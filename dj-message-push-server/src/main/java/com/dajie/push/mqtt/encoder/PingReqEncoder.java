package com.dajie.push.mqtt.encoder;

import com.dajie.push.mqtt.message.AbstractMessage;
import com.dajie.push.mqtt.message.PingReqMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author wills
 */
class PingReqEncoder  extends BaseEncoder<PingReqMessage> {

    @Override
    protected void encode(ChannelHandlerContext chc, PingReqMessage msg, ByteBuf out) {
        out.writeByte(AbstractMessage.PINGREQ << 4).writeByte(0);
    }
}
