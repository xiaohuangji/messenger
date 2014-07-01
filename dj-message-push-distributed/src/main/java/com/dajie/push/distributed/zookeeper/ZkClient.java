package com.dajie.push.distributed.zookeeper;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by wills on 3/19/14.
 */
public class ZkClient implements IZkClient{

    private static final Logger LOGGER= LoggerFactory.getLogger(ZkClient.class);

    private String host;

    private ZooKeeper zooKeeper;

    private String path="/";

    private CountDownLatch connectionCountDown;

    private List<String> data;

    private IZkCallback zkCallback;

    private static final int SESSION_TIMEOUT=4000;

    public ZkClient(String host, String path,IZkCallback zkCallback) {
        this.host = host;
        this.path = path;
        this.zkCallback=zkCallback;
        init();
    }

    private void init(){
        synchronized (ZkClient.class){
            try{
                connectionCountDown=new CountDownLatch(1);
                zooKeeper=new ZooKeeper(host,SESSION_TIMEOUT,new ConnectionWatch());
                //LOGGER.info("[zookeeper] zkclient start to connect");
                connectionCountDown.await();
                //LOGGER.info("[zookeeper] zkclient connect succ");
                watchPath();
            }catch(Exception e){
                LOGGER.error("[zookeeper] zkclient connection fail:",e);
            }
        }
    }

    private void watchPath(){
        try{
            data=zooKeeper.getChildren(path, new NodeWatcher());
            LOGGER.info("[zookeeper] data has changed,"+Arrays.toString(data.toArray()));
            zkCallback.notifyChange(data);
        }catch(Exception e){
            LOGGER.error("[zookeeper] zkclient getdata fail:",e);
        }
    }

    @Override
    public List getData(){
        return data;
    }

    @Override
    public List createNode(String nodename){
        try{
            zooKeeper.create(path+"/"+nodename,new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            LOGGER.info("[zookeeper] create node succ,nodename:"+nodename);
        }catch(Exception e){
            LOGGER.error("[zookeeper] create node error,",e);
        }
        return data;
    }


    class NodeWatcher implements Watcher{

        @Override
        public void process(WatchedEvent watchedEvent) {
            if(watchedEvent.getType()== Event.EventType.NodeChildrenChanged){
                //LOGGER.debug("[zookeeper] NodeChildrenChanged,reregister watcher");
                watchPath();
            }else{
                //LOGGER.debug("[zookeeper] NodewatchedEvent happen:"+watchedEvent.getType());
                //watchPath();
            }
        }
    }

    class ConnectionWatch implements Watcher{

        @Override
        public void process(WatchedEvent watchedEvent) {
            if(watchedEvent.getState()== Event.KeeperState.SyncConnected){
                LOGGER.info("[zookeeper] zkclient session connect succ,sessionId:"+zooKeeper.getSessionId());
                connectionCountDown.countDown();
            }else if(watchedEvent.getState()== Event.KeeperState.Disconnected){
//                try{
                    LOGGER.warn("[zookeeper] zkclient session disconncet");
//                    zooKeeper.close();
//                    init();
//                }catch(Exception e){
//                    LOGGER.warn("[zookeeper] zookeeper close fail,",e);
//                }
            }else if(watchedEvent.getState()== Event.KeeperState.Expired){
                try{
                    LOGGER.warn("[zookeeper] zkclient session expire,reconnect");
                    zooKeeper.close();
                    init();
                }catch(Exception e){
                    LOGGER.warn("[zookeeper] zookeeper close fail,",e);
                }
            }
        };
    }
}
