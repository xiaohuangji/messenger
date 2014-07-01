package com.dajie.messageadmin.controller;

import com.dajie.message.dao.PushStatsInfoDAO;
import com.dajie.message.model.push.PushStatsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wills on 4/25/14.
 */
@Controller
@RequestMapping("/admin/push")
public class MonitorController {

    @Autowired
    private PushStatsInfoDAO pushStatsInfoDAO;

    @RequestMapping("monitor")
    public ModelAndView getPushStatsInfo(){
        ModelAndView mv=new ModelAndView("admin/monitor");

        Date date=new Date(System.currentTimeMillis()-1*86400*1000);
        pushStatsInfoDAO.deleteOldData(date);

        List<PushStatsInfo> infos=pushStatsInfoDAO.getLatestPushStats();
        Map<String,PushStatsInfo> map=new HashMap<String, PushStatsInfo>();
        if(infos!=null&&infos.size()!=0){
            for(PushStatsInfo info:infos){
                if(!map.containsKey(info.getServerName())){
                    map.put(info.getServerName(),info);
                }
            }
        }

        PushStatsInfo totalInfo=new PushStatsInfo("Total");
        infos.clear();
        for(Map.Entry<String ,PushStatsInfo> entry:map.entrySet()){
            totalInfo.setIosConn(totalInfo.getIosConn()+entry.getValue().getIosConn());
            totalInfo.setAndroidConn(totalInfo.getAndroidConn()+entry.getValue().getAndroidConn());
            totalInfo.setServerConn(totalInfo.getServerConn()+entry.getValue().getServerConn());

            totalInfo.setFromIos(totalInfo.getFromIos() + entry.getValue().getFromIos());
            totalInfo.setFromAndroid(totalInfo.getFromAndroid() + entry.getValue().getFromAndroid());
            totalInfo.setFromServer(totalInfo.getFromServer() + entry.getValue().getFromServer());

            totalInfo.setToAndroid(totalInfo.getToAndroid() + entry.getValue().getToAndroid());
            totalInfo.setToIos(totalInfo.getToIos() + entry.getValue().getToIos());
            totalInfo.setUpdateTime(entry.getValue().getUpdateTime());
            infos.add(entry.getValue());
        }

        infos.add(totalInfo);
        mv.addObject("infos",infos);
        return mv;
    }
}
