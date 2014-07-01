package com.dajie.push.utils;

/**
 * Created by wills on 4/2/14.
 */
public class ClientUAUtil {

    public static enum ClientUA{
        ANDROID,IOS,SERVER,UNKNOWN;
    }

    public static ClientUA checkUA(String clientId){
        if(clientId.startsWith("1")){
            return ClientUA.IOS;
        }else if(clientId.startsWith("2")){
            return ClientUA.ANDROID;
        }else if(clientId.startsWith("s")){
            return ClientUA.SERVER;
        }
        return ClientUA.UNKNOWN;
    }
}
