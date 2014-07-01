package com.dajie.message.model.message;

/**
 * Created by wills on 3/24/14.
 */
class GoudaMessage {

    public static final int CHAT=1;//聊天消息

    public static final int SYSTEM=2;//系统消息

    /**
     * 消息类型
     */
    private int msgType;

    /**
     * 聊天类型
     */
    private int contentType;

    /**
     * 消息产生时间。下行有效。
     */
    private long time;

    /**
     * 消息发送者
     */
    private int from;

    /**
     * 消息接收者
     */
    private int to;

    /**
     * 消息内容，不同类型的消息，内容不一样
     */
    private Object content;

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}

