package com.zifisense.zeta.http.api.util;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.zifisense.zeta.http.api.model.ZiFiException;

public class MiscUtilTest {
	private static final String CHARSET = "utf8";
	@Test
	public void testEncodeUrlSucc() {
		String beforeString = "+";
		String afterString = "%2B";
		String resultString = "";
		try {
			resultString = MiscUtil.encodeUrl(beforeString,CHARSET);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),beforeString, resultString);
			assertEquals(afterString, resultString);
		} catch (ZiFiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testEncodeUrlSucc1() {
		String beforeString = "*";
		String afterString = "%2A";
		String resultString = "";
		try {
			resultString = MiscUtil.encodeUrl(beforeString,CHARSET);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),beforeString, resultString);
			assertEquals(afterString, resultString);
		} catch (ZiFiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testEncodeUrlSucc2() {
		String beforeString = "~";
		String afterString = "~";
		String resultString = "";
		try {
			resultString = MiscUtil.encodeUrl(beforeString,CHARSET);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),beforeString, resultString);
			assertEquals(afterString, resultString);
		} catch (ZiFiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testEncodeUrlSucc3() {
		String beforeString = "=";
		String afterString = "%3D";
		String resultString = "";
		try {
			resultString = MiscUtil.encodeUrl(beforeString,CHARSET);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),beforeString, resultString);
			assertEquals(afterString, resultString);
		} catch (ZiFiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testEncodeUrlFail() {
		String beforeString = "=";
		String charSetString = "ukf";
		try {
			MiscUtil.encodeUrl(beforeString,charSetString);
		} catch (ZiFiException e) {
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),charSetString, e.getMessage());
			assertEquals("The Character Encoding is not supported:" + charSetString, e.getMessage());
			return;
		}
		fail("Simulated exception, no exception reported！");
	}

	@Test
	public void testQueryParamMap2UrlStringFalse() {
		Map<String, Object> queryParaMap = new HashMap<String, Object>(2);
		queryParaMap.put("apikey", "012345678901234567890123456789+~*=");
		queryParaMap.put("accesstoken", "abcdef9876543210123456789");
		String afterString = "apikey=012345678901234567890123456789+~*=&accesstoken=abcdef9876543210123456789";
		String resultString = "";
		try {
			resultString = MiscUtil.queryParamMap2UrlString(queryParaMap,CHARSET,false);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),queryParaMap, resultString);
			assertEquals(afterString, resultString);
		} catch (ZiFiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}
	@Test
	public void testQueryParamMap2UrlStringNull1() {
		Map<String, Object> queryParaMap = null;
		String afterString = null;
		String resultString = "";
		try {
			resultString = MiscUtil.queryParamMap2UrlString(queryParaMap,CHARSET,false);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),queryParaMap, resultString);
			assertEquals(afterString, resultString);
		} catch (ZiFiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}
	@Test
	public void testQueryParamMap2UrlStringNull2() {
		Map<String, Object> queryParaMap = new HashMap<String, Object>(2);
		String afterString = "";
		String resultString = "";
		try {
			resultString = MiscUtil.queryParamMap2UrlString(queryParaMap,CHARSET,false);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),queryParaMap, resultString);
			assertEquals(afterString, resultString);
		} catch (ZiFiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}

	@Test
	public void testQueryParamMap2UrlStringTrue() {
		Map<String, Object> queryParaMap = new HashMap<String, Object>(2);
		queryParaMap.put("apikey", "012345678901234567890123456789+~*=");
		queryParaMap.put("accesstoken", "abcdef9876543210123456789");
		String afterString = "apikey=012345678901234567890123456789%2B~%2A%3D&accesstoken=abcdef9876543210123456789";
		String resultString = "";
		try {
			resultString = MiscUtil.queryParamMap2UrlString(queryParaMap,CHARSET,true);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),queryParaMap, resultString);
			assertEquals(afterString, resultString);
		} catch (ZiFiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}

	@Test
	public void testurlCheckStart() {
		String beforeString = "//abc/def";
		String aftString = MiscUtil.urlCheckStart(beforeString);
		System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),beforeString, aftString);
		assertEquals(beforeString, "//" + aftString);
	}

	@Test
	public void testurlCheckEnd() {
		String beforeString = "/abc/def//";
		String aftString = MiscUtil.urlCheckEnd(beforeString);
		System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),beforeString, aftString);
		assertEquals(beforeString, aftString + "//");
	}
}
