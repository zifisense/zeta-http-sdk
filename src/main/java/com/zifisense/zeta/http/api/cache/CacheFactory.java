package com.zifisense.zeta.http.api.cache;
/**
 * 缓存工厂，用户创建缓存集合 单例模式
 * @Title: com.zifisense.zeta.http.api.TokenCache.CacheFactory.java
 * @Description: 
 * @author huangdg
 * @date 2020年8月31日
 */
@SuppressWarnings("all")
public class CacheFactory {
	private static CacheMap instance;
	private CacheFactory() {
		
	}
	
	public synchronized static CacheMap getInstance() {
		if (null == instance) {
			instance = new CacheMap();
			//开启守护线程，用于清理过期数据
			CacheWatch watch = new CacheWatch();
			watch.setDaemon(true);
			watch.setName("CacheWatch");
			watch.start();
		}
		return instance;
	}
	
//	/**
//	 * 双端检索单例
//	 */
//	private static volatile CacheMap instance = null;
//
//    public static CacheMap getInstance(){
//    	if (instance == null) {
//			synchronized (CacheFactory.class) {
//				if (instance == null) {
//					instance = new CacheMap();
//					//开启守护线程，用于清理过期数据
//					CacheWatch watch = new CacheWatch();
//					watch.setDaemon(true);
//					watch.start();
//				}
//			}
//		}
//    	return instance;
//    }
}
