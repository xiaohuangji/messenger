package com.dajie.push.mqtt.encoder;

import com.dajie.push.mqtt.MQTTParseUtils;
import com.dajie.push.mqtt.message.AbstractMessage;
import com.dajie.push.mqtt.message.ConnectMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author wills
 */
public class ConnectEncoder extends BaseEncoder<ConnectMessage> {

    @Override
    protected void encode(ChannelHandlerContext chc, ConnectMessage message, ByteBuf out) {
        ByteBuf staticHeaderBuff = chc.alloc().buffer(12);
        ByteBuf buff = chc.alloc().buffer();
        ByteBuf variableHeaderBuff = chc.alloc().buffer(12);
        try {
            staticHeaderBuff.writeBytes(MQTTParseUtils.encodeString("MQIsdp"));

            //version 
            staticHeaderBuff.writeByte(0x03);

            //connection flags and Strings
            byte connectionFlags = 0;
            if (message.isCleanSession()) {
                connectionFlags |= 0x02;
            }
            if (message.isWillFlag()) {
                connectionFlags |= 0x04;
            }
            connectionFlags |= ((message.getWillQos() & 0x03) << 3);
            if (message.isWillRetain()) {
                connectionFlags |= 0x020;
            }
            if (message.isPasswordFlag()) {
                connectionFlags |= 0x040;
            }
            if (message.isUserFlag()) {
                connectionFlags |= 0x080;
            }
            staticHeaderBuff.writeByte(connectionFlags);

            //Keep alive timer
            staticHeaderBuff.writeShort(message.getKeepAlive());

            //Variable part
            if (message.getClientID() != null) {
                variableHeaderBuff.writeBytes(MQTTParseUtils.encodeString(message.getClientID()));
                if (message.isWillFlag()) {
                    variableHeaderBuff.writeBytes(MQTTParseUtils.encodeString(message.getWillTopic()));
                    variableHeaderBuff.writeBytes(MQTTParseUtils.encodeString(message.getWillMessage()));
                }
                if (message.isUserFlag() && message.getUsername() != null) {
                    variableHeaderBuff.writeBytes(MQTTParseUtils.encodeString(message.getUsername()));
                    if (message.isPasswordFlag() && message.getPassword() != null) {
                        variableHeaderBuff.writeBytes(MQTTParseUtils.encodeString(message.getPassword()));
                    }
                }
            }

            int variableHeaderSize = variableHeaderBuff.readableBytes();
            buff.writeByte(AbstractMessage.CONNECT << 4);
            buff.writeBytes(MQTTParseUtils.encodeRemainingLength(12 + variableHeaderSize));
            buff.writeBytes(staticHeaderBuff).writeBytes(variableHeaderBuff);

            out.writeBytes(buff);
        } finally {
            staticHeaderBuff.release();
            buff.release();
            variableHeaderBuff.release();
        }
    }
    
}
