package com.dajie.push.mqtt.encoder;

import com.dajie.push.mqtt.MQTTParseUtils;
import com.dajie.push.mqtt.message.AbstractMessage;
import com.dajie.push.mqtt.message.PublishMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author wills
 */
class PublishEncoder extends BaseEncoder<PublishMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, PublishMessage message, ByteBuf out) {
        if (message.getQos() == AbstractMessage.QOSType.RESERVED) {
            throw new IllegalArgumentException("Found a message with RESERVED Qos");
        }
        if (message.getTopicName() == null || message.getTopicName().isEmpty()) {
            throw new IllegalArgumentException("Found a message with empty or null topic name");
        }
        
        ByteBuf variableHeaderBuff = ctx.alloc().buffer(2);
        try {
            variableHeaderBuff.writeBytes(MQTTParseUtils.encodeString(message.getTopicName()));
            if (message.getQos() == AbstractMessage.QOSType.LEAST_ONE || 
                message.getQos() == AbstractMessage.QOSType.EXACTLY_ONCE ) {
                if (message.getMessageID() == null) {
                    throw new IllegalArgumentException("Found a message with QOS 1 or 2 and not MessageID setted");
                }
                variableHeaderBuff.writeShort(message.getMessageID());
            }
            variableHeaderBuff.writeBytes(message.getPayload());
            int variableHeaderSize = variableHeaderBuff.readableBytes();

            byte flags = MQTTParseUtils.encodeFlags(message);

            ByteBuf buff = ctx.alloc().buffer(2 + variableHeaderSize);
            buff.writeByte(AbstractMessage.PUBLISH << 4 | flags);
            buff.writeBytes(MQTTParseUtils.encodeRemainingLength(variableHeaderSize));
            buff.writeBytes(variableHeaderBuff);
            out.writeBytes(buff);
        } finally {
            variableHeaderBuff.release();
            message.getPayload().clear();
        }
    }
    
}
