package com.dajie.push.netty.channel;

import com.dajie.push.utils.ClientUAUtil;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * I think the class is suck
 * Created by wills on 3/13/14.
 */
public class DefaultChannelManager implements IChannelManager{

    private static final Logger LOGGER= LoggerFactory.getLogger(DefaultChannelManager.class);

    private static Map<Channel,NettyChannel> channelMap=new ConcurrentHashMap<Channel, NettyChannel>();

    private static Map<String,NettyChannel> channelMap2=new ConcurrentHashMap<String, NettyChannel>();

    private static Map<Integer,AtomicInteger> connStatsMap=new ConcurrentHashMap<Integer, AtomicInteger>();

    public DefaultChannelManager() {
        connStatsMap.put(CONN_ANDROID,new AtomicInteger(0));
        connStatsMap.put(CONN_IOS,new AtomicInteger(0));
        connStatsMap.put(CONN_SERVER,new AtomicInteger(0));
    }

    @Override
    public void addChannel(NettyChannel nettyChannel) {
        Channel channel=nettyChannel.getChannel();
        String clientId=nettyChannel.getClientId();
        NettyChannel oldNettyChannel=channelMap2.get(clientId);
        if(oldNettyChannel!=null){
            LOGGER.info("kick out the old connection, clientId:"+clientId);
            channelMap.remove(oldNettyChannel.getChannel());
            channelMap2.remove(clientId);
            decrConn(clientId);
            if(oldNettyChannel.getChannel().isOpen()){
                oldNettyChannel.getChannel().close();
            }

        }
        channelMap.put(channel,nettyChannel);
        channelMap2.put(clientId,nettyChannel);
        incrConn(clientId);
    }

    @Override
    public void removeChannel(Channel channel) {
        NettyChannel nettyChannel= channelMap.get(channel);
        if(nettyChannel!=null){
            String clientId=nettyChannel.getClientId();
            channelMap2.remove(clientId);
            decrConn(clientId);
            LOGGER.info("remove channel,clientId:"+nettyChannel.getClientId());
        }
        channelMap.remove(channel);
    }

    private void decrConn(String clientId){
        ClientUAUtil.ClientUA ua=ClientUAUtil.checkUA(clientId);
        if(ua.equals(ClientUAUtil.ClientUA.ANDROID)){
            connStatsMap.get(CONN_ANDROID).decrementAndGet();
        }else if(ua.equals(ClientUAUtil.ClientUA.IOS)){
            connStatsMap.get(CONN_IOS).decrementAndGet();
        }else{
            connStatsMap.get(CONN_SERVER).decrementAndGet();
        }
    }

    private void incrConn(String clientId){
        ClientUAUtil.ClientUA ua=ClientUAUtil.checkUA(clientId);
        if(ua.equals(ClientUAUtil.ClientUA.ANDROID)){
            connStatsMap.get(CONN_ANDROID).incrementAndGet();
        }else if(ua.equals(ClientUAUtil.ClientUA.IOS)){
            connStatsMap.get(CONN_IOS).incrementAndGet();
        }else{
            connStatsMap.get(CONN_SERVER).incrementAndGet();
        }
    }
    @Override
    public ConnectionStats getConnStats() {
        int connServer=connStatsMap.get(CONN_SERVER).intValue();
        int connAndroid=connStatsMap.get(CONN_ANDROID).intValue();
        int connIos=connStatsMap.get(CONN_IOS).intValue();
        return new ConnectionStats(connIos,connAndroid,connServer);
    }

    @Override
    public NettyChannel getChannel(Channel channel) {
        return channelMap.get(channel);
    }

    @Override
    public NettyChannel getChannel(String clientId){
        return channelMap2.get(clientId);
    }

    @Override
    public void bindUserIdToChannel(String clientId, String userId) {
        NettyChannel channel=getChannel(clientId);
        channel.setUserId(userId);
    }
}
