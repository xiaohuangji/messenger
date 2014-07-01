package com.dajie.message.service.cache;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.dajie.framework.cache.StringCache;
import com.dajie.framework.cache.inject.spring.StringCacheFactoryBean;
import com.dajie.framework.cache.model.ClusterType;
import com.dajie.message.util.log.JsonMapping;
import com.dajie.message.util.log.LoggerInformation;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wills on 4/15/14.
 */
@Component("redisCache")
public class RedisCacheImpl implements IRedisCache {

	private StringCache<Map<String, Object>, String> stringCacheBeanString;

	public static final int DEFAULT_EXPIRE_TIME = 60 * 60 * 24 * 365;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		@SuppressWarnings("rawtypes")
		StringCacheFactoryBean bean = new StringCacheFactoryBean();
		bean.setBeanName("stringCacheBeanString");
		bean.setClusterType(ClusterType.MOBILE);
		bean.setKeyTemplate("messenger_{prefix}_{key}");
		bean.setSerializeClass(String.class);
		try {
			stringCacheBeanString = bean.getObject();
		} catch (Exception e) {
			LoggerInformation.LoggerErr(
					"string cache bean string create error ,check base cache ",
					e);
		}
	}

	@Override
	public <T, K> void set(String prefix, K key, T o) {
		set(prefix, key, o, DEFAULT_EXPIRE_TIME);
	}

	@Override
	public <T, K> void set(String prefix, K key, T o, int expiredSec) {
		if (o == null)
			return;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("prefix", prefix);
		params.put("key", key);

		String t = "";
		try {
			t = JsonMapping.getMapper().writeValueAsString(o);
		} catch (JsonProcessingException e) {
		}
		if (!StringUtils.isEmpty(t)) {
			stringCacheBeanString.put(params, t, expiredSec, TimeUnit.SECONDS);
		}
	}

	@Override
	public <T, K> T get(String prefix, K key, Class<T> clazz) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("prefix", prefix);
		params.put("key", key);
		String ret = stringCacheBeanString.get(params);

		if (StringUtils.isEmpty(ret)) {
			return null;
		}
		try {
			return JsonMapping.getMapper().readValue(ret, clazz);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public <K> Long incr(String prefix, K key) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("prefix", prefix);
		params.put("key", key);
		return stringCacheBeanString.incr(params);

	}

	@Override
	public <K> Long incr(String prefix, K key, int expiredDay) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("prefix", prefix);
		params.put("key", key);
		Long value = stringCacheBeanString.incr(params);
		stringCacheBeanString.expire(params, expiredDay, TimeUnit.DAYS);
		return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K> void del(String prefix, K key) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("prefix", prefix);
		params.put("key", key);
		stringCacheBeanString.clear(params);
	}

}
