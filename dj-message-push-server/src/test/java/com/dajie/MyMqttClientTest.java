package com.dajie;

import org.eclipse.paho.client.mqttv3.MqttClient;

/**
 * Created by wills on 3/21/14.
 */
public class MyMqttClientTest {

    public static void main(String[] args)throws Exception{
        MyMqttClientTest test=new MyMqttClientTest();
        test.test();
    }


    public void test(){
        int i=0;
        while(i++<10){
            new Thread(new Mythread("client"+i,"appid1/"+i)).start();
        }
    }


    class Mythread implements Runnable{

        private String clientId;

        private MyMqttClient myMqttClient;

        private String topic;

        Mythread(String clientId,String topic) {
            this.clientId = clientId;
            this.topic=topic;
            this.myMqttClient=new MyMqttClient(this.clientId);
        }

        @Override
        public void run() {
            myMqttClient.connection();
            System.out.println("conection succ:"+clientId);
            myMqttClient.subscribe(topic);
            System.out.println("subscribe succ:"+topic);
        }
    }
}
