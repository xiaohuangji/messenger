package com.dajie.push.mqtt.decoder;

import com.dajie.push.mqtt.message.MessageIDMessage;
import com.dajie.push.mqtt.message.PubRecMessage;

/**
 *
 * @author wills
 */
class PubRecDecoder extends MessageIDDecoder {

    @Override
    protected MessageIDMessage createMessage() {
        return new PubRecMessage();
    }
}
