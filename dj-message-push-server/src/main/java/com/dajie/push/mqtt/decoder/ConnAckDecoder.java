package com.dajie.push.mqtt.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;
import com.dajie.push.mqtt.message.ConnAckMessage;

/**
 *
 * @author wills
 */
class ConnAckDecoder extends BaseDecoder {

    @Override
    void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.resetReaderIndex();
        //Common decoding part
        ConnAckMessage message = new ConnAckMessage();
        if (!decodeCommonHeader(message, in)) {
            in.resetReaderIndex();
            return;
        }
        //skip reserved byte
        in.skipBytes(1);
        
        //read  return code
        message.setReturnCode(in.readByte());
        out.add(message);
    }
    
}
