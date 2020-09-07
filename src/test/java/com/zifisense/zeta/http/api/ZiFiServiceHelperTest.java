package com.zifisense.zeta.http.api;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.zifisense.zeta.http.api.cache.CacheFactory;
import com.zifisense.zeta.http.api.model.ApiResponse;
import com.zifisense.zeta.http.api.model.HttpMethod;

import net.sf.json.JSONObject;

public class ZiFiServiceHelperTest {
	private static final String API_KEY = "c6d8e5d10c6a4a5e8d9666ffd4c54d7c";
	private static final String SECRET_KEY = "91b7dcb079cf4040afd6e9b8146151d6";
	private static final String HOST_NAME = "https://test-cn.zifisense.com";
	private static final String DEVICE_UID = "a0000001";

//	private static final String API_KEY = "07ae55d646d0cb80d957dccd5b397401";
//	private static final String SECRET_KEY = "c020a297718c253afeaf88ff1eabe8f1";
//	private static final String HOST_NAME = "http://192.168.0.12:25455";
//	private static final String DEVICE_UID = "00000001";

	private static final String API_GET_MS_LIST = "/teamcms/ws/zeta_v1/wan_ms/query/%s/getMsList";
	private static final String API_GET_MS_STATUS = "/teamcms/ws/zeta_v1/wan_ms/query/%s/getMsStatus";
	private static final String API_POST_MS_UPLOADDATA = "/teamcms/ws/zeta_v1/wan_ms/query/%s/getMsUploadDataByDate";


