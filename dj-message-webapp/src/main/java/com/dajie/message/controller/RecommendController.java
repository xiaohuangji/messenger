package com.dajie.message.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.dajie.message.model.job.SimpleJobInfo;
import com.dajie.message.model.user.ProfileView;
import com.dajie.message.service.IJobService;
import com.dajie.message.service.IProfileService;
import com.dajie.message.util.StringUtil;
import com.dajie.message.util.UrlEncryptUtil;

@Controller
@RequestMapping("recommend")
public class RecommendController {

	private static final Logger logger = Logger
			.getLogger(RecommendController.class);

	private final String UID_SEPARATOR = ",";

	private final int MAX_RECOMMAND = 20;

	@Autowired
	private IProfileService messageProfileService;

	@Autowired
	private ProfileController profileController;

	@Autowired
	private IJobService jobService;

	@RequestMapping(value = "profile/{uids}", method = RequestMethod.GET)
	public ModelAndView getProfile(HttpServletRequest request,
			@PathVariable("uids") String uids) {
		String decryptUids = UrlEncryptUtil.decrypt(uids);
		logger.info("recommend profile: deccrpt uids " + decryptUids);
		List<Integer> userIds = extractId(decryptUids);
		logger.info("input uids:" + uids + ", extract ids: " + userIds);
		WebStatisticLogger.logPV("/recommend/profile||" + userIds);
		// not a valid request
		if (null == userIds || userIds.size() == 0) {
			return new ModelAndView("redirect:/");
		} else {
			List<ProfileView> profileViews = new ArrayList<ProfileView>();
			// 显示白领，学生，还是没有学生和工作信息的用户
			Map<Integer, Integer> statusMap = new HashMap<Integer, Integer>();

			// 对应的个人profile
			Map<Integer, String> profileUrlMap = new HashMap<Integer, String>();
			for (int userId : userIds) {
				ProfileView profileView = messageProfileService.get(userId,
						userId);
				if (profileView != null
						&& StringUtil.isNotEmpty((profileView.getName()))) {
					// 处理头像,有遮罩用遮罩头像，没有用原图
					String avatarShow = profileView.getAvatar();
					if (StringUtil.isNotEmpty(profileView.getAvatarMask())) {
						avatarShow = profileView.getAvatarMask();
					}
					profileView.setAvatar(avatarShow);
					profileController.processName(profileView);

					// 用户身份
					int status = 0;
					if (StringUtil.isNotEmpty(profileView.getSchool())) {
						status = 1;
					}
					if (StringUtil.isNotEmpty(profileView.getCorpName())) {
						status = 2;
					}
					statusMap.put(userId, status);

					// 个人profile
					String profileUrl = "/profile/"
							+ UrlEncryptUtil.encrypt(userId + "");
					profileUrlMap.put(userId, profileUrl);

					profileViews.add(profileView);
				}
			}
			ModelAndView mv = new ModelAndView("wap/recommend_profile");

			// 页面数据
			mv.addObject("statusMap", statusMap);
			mv.addObject("profileUrlMap", profileUrlMap);
			mv.addObject("profileList", profileViews);
			
			mv.addObject("share_desc", "本周推荐好人才！");
			return mv;
		}

	}

	@RequestMapping(value = "job/{jobIds}", method = RequestMethod.GET)
	public ModelAndView getJob(@PathVariable(value = "jobIds") String jobIds) {
		String decryptJobIds = UrlEncryptUtil.decrypt(jobIds);
		logger.info("recommend profile: deccrpt uids " + decryptJobIds);
		WebStatisticLogger.logPV("/recommend/job||" + decryptJobIds);
		ModelAndView mv = new ModelAndView("wap/recommend_job");
		List<SimpleJobInfo> sjis = jobService.listJobsByJobIds(decryptJobIds);
		mv.addObject("simpleJobInfos", sjis);

		// 职业详情url
		List<Integer> jobIdList = extractId(decryptJobIds);
		Map<Integer, String> jobUrlMap = new HashMap<Integer, String>();
		if (jobIdList != null && jobIdList.size() > 0) {
			for (int jobId : jobIdList) {
				String jobDetailUrl = "/job/"+ UrlEncryptUtil.encrypt(jobId + "");
				jobUrlMap.put(jobId,jobDetailUrl);
			}
		}
		mv.addObject("jobUrlMap", jobUrlMap);
		
		mv.addObject("share_desc", "本周推荐好机会！");
		return mv;
	}

	private List<Integer> extractId(String ids) {
		if (StringUtil.isEmpty(ids)) {
			return null;
		}
		String[] idArray = ids.split(UID_SEPARATOR);
		int idCount = idArray.length;
		if (idCount > MAX_RECOMMAND) {
			return null;
		}
		List<Integer> idList = new ArrayList<Integer>();
		for (String id : idArray) {
			int userId = NumberUtils.toInt(id);
			if (userId > 0) {
				idList.add(userId);
			}
		}
		return idList;
	}
}
