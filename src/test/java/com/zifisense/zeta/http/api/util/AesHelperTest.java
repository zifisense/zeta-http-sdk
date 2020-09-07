package com.zifisense.zeta.http.api.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class AesHelperTest {
	private final static String SECRET_KEY_STRING = "4e0e0947ae8f435a96d398b715c5f078";
	private final static String BEFORE_STRING = "{\"data\":\"889954\"}";
	private final static String IV_STRING = "123456789abcdef0";
	private final static String AFTER_STRING = "MTIzNDU2Nzg5YWJjZGVmMEqvMCIL3enskNjSphOfYjhI";

	@Test
	public void testEncryptSucc() {
		String enString = "";
		try {
			enString = AesHelper.encrypt(BEFORE_STRING, SECRET_KEY_STRING.getBytes("utf8"), IV_STRING.getBytes("utf8"));
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),BEFORE_STRING, enString);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		assertEquals(AFTER_STRING, enString);
	}
	@Test
	public void testEncryptFail() {
		String keyString = "abc";
		try {
			AesHelper.encrypt(BEFORE_STRING, SECRET_KEY_STRING.getBytes("utf8"), keyString.getBytes("utf8"));
		} catch (Exception e) {
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),keyString, e.getMessage());
			assertEquals("Aes encrypt failed", e.getMessage());
			return;
		}
		fail("Simulated exception, no exception reported！");
	}

	@Test
	public void testDecryptSucc() {
		String deString = "";
		try {
			deString = AesHelper.decrypt(AFTER_STRING, SECRET_KEY_STRING.getBytes("utf8"));
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),AFTER_STRING, deString);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		assertEquals(BEFORE_STRING, deString);
	}
	@Test
	public void testDecryptFail() {
		String keyString = "abc";
		try {
			AesHelper.decrypt(AFTER_STRING, keyString.getBytes("utf8"));
		} catch (Exception e) {
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),keyString, e.getMessage());
			assertEquals("Aes decrypt failed", e.getMessage());
			return;
		}
		fail("Simulated exception, no exception reported！");
	}
}
