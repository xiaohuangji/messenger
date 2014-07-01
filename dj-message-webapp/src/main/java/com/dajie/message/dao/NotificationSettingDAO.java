package com.dajie.message.dao;

import com.dajie.message.annotation.database.DBBean;
import com.dajie.message.model.system.NotificationSetting;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by wills on 5/4/14.
 */
@DBBean
public interface NotificationSettingDAO {

    @Select("select userId,newMessage,sound,vibration,nightNoDisturbance from notification_setting where userId=#{userId}")
    public NotificationSetting getNotificationSetting(@Param("userId")int userId);

    @Insert("replace into notification_setting (userId,newMessage,sound,vibration,nightNoDisturbance,createTime) values (#{userId},#{newMessage},#{sound},#{vibration},#{nightNoDisturbance},#{createTime})")
    public int setNotificationSetting(NotificationSetting notificationSetting);

}
