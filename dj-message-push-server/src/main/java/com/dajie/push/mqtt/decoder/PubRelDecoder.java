package com.dajie.push.mqtt.decoder;

import com.dajie.push.mqtt.message.MessageIDMessage;
import com.dajie.push.mqtt.message.PubRelMessage;

/**
 *
 * @author wills
 */
class PubRelDecoder extends MessageIDDecoder {

    @Override
    protected MessageIDMessage createMessage() {
        return new PubRelMessage();
    }

}

