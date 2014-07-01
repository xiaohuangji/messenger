package com.dajie.push.mqtt.encoder;

import com.dajie.push.mqtt.MQTTException;
import com.dajie.push.mqtt.message.AbstractMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wills
 */
public class MQTTEncoderHandler extends MessageToByteEncoder {
    
    private Map<Byte, BaseEncoder> m_encoderMap = new HashMap<Byte, BaseEncoder>();
    
    public MQTTEncoderHandler() {
       m_encoderMap.put(AbstractMessage.CONNECT, new ConnectEncoder());
       m_encoderMap.put(AbstractMessage.CONNACK, new ConnAckEncoder());
       m_encoderMap.put(AbstractMessage.PUBLISH, new PublishEncoder());
       m_encoderMap.put(AbstractMessage.PUBACK, new PubAckEncoder());
       m_encoderMap.put(AbstractMessage.SUBSCRIBE, new SubscribeEncoder());
       m_encoderMap.put(AbstractMessage.SUBACK, new SubAckEncoder());
       m_encoderMap.put(AbstractMessage.UNSUBSCRIBE, new UnsubscribeEncoder());
       m_encoderMap.put(AbstractMessage.DISCONNECT, new DisconnectEncoder());
       m_encoderMap.put(AbstractMessage.PINGREQ, new PingReqEncoder());
       m_encoderMap.put(AbstractMessage.PINGRESP, new PingRespEncoder());
       m_encoderMap.put(AbstractMessage.UNSUBACK, new UnsubAckEncoder());
       m_encoderMap.put(AbstractMessage.PUBCOMP, new PubCompEncoder());
       m_encoderMap.put(AbstractMessage.PUBREC, new PubRecEncoder());
       m_encoderMap.put(AbstractMessage.PUBREL, new PubRelEncoder());
       m_encoderMap.put(AbstractMessage.COMMAND, new CommandEncoder());
    }
    
    @Override
    protected void encode(ChannelHandlerContext chc, Object msg, ByteBuf bb) throws Exception{
        if(!(msg instanceof AbstractMessage)){
            //not mqtt message,but a string. so just write it to channel derectly
            bb.writeBytes(((String) msg).getBytes());
            return;
        }
        AbstractMessage message=(AbstractMessage)msg;
        BaseEncoder encoder = m_encoderMap.get(message.getMessageType());
        if (encoder == null) {
            throw new MQTTException("Can't find any suitable encoder for message type: " + message.getMessageType());
        }
        encoder.encode(chc, message, bb);
    }
}
