package com.dajie.push;

import com.dajie.push.netty.NettyServer;
import com.dajie.push.netty.NettyServerBuilder;
import com.dajie.push.utils.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wills on 3/12/14.
 */
public class Broker {

    private static final Logger LOGGER= LoggerFactory.getLogger(Broker.class);

    public static void main(String[] args) {

        LOGGER.info("Broker start");

        Configuration configuration=Configuration.getInstance();
        boolean isAsyn= configuration.getBoolean("asyn");
        boolean isDistributed=configuration.getBoolean("distributed");
        boolean isCallback=configuration.getBoolean("callback");
        int port=configuration.getInt("port");
        
        NettyServerBuilder builder=new NettyServerBuilder().isAsyn(isAsyn)
                .port(port).isDistributed(isDistributed).isCallback(isCallback);

        final NettyServer nettyServer=builder.getNettyServer();
        nettyServer.start();

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
                nettyServer.stop();
            }
        });
    }
}
