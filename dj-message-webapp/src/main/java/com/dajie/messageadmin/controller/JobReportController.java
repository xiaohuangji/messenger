package com.dajie.messageadmin.controller;

import com.dajie.message.dao.JobInformDAO;
import com.dajie.message.enums.JobStatus;
import com.dajie.message.model.job.JobInfo;
import com.dajie.message.model.job.JobInformModel;
import com.dajie.message.service.IJobService;
import com.dajie.message.service.IPushService;
import com.dajie.messageadmin.utils.DatetimeUtil;
import com.dajie.messageadmin.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

/**
 * Created by wills on 5/17/14.
 */
@Controller
@RequestMapping("/admin/jobreport")
public class JobReportController {

    @Autowired
    private IJobService jobService;

    @Autowired
    private JobInformDAO jobInformDAO;

    @Autowired
    private IPushService pushService;

    private static final int PAGESIZE=10;

    private static final int SHOWPAGE=10;

    private static final Date TIME1970=new Date(0);


    @RequestMapping("get")
    public ModelAndView getFeedback(@RequestParam(required = false)Integer userId,@RequestParam(required = false)String jobName,@RequestParam(required = false)String startTime,@RequestParam(required = false)String endTime,
                     @RequestParam(required = false,defaultValue = "1")Integer pageNum){

        ModelAndView mv=new ModelAndView("admin/jobReport");
        Date start= DatetimeUtil.stringToDate(startTime);
        Date end=DatetimeUtil.stringToDate(endTime);

        int offset=PAGESIZE*(pageNum-1);

        List<JobInformModel> jobs= jobInformDAO.getInformedJobs(userId,jobName==null?null:"%"+jobName+"%",start==null?TIME1970:start,end==null?new Date():end,offset,PAGESIZE);

        int totalCount=jobInformDAO.getCount();

        //分页
        mv.addObject("pageObject", PageUtil.buildPage(PAGESIZE, pageNum, SHOWPAGE, totalCount, "/admin/jobreport/get?"));
        //参数传入
        mv.addObject("userId", userId);
        mv.addObject("jobName", jobName);
        mv.addObject("startTime", startTime);
        mv.addObject("endTime", endTime);
        //数据
        mv.addObject("jobs", jobs);
        return mv;
    }

    @RequestMapping("getDetail")
    @ResponseBody
    public JobInfo getJobDetail(int id){
        return jobService.getJobDetail(id);
    }

    @RequestMapping("updateStatus")
    @ResponseBody
    public int updateStatus(int id,int status,Integer jobId){
        if(status==JobInformModel.STATUS_DELETED){
            JobInformModel jobInformModel=jobInformDAO.getInformJobById(id);

            jobService.changeJobStatus(jobId, JobStatus.JOB_INFROM);
            pushService.sendTextMessage(jobInformModel.getInformUserId(), "您的举报已受理成功，该机会已经下线");
            pushService.sendTextMessage(jobInformModel.getJobUserId(),
                    "抱歉，您的机会" + jobInformModel.getPositionName() + "由于"
                            + jobInformModel.getDescription()
                            + "被用户举报，经审核后已下线，重新编辑后可以再次发布");
        }
        jobInformDAO.updateStatusByJobId(jobId,status);
        return 0;
    }
}
