package com.dajie.push.distributed;

/**
 * Created by wills on 3/19/14.
 */
public interface IDistributedManager {


    /**
     * get push server with private ip
     * @param clientId
     * @return
     */
    public String getPrivateIpServer(String clientId);

    /**
     * get push server with public ip
     * @param clientId
     * @return
     */
    public String getPublicIpServer(String clientId);

    /**
     * register info to distributed system
     * @param privateIp publicIp,port
     */
    public void register(String privateIp,String publicIp,int port);
}
