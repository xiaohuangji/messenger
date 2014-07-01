package com.dajie.push.authenticator;

import com.dajie.message.model.push.AppInfo;
import com.dajie.push.storage.IStorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by wills on 3/13/14.
 */
public class DefaultAuthenticator implements IAuthenticator {

    private static final Logger LOGGER= LoggerFactory.getLogger(DefaultAuthenticator.class);

    private IStorageManager storageManager;

    //local cache
    //<apiKey&secretKey,appId>
    private Map<String,String> apiInfoMap=new WeakHashMap<String, String>();

    public DefaultAuthenticator(IStorageManager storageManager) {
        this.storageManager = storageManager;
    }

    @Override
    public boolean checkConnValid(String apiKey, String secretKey) {
        String apiInfo=apiKey+secretKey;
        boolean valid=false;
        if(apiInfoMap.containsKey(apiInfo)){
            valid=true;
        }else{
            AppInfo appInfo=storageManager.getAppInfo(apiKey, secretKey);
            if(appInfo!=null){
                synchronized (this){
                    apiInfoMap.put(apiInfo,appInfo.getAppId());
                }
                valid=true;
            }
        }
        return valid;
    }

    @Override
    public boolean checkAppIdValid(String appId, String apiKey, String secretKey) {
        String apiInfo=apiKey+secretKey;
        boolean valid=false;
        if(apiInfoMap.containsKey(apiInfo)){
            if(appId.equals(apiInfoMap.get(apiInfo))){
                valid=true;
            }
        }
        return valid;
    }
}
