package com.dajie.push.client;

/**
 * Created by wills on 3/25/14.
 */
public interface IPushClient {

    public boolean sendPush(String userId,Object object);

}
