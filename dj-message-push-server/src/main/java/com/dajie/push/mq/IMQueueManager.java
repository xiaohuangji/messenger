package com.dajie.push.mq;

/**
 * Created by wills on 3/25/14.
 */
public interface IMQueueManager {

    /**
     * when broker receive a upstream message and put it to MQ
     * @param appId
     */
    public void put(String appId,byte[] data);
}
