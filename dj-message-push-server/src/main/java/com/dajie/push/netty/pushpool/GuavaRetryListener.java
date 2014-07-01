package com.dajie.push.netty.pushpool;

import com.dajie.push.utils.Configuration;
import com.dajie.push.netty.channel.IChannelManager;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wills on 3/14/14.
 */

public class GuavaRetryListener implements RemovalListener<String,PushEvent> {

    private static final Logger LOGGER= LoggerFactory.getLogger(GuavaRetryListener.class);

    private IPushPoolManager pushPoolManager;

    private IChannelManager channelManager;

    private static final int RETRYTIME=Configuration.getInstance().getInt("push_retry_time");

    public GuavaRetryListener(IPushPoolManager pushPoolManager,IChannelManager channelManager) {
        this.pushPoolManager=pushPoolManager;
        this.channelManager=channelManager;
    }

    @Override
    public void onRemoval(RemovalNotification<String, PushEvent> notification) {
        if(notification.wasEvicted()){
            PushEvent pushEvent=notification.getValue();
            if(pushEvent.getAndIncrRetryTime()<RETRYTIME){
                LOGGER.debug("[pushpool] pushevent retry, retrytime:"+pushEvent.getRetryTime()+" , key:"+pushEvent.getPushKey());
                pushPoolManager.addPushEvent(pushEvent);
            }else{
                LOGGER.debug("[pushpool] pushevent finish retry, remove pushevent and close channel, key:"+pushEvent.getPushKey());
                //close the channel and remove it from channelmanager
                channelManager.removeChannel(pushEvent.getChannel());
                pushEvent.getChannel().close();
            }
        }
    }


}