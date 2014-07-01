package com.dajie.push.mqtt.message;

import java.nio.ByteBuffer;

/**
 * Created by wills on 3/12/14.
 */

public class PublishMessage extends MessageIDMessage {

    private String m_topicName;

    private ByteBuffer m_payload;

    public PublishMessage() {
        m_messageType = PUBLISH;
    }

    public String getTopicName() {
        return m_topicName;
    }

    public void setTopicName(String topicName) {
        this.m_topicName = topicName;
    }

    public ByteBuffer getPayload() {
        return m_payload;
    }

    public void setPayload(ByteBuffer payload) {
        this.m_payload = payload;
    }
}
