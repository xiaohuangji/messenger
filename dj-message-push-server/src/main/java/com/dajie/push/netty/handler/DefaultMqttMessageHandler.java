package com.dajie.push.netty.handler;

import com.dajie.message.model.push.ClientInfo;
import com.dajie.message.model.push.PushInfo;
import com.dajie.message.model.push.PushStatsInfo;
import com.dajie.push.authenticator.IAuthenticator;
import com.dajie.push.distributed.IDistributedManager;
import com.dajie.push.idgenerator.IMsgIdGenerator;
import com.dajie.push.mq.IMQueueManager;
import com.dajie.push.mqtt.MQTTTopic;
import com.dajie.push.mqtt.MQTTTopicUtils;
import com.dajie.push.mqtt.message.*;
import com.dajie.push.netty.channel.ConnectionStats;
import com.dajie.push.netty.channel.IChannelManager;
import com.dajie.push.netty.channel.NettyChannel;
import com.dajie.push.netty.client.NettyClient;
import com.dajie.push.netty.filter.IFilter;
import com.dajie.push.netty.pushpool.IPushPoolManager;
import com.dajie.push.netty.pushpool.PushEvent;
import com.dajie.push.netty.stats.IStatsManager;
import com.dajie.push.netty.stats.PushStats;
import com.dajie.push.storage.IStorageManager;
import com.dajie.push.storage.constant.PushStatus;
import com.dajie.push.utils.ClientUAUtil;
import com.dajie.push.utils.EmojiFilterUtil;
import com.dajie.push.utils.IpAddressUtil;
import com.dajie.push.utils.JsonUtil;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.dajie.push.mqtt.message.ConnAckMessage.*;

/**
 * Created by wills on 3/13/14.
 */
public class DefaultMqttMessageHandler implements IMqttMessageHandler {

    private static final Logger LOGGER= LoggerFactory.getLogger(DefaultMqttMessageHandler.class);

    private static final String THISNODEADDRESS=IpAddressUtil.getThisNodeAddress();

    private IChannelManager channelManager;

    private IStorageManager storageManager;

    private IMsgIdGenerator msgIdGenerator;

    private IPushPoolManager pushPoolManager;

    private IAuthenticator authenticator;

    private IDistributedManager distributedManager;

    private IFilter publishFilter;

    private IMQueueManager mQueueManager;

    private IStatsManager statsManager;

    private Map<String,NettyClient> otherNodeMap=new ConcurrentHashMap<String, NettyClient>();

    private Map<String,List<Long>> subMergeMap= new ConcurrentHashMap<String,List<Long>>();

    public DefaultMqttMessageHandler(IAuthenticator authenticator,IChannelManager channelManager,IDistributedManager distributedManager,IFilter publishFilter,IMQueueManager mQueueManager, IMsgIdGenerator msgIdGenerator, IPushPoolManager pushPoolManager,IStatsManager statsManager, IStorageManager storageManager) {
        this.channelManager = channelManager;
        this.storageManager = storageManager;
        this.msgIdGenerator = msgIdGenerator;
        this.pushPoolManager = pushPoolManager;
        this.authenticator = authenticator;
        this.distributedManager=distributedManager;
        this.mQueueManager=mQueueManager;
        this.statsManager=statsManager;
        this.publishFilter=publishFilter;
    }

    @Override
    public void handleMqttMessage(MessageEvent event) {
        AbstractMessage message=event.getAbstractMessage();
        switch(message.getMessageType()){
            case CONNECT:
                connectEventProcessor(event);
                break;
            case SUBSCRIBE:
                subscribeEventProcessor(event);
                break;
            case PUBLISH:
                publishEventProcessor(event);
                break;
            case DISCONNECT:
                disconnectEventProcessor(event);
                break;
            case PUBACK:
                pubackEventProcessor(event);
                break;
            case UNSUBSCRIBE:
                unsubscribeEventProcessor(event);
                break;
            case PINGREQ:
                pingreqEventProcessor(event);
                break;
            case COMMAND:
                commandEventProcessor(event);
                break;
        }
    }

