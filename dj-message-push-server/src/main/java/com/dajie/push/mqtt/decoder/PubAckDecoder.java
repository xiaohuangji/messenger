package com.dajie.push.mqtt.decoder;

import com.dajie.push.mqtt.message.MessageIDMessage;
import com.dajie.push.mqtt.message.PubAckMessage;

/**
 *
 * @author wills
 */
class PubAckDecoder extends MessageIDDecoder {

    @Override
    protected MessageIDMessage createMessage() {
        return new PubAckMessage();
    }
    
}
