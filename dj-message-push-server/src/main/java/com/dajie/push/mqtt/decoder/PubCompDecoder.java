package com.dajie.push.mqtt.decoder;

import com.dajie.push.mqtt.message.MessageIDMessage;
import com.dajie.push.mqtt.message.PubCompMessage;


/**
 *
 * @author wills
 */
class PubCompDecoder extends MessageIDDecoder {

    @Override
    protected MessageIDMessage createMessage() {
        return new PubCompMessage();
    }
}
