package com.zifisense.zeta.http.api.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.zifisense.zeta.http.api.model.ZiFiException;

/**
 * AES加解密工具类
 * 
 * @Title: com.zifisense.zeta.http.api.util.AesHelper.java
 * @Description:
 * @author huangdg
 * @date 2020年8月27日
 */
public class AesHelper {
	private AesHelper() {

	}

	/**
	 * AES/CFB8/NoPadding 加密
	 * 
	 * @param content 待加密内容
	 * @param key     加密key
	 * @param ivS     偏移量
	 * @return
	 * @throws ZiFiException
	 */
	public static String encrypt(String content, byte[] key, byte[] ivS) throws ZiFiException {
		try {
			byte[] raw = key;
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
			IvParameterSpec iv = new IvParameterSpec(ivS);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			byte[] encrypted = cipher.doFinal(content.getBytes("utf8"));
			byte[] newB = new byte[ivS.length + encrypted.length];
			System.arraycopy(ivS, 0, newB, 0, ivS.length);
			System.arraycopy(encrypted, 0, newB, ivS.length, encrypted.length);
			return Base64.encode(newB);
		} catch (Exception e) {
			throw new ZiFiException("Aes encrypt failed", e);
		}
	}

	/**
	 * AES/CFB8/NoPadding 解密
	 * 
	 * @param content 待解密内容
	 * @param key     解密key
	 * @return
	 * @throws ZiFiException
	 */
	public static String decrypt(String content, byte[] key) throws ZiFiException {
		try {
			byte[] raw = key;
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
			byte[] encrypted = Base64.decode(content);

			byte[] ivS = new byte[16];
			System.arraycopy(encrypted, 0, ivS, 0, 16);

			byte[] encrypted1 = new byte[encrypted.length - 16];
			System.arraycopy(encrypted, 16, encrypted1, 0, encrypted.length - 16);

			IvParameterSpec iv = new IvParameterSpec(ivS);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

			byte[] original = cipher.doFinal(encrypted1);
			return new String(original);
		} catch (Exception e) {
			throw new ZiFiException("Aes decrypt failed", e);
		}
	}
}
