package com.dajie.push.mqtt.message;

/**
 * Created by wills on 3/12/14.
 */
public class PubRecMessage extends MessageIDMessage {
    
    public PubRecMessage() {
        m_messageType = PUBREC;
    }
}