    private void commandEventProcessor(MessageEvent event){
        CommandMessage message=(CommandMessage)event.getAbstractMessage();
        String command=message.getCommand();
        byte[] replyCommand=handleCommand(command).getBytes();
        PublishMessage publishMessage=new PublishMessage();
        publishMessage.setTopicName("command");
        publishMessage.setQos(QOSType.MOST_ONE);
        ByteBuffer byteBuffer=ByteBuffer.allocate(replyCommand.length);
        byteBuffer.put(replyCommand);
        byteBuffer.flip();
        publishMessage.setPayload(byteBuffer);
        event.getChannel().writeAndFlush(publishMessage);
    }

    private String handleCommand(String command){
        return command;
    }

    private boolean checkDistributeValid(String clientId){
        if(distributedManager!=null){
            String shouldNode=distributedManager.getPrivateIpServer(clientId);
            return shouldNode.contains(THISNODEADDRESS)?true:false;
        }else{
            return true;
        }
    }

    private void pingreqEventProcessor(MessageEvent event){
        String clientId=channelManager.getChannel(event.getChannel()).getClientId();
        LOGGER.debug("ping ping ping: "+clientId);
        if(checkDistributeValid(clientId)){
            PingRespMessage pingRespMessage=new PingRespMessage();
            event.getChannel().writeAndFlush(pingRespMessage);
        }else{
            LOGGER.info("distributed check fail, close the channel:" + clientId);
            event.getChannel().close();
        }

    }
    private void connectEventProcessor(MessageEvent event){
        ConnectMessage message=(ConnectMessage)event.getAbstractMessage();
        ConnAckMessage connAckMessage=new ConnAckMessage();
        //check procotol version
        if(message.getProcotolVersion()!=0x03){
            connAckMessage.setReturnCode(UNACEPTABLE_PROTOCOL_VERSION);
        }
        //check clientId
        if(message.getClientID()==null||message.getClientID().length()>23||ClientUAUtil.checkUA(message.getClientID())== ClientUAUtil.ClientUA.UNKNOWN){
            connAckMessage.setReturnCode(IDENTIFIER_REJECTED);
        }
        if(!checkDistributeValid(message.getClientID())){
            connAckMessage.setReturnCode(SERVER_UNAVAILABLE);
        }
        //check username and password
        if(!authenticator.checkConnValid(message.getUsername(),message.getPassword())){
            connAckMessage.setReturnCode(BAD_USERNAME_OR_PASSWORD);
        }

        //send connack
        Channel channel=event.getChannel();
        channel.writeAndFlush(connAckMessage);

        if(connAckMessage.getReturnCode()!=CONNECTION_ACCEPTED){
            //unnormal connection, close it
            LOGGER.info("[channel] connection refuse, returncode:"+connAckMessage.getReturnCode()+" clientId:"+message.getClientID());
            channel.close();
        }else{
            //normal connection, add it to channelManager
            String clientId=message.getClientID();
            LOGGER.info("[channel] connection succ,clientId:"+clientId);
            NettyChannel nettyChannel=new NettyChannel(channel,clientId,message.getUsername(),message.getPassword());
            channelManager.addChannel(nettyChannel);
        }
    }

