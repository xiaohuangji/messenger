package com.dajie.push.distributed.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wills on 4/3/14.
 */
public class ZkPathUtil {

    private static final Logger LOGGER= LoggerFactory.getLogger(ZkPathUtil.class);

    public static String genRisteryNodeInfo(String privateIp,String publicIp,int port){
        return new StringBuffer().append(privateIp).append(":").append(port).append("##").append(publicIp)
        .append(":").append(port).toString();
    }

    public static RegisterNodeInfo parseRisteryNodeInfo(String infoStr){
        try{
            String[] hosts=infoStr.split("##");
            String[] privateHost=hosts[0].split(":");
            String[] publicHost=hosts[1].split(":");
            return new RegisterNodeInfo(privateHost[0],publicHost[0],Integer.valueOf(privateHost[1]));
        }catch(Exception e){
            LOGGER.error("zkPath nodeinfo parse error",e);
            return null;
        }
    }

    public static String getPrivateHost(String infoStr){
        try{
            return infoStr.split("##")[0];
        }catch(Exception e){
            LOGGER.error("zkPath nodeinfo parse error",e);
            return null;
        }
    }

    public static String getPublicHost(String infoStr){
        try{
            return infoStr.split("##")[1];
        }catch(Exception e){
            LOGGER.error("zkPath nodeinfo parse error",e);
            return null;
        }
    }
}



