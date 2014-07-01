package com.dajie.messageadmin.utils;

import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wills on 5/13/14.
 */
public class DatetimeUtil{

    private static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static  Date stringToDate(String time){
        try{
            if(StringUtils.isEmpty(time))
                return null;
            return simpleDateFormat.parse(time);
        }catch(Exception e){
            return null;
        }
    }

//    public static void main(String[] args) {
//        System.out.println(DatetimeUtil.stringToDate("2014-05-16 12:09"));
//    }
}
