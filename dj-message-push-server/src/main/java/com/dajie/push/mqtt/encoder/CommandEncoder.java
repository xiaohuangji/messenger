package com.dajie.push.mqtt.encoder;

import com.dajie.push.mqtt.MQTTParseUtils;
import com.dajie.push.mqtt.message.AbstractMessage;
import com.dajie.push.mqtt.message.CommandMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by wills on 4/1/14.
 */
public class CommandEncoder extends BaseEncoder<CommandMessage>{

    @Override
    protected void encode(ChannelHandlerContext chc, CommandMessage msg, ByteBuf out) {
        out.writeByte(AbstractMessage.COMMAND << 4).writeByte(0);
        out.writeBytes(MQTTParseUtils.encodeString(msg.getCommand()));
    }
}
