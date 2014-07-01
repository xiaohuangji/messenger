package com.dajie.push.mqtt.message;


/**
 * Created by wills on 3/12/14.
*/
public class PubRelMessage extends MessageIDMessage {
    
    public PubRelMessage() {
        m_messageType = PUBREL;
    }
}
