package com.dajie.messageadmin.controller;

import com.dajie.messageadmin.model.FeedbackOption;
import com.dajie.messageadmin.utils.DatetimeUtil;
import com.dajie.messageadmin.utils.PageUtil;
import com.dajie.message.model.operation.Feedback;
import com.dajie.message.service.IFeedbackService;
import com.dajie.message.service.IPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

/**
 * Created by wills on 5/12/14.
 */
@Controller
@RequestMapping("/admin/feedback")
public class FeedbackController {

    private static final int PAGESIZE=10;

    private static final int SHOWPAGE=10;

    @Autowired
    private IFeedbackService feedbackService;

    @Autowired
    private IPushService pushService;

    @RequestMapping("get")
    public ModelAndView getFeedback(@RequestParam(required = false)Integer userId,@RequestParam(required = false)String mobile,@RequestParam(required = false)String startTime,@RequestParam(required = false)String endTime,
                                    @RequestParam(required = false)Integer status,@RequestParam(required = false)Integer system,@RequestParam(required = false)String mobileBrand
                                    ,@RequestParam(required = false)String channel,@RequestParam(required = false)String clientVersion
                                    ,@RequestParam(required = false,defaultValue = "1")Integer pageNum){
        ModelAndView mv=new ModelAndView("admin/feedback");
        Date start= DatetimeUtil.stringToDate(startTime);
        Date end=DatetimeUtil.stringToDate(endTime);
        int totalCount=feedbackService.getCount(userId, mobile, start, end, status, system, null, mobileBrand, null, channel, clientVersion, null);
        List<Feedback> feedbacks= feedbackService.getFeedback(userId, mobile, start, end, status, system, null, mobileBrand, null, channel, clientVersion, null, PAGESIZE, pageNum);
        mv.addObject("pageObject", PageUtil.buildPage(PAGESIZE, pageNum, SHOWPAGE, totalCount,"/admin/feedback/get?"));
        FeedbackOption fbo=new FeedbackOption(userId, mobile, startTime, endTime, status, system, null, mobileBrand, null, null, channel, clientVersion);
        mv.addObject("feedbackOption", fbo);
        mv.addObject("feedbacks", feedbacks);
        return mv;
    }


    @RequestMapping("updateStatus")
    @ResponseBody
    public int updateStatus(int id,int status){
        return feedbackService.updateStatus(id, status);
    }


    @RequestMapping("reply")
    @ResponseBody
    public int reply(int id,int userId,String reply){
        //发送push
        pushService.sendTextMessage(userId,reply);
        //修改状态
        return feedbackService.reply(id, reply);
    }

    @RequestMapping("delete")
    @ResponseBody
    public int delete(int id){
        return feedbackService.updateStatus(id,Feedback.STATUS_DELETED);
    }

}
