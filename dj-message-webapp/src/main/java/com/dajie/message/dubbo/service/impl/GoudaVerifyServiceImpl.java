package com.dajie.message.dubbo.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dajie.message.dao.JobDAO;
import com.dajie.message.dao.UserLabelDAO;
import com.dajie.message.dubbo.service.GoudaVerifyService;
import com.dajie.message.enums.JobStatus;
import com.dajie.message.model.job.JobModel;
import com.dajie.message.model.user.UserLabel;
import com.dajie.message.service.IJobService;
import com.dajie.message.service.IPushService;
import com.dajie.message.service.IUserProfileService;
import com.dajie.message.service.mq.GoudaVerifyType;
import com.dajie.message.util.EmojiFilterUtil;
import com.dajie.message.util.log.LoggerInformation;

@Component("goudaVerifyService")
public class GoudaVerifyServiceImpl implements GoudaVerifyService {

	public static final Logger logger = Logger
			.getLogger(GoudaVerifyServiceImpl.class);

	@Autowired
	private JobDAO jobDao;

	@Autowired
	private IJobService jobService;

	@Autowired
	private IPushService pushService;

	@Autowired
	private UserLabelDAO userLabelDAO;

	@Autowired
	private IUserProfileService userProfileService;

	@Override
	public Integer verifyErr(List<Integer> ids, Integer verifyType) {

		if (ids == null)
			return 0;

		if (verifyType == GoudaVerifyType.VERIFY_JOB.getCode()) {
			for (Integer id : ids) {
				JobModel model = jobDao.selectJobModelByJobId(id);
				if (model == null)
					continue;
				jobService.changeJobStatus(id, JobStatus.JOB_VERIFY_ERR);
				try {
					pushService.sendTextMessage(model.getUserId(), "您发布的职位\""
							+ model.getPositionName() + "\"未能通过审核，请重新编辑");
				} catch (Exception e) {
					LoggerInformation.LoggerErr(logger,
							"push verify job error error ", e);
				}
			}
		} else if (verifyType == GoudaVerifyType.VERIFY_LABEL.getCode()) {
			for (Integer id : ids) { // ids列表里是labelId
				UserLabel userLabel = userLabelDAO.getLabelById(id);
				try {
					pushService.sendTextMessage(
							userLabel.getUserId(),
							"您填写的个人亮点\""
									+ EmojiFilterUtil.recoverToEmoji(userLabel
											.getContent()) + "\"未通过审核，请重新添加");
				} catch (Exception e) {
					logger.error(
							"Push verifyCareerErr for userId:"
									+ userLabel.getUserId() + " failed", e);
				}
				userLabelDAO.delLabel(userLabel.getUserId(), id);
			}
		} else if (verifyType == GoudaVerifyType.VERIFY_NAME.getCode()) {
			for (Integer id : ids) { // ids列表里是userId
				try {
					pushService.sendTextMessage(id,
							"您填写的姓名未通过审核，为避免影响与附近的人才聊天，请使用真实姓名");
				} catch (Exception e) {
					logger.error("Push verifyNameErr for userId:" + id
							+ " failed", e);
				}
				userProfileService.auditUserNameNotPass(id);
			}
		} else if (verifyType == GoudaVerifyType.VERIFY_WORK.getCode()) {
			for (Integer id : ids) { // ids列表里是userId
				try {
					pushService.sendTextMessage(id, "您填写工作经历未通过审核，请确认信息并重新填写");
				} catch (Exception e) {
					logger.error("Push verifyCareerErr for userId:" + id
							+ " failed", e);
				}
				userProfileService.auditUserCareerNotPass(id);
			}
		} else if (verifyType == GoudaVerifyType.VERIFY_EDUCATION.getCode()) {
			for (Integer id : ids) { // ids列表里是userId
				try {
					pushService.sendTextMessage(id, "您填写教育经历未通过审核，请确认信息并重新填写");
				} catch (Exception e) {
					logger.error("Push verifyEducationErr for userId:" + id
							+ " failed", e);
				}
				userProfileService.auditUserEducationNotPass(id);
			}
		}

		return 1;
	}

}
