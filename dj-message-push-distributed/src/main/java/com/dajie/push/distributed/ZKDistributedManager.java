package com.dajie.push.distributed;

import com.dajie.push.distributed.hash.ConsistentHash;
import com.dajie.push.distributed.hash.IHashFunction;
import com.dajie.push.distributed.util.ZkPathUtil;
import com.dajie.push.distributed.zookeeper.IZkCallback;
import com.dajie.push.distributed.zookeeper.ZkClient;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wills on 3/20/14.
 */
public class ZKDistributedManager implements IDistributedManager{

    private static final Logger LOGGER= LoggerFactory.getLogger(ZKDistributedManager.class);

    private  String zkHost;

    private  static String zkPath="/wireless/push_server";

    private  ConsistentHash<String> consistentHash;

    private  ZkClient zkClient;

    private List<String> saveNodes;

    private Lock lock=new ReentrantLock();

    private String registerNodeInfo;

    //多个环境的zk配置

    private static final String DEV_ZK="192.168.27.44:2181";

    private static final String TEST_ZK="172.20.10.1:2181";

    private static final String PRO_ZK="zookeeper01.bjyz.dajie-inc.com:3181,zookeeper02.bjyz.dajie-inc.com:3181,zookeeper03.bjyz.dajie-inc.com:3181,zookeeper04.bjyz.dajie-inc.com:3181,zookeeper05.bjyz.dajie-inc.com:3181";

    public ZKDistributedManager(String env){
        //get zk-cluster info automatically
        zkHost=DEV_ZK;
        if(env!=null){
            if(env.toLowerCase().equals("test")){
                zkHost=TEST_ZK;
            }else if(env.toLowerCase().equals("product")||env.toLowerCase().equals("pre_release")){
                zkHost=PRO_ZK;
            }
        }
        //init consistenthash
        initConsistentHash();
        //add zk listener
        addZKListener();
    }

    public ZKDistributedManager(String zkHost,String zkPath) {
        this.zkHost=zkHost;
        this.zkPath=zkPath;

        //init consistenthash
        initConsistentHash();
        //add zk listener
        addZKListener();
    }

    private void initConsistentHash(){
        consistentHash=new ConsistentHash<String>(new IHashFunction() {
            @Override
            public int hash(String key) {
                return key.hashCode();
            }
        });
    }

    private void addZKListener(){
        //listen zk
        zkClient=new ZkClient(zkHost,zkPath,new IZkCallback() {
            @Override
            public void notifyChange(List<String> data) {
                //Collections.sort(data);
                lock.lock();
                //add all.when initialization
                if(saveNodes==null||saveNodes.size()==0){
                    LOGGER.info("distributed system start init");
                    if(data!=null){
                        for(String node:data){
                            consistentHash.add(node);
                        }
                    }else{
                        LOGGER.info("there is not live node before");
                    }

                }
                //remove all,this would not happen
                if(data==null||data.size()==0){
                    LOGGER.info("zk has no live node ");
                    if(saveNodes!=null){
                        for(String node:saveNodes){
                            consistentHash.remove(node);
                        }
                    }
                }

                if(saveNodes!=null&&data!=null){
                    if(saveNodes.size()<data.size()){
                        List node=(List)CollectionUtils.subtract(data,saveNodes);
                        //suppose only one node has changed
                        LOGGER.info("distributed system add node,"+node.get(0));
                        consistentHash.add(node.get(0).toString());
                    }
                    if(data==null||data.size()<saveNodes.size()){
                        List node=(List)CollectionUtils.subtract(saveNodes,data);
                        //suppose only one node has changed
                        LOGGER.info("distributed system remove node,"+node.get(0));
                        consistentHash.remove(node.get(0).toString());
                    }
                }

                //如果是注册的节点，每次变动需检查自身是否在zk中已注册。如果没有，需要重新注册。
                //如果只是client，registerNodeInfo为空，不进行这一步。
                if(registerNodeInfo!=null&&!data.contains(registerNodeInfo)){
                    register0();
                }
                saveNodes=data;
                lock.unlock();

            }
        });
    }


    private void register0(){
        zkClient.createNode(registerNodeInfo);
        LOGGER.info("re-register to zk succ:"+registerNodeInfo);
    }

    @Override
    public void register(String privateIp,String publicIp,int port) {
        this.registerNodeInfo=ZkPathUtil.genRisteryNodeInfo(privateIp,publicIp,port);
        zkClient.createNode(registerNodeInfo);
        LOGGER.info("register to zk succ:"+registerNodeInfo);

    }


    private String getNode(String clientId) {
        lock.lock();
        String node= consistentHash.get(clientId);
        lock.unlock();
        return node;
    }

    @Override
    public String getPrivateIpServer(String clientId) {
        String nodeInfo=getNode(clientId);
        return ZkPathUtil.getPrivateHost(nodeInfo);
    }

    @Override
    public String getPublicIpServer(String clientId) {
        String nodeInfo=getNode(clientId);
        return ZkPathUtil.getPublicHost(nodeInfo);
    }
}
