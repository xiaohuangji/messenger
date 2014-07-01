package com.dajie.message.util;

import java.util.Random;

public class SMSUtil {
	public static String getPhoneCode() {
		Random r = new Random();
		int code = r.nextInt(9000) + 1000;
		return code + "";
	}
}
