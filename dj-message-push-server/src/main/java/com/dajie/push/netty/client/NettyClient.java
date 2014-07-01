package com.dajie.push.netty.client;

import com.dajie.push.netty.pushpool.IPushPoolManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * broker supports distributed cluster, so node has to comminucate with each other.
 * it used to connected with other broker node.
 * it bases on netty and has its channel handler.
 * Created by wills on 4/1/14.
 */
public class NettyClient{

    private static final Logger LOGGER= LoggerFactory.getLogger(NettyClient.class);

    private final String host;

    private final int port;

    private Channel channel;

    private Bootstrap b;

    private IPushPoolManager pushPoolManager;

    private EventLoopGroup group = new NioEventLoopGroup();

    public NettyClient(String host, int port,IPushPoolManager pushPoolManager) {
        this.pushPoolManager=pushPoolManager;
        this.host = host;
        this.port = port;
        b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(this.host, this.port))
                .handler(new NettyClientChannelInitializer(this.pushPoolManager));
    }


    public void connect(){
        try {
            ChannelFuture f = b.connect().sync();
            channel=f.channel();
        }catch(Exception e){
            LOGGER.error("connect to server fail,",e);
        }
    }

    public void close() throws InterruptedException{
        group.shutdownGracefully().sync();
    }

    public Channel getChannel(){
        return channel;
    }

}

