package com.dajie.messageadmin.controller;

import com.dajie.message.dao.JobDetailCntDAO;
import com.dajie.message.dao.UserStatisticsDAO;
import com.dajie.message.model.job.JobInfo;
import com.dajie.message.model.user.SimpleUserView;
import com.dajie.message.model.user.UserLabel;
import com.dajie.message.service.IJobService;
import com.dajie.message.service.IShortUrlService;
import com.dajie.message.service.IUserProfileService;
import com.dajie.message.util.StringUtil;
import com.dajie.message.util.UrlEncryptUtil;
import com.dajie.messageadmin.model.ExtUserView;
import com.dajie.messageadmin.utils.DatetimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wills on 6/7/14.
 */
@Controller
@RequestMapping("/admin/viral")
public class ViralController {


    @Autowired
    private IJobService jobService;

    @Autowired
    private IUserProfileService userProfileService;

    @Autowired
    private JobDetailCntDAO jobDetailCntDAO;

    @Autowired
    private UserStatisticsDAO userStatisticsDAO;

    @Autowired
    private IShortUrlService shortUrlService;

    private static final int DAY7=86400000*7;

    @RequestMapping("/getjobs")
    public ModelAndView getJobs(@RequestParam(required = false)String time){
        ModelAndView mv=new ModelAndView("admin/viraljob");
        Date createDate=new Date(System.currentTimeMillis()-DAY7);
        if(!StringUtil.isEmpty(time)){
            createDate=DatetimeUtil.stringToDate(time+" 00:00");
        }
        List<Integer> jobIds=jobDetailCntDAO.getHotJobs(createDate);
        List<JobInfo> jobInfos=new ArrayList<JobInfo>();
        for(Integer jobId:jobIds){
            JobInfo jobInfo=jobService.getJobDetail(jobId);
            if(jobInfo!=null&&jobInfo.getUserId()!=0){
                jobInfos.add(jobInfo);
            }
        }
        mv.addObject("jobs",jobInfos);
        return mv;
    }

    @RequestMapping("/getusers")
    public ModelAndView getUsers(@RequestParam(required = false)String time){
        ModelAndView mv=new ModelAndView("admin/viraluser");
        Date createDate=new Date(System.currentTimeMillis()-DAY7);
        if(!StringUtil.isEmpty(time)){
            createDate=DatetimeUtil.stringToDate(time+" 00:00");
        }
        List<Integer> userIds=userStatisticsDAO.getHotUsers(createDate);
        List<ExtUserView> userViews=new ArrayList<ExtUserView>();
        for(Integer userId:userIds){
            ExtUserView userView=new ExtUserView();
            SimpleUserView simpleUserView=userProfileService.getSimpleUserView(userId);
            List<UserLabel> userLabels=userProfileService.getLabels(userId);
            userView.setSimpleUserView(simpleUserView);
            userView.setUserLabelList(userLabels);
            userViews.add(userView);
        }
        mv.addObject("users",userViews);

        return mv;
    }

    @RequestMapping("/sendJobMail")
    @ResponseBody
    public String sendJobMail(String ids){
        String url= "http://www.goudajob.com/recommend/job/"+UrlEncryptUtil.encrypt(ids);
        return shortUrlService.genShortUrl(url);
    }

    @RequestMapping("/sendUserMail")
    @ResponseBody
    public String sendUserMail(String ids){
        String url= "http://www.goudajob.com/recommend/profile/"+UrlEncryptUtil.encrypt(ids);
        return shortUrlService.genShortUrl(url);

    }
}
