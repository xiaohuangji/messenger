package com.dajie.message.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class StringUtil {

	/**
	 * 去除两头空格，中间空间合并成一个空格
	 * 
	 * @param input
	 * @return
	 */
	public static String trim(String input) {
		if (input == null || input.equals("")) {
			return input;
		}
		String[] inputStrings = input.split(" ");
		List<String> inputs = new ArrayList<String>();
		for (String s : inputStrings) {
			if (!s.equals(" ") && !s.equals("")) {
				inputs.add(s);
			}
		}
		StringBuffer sb = new StringBuffer();
		if (inputs != null && inputs.size() > 0) {
			int len = inputs.size();
			for (int i = 0; i < len; i++) {
				sb.append(inputs.get(i));
				if (i < len - 1) {
					sb.append(" ");
				}
			}
		}
		return sb.toString();
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isNotEmpty(String str) {
		return !StringUtils.isEmpty(str);
	}

	public static boolean isEmail(String email) {
		if (isEmpty(email)) {
			return false;
		}
		Pattern p = Pattern
				.compile("^[_a-z0-9\\.\\-]+@([\\._a-z0-9\\-]+\\.)+[a-z0-9]{2,4}$");
		Matcher m = p.matcher(email.toLowerCase());
		return m.matches();
	}

	
}