package com.zifisense.zeta.http.api.cache;

/**
 * 缓存值对象
 * @Title: com.zifisense.zeta.http.api.TokenCache.CacheValue.java
 * @Description: 
 * @author huangdg
 * @date 2020年8月28日
 */
public class CacheValue<V> {
	/**
	 * 缓存值
	 */
	private V value;
	/**
	 * 到期时间
	 */
	private long time;
	
	public V getValue() {
		return value;
	}
	public void setValue(V value) {
		this.value = value;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
	
}
