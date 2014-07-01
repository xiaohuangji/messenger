package com.dajie.push.mqtt.message;

/**
 * Created by wills on 3/12/14.
 */

public class DisconnectMessage extends ZeroLengthMessage {
    
    public DisconnectMessage() {
        m_messageType = DISCONNECT;
    }
}
