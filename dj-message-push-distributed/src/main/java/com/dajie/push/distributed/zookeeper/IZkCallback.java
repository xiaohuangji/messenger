package com.dajie.push.distributed.zookeeper;

import java.util.List;

/**
 * Created by wills on 3/20/14.
 */
public interface IZkCallback {

    public void notifyChange(List<String> data);
}
