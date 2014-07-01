package com.dajie.push.netty.handler;

import com.dajie.push.mqtt.decoder.MQTTDecoderHandler;
import com.dajie.push.mqtt.encoder.MQTTEncoderHandler;
import com.dajie.push.netty.channel.IChannelManager;
import com.dajie.push.utils.Configuration;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;


/**
 * Created by wills on 3/12/14.
 */
public class ChildChannelInitializer extends ChannelInitializer<SocketChannel> {

    private IMqttMessageHandler mqttMessageHandler;

    private IChannelManager channelManager;

    public ChildChannelInitializer(IMqttMessageHandler mqttMessageHandler, IChannelManager channelManager) {
        this.mqttMessageHandler = mqttMessageHandler;
        this.channelManager = channelManager;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline=socketChannel.pipeline();

        Integer idleTimeout=Configuration.getInstance().getInt("idle_timeout");
        //timeout handler
        pipeline.addFirst("idlehandler",new IdleStateHandler(idleTimeout,0,0));
        pipeline.addAfter("idlehandler","timeouthandler",new TimeoutHandler(channelManager));

        //codec handler
        pipeline.addLast("mqttdecoder",new MQTTDecoderHandler());
        pipeline.addLast("mqttencoder",new MQTTEncoderHandler());
        //
        //pipeline.addLast("stringdecode",new StringDecoder());
        //logic handler
        pipeline.addLast("mqtthandle",new MessageHandler(mqttMessageHandler));
    }
}
