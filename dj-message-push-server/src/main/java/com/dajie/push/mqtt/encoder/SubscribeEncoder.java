package com.dajie.push.mqtt.encoder;

import com.dajie.push.mqtt.MQTTParseUtils;
import com.dajie.push.mqtt.message.AbstractMessage;
import com.dajie.push.mqtt.message.SubscribeMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author wills
 */
class SubscribeEncoder extends BaseEncoder<SubscribeMessage> {

    @Override
    protected void encode(ChannelHandlerContext chc, SubscribeMessage message, ByteBuf out) {
         if (message.subscriptions().isEmpty()) {
            throw new IllegalArgumentException("Found a subscribe message with empty topics");
        }

        if (message.getQos() != AbstractMessage.QOSType.LEAST_ONE) {
            throw new IllegalArgumentException("Expected a message with QOS 1, found " + message.getQos());
        }
        
        ByteBuf variableHeaderBuff = chc.alloc().buffer(4);
        ByteBuf buff = null;
        try {
            variableHeaderBuff.writeShort(message.getMessageID());
            for (SubscribeMessage.Couple c : message.subscriptions()) {
                variableHeaderBuff.writeBytes(MQTTParseUtils.encodeString(c.getTopic()));
                variableHeaderBuff.writeByte(c.getQos());
            }

            int variableHeaderSize = variableHeaderBuff.readableBytes();
            byte flags = MQTTParseUtils.encodeFlags(message);
            buff = chc.alloc().buffer(2 + variableHeaderSize);

            buff.writeByte(AbstractMessage.SUBSCRIBE << 4 | flags);
            buff.writeBytes(MQTTParseUtils.encodeRemainingLength(variableHeaderSize));
            buff.writeBytes(variableHeaderBuff);

            out.writeBytes(buff);
        } finally {
             variableHeaderBuff.release();
             buff.release();
        }
    }
    
}
