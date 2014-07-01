package com.dajie.push.utils;

import com.dajie.push.netty.channel.NettyChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wills on 5/9/14.
 */
public class StatisticLogger {

    private static final Logger LOGGER= LoggerFactory.getLogger(StatisticLogger.class);

    private static final String SPLI="||";

    public static void  printChatLogger(NettyChannel channel,String toUserId,int msgType){
        StringBuffer sb=new StringBuffer();
        sb.append(channel.getUserId()).append(SPLI).append(toUserId).append(SPLI).append(msgType)
                .append(SPLI).append(ClientUAUtil.checkUA(channel.getClientId()).ordinal())
                .append(SPLI).append(System.currentTimeMillis());

        LOGGER.info(sb.toString());
    }
}
