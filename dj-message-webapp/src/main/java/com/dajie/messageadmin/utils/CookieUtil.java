package com.dajie.messageadmin.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xing.feng
 * 简单地加密。
 *
 */
public class CookieUtil {

	private static final Logger logger=LoggerFactory.getLogger(CookieUtil.class);

	private static final  int cookieMaxAge=86400*7;
	
	public static String get(HttpServletRequest request,String type){
		try{
			Cookie[] cookies=request.getCookies();
			if(cookies==null)
				return null;
			for(Cookie cookie:cookies){
				if(cookie.getName().equals(type)){
					String token=cookie.getValue();
					//解密
					return EncrUtil.decrypt(token);
				}
			}
		}catch(Exception e){
			logger.error("get cookie ERROR",e);
		}
		return null;
		

	}
	
	public static void set(HttpServletResponse response,String type,String value){
		try{
			//加密
			String token= EncrUtil.encrypt(value);
			Cookie cookie = new Cookie(type,token);
			cookie.setPath("/");
			cookie.setMaxAge(cookieMaxAge);
			response.addCookie(cookie);
		}
		catch(Exception e){
			logger.error("set cookie ERROR",e);
		}
	}
	
	public static void remove(HttpServletResponse response,String type){
		Cookie cookie = new Cookie(type,null);
		cookie.setPath("/");
		cookie.setMaxAge(0); 
		response.addCookie(cookie); 
	}

	

	
}