	@Test
	public void testGetJsonRequestSucc1() {
		ZiFiHttpClient ziFiHttpClient = new ZiFiHttpClient(API_KEY, SECRET_KEY, HOST_NAME);
		try {
			String resultJson = ziFiHttpClient.send(String.format(API_GET_MS_STATUS, DEVICE_UID), HttpMethod.GET, null,null, 10);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),ziFiHttpClient.getApiConfig(), resultJson);
			// 格式化返回的json数据
			ApiResponse apiResponse = (ApiResponse) JSONObject.toBean(JSONObject.fromObject(resultJson),ApiResponse.class);
			assertEquals(0, apiResponse.getStatus().longValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetJsonRequestSucc2() {
		ZiFiHttpClient ziFiHttpClient = new ZiFiHttpClient(API_KEY, SECRET_KEY, HOST_NAME);
		String apiInfoString = String.format("%s,%s,%s",ziFiHttpClient.getApiConfig().getApiKey(),ziFiHttpClient.getApiConfig().getSecretKey(),ziFiHttpClient.getApiConfig().getHostName());
		try {
			String resultJson = ziFiHttpClient.send(String.format(API_GET_MS_LIST, API_KEY), HttpMethod.GET, null, null,10);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),apiInfoString, resultJson);
			// 格式化返回的json数据
			ApiResponse apiResponse = (ApiResponse) JSONObject.toBean(JSONObject.fromObject(resultJson),ApiResponse.class);
			assertEquals(0, apiResponse.getStatus().longValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetJsonRequestSucc3() {
		ZiFiHttpClient ziFiHttpClient = new ZiFiHttpClient(API_KEY, SECRET_KEY, HOST_NAME);
		Map<String, Object> bodyMap = new HashMap<String, Object>();
		// 模拟查询往前一小时的数据
		long currentTimeLong = System.currentTimeMillis() / 1000;
		bodyMap.put("starttime", currentTimeLong - 1 * 60 * 60);
		bodyMap.put("endtime", currentTimeLong);
		try {
			String resultJson = ziFiHttpClient.send(String.format(API_POST_MS_UPLOADDATA, DEVICE_UID), HttpMethod.POST,null, bodyMap, 10);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),ziFiHttpClient.getApiConfig().toString() + bodyMap, resultJson);
			// 格式化返回的json数据
			ApiResponse apiResponse = (ApiResponse) JSONObject.toBean(JSONObject.fromObject(resultJson),ApiResponse.class);
			assertEquals(0, apiResponse.getStatus().longValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetJsonRequestSucc4() {
		ZiFiHttpClient ziFiHttpClient = new ZiFiHttpClient(API_KEY, SECRET_KEY, HOST_NAME);
		//模拟多穿query参数
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("test", "testValue");
		queryMap.put("signal", "testsignal");
		Map<String, Object> bodyMap = new HashMap<String, Object>();
		// 模拟查询往前一小时的数据
		long currentTimeLong = System.currentTimeMillis() / 1000;
		bodyMap.put("starttime", currentTimeLong - 1 * 60 * 60);
		bodyMap.put("endtime", currentTimeLong);
		try {
			String resultJson = ziFiHttpClient.send(String.format(API_POST_MS_UPLOADDATA, DEVICE_UID), HttpMethod.POST,queryMap, bodyMap, 10);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),ziFiHttpClient.getApiConfig().toString() + bodyMap, resultJson);
			// 格式化返回的json数据
			ApiResponse apiResponse = (ApiResponse) JSONObject.toBean(JSONObject.fromObject(resultJson),ApiResponse.class);
			assertEquals(0, apiResponse.getStatus().longValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Test
	public void testGetJsonRequestSucc5() {
		ZiFiHttpClient ziFiHttpClient = new ZiFiHttpClient(API_KEY, SECRET_KEY, HOST_NAME);
		Map<String, Object> bodyMap = new HashMap<String, Object>();
		// 模拟查询往前一小时的数据
		long currentTimeLong = System.currentTimeMillis() / 1000;
		bodyMap.put("starttime", currentTimeLong - 1 * 60 * 60);
		bodyMap.put("endtime", currentTimeLong);
		try {
			//模拟token无效
			CacheFactory.getInstance().addCache("accessTokenCache", "tokenNull", 10);
			String resultJson = ziFiHttpClient.send(String.format(API_POST_MS_UPLOADDATA, DEVICE_UID), HttpMethod.POST,null, bodyMap, 10);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),ziFiHttpClient.getApiConfig().toString() + bodyMap, resultJson);
			// 格式化返回的json数据
			ApiResponse apiResponse = (ApiResponse) JSONObject.toBean(JSONObject.fromObject(resultJson),ApiResponse.class);
			assertEquals(0, apiResponse.getStatus().longValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Test
	public void testGetJsonRequestFail1() {
		ZiFiHttpClient ziFiHttpClient = new ZiFiHttpClient(API_KEY, "12345678901234567890123456789012", HOST_NAME);
		Map<String, Object> bodyMap = new HashMap<String, Object>();
		// 模拟查询往前一小时的数据
		long currentTimeLong = System.currentTimeMillis() / 1000;
		bodyMap.put("starttime", currentTimeLong - 1 * 60 * 60);
		bodyMap.put("endtime", currentTimeLong);
		try {
			String resultJson = ziFiHttpClient.send(String.format(API_POST_MS_UPLOADDATA, DEVICE_UID), HttpMethod.POST,null, bodyMap, 10);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),ziFiHttpClient.getApiConfig(), resultJson);
			if (resultJson.contains("Encryption error")) {
				return;
			}
		} catch (Exception e) {
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),ziFiHttpClient.getApiConfig(), e.getMessage());
			assertEquals("Authentication failed!", e.getMessage());
			return;
		}
		fail("Simulated exception, no exception reported！");
	}
	@Test
	public void testGetJsonRequestFail2() {
		ZiFiHttpClient ziFiHttpClient = new ZiFiHttpClient(API_KEY, SECRET_KEY, HOST_NAME);
		Map<String, Object> bodyMap = new HashMap<String, Object>();
		// 模拟查询往前一小时的数据
		long currentTimeLong = System.currentTimeMillis() / 1000;
		bodyMap.put("start", currentTimeLong - 1 * 60 * 60);
		bodyMap.put("endtime", currentTimeLong);
		try {
			ziFiHttpClient.send(String.format(API_POST_MS_UPLOADDATA, DEVICE_UID), HttpMethod.POST,null, bodyMap, 10);
		} catch (Exception e) {
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),ziFiHttpClient.getApiConfig(), e.getMessage());
			assertEquals("Params Error 400!", e.getMessage());
			return;
		}
		fail("Simulated exception, no exception reported！");
	}
	@Test
	public void testGetJsonRequestFail3() {
		ZiFiHttpClient ziFiHttpClient = new ZiFiHttpClient(API_KEY, SECRET_KEY, HOST_NAME);
		Map<String, Object> bodyMap = new HashMap<String, Object>();
		// 模拟查询往前一小时的数据
		long currentTimeLong = System.currentTimeMillis() / 1000;
		bodyMap.put("starttime", currentTimeLong - 1 * 60 * 60);
		bodyMap.put("endtime", currentTimeLong);
		try {
			//模拟空body
			ziFiHttpClient.send(String.format(API_POST_MS_UPLOADDATA, DEVICE_UID), HttpMethod.POST,null, null, 10);
		} catch (Exception e) {
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),ziFiHttpClient.getApiConfig(), e.getMessage());
			assertEquals("Params Error 400!", e.getMessage());
			return;
		}
		fail("Simulated exception, no exception reported！");
	}
	@Test
	public void testGetJsonRequestFail4() {
		ZiFiHttpClient ziFiHttpClient = new ZiFiHttpClient(API_KEY, SECRET_KEY, HOST_NAME);
		Map<String, Object> bodyMap = new HashMap<String, Object>();
		// 模拟查询往前一小时的数据
		long currentTimeLong = System.currentTimeMillis() / 1000;
		bodyMap.put("starttime", currentTimeLong - 1 * 60 * 60);
		bodyMap.put("endtime", currentTimeLong);
		try {
			//模拟空body
			ziFiHttpClient.send(String.format(API_POST_MS_UPLOADDATA, DEVICE_UID), HttpMethod.POST,null, null, 10);
		} catch (Exception e) {
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),ziFiHttpClient.getApiConfig(), e.getMessage());
			assertEquals("Params Error 400!", e.getMessage());
			return;
		}
		fail("Simulated exception, no exception reported！");
	}
	@Test
	public void testTokenRequestSucc() {
		ZiFiHttpClient ziFiHttpClient = new ZiFiHttpClient(API_KEY, SECRET_KEY, HOST_NAME);
		try {
			String tokenString = ziFiHttpClient.tokenRequest();
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),ziFiHttpClient.getApiConfig(), tokenString);
			assertEquals(32, tokenString.length());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testTokenRequestFail() {
		ZiFiHttpClient ziFiHttpClient = new ZiFiHttpClient("12345678901234567890123456789012", SECRET_KEY, HOST_NAME);
		try {
			ziFiHttpClient.tokenRequest();
		} catch (Exception e) {
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),ziFiHttpClient.getApiConfig(), e.getMessage());
			assertEquals("Authentication failed!", e.getMessage());
			return;
		}
		fail("Simulated exception, no exception reported！");
	}

}
