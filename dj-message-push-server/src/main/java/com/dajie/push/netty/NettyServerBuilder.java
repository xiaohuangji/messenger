package com.dajie.push.netty;

import com.dajie.message.model.push.PushStatsInfo;
import com.dajie.push.authenticator.DefaultAuthenticator;
import com.dajie.push.authenticator.IAuthenticator;
import com.dajie.push.distributed.IDistributedManager;
import com.dajie.push.distributed.ZKDistributedManager;
import com.dajie.push.idgenerator.IMsgIdGenerator;
import com.dajie.push.idgenerator.RandomMsgIdGenerator;
import com.dajie.push.mq.IMQueueManager;
import com.dajie.push.mq.RabbitMQManager;
import com.dajie.push.netty.channel.DefaultChannelManager;
import com.dajie.push.netty.channel.IChannelManager;
import com.dajie.push.netty.filter.IFilter;
import com.dajie.push.netty.filter.PublishFilter;
import com.dajie.push.netty.handler.DefaultMqttMessageHandler;
import com.dajie.push.netty.handler.DisruptorMqttMessageHandler;
import com.dajie.push.netty.handler.IMqttMessageHandler;
import com.dajie.push.netty.pushpool.GuavaPushPoolManager;
import com.dajie.push.netty.pushpool.IPushPoolManager;
import com.dajie.push.netty.stats.DefaultStatsManager;
import com.dajie.push.netty.stats.IStatsManager;
import com.dajie.push.spring.SpringBeanFactory;
import com.dajie.push.storage.DBStorageManager;
import com.dajie.push.storage.IStorageManager;
import com.dajie.push.utils.Configuration;
import com.dajie.push.utils.IpAddressUtil;

/**
 * Created by wills on 3/17/14.
 */
final public class NettyServerBuilder {

    private IChannelManager channelManager;

    private IAuthenticator authenticator;

    private IMsgIdGenerator msgIdGenerator;

    private IPushPoolManager pushPoolManager;

    private IMqttMessageHandler mqttMessageHandler;

    private IMqttMessageHandler asynQqttMessageHandler;

    private IMqttMessageHandler synQqttMessageHandler;

    private IStorageManager storageManager;

    private IDistributedManager distributedManager;

    private IMQueueManager mQueueManager;

    private IStatsManager statsManager;

    private IFilter publishFilter;

    private boolean asyn=false;

    private boolean distributed=false;

    private boolean callback=false;

    private int port=1883;

    private static NettyServer nettyServer;

    public NettyServerBuilder() {

    }

    public NettyServerBuilder port(int port){
        this.port=port;
        return this;
    }

    public NettyServerBuilder isAsyn(boolean asyn){
        this.asyn=asyn;
        return this;
    }

    public NettyServerBuilder isDistributed(boolean distributed){
        this.distributed=distributed;
        return this;
    }

    public NettyServerBuilder isCallback(boolean callback){
        this.callback=callback;
        return this;
    }

    public NettyServerBuilder channelManager(IChannelManager channelManager){
        this.channelManager=channelManager;
        return this;
    }
    public NettyServerBuilder storageManager(IStorageManager storageManager){
        this.storageManager=storageManager;
        return this;
    }
    public NettyServerBuilder authenticator(IAuthenticator authenticator){
        this.authenticator=authenticator;
        return this;
    }
    public NettyServerBuilder msgIdGenerator(IMsgIdGenerator msgIdGenerator){
        this.msgIdGenerator=msgIdGenerator;
        return this;
    }
    public NettyServerBuilder pushPoolManager(IPushPoolManager pushPoolManager){
        this.pushPoolManager=pushPoolManager;
        return this;
    }

    public NettyServerBuilder statsManager(IStatsManager statsManager){
        this.statsManager=statsManager;
        return this;
    }

    public NettyServerBuilder publishFilter(IFilter publishFilter){
        this.publishFilter=publishFilter;
        return this;
    }

    public NettyServer getNettyServer(){
        if(nettyServer==null){

            if(channelManager==null){
                channelManager=new DefaultChannelManager();
            }

            if(msgIdGenerator==null){
                msgIdGenerator=new RandomMsgIdGenerator();
            }

            if(storageManager==null){
                storageManager= SpringBeanFactory.getBean(DBStorageManager.class);
            }

            if(authenticator==null){
                authenticator=new DefaultAuthenticator(storageManager);
            }

            if(pushPoolManager==null){
                pushPoolManager=new GuavaPushPoolManager(channelManager);
            }

            if(statsManager==null){
                PushStatsInfo pushStatsInfo=storageManager.getLatestPushStats(IpAddressUtil.getThisNodeAddress());
                statsManager=new DefaultStatsManager(pushStatsInfo);
            }

            if(publishFilter==null){
                publishFilter=new PublishFilter();
            }

            if(distributed==true){
                String zkHost= Configuration.getInstance().getString("zk_host");
                String zkPath=Configuration.getInstance().getString("zk_path");
                String privateIp= IpAddressUtil.getPrivateIp();
                String publicIp=IpAddressUtil.getPublicIp();
                int port=Configuration.getInstance().getInt("port");
                distributedManager=new ZKDistributedManager(zkHost,zkPath);
                distributedManager.register(privateIp,publicIp,port);
            }

            if(callback==true){
                mQueueManager=new RabbitMQManager();
            }
            synQqttMessageHandler =new DefaultMqttMessageHandler(authenticator,channelManager,distributedManager,publishFilter,mQueueManager,msgIdGenerator,pushPoolManager,statsManager,storageManager);

            if(asyn){
                asynQqttMessageHandler=new DisruptorMqttMessageHandler(synQqttMessageHandler);
                mqttMessageHandler=asynQqttMessageHandler;
            }else{
                mqttMessageHandler=synQqttMessageHandler;
            }

            nettyServer=new NettyServer(port,mqttMessageHandler,channelManager);
        }
        return nettyServer;

    }
}
