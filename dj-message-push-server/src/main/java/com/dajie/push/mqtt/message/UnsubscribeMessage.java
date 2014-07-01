package com.dajie.push.mqtt.message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wills on 3/12/14.
 */

public class UnsubscribeMessage extends MessageIDMessage {
    List<String> m_types = new ArrayList<String>();
    
    public UnsubscribeMessage() {
        m_messageType = UNSUBSCRIBE;
    }

    public List<String> topics() {
        return m_types;
    }

    public void addTopic(String type) {
        m_types.add(type);
    }
}
