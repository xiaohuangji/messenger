package com.dajie.message.service.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("sigCache")
public class SigCache {

	@Autowired
	private IRedisCache redisCache;
	
	public static final String SIG_PREFIX = "sig";
	
	
	public void addsig(String sig,Integer value){
		redisCache.set(SIG_PREFIX, sig, value,5);
		
	}
	
	public Integer getSig(String sig){
		return redisCache.get(SIG_PREFIX, sig, Integer.class);
	}
	
}
