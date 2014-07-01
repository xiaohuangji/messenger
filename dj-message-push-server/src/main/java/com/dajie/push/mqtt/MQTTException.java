package com.dajie.push.mqtt;

/**
 * Created by wills on 3/13/14.
 */
public class MQTTException extends RuntimeException {

    public MQTTException(){
        super();
    }

    public MQTTException(String msg){
        super(msg);
    }

    public MQTTException(Throwable cause){
        super(cause);
    }

    public MQTTException(String msg,Throwable cause){
        super(msg,cause);
    }
}
