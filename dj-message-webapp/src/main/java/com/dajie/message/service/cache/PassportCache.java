package com.dajie.message.service.cache;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.dajie.framework.cache.StringCache;
import com.dajie.framework.cache.inject.spring.StringCacheFactoryBean;
import com.dajie.framework.cache.model.ClusterType;
import com.dajie.message.mcp.model.UserPassport;
import com.dajie.message.util.log.JsonMapping;
import com.dajie.message.util.log.LoggerInformation;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component("passportCache")
@Deprecated
public class PassportCache {
	
	private  StringCache<Map<String, Object>, String> stringCacheBeanString;
	

	/**
	 * passport服务前缀
	 */
	public static final String PASSPORT_PREFIX = "passport";
	/**
	 * 过期时间为一年
	 */
	public static final int EXPIRE_TIME = 60*60*24*365*1000;
	
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void after(){
		@SuppressWarnings("rawtypes")
		StringCacheFactoryBean bean = new StringCacheFactoryBean();
		bean.setBeanName("stringCacheBeanString");
		bean.setClusterType(ClusterType.MOBILE);
		bean.setKeyTemplate("messenger_{prefix}_{key}");
		bean.setSerializeClass(String.class);
		try {
			stringCacheBeanString = bean.getObject();
		} catch (Exception e) {
			LoggerInformation.LoggerErr("string cache bean string create error ,check base cache ",e);
		}
	}
	
	//@CacheString(prefix=PASSPORT_PREFIX,operation=CacheStringEnum.set,expire=EXPIRE_TIME)
	public void addPassport(int uid,UserPassport passport){
		if(passport == null)
			return ;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("prefix", PASSPORT_PREFIX);
		params.put("key", uid);
		
		String t = "";
		try {
			t = JsonMapping.getMapper().writeValueAsString(passport);
		} catch (JsonProcessingException e) {
		}
		if(!StringUtils.isEmpty(t)){
			stringCacheBeanString.put(params, t);
			stringCacheBeanString.expire(params, EXPIRE_TIME, TimeUnit.MILLISECONDS);
		}

	}
	
	//@CacheString(prefix=PASSPORT_PREFIX,operation=CacheStringEnum.get)
	public UserPassport getPassport(int uid){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("prefix", PASSPORT_PREFIX);
		params.put("key", uid);
		String ret = stringCacheBeanString.get(params);
		
		if(StringUtils.isEmpty(ret)){
			return null;
		}
		try {
			return JsonMapping.getMapper().readValue(ret,UserPassport.class);
		} catch (Exception e) {
			return null;
		}
		
	}
	
}