    private void unsubscribeEventProcessor(MessageEvent event){
        UnsubscribeMessage message=(UnsubscribeMessage)event.getAbstractMessage();
        NettyChannel nettyChannel=channelManager.getChannel(event.getChannel());

        String topic=message.topics().get(0);
        MQTTTopic mqttTopic= MQTTTopicUtils.parseTopic(topic);
        String clientId=nettyChannel.getClientId();
        String appId=mqttTopic.getAppId();
        String userId=mqttTopic.getUserId();
        LOGGER.debug("unsubscribe, delete clientinfo, clientId:"+clientId+" appId:"+appId);

        storageManager.deleteClientInfo(clientId,appId,userId);

        UnsubAckMessage unsubAckMessage=new UnsubAckMessage();
        unsubAckMessage.setMessageID(message.getMessageID());
        event.getChannel().writeAndFlush(unsubAckMessage);

    }
    private void subscribeEventProcessor(MessageEvent event){
        SubscribeMessage message=(SubscribeMessage)event.getAbstractMessage();
        NettyChannel nettyChannel=channelManager.getChannel(event.getChannel());

        //GET MQTT SUBSCRIBE TOPIC
        String topic=message.subscriptions().get(0).getTopic();

        //STRING TOPIC --> MQTTTOPIC (appId,userId)
        MQTTTopic mqttTopic= MQTTTopicUtils.parseTopic(topic);

        //check appId valid
        if(!authenticator.checkAppIdValid(mqttTopic.getAppId(),nettyChannel.getApiKey(),nettyChannel.getSecretKey())){
            LOGGER.warn("subscribe, appId invalid,close channel,"+mqttTopic.getAppId());
            event.getChannel().close();
            return ;
        }

        //update clientInfo
        ClientInfo clientInfo=new ClientInfo();
        clientInfo.setClientId(nettyChannel.getClientId());
        clientInfo.setAppId(mqttTopic.getAppId());
        clientInfo.setUserId(mqttTopic.getUserId());
        storageManager.updateClientInfo(clientInfo);

        //update channelInfo
        channelManager.bindUserIdToChannel(nettyChannel.getClientId(),mqttTopic.getUserId());

        //send suback
        SubAckMessage subAckMessage=new SubAckMessage();
        subAckMessage.addType(QOSType.LEAST_ONE);
        subAckMessage.setMessageID(message.getMessageID());
        event.getChannel().writeAndFlush(subAckMessage);

        //triger push
        List<PushInfo> pushInfos=null;
        if(mqttTopic.getUserId()==null){
            //pushInfos= storageManager.getPushInfoByClientId(mqttTopic.getAppId(),nettyChannel.getClientId());
            //if userId==null, return
        }else{
            pushInfos=storageManager.getPushInfosByUserId(mqttTopic.getAppId(),mqttTopic.getUserId());
        }
        //
        LOGGER.debug("[subscribe] triger push,userId:"+clientInfo.getUserId());
        pushPoolManager.addPushEvent(convertPushInfoToPushevent(pushInfos));

    }

    //被动push
    private PushEvent convertPushInfoToPushevent(List<PushInfo> pushInfos){
        if(pushInfos==null||pushInfos.size()==0){
            return null;
        }

        PushInfo pushInfo=pushInfos.get(0);
        //批量，进行消息合并
        PushEvent pushEvent=new PushEvent();
        //取最新绑定的clientId
        //String clientId=pushInfo.getClientId();
        // if(clientId==null){
        String clientId=storageManager.getClientId(pushInfo.getAppId(),pushInfo.getUserId());
        //}
        pushEvent.setClientId(clientId);
        pushEvent.setMsgId(pushInfo.getMsgId());
        pushEvent.setTopic(MQTTTopicUtils.genTopic(pushInfo.getAppId(),pushInfo.getUserId()));

        NettyChannel nettyChannel=channelManager.getChannel(clientId);
        if(nettyChannel!=null){
            pushEvent.setChannel(nettyChannel.getChannel());
        }else{
            //not online,don't push
            LOGGER.debug("[publish] "+pushInfo.getUserId()+" is not online,do not push");
            return null;
        }

        String payload="";
        if(pushInfos.size()>1){
            List<Long> ids=new ArrayList<Long>();
            StringBuffer sbPayload=new StringBuffer("[");
            for(PushInfo tempPushInfo:pushInfos){
                sbPayload.append(tempPushInfo.getPayload()).append(",");
                ids.add(tempPushInfo.getId());
            }
            subMergeMap.put(pushEvent.getPushKey(),ids);
            int lastIndex=sbPayload.length()-1;
            payload= sbPayload.replace(lastIndex,lastIndex+1,"]").toString() ;
        }else{
            payload=pushInfo.getPayload();
        }

        pushEvent.setPayload(payload);

        return pushEvent;

    }

