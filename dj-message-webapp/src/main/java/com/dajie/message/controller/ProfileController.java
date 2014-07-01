package com.dajie.message.controller;

import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.dajie.common.global.model.Pinyin;
import com.dajie.common.global.service.PinyinDataService;
import com.dajie.message.enums.GenderEnum;
import com.dajie.message.model.user.ProfileView;
import com.dajie.message.service.IProfileService;
import com.dajie.message.util.StringUtil;
import com.dajie.message.util.UrlEncryptUtil;

@Controller("profileController")
@RequestMapping("/profile")
public class ProfileController {

	@Autowired
	private IProfileService messageProfileService;

	@Autowired
	private PinyinDataService pinyinDataService;

	@RequestMapping(value = "/{encryptUserId}", method = RequestMethod.GET)
	public ModelAndView getProfile(
			@PathVariable(value = "encryptUserId") String encryptUserId) {
		int userId = NumberUtils.toInt(UrlEncryptUtil.decrypt(encryptUserId));
		WebStatisticLogger.logPV("/profile||" + userId);
		ModelAndView mv = new ModelAndView("wap/profile");
		ProfileView profileView = messageProfileService.get(userId, userId);
		if (profileView != null && StringUtil.isNotEmpty(profileView.getName())) {
			// 处理名字
			processName(profileView);
			// 处理头像,有遮罩用遮罩头像，没有用原图
			String avatarShow = profileView.getAvatar();
			if (StringUtil.isNotEmpty(profileView.getAvatarMask())) {
				avatarShow = profileView.getAvatarMask();
			}

			int status = 0;
			if (StringUtil.isNotEmpty(profileView.getSchool())) {
				status = 1;
			}
			if (StringUtil.isNotEmpty(profileView.getCorpName())) {
				status = 2;
			}

			// 页面填充
			// 微信的分享文案
			String corpName = profileView.getCorpName();
			String positionName = profileView.getPositionName();
			mv.addObject("share_title", "我在“勾搭”发现了一个牛人");
			mv.addObject("share_desc", corpName + "的" + positionName
					+ "，点击查看详细介绍");

			mv.addObject("status", status);
			mv.addObject("avatarShow", avatarShow);
			mv.addObject("profile", profileView);
			return mv;
		}
		// 找不到这个用户
		else {
			return new ModelAndView("redirect:/");
		}
	}

	public void processName(ProfileView profileView) {
		// 处理名字
		String name = profileView.getName();
		if (StringUtil.isNotEmpty(name)) {
			char nameFirstChar = name.toCharArray()[0];
			// 如果是128位的可见字符，直接转成大写
			if (nameFirstChar > 0x21 && nameFirstChar < 0x7e) {
				name = String.valueOf(nameFirstChar).toUpperCase();
			}
			// 现在的name的字段，除去英文就是中文，查询他的拼音
			else {
				List<Pinyin> namePinyin = pinyinDataService.getPinyin(String
						.valueOf(nameFirstChar));
				if (namePinyin != null && namePinyin.size() > 0) {
					name = String.valueOf(
							namePinyin.get(0).getPinyin().toCharArray()[0])
							.toUpperCase();
				} else {
					name = String.valueOf(nameFirstChar);
				}
			}

			String nameSuffix = "Mr. ";
			int gender = profileView.getGender();
			if (gender == GenderEnum.FEMALE.getCode()) {
				nameSuffix = "Ms. ";
			}
			profileView.setName(nameSuffix + name);
		}
	}
}
