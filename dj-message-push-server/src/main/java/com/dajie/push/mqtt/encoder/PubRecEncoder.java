package com.dajie.push.mqtt.encoder;

import com.dajie.push.mqtt.MQTTParseUtils;
import com.dajie.push.mqtt.message.AbstractMessage;
import com.dajie.push.mqtt.message.PubRecMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author wills
 */
class PubRecEncoder extends BaseEncoder<PubRecMessage> {

    @Override
    protected void encode(ChannelHandlerContext chc, PubRecMessage msg, ByteBuf out) {
        out.writeByte(AbstractMessage.PUBREC << 4);
        out.writeBytes(MQTTParseUtils.encodeRemainingLength(2));
        out.writeShort(msg.getMessageID());
    }
}