package com.zifisense.zeta.http.api;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.junit.Test;

import com.zifisense.zeta.http.api.cache.CacheFactory;
import com.zifisense.zeta.http.api.model.ApiResponse;
import com.zifisense.zeta.http.api.model.HttpMethod;

/**
 * v1测试用例,现在转到ZifiNewHttpClientTest
 */
@Deprecated
public class ZiFiServiceHelperTest {
	private static final String API_KEY = "";
	private static final String SECRET_KEY = "";
	private static final String HOST_NAME = "";
	private static final String DEVICE_UID = "";

	private static final String API_GET_MS_LIST = "/teamcms/ws/zeta_v1/wan_ms/query/%s/getMsList";
	private static final String API_GET_MS_STATUS = "/teamcms/ws/zeta_v1/wan_ms/query/%s/getMsStatus";
	private static final String API_POST_MS_UPLOADDATA = "/teamcms/ws/zeta_v1/wan_ms/query/%s/getMsUploadDataByDate";


	@Test
	public void testGetMsStatus() {
		ZiFiHttpClient ziFiHttpClient = new ZiFiHttpClient(API_KEY, SECRET_KEY, HOST_NAME);
		try {
			String resultJson = ziFiHttpClient.send(String.format(API_GET_MS_STATUS, DEVICE_UID), HttpMethod.GET, null,null, 10);
			// 格式化返回的json数据
			ApiResponse apiResponse = JSONObject.parseObject(resultJson,ApiResponse.class);
			System.out.println(JSON.toJSONString(apiResponse));
			assertEquals(0, apiResponse.getStatus().longValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetMsList() {
		ZiFiHttpClient ziFiHttpClient = new ZiFiHttpClient(API_KEY, SECRET_KEY, HOST_NAME);
		try {
			String resultJson = ziFiHttpClient.send(String.format(API_GET_MS_LIST, API_KEY), HttpMethod.GET, null, null,10);
			// 格式化返回的json数据
			ApiResponse apiResponse = JSONObject.parseObject(resultJson,ApiResponse.class);
			System.out.println(JSON.toJSONString(apiResponse));
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
			ApiResponse apiResponse = JSONObject.parseObject(resultJson,ApiResponse.class);
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
			ApiResponse apiResponse = JSONObject.parseObject(resultJson,ApiResponse.class);
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
			ApiResponse apiResponse = JSONObject.parseObject(resultJson,ApiResponse.class);
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
