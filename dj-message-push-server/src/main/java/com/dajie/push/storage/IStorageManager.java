package com.dajie.push.storage;


import com.dajie.message.model.push.AppInfo;
import com.dajie.message.model.push.ClientInfo;
import com.dajie.message.model.push.PushInfo;
import com.dajie.message.model.push.PushStatsInfo;

import java.util.List;

/**
 * Created by wills on 3/13/14.
 */
public interface IStorageManager {

    //------client info------
    public void updateClientInfo(ClientInfo clientInfo);

    public void deleteClientInfo(String clientId,String appId,String userId);

    public String getClientId(String appId,String userId);

    // ------push info------
    public PushInfo addPushInfo(PushInfo pushInfo);

    public void updatePushInfoStatus(String userId,int msgId,int status);

    public void updatePushInfoStatusById(List<Long> ids,int status);

    public List<PushInfo> getPushInfosByUserId(String appId,String userId);

    public boolean pushInfoIsExisted(String userId,long upMsgId);

    //------apiKey,sceretKey------
    public AppInfo getAppInfo(String apIKey,String secretKey);

    //------push stats
    public PushStatsInfo getLatestPushStats(String serverName);

    public int insertPushStats(PushStatsInfo pushStatsInfo);

}
