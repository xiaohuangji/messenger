package com.dajie.push.idgenerator;

/**
 * Created by wills on 3/13/14.
 */
public interface IMsgIdGenerator {

    /**
     * gen mqtt-publish's msgId
     * @return
     */
    public short getMsgId();
}
