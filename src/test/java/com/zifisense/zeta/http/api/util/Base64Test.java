package com.zifisense.zeta.http.api.util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class Base64Test {
	private final static byte[] BEFORE_BYTE = { 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 43, 47 };
	private final static String AFTER_STRING = "MTIzNDU2Nzg5MGFiY2RlZmdoaWprbG1ub3BxcnN0dXZ3eHl6QUJDREVGR0hJSktMTU5PUFFSU1RVVldYWVorLw==";
	private final static byte[] BEFORE_BYTE2= { 0,49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 43, 47 };
	private final static String AFTER_STRING2 = "ADEyMzQ1Njc4OTBhYmNkZWZnaGlqa2xtbm9wcXJzdHV2d3h5ekFCQ0RFRkdISUpLTE1OT1BRUlNUVVZXWFlaKy8=";
	private final static byte[] BEFORE_BYTE3= { 62,62,127,61,62,62,62,62,62,50, 55, 52, 53, 54, 55, 56, 57, 48, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 43, 47 };
	private final static String AFTER_STRING3 = "Pj5/PT4+Pj4+Mjc0NTY3ODkwYWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4eXpBQkNERUZHSElKS0xNTk9QUVJTVFVWV1hZWisv";
	private final static byte[] BEFORE_BYTE4= {};
	private final static String AFTER_STRING4 = "AAECAwQFBgcICQoLDA0ODxAREhMUFRYXGBkaGxwdHh8gISIjJCUmJygpKissLS4vMDEyMzQ1Njc4OTo7PD0+P0BBQkNERUZHSElKS0xNTk9QUVJTVFVWV1hZWltcXV5fYGFiY2RlZmdoaWprbG1ub3BxcnN0dXZ3eHl6e3x9fn8=";
	@Test
	public void testEncodeSucc1() {
		String enString = "";
		enString = Base64.encode(BEFORE_BYTE);
		assertEquals(AFTER_STRING, enString);
	}
	@Test
	public void testEncodeSucc2() {
		String enString = "";
		enString = Base64.encode(BEFORE_BYTE2);
		System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),Arrays.toString(BEFORE_BYTE2), enString);
		assertEquals(AFTER_STRING2, enString);
	}
	@Test
	public void testEncodeSucc3() {
		String enString = "";
		enString = Base64.encode(BEFORE_BYTE3);
		System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),Arrays.toString(BEFORE_BYTE3), enString);
		assertEquals(AFTER_STRING3, enString);
	}
	@Test
	public void testEncodeSucc4() {
		String enString = "";
		byte[] before = new byte[128];
		for (int i = 0; i < before.length; i++) {
			before[i] = (byte)i;
		}
		
		enString = Base64.encode(before);
		System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),Arrays.toString(before), enString);
		assertEquals(AFTER_STRING4, enString);
	}

	@Test
	public void testDecodeSucc1() {
		byte[] deString = {};
		try {
			deString = Base64.decode(AFTER_STRING);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),AFTER_STRING, Arrays.toString(deString));
			assertArrayEquals(BEFORE_BYTE, deString);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Test
	public void testDecodeSucc2() {
		byte[] deString = {};
		try {
			deString = Base64.decode(AFTER_STRING2);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),AFTER_STRING2, Arrays.toString(deString));
			assertArrayEquals(BEFORE_BYTE2, deString);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Test
	public void testDecodeSucc3() {
		byte[] deString = {};
		try {
			deString = Base64.decode(AFTER_STRING3);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),AFTER_STRING3, Arrays.toString(deString));
			assertArrayEquals(BEFORE_BYTE3, deString);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Test
	public void testDecodeFail1() {
		String errString = "*";
		try {
			Base64.decode(errString+AFTER_STRING);
		}  catch (Exception e) {
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),errString, e.getMessage());
			assertEquals("Base64 the char encoding is not supported:"+ errString, e.getMessage());
			return;
		}
		fail("Simulated exception, no exception reported！");
	}
}
