package com.dajie.message.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("download")
public class DownloadController {

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView getDownloadPage(
			@RequestParam(value = "source", required = false) String source) {
		WebStatisticLogger.logPV("/download||" + source);
		return new ModelAndView("wap/download");
	}

	/**
	 * 记录一个点击下载的日志
	 * 
	 * @param request
	 * @param platform
	 * @return
	 */
	@RequestMapping("clicklog")
	@ResponseBody
	public String clickLog(HttpServletRequest request,
			@RequestParam("platform") String platform) {
		String referer = request.getHeader("referer");
		WebStatisticLogger.log("download click statistic : " + platform + "||"
				+ referer);
		return "suc";
	}
}
