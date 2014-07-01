package com.dajie.push.mqtt.message;

/**
 * Created by wills on 4/1/14.
 */
public class CommandMessage extends AbstractMessage {

    public CommandMessage() {
        m_messageType=COMMAND;
    }

    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
