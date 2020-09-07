package com.zifisense.zeta.http.api.util;

import javax.crypto.Mac;  
import javax.crypto.spec.SecretKeySpec;

import com.zifisense.zeta.http.api.model.ZiFiException; 

/**
 * HMACSHA1签名工具类
 * @Title: com.zifisense.zeta.http.api.util.HmacShaHelper.java
 * @Description: 
 * @author huangdg
 * @date 2020年8月28日
 */
public class SignalHelper {
	private SignalHelper() {
		
	}
	/**	
	 * 生成签名数据
	 * 
	 * @param data 待加密的数据
	 * @param key  加密使用的key
	 * @return 生成十六进制字符串 
	 * @throws ZiFiException
	 */
	public static String getSignature(byte[] data, byte[] key, String algorithm) throws ZiFiException {
		try {
			SecretKeySpec signingKey = new SecretKeySpec(key, algorithm);
			Mac mac = Mac.getInstance(algorithm);
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(data);
			return CommonUtils.bytesToHexString(rawHmac);
		} catch (Exception e) {
			throw new ZiFiException("Failed to get signature,algorithm :" + algorithm,e);
		} 
	}

}
