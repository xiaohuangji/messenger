package com.dajie.push.client;

import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wills on 3/17/14.
 *
 */
class MqttClient {

    private static final Logger LOGGER= LoggerFactory.getLogger(MqttClient.class);

    private String clientId;

    private String brokerUrl;

    private String username;

    private String password;

    private org.eclipse.paho.client.mqttv3.MqttClient mqttClient;

    /**
     * 与broker保持心跳的间隔
     */
    private static final int KEEPALIVEINTERVAL=100;

    private static final int CONNTIMEOUT=20;

    public MqttClient(String brokerUrl, String clientId, String username, String password) {
        try{
            this.clientId = clientId;
            this.brokerUrl= "tcp://"+brokerUrl;
            this.username = username;
            this.password= password;

            mqttClient=new org.eclipse.paho.client.mqttv3.MqttClient(this.brokerUrl,this.clientId);
//            mqttClient.setCallback(new MqttCallback() {
//                @Override
//                public void connectionLost(Throwable cause) {
//                }
//
//                @Override
//                public void messageArrived(String topic, MqttMessage message) throws Exception {
//                }
//
//                @Override
//                public void deliveryComplete (IMqttDeliveryToken token) {
//                }
//            });
        }catch(Exception e){
            LOGGER.error("mqtt client init fail,",e);
        }

    }

    public boolean isConnected(){
        return mqttClient==null?false:mqttClient.isConnected();
    }
    public boolean connect(){
        try{
            MqttConnectOptions options=new MqttConnectOptions();
            options.setConnectionTimeout(CONNTIMEOUT);
            options.setUserName(this.username);
            options.setPassword(this.password.toCharArray());
            options.setKeepAliveInterval(KEEPALIVEINTERVAL);
            options.setCleanSession(true);
            mqttClient.connect(options);
            return true;
        }catch(Exception e){
            LOGGER.error("push client connect fail,",e);
            return false;
        }
    }

    public boolean publish(String topic,String content){
        try{
            mqttClient.publish(topic,content.getBytes(),1,true);
            return true;
        }catch(Exception e){
            LOGGER.error("mqtt push client publish fail,",e);
            return false;
        }
    }

    public boolean unsubscribe(String topic){
        try{
            mqttClient.unsubscribe(topic);
            return true;
        }catch(Exception e){
            LOGGER.error("mqtt push client unsubscribe fail,",e);
            return false;
        }
    }

    public boolean subscribe(String topic){
        try{
            mqttClient.subscribe(topic);
            return true;
        }catch(Exception e){
            LOGGER.error("mqtt push client unsubscribe fail,",e);
            return false;
        }
    }

}
