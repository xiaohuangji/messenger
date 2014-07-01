package com.dajie.push.mqtt.message;

/**
 * Doesn't care DUP, QOS and RETAIN flags.
 *
 * Created by wills on 3/12/14.
 */
public class PingRespMessage extends ZeroLengthMessage {
    
    public PingRespMessage() {
        m_messageType = PINGRESP;
    }
}
