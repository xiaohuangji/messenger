package com.dajie.message.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dajie.message.service.ISmsSendService;
import com.dajie.sms.model.DefaultTplKey;
import com.dajie.sms.model.SmsRequest;
import com.dajie.sms.model.SmsResponse;
import com.dajie.sms.service.SmsService;

@Component("smsSendService")
public class SMSSendServiceImpl implements ISmsSendService {
	private Logger logger = LoggerFactory.getLogger(SMSSendServiceImpl.class);

	@Autowired
	private SmsService smsService;

	/**
	 * 发送短信，大于0为正常发送,sms计划不废弃接口，现可以使用
	 * 
	 * @param mobile
	 * @param msg
	 * @return
	 */
	@Deprecated
	public int smsSend(String mobile, String msg) {
		SmsRequest request = new SmsRequest();
		request.setMobile(mobile);
		request.setMsg(msg);
		// TODO 发送邮件确定短信通道
		request.setType("c");
		SmsResponse response = smsService.mt(request);
		// 短信下发的返回值处理
		int returnCode = NumberUtils.toInt(response.getResult());
		if (returnCode > 0) {
			logger.info("send Message success:" + "|"
					+ StringUtils.defaultString(mobile) + "|"
					+ StringUtils.defaultString(msg));
		} else {
			logger.info("send Message fail:" + "|"
					+ StringUtils.defaultString(mobile) + "|"
					+ StringUtils.defaultString(msg));
		}
		return returnCode;
	}

	/**
	 * 发送短信验证码
	 * 
	 * @param mobile
	 * @param code
	 * @return
	 */
	@Override
	public int smsSendVerifyCode(String mobile, String code) {
		Map<String, String> data = new HashMap<String, String>();
		data.put(DefaultTplKey.MOBILE.getKey(), mobile); // 目标手机号
		data.put("captcha", code); // 验证码
		// 3004是分配给无线部门的实时业务类型号，149是短信的模板号
		SmsResponse res = smsService.sendSMS(3004, 149, data);
		// 短信下发的返回值处理
		int returnCode = NumberUtils.toInt(res.getResult());
		if (returnCode > 0) {
			logger.info("send verify code success:" + "|"
					+ StringUtils.defaultString(mobile) + "|"
					+ StringUtils.defaultString(code));
		} else {
			logger.info("send verify code fail:" + "|"
					+ StringUtils.defaultString(mobile) + "|"
					+ StringUtils.defaultString(code));
		}
		return returnCode;
	}

	@Override
	public boolean smsSendRecommend(String mobile, String name, String jCount) {
		Map<String, String> data = new HashMap<String, String>();
		data.put(DefaultTplKey.MOBILE.getKey(), mobile);
		data.put("UNAME", name); // 用户姓名
		data.put("personCount", jCount); // 回应的人数
		SmsResponse res = null;
		try {
			res = smsService.sendSMS(3004, 150, data); // 3004是业务类型号，150是短信模板号
		} catch (Exception e) {
			logger.error("smsSendRecommend failed", e);
			return false;
		}
		if (res != null) {
			logger.info("[EveryDayTask] smsSendRecommend: " + data + ", res:"
					+ res.getResult());
		}
		return true;
	}
	
	public SmsService getSmsService() {
		return smsService;
	}

	public void setSmsService(SmsService smsService) {
		this.smsService = smsService;
	}

}
