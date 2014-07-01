package com.dajie.push.netty.client;

import com.dajie.push.mqtt.decoder.MQTTDecoderHandler;
import com.dajie.push.mqtt.encoder.MQTTEncoderHandler;
import com.dajie.push.mqtt.message.PubAckMessage;
import com.dajie.push.netty.pushpool.IPushPoolManager;
import com.dajie.push.utils.IpAddressUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wills on 4/3/14.
 */
public class NettyClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final Logger LOGGER= LoggerFactory.getLogger(NettyClientChannelInitializer.class);

    private static final String THISNODEADDRESS= IpAddressUtil.getThisNodeAddress();

    private IPushPoolManager pushPoolManager;

    public NettyClientChannelInitializer(IPushPoolManager pushPoolManager) {
        this.pushPoolManager = pushPoolManager;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("innermqttencoder", new MQTTEncoderHandler());
        ch.pipeline().addLast("innermqttdecoder",new MQTTDecoderHandler());
        ch.pipeline().addLast("innerhandler",new SimpleChannelInboundHandler() {
            @Override
            protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
                if(msg instanceof PubAckMessage){
                    PubAckMessage pubAckMessage=(PubAckMessage)msg;
                    int msgId=pubAckMessage.getMessageID();
                    //clean push info in pushpool.
                    String key=THISNODEADDRESS+msgId;
                    LOGGER.debug("[puback] from other node, key:"+key);
                    pushPoolManager.removePushEvent(key);

                }else{
                    LOGGER.warn("[puback] nettyclient receive unusual message");
                }
            }
        });
    }
}
