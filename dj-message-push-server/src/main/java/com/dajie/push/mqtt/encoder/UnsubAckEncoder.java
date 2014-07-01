package com.dajie.push.mqtt.encoder;

import com.dajie.push.mqtt.MQTTParseUtils;
import com.dajie.push.mqtt.message.AbstractMessage;
import com.dajie.push.mqtt.message.UnsubAckMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author wills
 */
class UnsubAckEncoder extends BaseEncoder<UnsubAckMessage> {
    
    @Override
    protected void encode(ChannelHandlerContext chc, UnsubAckMessage msg, ByteBuf out) {
        out.writeByte(AbstractMessage.UNSUBACK << 4).
                writeBytes(MQTTParseUtils.encodeRemainingLength(2)).
                writeShort(msg.getMessageID());
    }
}