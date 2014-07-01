package com.dajie.message.controller;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.dajie.message.model.job.JobInfo;
import com.dajie.message.service.IJobService;
import com.dajie.message.util.UrlEncryptUtil;

@Controller("jobController")
@RequestMapping("/job")
public class JobController {

	@Autowired
	private IJobService jobService;
	
	@RequestMapping(value="/{encryJobId}", method = RequestMethod.GET)
	public ModelAndView getJobDetail(@PathVariable("encryJobId") String encryJobId){
		int jobId = NumberUtils.toInt(UrlEncryptUtil.decrypt(encryJobId));
		WebStatisticLogger.logPV("/job||"+jobId);
		ModelAndView mv = new ModelAndView("wap/job");
		
		JobInfo jobInfo = jobService.getJobDetail(jobId);
		
		//微信的分享文案
		String positionName = jobInfo.getPositionName();
		int salaryStart  = jobInfo.getSalaryStart();
		int salaryEnd = jobInfo.getSalaryEnd();
		String corpName = jobInfo.getCorpName();
		mv.addObject("share_title","我在“勾搭”发现了一个"+positionName+"的好机会");
		mv.addObject("share_desc", corpName + "（"+salaryStart+"-" + salaryEnd+"），点击了解更多信息");
		
		mv.addObject("job",jobInfo);
		
		return mv;
	}
	
}
