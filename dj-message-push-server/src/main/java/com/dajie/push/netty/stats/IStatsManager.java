package com.dajie.push.netty.stats;

/**
 * Created by wills on 4/2/14.
 */
public interface IStatsManager {

    static final int PUSH_FROM_SERVER=1;

    static final int PUSH_FROM_ANDROID=2;

    static final int PUSH_FROM_IOS=3;

    static final int PUSH_TO_SERVER=4;

    static final int PUSH_TO_ANDROID=5;

    static final int PUSH_TO_IOS=6;

    public PushStats getPushStats();

    public void incrPushStatus(String clientId,boolean down);

 }
