package com.dajie.push.client;

import com.dajie.push.distributed.IDistributedManager;
import com.dajie.push.distributed.ZKDistributedManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Created by wills on 3/25/14.
 */
public class PushClient implements  IPushClient {
    
    private static final Logger LOGGER= LoggerFactory.getLogger(PushClient.class);

    private String apiKey;

    private String secretKey;

    private String appId;

    private String clientId;

    private String brokerUrl;

    private IDistributedManager distributedManager;

    private MqttClient mqttClient;

    private static PushClient pushClient=null;

    private PushClient(String appId,String apiKey,String secretKey,String env){
        //String brokerUrl, String clientId, String username, String password
        try{
            this.clientId="s"+IpAddressUtil.getPrivateIp()+ "#"+new Random().nextInt(1000);
            this.appId=appId;
            this.apiKey=apiKey;
            this.secretKey=secretKey;
            distributedManager=new ZKDistributedManager(env);
            //this.brokerUrl=distributedManager.getPrivateIpServer(clientId);
            ensureConnected();
        }catch(Exception e){
            LOGGER.error("connect to mqtt-broker error ",e);
        }
    };

    private void ensureConnected(){
        try{
            if(mqttClient==null||!mqttClient.isConnected()){
                this.brokerUrl=distributedManager.getPrivateIpServer(clientId);
                mqttClient=new MqttClient(this.brokerUrl,this.clientId,this.apiKey,this.secretKey);
                mqttClient.connect();
            }
        }catch(Exception e){
            LOGGER.error("ensure mqtt-broker connect error",e);
        }
    }


    public static PushClient getInstance(String appId,String apiKey,String secretKey,String env){
        if(pushClient==null){
            pushClient=new PushClient(appId,apiKey,secretKey,env);
        }
        return pushClient;
    }

    @Override
    public boolean sendPush(String userId, Object object) {
        ensureConnected();
        return mqttClient.publish(appId+"/"+userId,JsonUtil.toJson(object));
    }

}
