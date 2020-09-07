package com.zifisense.zeta.http.api.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 缓存对象
 * @Title: com.zifisense.zeta.http.api.TokenCache.CacheMap.java
 * @Description: 
 * @author huangdg
 * @date 2020年8月28日
 */

public class CacheMap<V> {
	Map<String,CacheValue<V>> cache = new HashMap<>();
	CacheMap() {
	}
	
	/**
	 * 添加缓存
	 * @param key 键
	 * @param value 值
	 * @param secondTimes 有效期，单位s 
	 */
	public void addCache(String key,V value,int secondTimes) {
		CacheValue<V> cacheValue = new CacheValue<>();
		cacheValue.setValue(value);
		cacheValue.setTime(System.currentTimeMillis() + secondTimes * 1000);
		cache.put(key, cacheValue);
		
	}
	
	/**
	 * 获取缓存值
	 * @param key
	 * @return
	 */
	public V getValue(String key) {
		CacheValue<V> cacheValue =cache.get(key);
		if (cacheValue != null) {
			return cacheValue.getValue();
		}
		return null;
	}
	/**
	 * 获取缓存key值集合
	 * @return
	 */
	public Set<String> getKeys() {
		return cache.keySet();
	}
}
