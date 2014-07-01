package com.dajie.message.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dajie.message.dao.UserEmailVerifyDAO;
import com.dajie.message.model.user.UserEmailVerify;
import com.dajie.message.service.IProfileService;
import com.dajie.message.service.IPushService;
import com.dajie.message.service.IUserProfileService;

@Component("corpEmailVerifyJob")
public class CorpEmailVerifyJob {
	private static Logger logger = LoggerFactory
			.getLogger(CorpEmailVerifyJob.class);

	private static final long TWO_DAY_MILLIS = 2 * 24 * 3600 * 1000;

	@Autowired
	private UserEmailVerifyDAO userEmailVerifyDAO;

	@Autowired
	private IUserProfileService userProfileService;

	@Autowired
	private IPushService pushService;

	@Autowired
	private IProfileService profileService;

	public void execute() {

		logger.info("Start corp email verify task.................");
		Date twoDaysAgo = new Date(System.currentTimeMillis() - TWO_DAY_MILLIS);
		/**
		 * Delete UserEmailVerify verifyDate before 48 hours ago
		 */
		List<UserEmailVerify> twoDaysAgoList = userEmailVerifyDAO
				.getUserEmailVerifyTwoDaysAgo(twoDaysAgo);
		if (twoDaysAgoList != null) {
			for (UserEmailVerify userEmailVerify : twoDaysAgoList) {
				try {
					int userId = userEmailVerify.getUserId();
					pushService.sendTextMessage(userId,
							"对不起，由于邮箱与公司不匹配，认证审核未通过，请重新提交邮箱认证");
				} catch (Exception e) {
					logger.error("邮箱认证审核未通过消息发送失败", e);
				}
				userEmailVerifyDAO.delUserEmailVerify(userEmailVerify);
				userProfileService.updateVerification(
						userEmailVerify.getUserId(), -1);

			}
		}
		/**
		 * Process UserEmailVerify verifyDate after 48 hours ago
		 */
		List<UserEmailVerify> afterTwoDaysAgoList = userEmailVerifyDAO
				.getUserEmailVerifyAfterTwoDaysAgo(twoDaysAgo);
		if (afterTwoDaysAgoList != null) {
			for (UserEmailVerify userEmailVerify : afterTwoDaysAgoList) {
				/**
				 * GetLatest Verification
				 */
				int userId = userEmailVerify.getUserId();
				profileService.getLatestVerification(userId);
			}
		}
	}

}