    private Channel getDistributedChannel(String clientId){
        String privateIpServer=distributedManager.getPrivateIpServer(clientId);
        if(!otherNodeMap.containsKey(privateIpServer)){
            String host=privateIpServer.split(":")[0];
            int port=Integer.valueOf(privateIpServer.split(":")[1]);
            NettyClient client=new NettyClient(host,port,this.pushPoolManager);
            LOGGER.info("[client] connect with other node,"+privateIpServer);
            client.connect();
            otherNodeMap.put(privateIpServer,client);
        }
        NettyClient client=otherNodeMap.get(privateIpServer);
        if(client.getChannel()==null||!client.getChannel().isOpen()){
            LOGGER.info("[client] reconnection with other node,"+privateIpServer);
            client.connect();
        }
        return client.getChannel();
    }
    //主动push
    private PushEvent convertPushInfoToPushevent(PushInfo pushInfo){
        if(pushInfo==null){
            return null;
        }
        PushEvent pushEvent=new PushEvent();
        String clientId=storageManager.getClientId(pushInfo.getAppId(),pushInfo.getUserId());
        if(StringUtils.isEmpty(clientId)){
            return null;
        }
        pushEvent.setClientId(clientId);
        pushEvent.setMsgId(pushInfo.getMsgId());
        pushEvent.setPayload(pushInfo.getPayload());
        pushEvent.setTopic(MQTTTopicUtils.genTopic(pushInfo.getAppId(),pushInfo.getUserId()));

        //pick channel
        Channel channel=null;
        if(checkDistributeValid(clientId)){
            LOGGER.debug("[publish] publish to mobile,uId:"+pushInfo.getUserId()+" clientId:"+clientId);
            NettyChannel nettyChannel=channelManager.getChannel(clientId);
            if(nettyChannel!=null){
                channel=nettyChannel.getChannel();
            }else{
                //not online,don't push
                LOGGER.debug("[publish] "+pushInfo.getUserId()+" is not online,do not push");
                return null;
            }
        }else{
            channel=getDistributedChannel(clientId);
            LOGGER.debug("[publish] dispatch to other node,userId:"+ pushInfo.getUserId()+" clientId:"+clientId);
            //add flag int this topic
            pushEvent.setTopic(pushEvent.getTopic()+"/end");
            //modify clientId
            pushEvent.setClientId(THISNODEADDRESS);
        }
        pushEvent.setChannel(channel);
        return pushEvent;
    }


    private void publishEventProcessor(MessageEvent event){
        PublishMessage message=(PublishMessage)event.getAbstractMessage();
        String topic=message.getTopicName();
        MQTTTopic mqttTopic=MQTTTopicUtils.parseTopic(topic);

        NettyChannel channel=channelManager.getChannel(event.getChannel());
        String userId=mqttTopic.getUserId();


        if(mqttTopic.getFlag()==null){//未经分布式转发的消息.这里负责存储，统计等
            if(publishFilter!=null&&publishFilter.filter(channel,userId,new String(message.getPayload().array()))){
                boolean isDup=message.isDupFlag();
                //store
                PushInfo pushInfo=storage0(channel.getUserId(),message, isDup);
                //stats
                String sourceClientId=channel.getClientId();
                statsManager.incrPushStatus(sourceClientId,false);
                //dispatch
                //push之前进行转义回来
                pushInfo.setPayload(EmojiFilterUtil.recoverToEmoji(pushInfo.getPayload()));
                publish0(pushInfo);

            }else{
                LOGGER.error("publish filter block the message");
            }
        }else{//经转发的消息，直接发送。
            PushInfo pushInfo=new PushInfo();
            pushInfo.setUserId(mqttTopic.getUserId());
            pushInfo.setAppId(mqttTopic.getAppId());
            pushInfo.setPayload(new String(message.getPayload().array()));
            pushInfo.setMsgId(message.getMessageID().shortValue());
            publish0(pushInfo);
        }

        //send puback
        PubAckMessage pubAckMessage=new PubAckMessage();
        pubAckMessage.setMessageID(message.getMessageID());
        event.getChannel().writeAndFlush(pubAckMessage);

        //从手机端的角度看，分两种情况。上行和下行。
        //上行，是指publish来自手机，需要回调相应的服务回调程序。
        //下行，表示要直接publish到手机端。
//        boolean down=message.isRetainFlag();
//        if(down){
//            //回调，扔到mq
//            if(mQueueManager!=null){
//                mQueueManager.put(mqttTopic.getAppId(),message.getPayload().array());
//            }
//        }

    }

