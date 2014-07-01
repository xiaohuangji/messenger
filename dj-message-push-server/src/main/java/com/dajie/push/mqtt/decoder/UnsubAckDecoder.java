package com.dajie.push.mqtt.decoder;

import com.dajie.push.mqtt.message.MessageIDMessage;
import com.dajie.push.mqtt.message.UnsubAckMessage;

/**
 *
 * @author wills
 */
class UnsubAckDecoder extends MessageIDDecoder {

    @Override
    protected MessageIDMessage createMessage() {
        return new UnsubAckMessage();
    }
}

