package com.dajie.push.netty.stats;


import com.dajie.message.model.push.PushStatsInfo;
import com.dajie.push.utils.ClientUAUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wills on 4/2/14.
 */
public class DefaultStatsManager implements IStatsManager {

    private Map<Integer,AtomicInteger> map;

    public DefaultStatsManager(PushStatsInfo pushStatsInfo) {
        map=new ConcurrentHashMap<Integer, AtomicInteger>();
        map.put(PUSH_FROM_SERVER,new AtomicInteger(pushStatsInfo.getFromServer()));
        map.put(PUSH_FROM_ANDROID,new AtomicInteger(pushStatsInfo.getFromAndroid()));
        map.put(PUSH_FROM_IOS,new AtomicInteger(pushStatsInfo.getFromIos()));
        map.put(PUSH_TO_SERVER,new AtomicInteger(pushStatsInfo.getToServer()));
        map.put(PUSH_TO_ANDROID,new AtomicInteger(pushStatsInfo.getToAndroid()));
        map.put(PUSH_TO_IOS,new AtomicInteger(pushStatsInfo.getToIos()));

    }


    @Override
    public PushStats getPushStats() {
        int fromServer=map.get(PUSH_FROM_SERVER).intValue();
        int fromAndroid=map.get(PUSH_FROM_ANDROID).intValue();
        int fromIos=map.get(PUSH_FROM_IOS).intValue();
        int toServer=map.get(PUSH_TO_SERVER).intValue();
        int toAndroid=map.get(PUSH_TO_ANDROID).intValue();
        int toIos=map.get(PUSH_TO_IOS).intValue();

        return new PushStats(fromServer,fromAndroid,fromIos,toServer,toAndroid,toIos);
    }

    @Override
    public void incrPushStatus(String clientId,boolean down ) {
        ClientUAUtil.ClientUA ua=ClientUAUtil.checkUA(clientId);
        if(down){//to client
            if(ua.equals(ClientUAUtil.ClientUA.ANDROID)){
                map.get(PUSH_TO_ANDROID).incrementAndGet();
            }else if(ua.equals(ClientUAUtil.ClientUA.IOS)){
                map.get(PUSH_TO_IOS).incrementAndGet();
            }else{
                map.get(PUSH_TO_SERVER).incrementAndGet();
            }
        }else{//from client
            if(ua.equals(ClientUAUtil.ClientUA.ANDROID)){
                map.get(PUSH_FROM_ANDROID).incrementAndGet();
            }else if(ua.equals(ClientUAUtil.ClientUA.IOS)){
                map.get(PUSH_FROM_IOS).incrementAndGet();
            }else{
                map.get(PUSH_FROM_SERVER).incrementAndGet();
            }

        }
    }
}