    private PushInfo storage0(String fromUserId,PublishMessage message,boolean isDup){
        try{
            PushInfo pushInfo=new PushInfo();
            MQTTTopic mqttTopic=MQTTTopicUtils.parseTopic(message.getTopicName());
            String userId=mqttTopic.getUserId();
            Map<String ,Object> payLoadMap= JsonUtil.jsonToMap(new String(message.getPayload().array()));
            long mobileTime=(Long)payLoadMap.get("time");

            if(isDup&&storageManager.pushInfoIsExisted(userId,mobileTime)){
                 return null;
            }

            long serverTime=System.currentTimeMillis();
            payLoadMap.put("time",serverTime);

            if(userId!=null){
                //String clientId=storageManager.getClientId(mqttTopic.getAppId(),mqttTopic.getUserId());
                //if clientId==null, it means the userId has not been bind,just set it to NULL
                pushInfo.setUserId(mqttTopic.getUserId());
                pushInfo.setAppId(mqttTopic.getAppId());
                pushInfo.setPayload(JsonUtil.toJson(payLoadMap));
                //pushInfo.setClientId(clientId);
                pushInfo.setUpMsgId(mobileTime);
                pushInfo.setFromUserId(fromUserId);
                pushInfo.setMsgId(msgIdGenerator.getMsgId());
            }else{
                LOGGER.warn("publish message, userId is null,discard");
                return null;
            }
            if(pushInfo!=null){
                pushInfo=storageManager.addPushInfo(pushInfo);
            }
            return pushInfo;
        }catch(Exception e){
            return null;
        }
    }


    private void publish0(PushInfo pushInfo){
        PushEvent event=convertPushInfoToPushevent(pushInfo);
        if(event!=null){
            pushPoolManager.addPushEvent(event);
        }
    }



    private void pubackEventProcessor(MessageEvent event){
        PubAckMessage pubAckMessage=(PubAckMessage)event.getAbstractMessage();
        int msgId=pubAckMessage.getMessageID();
        NettyChannel nettyChannel=channelManager.getChannel(event.getChannel());
        if(nettyChannel!=null){
            String clientId=nettyChannel.getClientId();
            String userId=nettyChannel.getUserId();
            String key=clientId+msgId;
            if(subMergeMap.containsKey(key)){
                LOGGER.debug("submerge-push, update message status by id");
                storageManager.updatePushInfoStatusById(subMergeMap.get(key),PushStatus.DELIVERED);
                subMergeMap.remove(key);
            }else{
                //update push status in DB.
                storageManager.updatePushInfoStatus(userId, msgId, PushStatus.DELIVERED);

            }
            //clean push info in pushpool.
            pushPoolManager.removePushEvent(key);
            LOGGER.debug("[puback] key:" + key);
            statsManager.incrPushStatus(clientId, true);
        }

    }

    private void disconnectEventProcessor(MessageEvent event){
        channelManager.removeChannel(event.getChannel());
    }

    @Override
    public PoolStats getPoolStats() {
        int pushpool= pushPoolManager.getPushPoolSize();
        int ringbuf=0;
        return new PoolStats(ringbuf,pushpool);
    }

    @Override
    public void removeChannel(Channel channel) {
        this.channelManager.removeChannel(channel);
    }

    @Override
    public PushStats getPushStats() {
        return statsManager.getPushStats();
    }

    @Override
    public ConnectionStats getConnStats() {
        return channelManager.getConnStats();
    }

    @Override
    public int flushPushStatsToDB() {
        PushStats pushStats=getPushStats();
        ConnectionStats connectionStats=getConnStats();
        PushStatsInfo pushStatsInfo=new PushStatsInfo(THISNODEADDRESS);
        pushStatsInfo.setAndroidConn(connectionStats.getAndroid());
        pushStatsInfo.setIosConn(connectionStats.getIos());
        pushStatsInfo.setServerConn(connectionStats.getServer());

        pushStatsInfo.setFromAndroid(pushStats.getFromAndroid());
        pushStatsInfo.setFromIos(pushStats.getFromIos());
        pushStatsInfo.setFromServer(pushStats.getFromServer());

        pushStatsInfo.setToAndroid(pushStats.getToAndroid());
        pushStatsInfo.setToIos(pushStats.getToIos());
        pushStatsInfo.setToServer(pushStats.getToServer());

        return storageManager.insertPushStats(pushStatsInfo);
    }
}
