package com.dajie.message.service.cache;

/**
 * Created by wills on 4/15/14.
 */
public interface IRedisCache {

	/**
	 * set a value use default expire time
	 * 
	 * @param prefix
	 * @param key
	 * @param o
	 */
	public <T, K> void set(String prefix, K key, T o);

	/**
	 * set a value and expired time
	 * 
	 * @param prefix
	 * @param key
	 * @param o
	 * @param expiredSec
	 */
	public <T, K> void set(String prefix, K key, T o, int expiredSec);

	/**
	 * get value
	 * 
	 * @param prefix
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T, K> T get(String prefix, K key, Class<T> clazz);

	/**
	 * incr a value
	 * 
	 * @param prefix
	 * @param key
	 * @return
	 */
	public <K> Long incr(String prefix, K key);

	/**
	 * incr and set expire time
	 * 
	 * @param prefix
	 * @param key
	 * @param expiredDay
	 * @return
	 */
	public <K> Long incr(String prefix, K key, int expiredDay);

	/**
	 * clear key
	 * 
	 * @param prefix
	 * @param key
	 */
	public <K> void del(String prefix, K key);

}
