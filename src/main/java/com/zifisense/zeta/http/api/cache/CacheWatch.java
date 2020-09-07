package com.zifisense.zeta.http.api.cache;

import java.util.HashSet;
import java.util.Set;

/**
 * 缓存守护线程，清理过期数据
 * @Title: com.zifisense.zeta.http.api.TokenCache.CacheWatch.java
 * @Description: 
 * @author huangdg
 * @date 2020年8月31日
 */
@SuppressWarnings("all")
public class CacheWatch extends Thread{	
	//轮询时间
	private static final long PERIOD_TIME = 1 * 1000L;
	CacheMap instance = CacheFactory.getInstance();
	
	@Override
	public synchronized void run() {
		while (true) {
			//轮询清理
			if (instance.cache != null) {
				try {
					//获取所有的key
					Set keySet = instance.cache.keySet();
					//需要清理的key
					Set removeSet = new HashSet();
					for (Object key : keySet) {
						CacheValue cacheValue = (CacheValue) instance.cache.get(key);
						if (cacheValue.getTime() < System.currentTimeMillis()) {
							removeSet.add(key);
						}
					}
					for (Object key : removeSet) {
						instance.cache.remove(key);
					}
					//快速释放内存
					removeSet = null;
					sleep(PERIOD_TIME);
				} catch (Exception e) {
					e.getStackTrace();
				}
			} 
		}
	}
	

}
