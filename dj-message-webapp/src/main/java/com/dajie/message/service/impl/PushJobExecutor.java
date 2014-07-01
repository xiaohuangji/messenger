package com.dajie.message.service.impl;

import com.dajie.message.dao.PushJobDAO;
import com.dajie.message.model.operation.PushJob;
import com.dajie.message.model.operation.PushJobRaw;
import com.dajie.message.service.IPushService;
import com.dajie.message.util.SpringContextUtil;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by wills on 5/16/14.
 */
public class PushJobExecutor implements Job {

    private IPushService pushService;

    private PushJobDAO pushJobDAO;

    public PushJobExecutor() {
        pushJobDAO= SpringContextUtil.getBean(PushJobDAO.class);
        pushService=SpringContextUtil.getBean(IPushService.class);
    }

    private static final Logger LOGGER= LoggerFactory.getLogger(PushJobExecutor.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap=context.getMergedJobDataMap();
        PushJob pushJob=(PushJob)jobDataMap.get("pushJob");
        String content=pushJob.getContent();
        List<Integer> userIds=pushJob.getUserIds();
        for(Integer userId:userIds){
            pushService.sendTextMessage(userId,content);
        }
        LOGGER.info("push job finished, jobkey:"+pushJob.getPushJobKey());
        if(pushJob.isLast()){
            pushJobDAO.changePushJobStatus(Integer.valueOf(pushJob.getPushJobKey()), PushJobRaw.STATUS_FINISHED);
        }


    }
}
