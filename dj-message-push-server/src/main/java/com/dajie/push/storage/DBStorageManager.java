package com.dajie.push.storage;

import com.dajie.message.model.push.AppInfo;
import com.dajie.message.model.push.ClientInfo;
import com.dajie.message.model.push.PushInfo;
import com.dajie.message.model.push.PushStatsInfo;
import com.dajie.push.storage.constant.PushStatus;
import com.dajie.push.storage.dao.AppInfoDAO;
import com.dajie.push.storage.dao.ClientInfoDAO;
import com.dajie.push.storage.dao.PushInfoDAO;
import com.dajie.push.storage.dao.PushStatsInfoDAO;
import com.dajie.push.utils.Configuration;
import com.dajie.push.utils.EmojiFilterUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by wills on 3/13/14.
 */
public class DBStorageManager implements IStorageManager {
    
    private static final Logger LOGGER= LoggerFactory.getLogger(DBStorageManager.class);

    private ClientInfoDAO clientInfoDAO;

    private PushInfoDAO pushInfoDAO;

    private AppInfoDAO appInfoDAO;

    private PushStatsInfoDAO pushStatsInfoDAO;

    public void setClientInfoDAO(ClientInfoDAO clientInfoDAO) {
        this.clientInfoDAO = clientInfoDAO;
    }

    public void setPushInfoDAO(PushInfoDAO pushInfoDAO) {
        this.pushInfoDAO = pushInfoDAO;
    }

    public void setAppInfoDAO(AppInfoDAO appInfoDAO) {
        this.appInfoDAO = appInfoDAO;
    }

    public void setPushStatsInfoDAO(PushStatsInfoDAO pushStatsInfoDAO) {
        this.pushStatsInfoDAO = pushStatsInfoDAO;
    }

    private static final boolean dbRatain= Configuration.getInstance().getBoolean("db_retain");

//    private BiMap<String,String> clientMap= HashBiMap.create();

    private  LoadingCache<String, String> cache;

    @PostConstruct
    public void init(){
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS).build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String userId) throws Exception {
                        LOGGER.info("reload clientInfo:"+userId);
                        return clientInfoDAO.getClientIdByUserId(userId);
                    }
                });
    }

    @Override
    public boolean pushInfoIsExisted(String userId, long upMsgId) {
        int result=pushInfoDAO.isExisted(userId, upMsgId);
        return result>0?true:false;
    }

    @Override
    public void deleteClientInfo(String clientId, String appId,String userId) {
        clientInfoDAO.deleteClientInfo(clientId,appId,userId);
        synchronized (this){
//            clientMap.remove(appId+userId);
            cache.invalidate(userId);
            LOGGER.debug("[clientInfo] delete clientInfo:"+clientId+" userId:"+userId);
        }
    }

    @Override
    public AppInfo getAppInfo(String apiKey, String secretKey) {
        return appInfoDAO.getAppInfo(apiKey,secretKey);
    }

    @Override
    public void updateClientInfo(ClientInfo clientInfo) {
        clientInfoDAO.updateClientInfo(clientInfo);
        synchronized (this){
//            clientMap.forcePut(clientInfo.getAppId()+clientInfo.getUserId(),clientInfo.getClientId());
            cache.put(clientInfo.getUserId(),clientInfo.getClientId());
            LOGGER.debug("[clientInfo] update clientInfo:"+clientInfo.getClientId()+" userId:"+clientInfo.getUserId());
        }
    }

    @Override
    public String getClientId(String appId, String userId) {
        try{
            return cache.get(userId);
        }catch(Exception e){
            LOGGER.warn("cache get error,",e);
            return null;
        }
//        String clientId= clientMap.get(appId+userId);
//        if(StringUtils.isEmpty(clientId)){
//            clientId=clientInfoDAO.getClientId(appId,userId);
//            if(!StringUtils.isEmpty(clientId)){
//                synchronized (this){
//                    clientMap.forcePut(appId+userId,clientId);
//                    LOGGER.debug("[clientInfo] add clientInfo to cache:"+clientId+" userId:"+userId);
//
//                }
//            }
//        }
//        return clientId;
    }

    @Override
    public PushInfo addPushInfo(PushInfo pushInfo) {
        pushInfo.setPayload(EmojiFilterUtil.emojiFilter(pushInfo.getPayload()));
        pushInfoDAO.addPushInfo(pushInfo);
        return pushInfo;
    }


    @Override
    public void updatePushInfoStatus(String userId, int msgId, int status) {
        if(status == PushStatus.DELIVERED && dbRatain==false){
            pushInfoDAO.deletePushInfoStatus(userId,msgId);
        }else{
            pushInfoDAO.updatePushInfoStatus(userId,msgId,status);
        }
    }

    @Override
    public void updatePushInfoStatusById(List<Long> ids, int status) {
        if(status == PushStatus.DELIVERED && dbRatain==false){
            pushInfoDAO.deletePushInfoStatusById(ids);
        }else{
            pushInfoDAO.updatePushInfoStatusById(ids,status);
        }
    }

    @Override
    public List<PushInfo> getPushInfosByUserId(String appId, String userId) {
        List<PushInfo> pushInfos= pushInfoDAO.getPushInfosByUserId(appId,userId);
        if(pushInfos==null){
            return null;
        }
        for(PushInfo pushInfo:pushInfos){
            pushInfo.setPayload(EmojiFilterUtil.recoverToEmoji(pushInfo.getPayload()));
        }
        return pushInfos;
    }

    @Override
    public PushStatsInfo getLatestPushStats(String serverName) {
        PushStatsInfo info= pushStatsInfoDAO.getLatestPushStats(serverName);
        if(info==null){
            return new PushStatsInfo(serverName);
        }
        return info;
    }

    @Override
    public int insertPushStats(PushStatsInfo pushStatsInfo) {
        return pushStatsInfoDAO.InsertLatestPushStats(pushStatsInfo);
    }
}
