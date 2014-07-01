package com.dajie.push.mqtt;

/**
 * Created by wills on 3/13/14.
 */
public class MQTTTopicUtils {

    public static MQTTTopic parseTopic(String topic){
        try{
            String[] str=topic.split("/");
            MQTTTopic mqttTopic=new MQTTTopic();
            mqttTopic.setAppId(str[0]);
            mqttTopic.setUserId(str.length>1?str[1]:null);
            mqttTopic.setFlag(str.length>2?str[2]:null);
            return mqttTopic;
        }catch(Exception e){
            throw new MQTTException("mqttTopic parse error:",e);
        }
    }

    public static String genTopic(String appId,String userId){
        return new StringBuffer(appId).append("/").append(userId).toString();
    }
}
