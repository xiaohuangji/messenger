package com.dajie.push.storage.constant;

/**
 * Created by wills on 3/13/14.
 */
public class PushStatus {

    /**
     * target is not online, push is waitting
     */
    public static final int WAITTING=0;

    /**
     * push is flighting, not receive puback
     */
    public static final int FLIGHTING=1;

    /**
     * receice puback
     */
    public static final int DELIVERED=2;

    /**
     * push expired
     */
    public static final int EXPIRED=3;
}
