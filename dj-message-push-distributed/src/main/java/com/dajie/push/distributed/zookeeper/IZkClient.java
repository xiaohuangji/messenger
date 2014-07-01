package com.dajie.push.distributed.zookeeper;

import java.util.List;

/**
 * Created by wills on 3/20/14.
 * simple zkclient just for this system
 */
public interface IZkClient {

    /**
     * get data
     * @return
     */
    public List getData();


    /**
     * create EPHEMERAL node
     * @param nodename
     * @return
     */
    public List createNode(String nodename);
}
