package com.zifisense.zeta.http.api.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CommonUtilsTest {

	@Test
	public void testBytesToHexStringSucc1() {
		byte[] beforeByte = { 77, 84, 73, 122, 78, 68, 85, 50, 78, 122, 103, 53, 89, 87, 74, 106, 90, 71, 86, 109, 77,69, 113, 118, 77, 67, 73, 76, 51, 101, 110, 115, 107, 78, 106, 83, 112, 104, 79, 102, 89, 106, 104,73 };
		String afterHex = "4d54497a4e4455324e7a673559574a6a5a47566d4d4571764d43494c33656e736b4e6a5370684f66596a6849";
		String hexString = CommonUtils.bytesToHexString(beforeByte);
		System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),Arrays.toString(beforeByte), hexString);

		assertEquals(afterHex, hexString);
	}
	public void testBytesToHexStringSucc2() {
		byte[] beforeByte = { 1 };
		String afterHex = "4d54497a4e4455324e7a673559574a6a5a47566d4d4571764d43494c33656e736b4e6a5370684f66596a6849";
		String hexString = CommonUtils.bytesToHexString(beforeByte);
		System.out.println(hexString);
		System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),Arrays.toString(beforeByte), hexString);

		assertEquals(afterHex, hexString);
	}
	@Test
	public void testBytesToHexStringFail() {
		byte[] beforeByte = null;
		String afterHex = null;
		String hexString = CommonUtils.bytesToHexString(beforeByte);
		System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),Arrays.toString(beforeByte), hexString);

		assertEquals(afterHex, hexString);
	}
	@Test
	public void testDateString2LongSucc() {
		String stringDate = "2020-08-27 14:26:00";
		long longDate = 1598509560;
		long resultLong = 0L;
		try {
			resultLong = CommonUtils.dateString2Long(stringDate);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),stringDate, resultLong);
			assertEquals(longDate, resultLong);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testDateString2LongFail() {
		String stringDate = "2020-08-27 14:26";
		long longDate = 1598509560;
		long resultLong = 0L;
		try {
			resultLong = CommonUtils.dateString2Long(stringDate);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),stringDate, resultLong);
			assertEquals(longDate, resultLong);
		} catch (Exception e) {
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),stringDate, e.getMessage());
			assertEquals(true, e.getMessage().contains("yyyy-MM-dd HH:mm:ss"));
		}
	}

	@Test
	public void convertDateToLong() {
		Date date = new Date();
		long longDate = System.currentTimeMillis() / 1000;
		long resultLong = CommonUtils.convertDateToLong(date);
		System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(), date, resultLong);

		assertEquals(longDate, resultLong);
	}

	@Test
	public void convertStringToDateSucc() {
		String stringDate = "2020-08-27 14:26:00";
		long longDate = 1598509560;
		Date resultDate = null;
		try {
			resultDate = CommonUtils.convertStringToDate(stringDate);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),stringDate, resultDate);
			assertEquals(longDate, resultDate.getTime() / 1000);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void convertStringToDateFail() {
		String stringDate = "2020/08/27 14:6:00";
		try {
			CommonUtils.convertStringToDate(stringDate);
		} catch (Exception e) {
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),stringDate, e.getMessage());
			assertEquals(true, e.getMessage().contains("yyyy-MM-dd HH:mm:ss"));
		}
	}

	@Test
	public void getRandomString() {
		int lengthInt = 16;
		String resultString = CommonUtils.getRandomString(lengthInt);
		System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),lengthInt, resultString);

		assertEquals(lengthInt, resultString.length());
	}

	@Test
	public void testJsonToMap1() {
		String beforeString = "{\"errmsg\":\"\",\"status\":0,\"ts\":1599044230}";
		Map<String, Object> resultMap = CommonUtils.jsonToMap(beforeString);
		System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),beforeString, resultMap);
		Map<String,Object> afterMap = new HashMap<>();
		afterMap.put("errmsg", "");
		afterMap.put("status", 0);
		afterMap.put("ts", 1599044230);
		assertEquals(afterMap, resultMap);
	}
	@Test
	public void testJsonToMap2() {
		String beforeString = "{\"errmsg\"=\"\",\"status\":0,\"ts\":1599044230}";
		Map<String, Object> resultMap = CommonUtils.jsonToMap(beforeString);
		System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),beforeString, resultMap);
		Map<String,Object> afterMap = new HashMap<>();
		afterMap.put("errmsg", "");
		afterMap.put("status", 0);
		afterMap.put("ts", 1599044230);
		assertEquals(afterMap, resultMap);
	}
	@Test
	public void testJsonToMapFail() {
		String beforeString = "\"errmsg\":\"\",\"status\":0,\"ts\":1599044230}";
		Map<String, Object> resultMap = CommonUtils.jsonToMap(beforeString);
		System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),beforeString, resultMap);
		Map<String,Object> afterMap = null;
		assertEquals(afterMap, resultMap);
	}

	@Test
	public void testJson2List() {
		String beforeString = "[{\"uid\":\"00000001\"},{\"uid\":\"11111154\"}]";
		List<Map<String, Object>> resultList = CommonUtils.json2List(beforeString);
		System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),beforeString, resultList);
		
		Map<String,Object> afterMap1 = new HashMap<>();
		afterMap1.put("uid", "00000001");
		Map<String,Object> afterMap2 = new HashMap<>();
		afterMap2.put("uid", "11111154");
		List<Map<String,Object>> afterList = new ArrayList<>();
		afterList.add(afterMap1);
		afterList.add(afterMap2);
		assertEquals(afterList, resultList);
	}	
	@Test
	public void string2byteSucc() {
		String beforeString = "abcd";
		byte[] afterArr = {97, 98, 99, 100};
		byte[] resultArr = {};
		try {
			resultArr = CommonUtils.string2byte(beforeString,"utf8");
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),beforeString, Arrays.toString(resultArr));
			assertArrayEquals(afterArr, resultArr);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void string2byteFail() {
		String beforeString = "abcd";
		String chartSet = "ukf";
		try {
			CommonUtils.string2byte(beforeString,chartSet);
		} catch (Exception e) {
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),chartSet, e.getMessage());
			assertEquals("The Character Encoding is not supported:"+chartSet, e.getMessage());
			return;
		}
		fail("Simulated exception, no exception reported！");
	}
	
}
