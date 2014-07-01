package com.dajie.push.mqtt.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;

import com.dajie.push.mqtt.message.PingReqMessage;

/**
 *
 * @author wills
 */
class PingReqDecoder extends BaseDecoder {

    @Override
    void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //Common decoding part
        in.resetReaderIndex();
        PingReqMessage message = new PingReqMessage();
        if (!decodeCommonHeader(message, in)) {
            in.resetReaderIndex();
            return;
        }
        out.add(message);
    }
}
