package com.dajie.push.distributed.util;

/**
 * Created by wills on 4/3/14.
 */
public class RegisterNodeInfo{

    public String privateIp;

    public String publicIp;

    public int port;

    public RegisterNodeInfo(String privateIp, String publicIp, int port) {
        this.privateIp = privateIp;
        this.publicIp = publicIp;
        this.port = port;
    }
}
