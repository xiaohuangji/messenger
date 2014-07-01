package com.dajie.message.service.impl;

import com.dajie.message.model.operation.PushJob;
import com.dajie.message.service.ISchedulerPushService;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by wills on 5/8/14.
 */
@Component("schedulerPushService")
public class SchedulerPushServiceImpl implements ISchedulerPushService {

    private static final Logger LOGGER= LoggerFactory.getLogger(SchedulerPushServiceImpl.class);

    private static Scheduler scheduler;

    static{
        try{
            scheduler=new StdSchedulerFactory().getScheduler();
            scheduler.start();
            LOGGER.info("init quarz scheduler");
        }catch(SchedulerException e){
            LOGGER.error("init quartz scheduler fail,",e);
        }
    }

    @Override
    public List getAllJobs() {
//        try{
//           Set<JobKey> jobKeys= scheduler.getJobKeys(GroupMatcher.jobGroupEquals("defaultGroup"));
//           for(JobKey jobKey:jobKeys){
//               //JobDetail jobDetail=scheduler.getJobDetail(jobKey);
//               //MutableTrigger trigger=(MutableTrigger)scheduler.getTriggersOfJob(jobKey).get(0);
//               //we can get all information
//           }
//        }catch(SchedulerException e){
//
//        }
        return null;
    }

    @Override
    public void addJob(PushJob pushJob) {
        try{
            String identity=pushJob.getPushJobKey();
            JobDataMap jobDataMap=new JobDataMap();
            jobDataMap.put("pushJob",pushJob);

            JobDetail jobDetail= JobBuilder.newJob(PushJobExecutor.class).withIdentity(identity,"defaultGroup")
                    .usingJobData(jobDataMap).build();

            Trigger trigger= TriggerBuilder.newTrigger().withIdentity(identity,"defaultGroup").
                            startAt(pushJob.getTriggerDate()).build();


            scheduler.scheduleJob(jobDetail, trigger);

            LOGGER.info("add push job to scheduler success,"+pushJob.getPushJobKey());
        }catch(SchedulerException e){
            LOGGER.warn("add push job error,",e);
        }
    }


    @Override
    public void removeJob(String jobKey) {
       try{
           boolean r=scheduler.deleteJob(JobKey.jobKey(jobKey,"defaultGroup"));

           LOGGER.info("remove push job from scheduler success,"+jobKey+"+"+r);
       }catch(SchedulerException e){
           LOGGER.warn("remove push job error,",e);
       }
    }

}
