package com.dajie;

import org.eclipse.paho.client.mqttv3.*;

/**
 * Created by wills on 3/17/14.
 */
public class MyMqttClient {

    private String clientId;

    private String brokerUrl="tcp://10.10.65.95:1883";

    private MqttClient mqttClient;

    public MyMqttClient(final String clientId) {
        try{
            this.clientId = clientId;
            mqttClient=new MqttClient(brokerUrl,clientId);
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    //System.out.println("connection lost");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("message arrived clientId:"+clientId+"  topic:"+topic);
                }

                @Override
                public void deliveryComplete (IMqttDeliveryToken token) {
                   // System.out.println("deliverty complete");
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public boolean connection(){
        try{
            MqttConnectOptions options=new MqttConnectOptions();
            options.setUserName("test_api_key");
            options.setPassword("test_secret_key".toCharArray());
            options.setKeepAliveInterval(100);
            options.setCleanSession(true);
            mqttClient.connect(options);
            System.out.println("conn succ");
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean subscribe(String topic){
        try{
            mqttClient.subscribe(topic,1);
            //System.out.println("subsrice succ");
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean publish(String topic,String content){
        try{
            mqttClient.publish(topic,content.getBytes(),1,true);
            //System.out.println("publish succ");
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean unsubscribe(String topic){
        try{
            mqttClient.unsubscribe(topic);
            System.out.println("unscribe succ");
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        MyMqttClient mqttClient=new MyMqttClient("xingfeng");
        mqttClient.connection();
        //mqttClient.subscribe("appid1/2");
        mqttClient.publish("appid1/10002","123");
        //System.exit(0);

        //mqttClient.publish("appid2/2","123");


    }

}
