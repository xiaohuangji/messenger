package com.dajie.push.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by wills on 3/20/14.
 */
public class IpAddressUtil {

    private static final Logger LOGGER=LoggerFactory.getLogger(IpAddressUtil.class);

    public static String getPrivateIp(){
        try{
            for (Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements();) {
                NetworkInterface item = e.nextElement();
                for (InterfaceAddress address : item.getInterfaceAddresses()) {
                    if (address.getAddress() instanceof Inet4Address) {
                        Inet4Address inet4Address = (Inet4Address) address.getAddress();
                        if(inet4Address.isLoopbackAddress()){
                            continue;
                        }
                        return inet4Address.getHostAddress();

                    }
                }
            }
        }catch(Exception e){
            LOGGER.warn("get private ip address fail,",e);
        }
        return null;
    }
}
