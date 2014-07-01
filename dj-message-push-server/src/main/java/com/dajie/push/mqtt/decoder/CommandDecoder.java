package com.dajie.push.mqtt.decoder;

import com.dajie.push.mqtt.MQTTParseUtils;
import com.dajie.push.mqtt.message.CommandMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * Created by wills on 4/1/14.
 */
public class CommandDecoder extends BaseDecoder {

    @Override
    void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        CommandMessage message = new CommandMessage();

        in.resetReaderIndex();
        if (!decodeCommonHeader(message, in)) {
            in.resetReaderIndex();
            return;
        }

        String command = MQTTParseUtils.decodeString(in);
        message.setCommand(command);

        out.add(message);
    }
}
