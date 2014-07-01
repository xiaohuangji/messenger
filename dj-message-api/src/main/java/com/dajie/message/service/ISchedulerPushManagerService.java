package com.dajie.message.service;

import com.dajie.message.model.operation.PushJobRaw;

import java.util.List;

/**
 * Created by wills on 5/8/14.
 * ISchedulerPushService单纯完成job调度工作
 * @see com.dajie.message.service.ISchedulerPushService
 *
 * ISchedulerPushManagerService完成上层工作，提供增删取job功能。
 * 操作dao完成入库，调用ISchedulerPushService执行定时
 *
 */
public interface ISchedulerPushManagerService {

    public int addPushJob(PushJobRaw pushJobRaw);

    public List<PushJobRaw> getPushJob(int offset,int limit);

    public int cancelPushJob(int jobId);

    public int getPushJobCount();
}
