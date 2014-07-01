package com.dajie.push.netty.handler;

import com.dajie.push.utils.JsonUtil;

/**
 * Created by wills on 4/2/14.
 */
public class PoolStats {

    private int ringbuf;

    private int pushpool;

    public PoolStats(int ringbuf, int pushpool) {
        this.ringbuf = ringbuf;
        this.pushpool = pushpool;
    }

    public int getRingbuf() {
        return ringbuf;
    }

    public void setRingbuf(int ringbuf) {
        this.ringbuf = ringbuf;
    }

    public int getPushpool() {
        return pushpool;
    }

    public void setPushpool(int pushpool) {
        this.pushpool = pushpool;
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
