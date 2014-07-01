package com.dajie.message.service.impl;

import com.dajie.message.constants.returncode.CommonResultCode;
import com.dajie.message.dao.PushJobDAO;
import com.dajie.message.model.operation.PushJob;
import com.dajie.message.model.operation.PushJobRaw;
import com.dajie.message.model.system.UserDeviceInfo;
import com.dajie.message.service.IPointOnMapService;
import com.dajie.message.service.ISchedulerPushManagerService;
import com.dajie.message.service.ISchedulerPushService;
import com.dajie.message.service.ISystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wills on 5/8/14.
 */
@Component("schedulerPushManagerService")
public class SchedulerPushManagerServiceImpl implements ISchedulerPushManagerService {


    private static final Logger LOGGER= LoggerFactory.getLogger(SchedulerPushManagerServiceImpl.class);

    @Autowired
    private PushJobDAO pushJobDAO;

    @Autowired
    private ISchedulerPushService schedulerPushService;

    @Autowired
    private IPointOnMapService pointOnMapService;

    @Autowired
    private ISystemService systemService;

    private static final int PAGESIZE=1000;

    private PushJobModel genPushJob(int jobId,PushJobRaw pushJobRaw){
        /**
         * 8989_1
         * 8989_2
         * 8989
         * 分片规则。最后一个标识为
         */
        int pageNum=0;
        int userCount=0;
        List<PushJob> pushJobs=new ArrayList<PushJob>();
        boolean hasMore=true;
        do{
            PushJob pushJob=new PushJob();
            pushJob.setContent(pushJobRaw.getContent());
            pushJob.setTriggerDate(pushJobRaw.getTriggerDate());
            List<Integer> userIds=new ArrayList<Integer>();
            try{
                userIds= pointOnMapService.searchPersonWithoutGeoInfo(null, null,null,
                        Integer.valueOf(pushJobRaw.getFilterIndustry()), Integer.valueOf(pushJobRaw.getFilterJobType()), 0, 0,0,
                        null, pushJobRaw.getFilterGender(), pushJobRaw.getFilterIsVerified(), 0,
                        null, null,null,null,null, pushJobRaw.getFilterCity(), pageNum, PAGESIZE);

            }catch(Exception e){
                LOGGER.error("search persion error,",e);
                break;
            }

            if(userIds.size()<PAGESIZE){
                hasMore=false;
                pushJob.setLast(true);
                pushJob.setPushJobKey(String.valueOf(jobId));
            }else{
                pageNum++;
                hasMore=true;
                pushJob.setLast(false);
                pushJob.setPushJobKey(String.valueOf(jobId)+"_"+pageNum);
            }

            //过滤 app version 和 mobile os
            if(pushJobRaw.getFilterMobileOs()!=0||!StringUtils.isEmpty(pushJobRaw.getFilterClientVersion())){
                int filterOs=pushJobRaw.getFilterMobileOs();
                String filterClientVersion=pushJobRaw.getFilterClientVersion();
                List<Integer> newUids=new ArrayList<Integer>();
                for(Integer userId:userIds){
                    UserDeviceInfo deviceInfo=systemService.getUserDeviceInfo(userId);
                    if(deviceInfo==null){
                        continue;
                    }
                    if(filterOs!=0&&deviceInfo.getSystem()!=filterOs){
                        //过滤os且不满足
                        continue;
                    }
                    if(!StringUtils.isEmpty(filterClientVersion)&&!filterClientVersion.equals(deviceInfo.getClientVersion())){
                        //过滤clientVersion且不满足
                        continue;
                    }
                    newUids.add(userId);
                }
                pushJob.setUserIds(newUids);
            }else{
                pushJob.setUserIds(userIds);
            }

            pushJobs.add(pushJob);
            userCount+=pushJob.getUserIds().size();

        }while(hasMore);

        return new PushJobModel(pushJobs,userCount);
    }

    @Override
    public int addPushJob(PushJobRaw pushJobRaw) {
        pushJobDAO.insertPushJob(pushJobRaw);
        int jobId=pushJobRaw.getId();
        if(jobId!=0){
            PushJobModel pushJobModel=genPushJob(jobId,pushJobRaw);
            if(pushJobModel.pushJobs==null||pushJobModel.pushJobs.size()==0||pushJobModel.userCount==0){
                pushJobDAO.changePushJobStatus(pushJobRaw.getId(),PushJobRaw.STATUS_INVALID);
                return CommonResultCode.OP_FAIL;
            }
            for(PushJob pushJob:pushJobModel.pushJobs){
                schedulerPushService.addJob(pushJob);
            }
            //更新回数字
            pushJobDAO.updateUserCount(pushJobRaw.getId(),pushJobModel.userCount);
            return CommonResultCode.OP_SUCC;
        }else{
            return CommonResultCode.OP_FAIL;
        }
    }

    @Override
    public int getPushJobCount() {
        return pushJobDAO.getPushJobCount();
    }

    @Override
    public List<PushJobRaw> getPushJob(int offset,int limit) {
        return pushJobDAO.getPushJob(offset,limit);
    }

    @Override
    public int cancelPushJob(int jobId) {
        int result=pushJobDAO.changePushJobStatus(jobId,PushJobRaw.STATUS_CANCELD);
        int userCount=pushJobDAO.getUserCount(jobId);
        int split=userCount/PAGESIZE;
        while(split>=0){
            if(split==0){
                schedulerPushService.removeJob(String .valueOf(jobId));
            }else{
                schedulerPushService.removeJob(String .valueOf(jobId+"_"+split));
            }
            split--;
        }

        return result>0?CommonResultCode.OP_SUCC:CommonResultCode.OP_FAIL;
    }

    class PushJobModel{
        public List<PushJob> pushJobs;
        public int userCount;

        PushJobModel(List<PushJob> pushJobs, int userCount) {
            this.pushJobs = pushJobs;
            this.userCount = userCount;
        }
    }
}
