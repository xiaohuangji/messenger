package com.dajie.message.util;

import org.apache.log4j.Logger;

public class SecurityUtils {

	private static final Logger logger = Logger.getLogger(SecurityUtils.class);

	/**
	 * 只有password输入为明文时，才会用到这个处理函数,最后结果为md5(md5(password),salt)
	 * 
	 * @param password
	 *            明文password
	 * @param salt
	 *            盐
	 * @return
	 */
	@Deprecated
	public static String handlePassword(String password, String salt) {
		return flavorPwdMD5(MD5Util.MD5Encrypt(password), salt);
	}

	/**
	 * 对已md5(password)加个盐
	 * 
	 * @param pwdMD5
	 * @param salt
	 * @return
	 */
	public static String flavorPwdMD5(String pwdMD5, String salt) {
		return MD5Util.MD5Encrypt(pwdMD5 + salt);
	}

	public static String getRandomSalt() {
		// 6-10位随机盐长度
		int saltLen = 6 + (int) (Math.random() * 5);
		String key = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < saltLen; i++) {
			sb.append(key.charAt((int) (Math.random() * key.length())));
		}
		logger.info("get random salt : " + sb.toString());
		return sb.toString();
	}

}
