package com.dajie.message.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dajie.common.config.ConfigurationManager;
import com.dajie.core.profile.model.UserDetail;
import com.dajie.core.profile.service.ProfileService;
import com.dajie.message.dao.RecommendTaskDAO;
import com.dajie.message.model.user.ContactView;
import com.dajie.message.service.ISmsSendService;
import com.dajie.message.util.AccountFieldValidator;
import com.dajie.pt.invitation.client.EmailClient;
import com.dajie.push.client.IpAddressUtil;

@Component("recommendToDJUser")
public class RecommendToDJUser {

	private static Logger logger = LoggerFactory
			.getLogger(RecommendToDJUser.class);

	private static final String ENV = ConfigurationManager.getInstance()
			.getEnvName();;

	private static final String WORKER_IP = "10.10.65.20";

	private static final long ONE_DAY_MILLIS = 24 * 3600 * 1000;

	@Autowired
	private RecommendTaskDAO recommendTaskDAO;

	@Autowired
	private ISmsSendService smsSendService;

	@Autowired
	private ProfileService profileService;

	public void execute() {
		if (ENV.equalsIgnoreCase("dev") || ENV.equalsIgnoreCase("test")
				|| IpAddressUtil.getPrivateIp().equals(WORKER_IP)) {

			logger.info("Start recommendToDJUser task.................");
			Date startTime = new Date(System.currentTimeMillis()
					- ONE_DAY_MILLIS);
			Date endTime = new Date();
			List<ContactView> recommendUsers = recommendTaskDAO
					.getRecommendUser(startTime, endTime);
			logger.info("[EveryDayTask] users:"
					+ JSONArray.fromObject(recommendUsers).toString());
			if (recommendUsers != null) {
				for (ContactView user : recommendUsers) {
					logger.info("[EveryDayTask] user:"
							+ JSONObject.fromObject(user).toString());
					String platformUid = user.getPlatformUid();
					String email = user.getEmail();
					UserDetail userDetail = profileService
							.getUserDetailByUid(NumberUtils.toInt(platformUid));
					String mobile = null;
					if (userDetail != null)
						mobile = userDetail.getMobile();
					String name = StringUtils.defaultString(user.getName());

					int personCount = user.getPersonCount();
					if (AccountFieldValidator.emailValidate(email)) {
						sendRecommendEmail(email, platformUid, name, personCount);
					}
					if (AccountFieldValidator.isPhoneNumber(mobile)) {
						smsSendService.smsSendRecommend(mobile, name,
								String.valueOf(personCount));
					}
				}
			}
		}

	}

	private void sendRecommendEmail(String email, String platformUid, String name,
			int personCount) {
		Map<String, String> mailData = new HashMap<String, String>();
		mailData.put("TO_EMAIL", email);
		mailData.put("s_TPL_VERSION_ID", "6113"); // 邮件模板id，邮件模板由发送中心统一制作和管理
		mailData.put("UID", platformUid); // hr用户id
		mailData.put("UNAME", name);
		mailData.put("peopleCount", String.valueOf(personCount));
		boolean res = EmailClient.getInstance().sendEmail(mailData);
		if (res) {
			logger.info(String.format(
					"[EveryDayTask] %s %s %s %d send success", email, platformUid,
					name, personCount));
		}
	}

}
