package com.dajie.message.service;

import com.dajie.message.model.operation.PushJob;

import java.util.List;

/**
 * Created by wills on 5/8/14.
 */
public interface ISchedulerPushService {

    public void addJob(PushJob pushJob);

    public void removeJob(String jobKey);

    public List getAllJobs();
}
