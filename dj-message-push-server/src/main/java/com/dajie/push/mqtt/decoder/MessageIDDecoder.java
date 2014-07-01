package com.dajie.push.mqtt.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;
import com.dajie.push.mqtt.message.MessageIDMessage;

/**
 *
 * @author wills
 */
abstract class MessageIDDecoder extends BaseDecoder {
    
    protected abstract MessageIDMessage createMessage();

    @Override
    void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.resetReaderIndex();
        //Common decoding part
        MessageIDMessage message = createMessage();
        if (!decodeCommonHeader(message, in)) {
            in.resetReaderIndex();
            return;
        }
        
        //read  messageIDs
        message.setMessageID(in.readUnsignedShort());
        out.add(message);
    }
    
}
