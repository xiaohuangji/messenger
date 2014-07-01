package com.dajie.messageadmin.controller;

import com.dajie.message.constants.returncode.CommonResultCode;
import com.dajie.message.model.operation.PushJobRaw;
import com.dajie.message.service.ISchedulerPushManagerService;
import com.dajie.messageadmin.utils.DatetimeUtil;
import com.dajie.messageadmin.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

/**
 * Created by wills on 5/13/14.
 */
@Controller
@RequestMapping("/admin/pushjob")
public class PushJobController {

    @Autowired
    private  ISchedulerPushManagerService schedulerPushManagerService;

    private static final int PAGESIZE=10;

    private static final int SHOWPAGE=10;

    @RequestMapping("get")
    public ModelAndView getPushJob(@RequestParam(required = false,defaultValue = "1")Integer pageNum){
        ModelAndView mv=new ModelAndView("admin/pushJob");

        List<PushJobRaw> pushJobRaws=schedulerPushManagerService.getPushJob(PAGESIZE*(pageNum-1),PAGESIZE);

        mv.addObject("pushJobRaws",pushJobRaws);
        int totalItem=schedulerPushManagerService.getPushJobCount();

        mv.addObject("pageObject", PageUtil.buildPage(PAGESIZE, pageNum, SHOWPAGE, totalItem,"/admin/pushjob/get?"));

        return mv;
    }

    @RequestMapping("cancel")
    @ResponseBody
    public int cancel(int id){
        return schedulerPushManagerService.cancelPushJob(id);
    }

    @RequestMapping("add")
    @ResponseBody
    public int add(String content,String link,Integer filterIsVerified,String filterCity,String triggerDate,Integer filterGender
                    ,String filterJobType,String filterIndustry,String operator,String filterJobTypeText,String filterIndustryText,String filterClientVersion,Integer filterMobileOs){
        PushJobRaw pushJobRaw=new PushJobRaw();

        Date date=DatetimeUtil.stringToDate(triggerDate);
        if(date==null){
            return CommonResultCode.OP_FAIL;
        }
        //<a href="http://www.dajie.com">http://www.dajie.com</a>
        if(!StringUtils.isEmpty(link)){
            if(!link.startsWith("http://")){
                link="http://"+link;
            }
            content=content+" <font color='#157dfb'><a href='"+link+"'>"+link+"</a></font>";
        }
        pushJobRaw.setContent(content);
        pushJobRaw.setFilterCity(filterCity);
        pushJobRaw.setFilterIsVerified(filterIsVerified);
        pushJobRaw.setFilterGender(filterGender);
        pushJobRaw.setFilterJobType(filterJobType);
        pushJobRaw.setFilterIndustry(filterIndustry);
        pushJobRaw.setOperator(operator);
        pushJobRaw.setFilterClientVersion(filterClientVersion);
        pushJobRaw.setFilterMobileOs(filterMobileOs);

        StringBuilder sb=new StringBuilder();
        if(filterIsVerified!=-1){
            sb.append(filterIsVerified==1?"认证用户":"未认证用户").append("+");
        }
        if(filterGender!=0){
            sb.append(filterGender==1?"男":"女").append("+");
        }
        if(!filterJobType.equals("0")){
            sb.append(filterJobTypeText).append("+");
        }
        if(!filterIndustry.equals("0")){
            sb.append(filterIndustryText).append("+");
        }
        if(!StringUtils.isEmpty(filterCity)){
            sb.append(filterCity).append("+");
        }
        if(!StringUtils.isEmpty(filterClientVersion)){
            sb.append(filterClientVersion).append("+");
        }
        if(filterMobileOs!=0){
            sb.append(filterMobileOs==1?"iOS":"Android").append("+");
        }

        if(sb.length()==0){
            pushJobRaw.setFilterDesc("全部用户");
        }else{
            pushJobRaw.setFilterDesc(sb.toString().substring(0,sb.length()-1));
        }


        pushJobRaw.setTriggerDate(date);
        return schedulerPushManagerService.addPushJob(pushJobRaw);
    }
}
