package com.dajie.message.service.impl;

import com.dajie.framework.config.EnvironmentEnum;
import com.dajie.framework.config.impl.DefaultConfigManager;
import com.dajie.message.constants.returncode.CommonResultCode;
import com.dajie.message.dao.*;
import com.dajie.message.model.system.NotificationSetting;
import com.dajie.message.model.system.PrivacySetting;
import com.dajie.message.model.system.UserDeviceInfo;
import com.dajie.message.service.IPersonMapObjectService;
import com.dajie.message.service.ISystemService;
import com.dajie.message.service.cache.IRedisCache;
import com.dajie.push.distributed.IDistributedManager;
import com.dajie.push.distributed.ZKDistributedManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Created by wills on 4/14/14.
 */
@Component("systemService")
public class SystemServiceImpl implements ISystemService {

	private static IDistributedManager distributedManager;

	@Autowired
	private PrivacySettingDAO privacySettingDAO;

	@Autowired
	private UserCustomizationDAO userCustomizationDAO;

	@Autowired
	private UserDeviceTokenDAO userDeviceTokenDAO;

	@Autowired
	private NotificationSettingDAO notificationSettingDAO;

	@Autowired
	private UserDeviceInfoDAO userDeviceInfoDAO;

	@Autowired
	private IRedisCache redisCache;

    @Autowired
    private IPersonMapObjectService personMapObjectService;

    static{
        EnvironmentEnum environment = DefaultConfigManager.getInstance().getEnvironmentEnum();
        distributedManager=new ZKDistributedManager(environment.getName());
    }
	@Override
	public String getPushServer(String clientId) {
		String server= distributedManager.getPublicIpServer(clientId);
        if(StringUtils.isEmpty(server)||server.startsWith("NULL")){
            server=distributedManager.getPrivateIpServer(clientId);
        }
        return server;
	}

	@Override
	public PrivacySetting getPrivacySetting(int _userId) {
		PrivacySetting setting= privacySettingDAO.getPrivacySetting(_userId);
        if(setting==null){
            setting=new PrivacySetting();
        }
        return setting;
	}

	@Override
	public int setPrivacySetting(int _userId, int colleagueVisibility,
			int visibility, int chatNotification) {

		PrivacySetting privacySetting = new PrivacySetting();
		privacySetting.setUserId(_userId);
		privacySetting.setChatNotification(chatNotification);
		privacySetting.setColleagueVisibility(colleagueVisibility);
		privacySetting.setVisibility(visibility);
		int r = privacySettingDAO.setPrivacySetting(privacySetting);
        if(r>0){//用户隐私设置发生变化，触发其他模块处理
            if(colleagueVisibility==0||visibility==0){
                personMapObjectService.updatePersonMapObject(_userId);
            }
            return CommonResultCode.OP_SUCC;
        }else{
            return CommonResultCode.OP_FAIL;
        }
	}

	@Override
	public String setCustomization(int _userId, String customization) {
		int result = userCustomizationDAO.setCustomization(_userId,
				customization);
		return (result > 0) ? customization : null;
	}

	@Override
	public String getCustomization(int _userId) {
		return userCustomizationDAO.getCustomization(_userId);
	}

	@Override
	public int bindIosToken(int _userId, String token) {
		int result = userDeviceTokenDAO.bindIosToken(_userId, token,new Date());
		return (result > 0) ? CommonResultCode.OP_SUCC
				: CommonResultCode.OP_FAIL;
	}

	@Override
	public int bindAndroidClientId(int _userId,String token) {
		int result  =userDeviceTokenDAO.bindAndroidToken(_userId, token, new Date());
		return (result > 0) ? CommonResultCode.OP_SUCC
				: CommonResultCode.OP_FAIL;
	}
	
	@Override
	public int setNotificationSetting(int _userId, int newMessage, int sound,
			int vibration, int nightNoDisturbance) {
		NotificationSetting notificationSetting = new NotificationSetting();
		notificationSetting.setUserId(_userId);
		notificationSetting.setNewMessage(newMessage);
		notificationSetting.setSound(sound);
		notificationSetting.setVibration(vibration);
		notificationSetting.setNightNoDisturbance(nightNoDisturbance);
		int r = notificationSettingDAO
				.setNotificationSetting(notificationSetting);
		return (r > 0) ? CommonResultCode.OP_SUCC : CommonResultCode.OP_FAIL;
	}

	@Override
	public NotificationSetting getNotificationSetting(int _userId) {
		NotificationSetting setting= notificationSettingDAO.getNotificationSetting(_userId);
        if(setting==null){
            setting=new NotificationSetting();
        }
        return setting;
	}

	@Override
	public int updateUserDeviceInfo(int _userId, int system,
			String systemVersion, String mobileBrand, String mobileModel,
			String channel, String clientVersion, String mobileResolution) {
		UserDeviceInfo userDeviceInfo = new UserDeviceInfo(_userId, system,
				systemVersion, mobileBrand, mobileModel, channel,
				clientVersion, mobileResolution);
		int updateResult = userDeviceInfoDAO.replaceDeviceInfo(userDeviceInfo);
		return updateResult > 0 ? CommonResultCode.OP_SUCC
				: CommonResultCode.OP_FAIL;

	}

    @Override
    public UserDeviceInfo getUserDeviceInfo(int _userId) {
        return userDeviceInfoDAO.getDeviceInfo(_userId);
    }

    @Override
    public int startToChat(int _userId, int from) {
        //开启聊天。除了增加统计点，当前无其他逻辑。
        return CommonResultCode.OP_SUCC;
    }
}
