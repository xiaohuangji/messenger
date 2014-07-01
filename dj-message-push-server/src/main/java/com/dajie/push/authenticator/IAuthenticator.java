package com.dajie.push.authenticator;

/**
 * Created by wills on 3/13/14.
 */
public interface IAuthenticator {

    /**
     * check apiKey&secretKey valid or not
     * @param username apiKey
     * @param password secretKey
     * @return
     */
    public boolean checkConnValid(String username,String password);

    /**
     * check appId valid or not
     * @param appId
     * @param apiKey
     * @param secretKey
     * @return
     */
    public boolean checkAppIdValid(String appId,String apiKey,String secretKey);
}
