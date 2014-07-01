package com.dajie.message.dao;

import com.dajie.message.annotation.database.DBBean;
import com.dajie.message.model.system.PrivacySetting;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by wills on 4/15/14.
 */
@DBBean
public interface PrivacySettingDAO {

    static final String TABLE="privacy_setting";

    @Select("select userId,colleagueVisibility,visibility,chatNotification from "+TABLE+" where userId=#{userId}")
    public PrivacySetting getPrivacySetting(@Param("userId")int userId);

    @Insert("replace into "+TABLE+" (userId,colleagueVisibility,visibility,chatNotification,createTime) values (#{userId},#{colleagueVisibility},#{visibility},#{chatNotification},#{createTime})")
    public int setPrivacySetting(PrivacySetting privacySetting);
}
