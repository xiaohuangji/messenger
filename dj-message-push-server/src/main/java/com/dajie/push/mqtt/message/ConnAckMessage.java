package com.dajie.push.mqtt.message;

/**
 * Created by wills on 3/12/14.
 */
public class ConnAckMessage extends AbstractMessage {
    public static final byte CONNECTION_ACCEPTED = 0x00;
    public static final byte UNACEPTABLE_PROTOCOL_VERSION = 0x01;
    public static final byte IDENTIFIER_REJECTED = 0x02;
    public static final byte SERVER_UNAVAILABLE = 0x03;
    public static final byte BAD_USERNAME_OR_PASSWORD = 0x04;
    public static final byte NOT_AUTHORIZED = 0x05;
    
    private byte m_returnCode=CONNECTION_ACCEPTED;
    
    public ConnAckMessage() {
        m_messageType = CONNACK;
    }

    public byte getReturnCode() {
        return m_returnCode;
    }

    public void setReturnCode(byte returnCode) {
        this.m_returnCode = returnCode;
    }
    
}
