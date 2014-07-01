package com.dajie.push.mqtt.encoder;

import com.dajie.push.mqtt.MQTTParseUtils;
import com.dajie.push.mqtt.message.AbstractMessage;
import com.dajie.push.mqtt.message.PubCompMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author wills
 */
class PubCompEncoder extends BaseEncoder<PubCompMessage> {

    @Override
    protected void encode(ChannelHandlerContext chc, PubCompMessage msg, ByteBuf out) {
        out.writeByte(AbstractMessage.PUBCOMP << 4);
        out.writeBytes(MQTTParseUtils.encodeRemainingLength(2));
        out.writeShort(msg.getMessageID());
    }
}
