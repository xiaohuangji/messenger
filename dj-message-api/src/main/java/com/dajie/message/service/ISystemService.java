package com.dajie.message.service;

import com.dajie.message.annotation.rest.RestBean;
import com.dajie.message.model.system.NotificationSetting;
import com.dajie.message.model.system.PrivacySetting;
import com.dajie.message.model.system.UserDeviceInfo;

/**
 * Created by wills on 4/14/14.
 */
@RestBean
public interface ISystemService {

	/**
	 * 获取push服务器地址
	 * 
	 * @param clientId
	 * @return
	 */
	public String getPushServer(String clientId);

	/**
	 * 获得个人隐私设置
	 * 
	 * @param _userId
	 * @return
	 */
	public PrivacySetting getPrivacySetting(int _userId);

	/**
	 * 设置个人隐私设置
	 * 
	 * @param _userId
	 * @param colleagueVisibility
	 * @param visibility
	 * @param chatNotification
	 * @return
	 */
	public int setPrivacySetting(int _userId, int colleagueVisibility,
			int visibility, int chatNotification);

	/**
	 * 设置定制化信息
	 * 
	 * @param _userId
	 * @param customization
	 * @return
	 */
	public String setCustomization(int _userId, String customization);

	/**
	 * 获取用户定制化信息
	 * 
	 * @param _userId
	 * @return
	 */
	public String getCustomization(int _userId);

	/**
	 * 设置消息提醒
	 * 
	 * @param _userId
	 * @param newMessage
	 * @param sound
	 * @param vibration
	 * @param nightNoDisturbance
	 * @return
	 */
	public int setNotificationSetting(int _userId, int newMessage, int sound,
			int vibration, int nightNoDisturbance);

	/**
	 * 获取消息提醒
	 * 
	 * @param _userId
	 * @return
	 */
	public NotificationSetting getNotificationSetting(int _userId);

	/**
	 * 绑定iostoken 解绑时传空即可
	 * 
	 * @param _userId
	 * @param token
	 * @return
	 */
	public int bindIosToken(int _userId, String token);

	/**
	 * 绑定android push的clientId
	 * @param _userId
	 * @param token
	 * @return
	 */
	public int bindAndroidClientId(int _userId, String token);
	
	/**
	 * 客户端上传用户的设装信息
	 * @param _userId
	 * @param system
	 * @param systemVersion
	 * @param mobileBrand
	 * @param mobileModel
	 * @param channel
	 * @param clientVersion
	 * @param mobileResolution
	 * @return
	 */
	public int updateUserDeviceInfo(int _userId, int system,
			String systemVersion, String mobileBrand, String mobileModel,
			String channel, String clientVersion, String mobileResolution);


    /**
     * 获取用户的设备信息
     * @param _userId
     * @return
     */
    public UserDeviceInfo getUserDeviceInfo(int _userId);

    /**
     * 用户开启聊天时调用
     * @param _userId
     * @param from
     * @return
     */
    public int startToChat(int _userId,int from);

}
