package com.zifisense.zeta.http.api.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class SignalHelperTest {


	@Test
	public void testGetSignatureSucc() {
		String apiKeyString = "d303e92eae5543fa8a48af4814d5244a";
		String secretString = "4e0e0947ae8f435a96d398b715c5f078";
		String afterString = "8d1d64adc49520b07e2ab0fc915cff90360e45dc";
		String signAlgorithm = "HmacSHA1";
		String resulString = "";
		try {
			resulString = SignalHelper.getSignature(apiKeyString.getBytes("utf8"),secretString.getBytes("utf8"),signAlgorithm);
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),apiKeyString, resulString);
			assertEquals(afterString, resulString);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Test
	public void testGetSignatureFail() {
		String apiKeyString = "d303e92eae5543fa8a48af4814d5244a";
		String secretString = "4e0e0947ae8f435a96d398b715c5f078";
		String signAlgorithm = "HmacSHA";
		try {
			SignalHelper.getSignature(apiKeyString.getBytes("utf8"),secretString.getBytes("utf8"),signAlgorithm);
		} catch (Exception e) {
			System.out.printf("%s,before:%s,after:%s\n", Thread.currentThread().getStackTrace()[1].getMethodName(),signAlgorithm, e.getMessage());
			assertEquals("Failed to get signature,algorithm :"+signAlgorithm, e.getMessage());
			return;
		}
		fail("Simulated exception, no exception reported！");
	}
}
