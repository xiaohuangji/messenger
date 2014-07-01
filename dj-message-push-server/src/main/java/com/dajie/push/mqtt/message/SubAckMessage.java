package com.dajie.push.mqtt.message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wills on 3/12/14.
 */
public class SubAckMessage extends MessageIDMessage {

    List<QOSType> m_types = new ArrayList<QOSType>();
    
    public SubAckMessage() {
        m_messageType = SUBACK;
    }

    public List<QOSType> types() {
        return m_types;
    }

    public void addType(QOSType type) {
        m_types.add(type);
    }
}
