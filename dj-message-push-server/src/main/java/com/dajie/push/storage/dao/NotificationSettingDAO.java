package com.dajie.push.storage.dao;

import com.dajie.message.model.system.NotificationSetting;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by wills on 5/4/14.
 */
public interface NotificationSettingDAO {

    @Select("select userId,newMessage,sound,vibration,nightNoDisturbance from notification_setting where userId=#{userId}")
    public NotificationSetting getNotificationSetting(@Param("userId") int userId);
}
