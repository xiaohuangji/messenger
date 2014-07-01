package com.dajie.push.storage.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by wills on 4/15/14.
 */
public interface PrivacySettingDAO {

    static final String TABLE="privacy_setting";

    @Select("select chatNotification from "+TABLE+" where userId=#{userId}")
    public Integer getChatNotificationSetting(@Param("userId") int userId);

}
