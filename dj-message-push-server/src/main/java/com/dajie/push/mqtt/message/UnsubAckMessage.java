package com.dajie.push.mqtt.message;

/**
 * Created by wills on 3/12/14.
 */
public class UnsubAckMessage extends MessageIDMessage {
    
    public UnsubAckMessage() {
        m_messageType = UNSUBACK;
    }
}

