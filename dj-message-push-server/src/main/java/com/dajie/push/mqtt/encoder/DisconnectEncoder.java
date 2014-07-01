package com.dajie.push.mqtt.encoder;

import com.dajie.push.mqtt.message.AbstractMessage;
import com.dajie.push.mqtt.message.DisconnectMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author wills
 */
public class DisconnectEncoder extends BaseEncoder<DisconnectMessage> {

    @Override
    protected void encode(ChannelHandlerContext chc, DisconnectMessage msg, ByteBuf out) {
        out.writeByte(AbstractMessage.DISCONNECT << 4).writeByte(0);
    }
    
}
