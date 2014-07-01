package com.dajie.push.netty.filter;

import com.dajie.message.enums.UserDeviceTokenTypeEnum;
import com.dajie.message.model.message.ChatContent;
import com.dajie.message.model.message.SystemContent;
import com.dajie.message.model.system.NotificationSetting;
import com.dajie.message.model.system.UserDeviceToken;
import com.dajie.push.netty.channel.NettyChannel;
import com.dajie.push.storage.dao.NotificationSettingDAO;
import com.dajie.push.storage.dao.UserDeviceTokenDAO;
import com.dajie.push.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Map;

/**
 * Created by wills on 5/4/14. 不会返回false.发送applepush
 */
public class PushFilter implements IFilter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PushFilter.class);

	private NotificationSettingDAO notificationSettingDAO;

	private UserDeviceTokenDAO userDeviceTokenDAO;

	public void setNotificationSettingDAO(
			NotificationSettingDAO notificationSettingDAO) {
		this.notificationSettingDAO = notificationSettingDAO;
	}

	public void setUserIosTokenDAO(UserDeviceTokenDAO userDeviceTokenDAO) {
		this.userDeviceTokenDAO = userDeviceTokenDAO;
	}

	private ApplePushClient applePushClient = ApplePushClient.getInstance();

	private AndroidPushClient androidPushClient = AndroidPushClient
			.getInstance();

	private static final String DEFAULT_CONTENT = "你收到一条新消息";

	private static final String IMAGE_CONTENT = "你收到一张图片";

	private static final String SOUND_CONTENT = "你收到一段语音";

	private static final String LOCATION_CONTENT = "有一个地理位置分享给你";

	private static final String CARD_CONTENT = "有一张名片分享给你";

	private static final String JOB_CONTENT = "有一个机会分享给你";

	private static final String ASSISTANT_CONTENT = "你收到一条小助手消息";

	@Override
	public boolean filter(NettyChannel channel, String destUserId,
			String payload) {
		int userId = Integer.valueOf(destUserId);
		// 判断是否在免打扰时间段
		NotificationSetting notificationSetting = notificationSettingDAO
				.getNotificationSetting(userId);
		if (notificationSetting != null
				&& notificationSetting.getNightNoDisturbance() == 1
				&& isNight()) {
			LOGGER.debug("nightNoDisturbance filter,userId:" + destUserId);
			return true;
		}

		// 发送push
		UserDeviceToken userDeviceToken = userDeviceTokenDAO.getDeviceToken(userId);
		if (StringUtils.isEmpty(userDeviceToken)) {
			LOGGER.warn("ios token is empty,do not push," + userId);
			return true;
		}

		Map<String, Object> jsonMap = JsonUtil.jsonToMap(payload);
		int contentType = (Integer) jsonMap.get("contentType");

		String content = DEFAULT_CONTENT;
		if (channel.getUserId() == null && contentType == ChatContent.TEXT) {
			// 系统发的文字消息判定为小助手消息
			content = ASSISTANT_CONTENT;
		} else {
			switch (contentType) {
			case ChatContent.TEXT:
				break;
			case ChatContent.IMAGE:
				content = IMAGE_CONTENT;
				break;
			case ChatContent.CARD:
				content = CARD_CONTENT;
				break;
			case ChatContent.JOB:
				content = JOB_CONTENT;
				break;
			case ChatContent.LOCATION:
				content = LOCATION_CONTENT;
				break;
			case ChatContent.SOUND:
				content = SOUND_CONTENT;
				break;
			case SystemContent.UNFRIEND:
			case SystemContent.USERUPDATE:
			case SystemContent.USERBLOCK:
				// 以上三种类型，直接返回true，不进行push.
				return true;
			default:
				break;
			}
		}
		jsonMap.put("content", content);
		String token = userDeviceToken.getToken();
		
		//send push 
		if (userDeviceToken.getType() == UserDeviceTokenTypeEnum.APPLE
				.getCode()) {
			applePushClient.push(token, jsonMap);
		} else if (userDeviceToken.getType() == UserDeviceTokenTypeEnum.ANDROID
				.getCode()) {
			androidPushClient.push(token, jsonMap);
		} else {
			LOGGER.info("this user token is unknow type.");
		}
		return true;
	}

	private boolean isNight() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if (hour > 23 || hour < 8) {
			return true;
		} else {
			return false;
		}

	}
}
