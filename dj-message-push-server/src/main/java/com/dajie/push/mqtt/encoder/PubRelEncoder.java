package com.dajie.push.mqtt.encoder;

import com.dajie.push.mqtt.MQTTParseUtils;
import com.dajie.push.mqtt.message.AbstractMessage;
import com.dajie.push.mqtt.message.PubRelMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author wills
 */
class PubRelEncoder extends BaseEncoder<PubRelMessage> {

    @Override
    protected void encode(ChannelHandlerContext chc, PubRelMessage msg, ByteBuf out) {
        out.writeByte(AbstractMessage.PUBREL << 4);
        out.writeBytes(MQTTParseUtils.encodeRemainingLength(2));
        out.writeShort(msg.getMessageID());
    }
}