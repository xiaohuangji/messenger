package com.dajie.push.netty.pushpool;

import com.dajie.push.mqtt.message.AbstractMessage;
import com.dajie.push.mqtt.message.PublishMessage;
import com.dajie.push.netty.channel.IChannelManager;
import com.dajie.push.utils.Configuration;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by wills on 3/14/14.
 */
public class GuavaPushPoolManager implements IPushPoolManager {

    private static final Logger LOGGER= LoggerFactory.getLogger(GuavaPushPoolManager.class);

    private IChannelManager channelManager;

    private LoadingCache<String, PushEvent> cache;

    private static final int RETRYINTERVAL=Configuration.getInstance().getInt("push_retry_interval");

    private static final int CLEANUPINTERVAL=Configuration.getInstance().getInt("push_cleanup_interval");

    public GuavaPushPoolManager(IChannelManager channelManager) {
        this.channelManager = channelManager;

        cache = CacheBuilder.newBuilder()
                .removalListener(new GuavaRetryListener(this,this.channelManager))
                .expireAfterWrite(RETRYINTERVAL, TimeUnit.SECONDS).build(new CacheLoader<String, PushEvent>() {
                    @Override
                    public PushEvent load(String s) throws Exception {
                        return null;
                    }
                });

        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //LOGGER.debug("push pool clean up, size:"+cache.size());
                cache.cleanUp();
            }
        },0,CLEANUPINTERVAL,TimeUnit.SECONDS);
    }

    @Override
    public void addPushEvent(PushEvent pushEvent) {
        if(pushEvent==null){
            return ;
        }
        Channel channel=pushEvent.getChannel();
        if(pushEvent.getClientId()!=null&&channel.isOpen()){
            PublishMessage publishMessage=new PublishMessage();
            publishMessage.setMessageID(pushEvent.getMsgId());

            String payload=pushEvent.getPayload();
            ByteBuffer byteBuffer = ByteBuffer.allocate(payload.getBytes().length);
            byteBuffer.put(payload.getBytes());
            byteBuffer.flip();
            //byteBuffer.wrap(payload);

            publishMessage.setPayload(byteBuffer);
            publishMessage.setTopicName(pushEvent.getTopic());
            publishMessage.setQos(AbstractMessage.QOSType.LEAST_ONE);

            channel.writeAndFlush(publishMessage);
        }
        LOGGER.debug("[pushpool] add pushevent to pool,key:"+pushEvent.getPushKey());
        cache.put(pushEvent.getPushKey(), pushEvent);
    }

    @Override
    public void removePushEvent(String key) {
        LOGGER.debug("[pushpool] remove pushevent from pool, key:"+key);
        cache.invalidate(key);
    }

    @Override
    public int getPushPoolSize() {
        return new Long(cache.size()).intValue();
    }
}
