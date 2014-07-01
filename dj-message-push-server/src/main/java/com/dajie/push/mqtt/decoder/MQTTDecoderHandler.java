package com.dajie.push.mqtt.decoder;

import com.dajie.push.mqtt.MQTTException;
import com.dajie.push.mqtt.MQTTParseUtils;
import com.dajie.push.mqtt.message.AbstractMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author wills
 */
public class MQTTDecoderHandler extends ByteToMessageDecoder {
    
    private Map<Byte, BaseDecoder> m_decoderMap = new HashMap<Byte, BaseDecoder>();
    
    public MQTTDecoderHandler() {
       m_decoderMap.put(AbstractMessage.CONNECT, new ConnectDecoder());
       m_decoderMap.put(AbstractMessage.CONNACK, new ConnAckDecoder());
       m_decoderMap.put(AbstractMessage.PUBLISH, new PublishDecoder());
       m_decoderMap.put(AbstractMessage.PUBACK, new PubAckDecoder());
       m_decoderMap.put(AbstractMessage.SUBSCRIBE, new SubscribeDecoder());
       m_decoderMap.put(AbstractMessage.SUBACK, new SubAckDecoder());
       m_decoderMap.put(AbstractMessage.UNSUBSCRIBE, new UnsubscribeDecoder());
       m_decoderMap.put(AbstractMessage.DISCONNECT, new DisconnectDecoder());
       m_decoderMap.put(AbstractMessage.PINGREQ, new PingReqDecoder());
       m_decoderMap.put(AbstractMessage.PINGRESP, new PingRespDecoder());
       m_decoderMap.put(AbstractMessage.UNSUBACK, new UnsubAckDecoder());
       m_decoderMap.put(AbstractMessage.PUBCOMP, new PubCompDecoder());
       m_decoderMap.put(AbstractMessage.PUBREC, new PubRecDecoder());
       m_decoderMap.put(AbstractMessage.PUBREL, new PubRelDecoder());
       m_decoderMap.put(AbstractMessage.COMMAND, new CommandDecoder());
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();
        if (!MQTTParseUtils.checkHeaderAvailability(in)) {
            in.resetReaderIndex();
            //copy bytebuf to out and stringdecode will decode it.
//            ByteBuf byteBuf= Unpooled.buffer(in.readableBytes());
//            in.readBytes(byteBuf);
//            out.add(byteBuf);
            return;
        }
        in.resetReaderIndex();
        
        byte messageType = MQTTParseUtils.readMessageType(in);
        
        BaseDecoder decoder = m_decoderMap.get(messageType);
        if (decoder == null) {
            throw new MQTTException("Can't find any suitable decoder for message type: " + messageType);
        }
        decoder.decode(ctx, in, out);
    }
}
