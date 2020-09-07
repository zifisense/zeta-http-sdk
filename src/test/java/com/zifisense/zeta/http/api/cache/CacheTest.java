package com.zifisense.zeta.http.api.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class CacheTest {

	@Test
	public void test() {
		// 初始值个数
		int iNumber = 10;
		// 缓存有效期s
		int iTime = 5;
		// 添加缓存初始值
		CacheMap instance = CacheFactory.getInstance();
		for (int i = 0; i < iNumber; i++) {
			instance.addCache("test" + i, i, iTime);
		}
		try {
			StringBuilder stringBuilder = new StringBuilder();
			// 初次取值
			String afterString = "";
			Object valueString1 = CacheFactory.getInstance().getValue("test1").toString();
			stringBuilder.append(valueString1);
			afterString += valueString1;
			// 有效期后
			TimeUnit.SECONDS.sleep(iTime + 2);
			int keySize = CacheFactory.getInstance().getKeys().size();
			Object valueString2 = CacheFactory.getInstance().getValue("test1");
			stringBuilder.append(valueString2);
			afterString += valueString2;
			// 添加一个值,获取新值内容
			CacheFactory.getInstance().addCache("test", 10, iTime);
			Object valueString3 = CacheFactory.getInstance().getValue("test");
			stringBuilder.append(valueString3);
			afterString += valueString3;
			
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),stringBuilder,afterString);
			// 三次缓存值个数 和 最后一个值拼接串
			assertEquals("1null10", afterString);
		} catch (Exception e) {
			e.getStackTrace();
			fail(e.getMessage());
		}
	}

}
