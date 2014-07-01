package com.dajie.message.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountFieldValidator {

	private static final Pattern PHONE_NUMBER_PATTERN = Pattern
			.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(14[5,7]))\\d{8}$");

	// 密码(不含空格的可显示8位字符)
	private static final String PASSWORD_PATTERN = "^[\\x21-\\x7e]{4,20}$";

//	private static final String NAME_PATTERN = "^[\u4e00-\u9fa5,\u2022]{2,5}$|^[a-zA-Z\\s,\u2022]{2,30}$";
	// 用户名2-5个汉字，汉字开头和结尾，中间可填• ， 2-30个英文，英文开头和结尾，中间可出填•,空格
	private static final String NAME_PATTERN  ="^[\u4e00-\u9fa5][\u4e00-\u9fa5,\u2022]{0,3}[\u4e00-\u9fa5]$|^[a-zA-Z][a-zA-Z\\s,\u2022]{0,28}[a-zA-Z]$";

	private static final String EMAIL_PATTERN = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";

	public static boolean passwordValidate(String password) {
		return validate(password, PASSWORD_PATTERN);
	}

	public static boolean nameValidate(String name) {
		return validate(name, NAME_PATTERN);
	}

	public static boolean emailValidate(String email) {
		return validate(email, EMAIL_PATTERN);
	}

	private static boolean validate(String input, String regex) {
		if (input == null || "".equals(input)) {
			return false;
		}
		boolean tag = true;
		Pattern pattern = Pattern.compile(regex);
		Matcher mat = pattern.matcher(input);
		if (!mat.find()) {
			tag = false;
		}
		return tag;
	}

	public static boolean isPhoneNumber(String phone) {
		if (StringUtil.isEmpty(phone)) {
			return false;
		}
		Matcher m = PHONE_NUMBER_PATTERN.matcher(phone);
		return m.matches();
	}

}
