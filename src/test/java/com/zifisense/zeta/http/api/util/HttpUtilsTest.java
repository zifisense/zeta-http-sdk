package com.zifisense.zeta.http.api.util;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.zifisense.zeta.http.api.model.HttpMethod;

public class HttpUtilsTest {

	@Test
	public void testSendHttpGetSucc() {
		try {
			Map<String, Object> queryMap = new HashMap<>(1);
			queryMap.put("lang", "zh-CN");
			Map<String, String> headerMap = new HashMap<>(1);
			headerMap.put("Accept-Charset", "utf8");
			String resulString = HttpUtils.send(HttpMethod.GET, "http://ip-api.com/json/", headerMap, queryMap, null, 10);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),null, resulString);
			assertEquals(true, resulString.contains("\"status\":\"success\""));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testSendHttpPostSucc() {
		try {
			String bodyString = "{\"username\":\"666666@qq.com\",\"password\":\"666\"}";
			String resulString = HttpUtils.send(HttpMethod.POST, "http://httpbin.org/post", null, null, bodyString, 10);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),bodyString, resulString);
			assertEquals(true, resulString.contains("http://httpbin.org/post"));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSendHttpPostFail() {
		String bodyString = "{\"username\":\"666666@qq.com\",\"password\":\"666\"}";
		try {
			HttpUtils.send(HttpMethod.POST, "http://127.0.0.1", null, null, bodyString, 10);
		} catch (Exception e) {
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),bodyString, e.getMessage());
			assertEquals("Http connect error", e.getMessage());
			return;
		}
		fail("Simulated exception, no exception reported！");
	}

	@Test
	public void testSendHttpsGetSucc() {
		try {
			Map<String, Object> queryMap = new HashMap<String, Object>(3);
			queryMap.put("page", 1);
			queryMap.put("count", 1);
			queryMap.put("type", "video");
			String resulString = HttpUtils.send(HttpMethod.GET, "https://api.apiopen.top/getJoke", null, queryMap, null,10);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),null, resulString);
			assertEquals(true, resulString.contains("\"code\":200,\"message\":\"成功!\""));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testSendHttpsPostSucc() {
		try {
			String bodyString = "{\"username\":\"666666@qq.com\",\"password\":\"666\"}";
			String resulString = HttpUtils.send(HttpMethod.POST, "https://httpbin.org/post", null, null, bodyString,10);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),bodyString, resulString);
			assertEquals(true, resulString.contains("https://httpbin.org/post"));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSendHttpsPostFail1() {
		String bodyString = "{\"username\":\"666666@qq.com\",\"password\":\"666\"}";
		try {
			HttpUtils.send(HttpMethod.POST, "https://127.0.0.1", null, null, bodyString, 10);
		} catch (Exception e) {
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),bodyString, e.getMessage());
			assertEquals("Https connect error", e.getMessage());
			return;
		}
		fail("Simulated exception, no exception reported！");
	}
	@Test
	public void testSendHttpsPostFail2() {
		String bodyString = "{\"username\":\"666666@qq.com\",\"password\":\"666\"}";
		try {
			HttpUtils.send(HttpMethod.POST, "hts://127.0.0.1", null, null, bodyString, 10);
		} catch (Exception e) {
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),bodyString, e.getMessage());
			assertEquals("a malformed URL", e.getMessage());
			return;
		}
		fail("Simulated exception, no exception reported！");
	}

}
