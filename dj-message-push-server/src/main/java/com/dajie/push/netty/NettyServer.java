package com.dajie.push.netty;

import com.dajie.push.netty.channel.IChannelManager;
import com.dajie.push.netty.handler.ChildChannelInitializer;
import com.dajie.push.netty.handler.IMqttMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by wills on 3/12/14.
 */
final public class NettyServer {

    private static final Logger LOGGER= LoggerFactory.getLogger(NettyServer.class);

    private EventLoopGroup parentEventLoop;

    private EventLoopGroup childEventLoop;

    private ServerBootstrap serverBootstrap;

    private int port;

    public NettyServer(int port,IMqttMessageHandler mqttMessageHandler,IChannelManager channelManager) {
        this.port = port;
        parentEventLoop=new NioEventLoopGroup();
        childEventLoop=new NioEventLoopGroup();
        serverBootstrap=new ServerBootstrap();

        this.mqttMessageHandler=mqttMessageHandler;
        this.channelManager=channelManager;

    }

    private IMqttMessageHandler mqttMessageHandler;

    private IChannelManager channelManager;

    public void start(){
        serverBootstrap.group(parentEventLoop,childEventLoop)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChildChannelInitializer(mqttMessageHandler,channelManager))
                .option(ChannelOption.SO_BACKLOG,128)
                .option(ChannelOption.SO_REUSEADDR,true)
                .childOption(ChannelOption.SO_KEEPALIVE,true);

        try{
             ChannelFuture future=serverBootstrap.bind().sync();
             future.channel().closeFuture().sync();
        }
        catch(InterruptedException ex){
            LOGGER.error("mqtt start fail:",ex);
        }finally {
            parentEventLoop.shutdownGracefully();
            childEventLoop.shutdownGracefully();
        }
    }

    public void stop(){
        if(parentEventLoop!=null){
            parentEventLoop.shutdownGracefully();
        }
        if(childEventLoop!=null){
            childEventLoop.shutdownGracefully();
        }
    }
}
