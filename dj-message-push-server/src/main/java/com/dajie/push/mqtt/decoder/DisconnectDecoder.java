package com.dajie.push.mqtt.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;
import com.dajie.push.mqtt.message.DisconnectMessage;

/**
 *
 * @author wills
 */
class DisconnectDecoder extends BaseDecoder {

    @Override
    void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //Common decoding part
        in.resetReaderIndex();
        DisconnectMessage message = new DisconnectMessage();
        if (!decodeCommonHeader(message, in)) {
            in.resetReaderIndex();
            return;
        }
        out.add(message);
    }
    
}
