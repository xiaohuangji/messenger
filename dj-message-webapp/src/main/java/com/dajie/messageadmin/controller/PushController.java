package com.dajie.messageadmin.controller;

import com.dajie.message.constants.GoudaConstant;
import com.dajie.message.dao.AppInfoDAO;
import com.dajie.message.dao.ClientInfoDAO;
import com.dajie.message.model.message.*;
import com.dajie.message.model.push.ClientInfo;
import com.dajie.message.service.IPushService;
import com.dajie.messageadmin.utils.JsonUtil;
import com.dajie.messageadmin.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by wills on 4/18/14.
 */
@Controller
@RequestMapping("/admin/push")
public class PushController {

    @Autowired
    private ClientInfoDAO clientInfoDAO;

    @Autowired
    private AppInfoDAO appInfoDAO;

    @Autowired
    private IPushService pushService;

    private static final int PAGESIZE=10;

    private static final int SHOWPAGE=10;

    private static final String appId="JMApp";

    @RequestMapping("index")
    public ModelAndView getClientInfo(){
        return new ModelAndView("admin/index");
    }

    @RequestMapping("clientInfo")
    public ModelAndView getClientInfo(@RequestParam(required = false,defaultValue ="1")Integer pageNum){
        ModelAndView mv=new ModelAndView("admin/clientInfo");
        List<ClientInfo> clientInfoList=clientInfoDAO.getClientInfos(appId,PAGESIZE*(pageNum-1),PAGESIZE);
        mv.addObject("clientInfoList",clientInfoList);
        mv.addObject("curAppId",appId);
        int totalItem=clientInfoDAO.getClientInfoCount(appId);
        mv.addObject("pageObject", PageUtil.buildPage(PAGESIZE, pageNum, SHOWPAGE, totalItem,"/admin/push/clientInfo?"));

        return mv;
    }

    @RequestMapping("sendPush")
    public ModelAndView getSendPush(@RequestParam(required = false)String userId){
        ModelAndView mv=new ModelAndView("admin/sendPush");
        mv.addObject("curAppId",appId);
        mv.addObject("curUserId",userId);
        //mv.addObject("appIds",appInfoDAO.getAllAppId());
        return mv;
    }

    @RequestMapping(value="removeClientInfo",method = RequestMethod.POST)
    @ResponseBody
    public int removeAjax(String appId,String userId,String clientId){
       // System.out.println("-->"+appId+userId+clientId);
        int result=clientInfoDAO.removeClientInfo(appId,userId,clientId);
        return result==1?0:-1;
    }

    @RequestMapping(value = "send",method = RequestMethod.POST)
    @ResponseBody
    public int sendPushAjax(HttpServletRequest request,Integer userId,Integer msgType,String content){
       // System.out.println(userId+msgType+content);
        GoudaMessageBuilder builder=new GoudaMessageBuilder();
        AbstractContent content1=null;
        switch (msgType){
            case ChatContent.TEXT:
                content1= JsonUtil.fromJson(content, Text.class);
                break;
            case ChatContent.IMAGE:
                content1= JsonUtil.fromJson(content, Image.class);
                break;
            case ChatContent.LINK:
                content1= JsonUtil.fromJson(content, Link.class);
                break;
            case ChatContent.SOUND:
                content1= JsonUtil.fromJson(content, Sound.class);
                break;
            default:
                return -1;
        }
        builder.from(GoudaConstant.ASSISTANT_ID).to(userId).content(content1);
        boolean result=pushService.sendPush(builder);
        return result?0:-1;
    }


}
