package com.dajie.push.netty.pushpool;

/**
 * Created by wills on 3/14/14.
 */
public interface IPushPoolManager {

    public void addPushEvent(PushEvent pushEvent);

    public void removePushEvent(String key);

    public int getPushPoolSize();
}
