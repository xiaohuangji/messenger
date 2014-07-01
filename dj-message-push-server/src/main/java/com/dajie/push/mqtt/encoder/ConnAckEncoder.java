package com.dajie.push.mqtt.encoder;

import com.dajie.push.mqtt.MQTTParseUtils;
import com.dajie.push.mqtt.message.AbstractMessage;
import com.dajie.push.mqtt.message.ConnAckMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author wills
 */
class ConnAckEncoder extends BaseEncoder<ConnAckMessage> {

    @Override
    protected void encode(ChannelHandlerContext chc, ConnAckMessage message, ByteBuf out) {
        out.writeByte(AbstractMessage.CONNACK << 4);
        out.writeBytes(MQTTParseUtils.encodeRemainingLength(2));
        out.writeByte(0);
        out.writeByte(message.getReturnCode());
    }
    
}
