package com.dajie.push.mqtt.message;

/**
 * Base class for all the messages that carries only MessageID. (PUBACK, PUBREC,
 * PUBREL, PUBCOMP, UNSUBACK)
 *
 * Created by wills on 3/12/14.
 */
public abstract class MessageIDMessage extends AbstractMessage {
    private Integer m_messageID; //could be null if Qos is == 0

    public Integer getMessageID() {
        return m_messageID;
    }

    public void setMessageID(Integer messageID) {
        this.m_messageID = messageID;
    }

}
