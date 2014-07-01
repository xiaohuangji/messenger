package com.dajie.push.netty.handler;

import com.dajie.push.mqtt.message.AbstractMessage;
import com.dajie.push.utils.Configuration;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.dajie.push.mqtt.message.AbstractMessage.*;
/**
 * MqttHandle is Logic handler which handle all kinds of mqtt-message
 * Created by wills on 3/12/14.
 */
public class MessageHandler extends SimpleChannelInboundHandler {

    private static final Logger LOGGER= LoggerFactory.getLogger(MessageHandler.class);

    private static final int MONITORINTERVAL=Configuration.getInstance().getInt("monitor_interval");

    private IMqttMessageHandler mqttMessageHandle;

    private static ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);

    private volatile static boolean isMonitor=false;

    public MessageHandler(IMqttMessageHandler mqttMessageHandle1) {
        this.mqttMessageHandle = mqttMessageHandle1;
        if(!isMonitor){
            isMonitor=true;
            LOGGER.info("start monitor");
            exec.scheduleAtFixedRate(new Runnable() {
                long i=1;
                @Override
                public void run() {
                    LOGGER.info(mqttMessageHandle.getPoolStats().toString());
                    LOGGER.info(mqttMessageHandle.getPushStats().toString());
                    LOGGER.info(mqttMessageHandle.getConnStats().toString());
                    i++;
                    if(i%10==0){
                        //打印十次统计日志，刷一次到DB
                        mqttMessageHandle.flushPushStatsToDB();
                    }
                }
            },0,MONITORINTERVAL, TimeUnit.SECONDS);
        }

    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof AbstractMessage){
            //handle mqtt message
            AbstractMessage message=(AbstractMessage)msg;
            LOGGER.debug("receive message,type: "+message.getMessageType());
            switch(message.getMessageType()){
                case CONNECT :
                case SUBSCRIBE :
                case PUBLISH :
                case DISCONNECT :
                case PUBACK:
                case UNSUBSCRIBE:
                case PINGREQ:
                case COMMAND:
                    mqttMessageHandle.handleMqttMessage(new MessageEvent(ctx.channel(),message));
                    break;
                default:
                    LOGGER.warn("unhandled mqttmessage type");
                    break;
            }
        }else{
            //receive not mqtt-message, close the channel
            LOGGER.warn("[channel] unusual mqtt-messge, close the channel");
            mqttMessageHandle.removeChannel(ctx.channel());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        LOGGER.info("[channel] channel in active, close the channel");
        mqttMessageHandle.removeChannel(ctx.channel());
    }

}
