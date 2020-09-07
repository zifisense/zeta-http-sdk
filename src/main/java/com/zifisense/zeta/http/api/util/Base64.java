package com.zifisense.zeta.http.api.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.zifisense.zeta.http.api.model.ZiFiException;

/**
 * Base64字符串与字节码转换工具
 * 
 * @Title: com.zifisense.zeta.http.api.util.Base64.java
 * @Description:
 * @author huangdg
 * @date 2020年8月27日
 */
public class Base64 {
	private Base64() {

	}

	private static final char[] LEGALCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

	/**
	 * data[]进行编码
	 * 
	 * @param data
	 * @return String
	 */
	public static String encode(byte[] data) {
		int start = 0;
		int len = data.length;
		StringBuilder buf = new StringBuilder(data.length * 3 / 2);

		int end = len - 3;
		int i = start;
		int n = 0;

		while (i <= end) {
			int d = ((((int) data[i]) & 0x0ff) << 16) | ((((int) data[i + 1]) & 0x0ff) << 8)
					| (((int) data[i + 2]) & 0x0ff);
			buf.append(LEGALCHARS[(d >> 18) & 63]);
			buf.append(LEGALCHARS[(d >> 12) & 63]);
			buf.append(LEGALCHARS[(d >> 6) & 63]);
			buf.append(LEGALCHARS[d & 63]);
			i += 3;
			if (n++ >= 14) {
				n = 0;
				buf.append("");
			}
		}

		if (i == start + len - 2) {
			int d = ((((int) data[i]) & 0x0ff) << 16) | ((((int) data[i + 1]) & 255) << 8);
			buf.append(LEGALCHARS[(d >> 18) & 63]);
			buf.append(LEGALCHARS[(d >> 12) & 63]);
			buf.append(LEGALCHARS[(d >> 6) & 63]);
			buf.append("=");
		} else if (i == start + len - 1) {
			int d = (((int) data[i]) & 0x0ff) << 16;
			buf.append(LEGALCHARS[(d >> 18) & 63]);
			buf.append(LEGALCHARS[(d >> 12) & 63]);
			buf.append("==");
		}

		return buf.toString();
	}

	private static int decode(char c) throws ZiFiException {
		if (c >= 'A' && c <= 'Z') {
			return ((int) c) - 65;
		} else if (c >= 'a' && c <= 'z') {
			return ((int) c) - 97 + 26;
		} else if (c >= '0' && c <= '9') {
			return ((int) c) - 48 + 26 + 26;
		} else {
			switch (c) {
			case '+':
				return 62;
			case '/':
				return 63;
			case '=':
				return 0;
			default:
				throw new ZiFiException("Base64 the char encoding is not supported:" + c);
			}
		}
	}

	/**
	 * String进行解码data[]
	 * @param s
	 * @return
	 * @throws ZiFiException
	 */
	public static byte[] decode(String s) throws ZiFiException {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
			decode(s, bos);
			return bos.toByteArray();
		} catch (IOException e) {
			throw new ZiFiException("Base64 decode io error", e);
		}
	}

	private static void decode(String s, OutputStream os) throws ZiFiException, IOException {
		int i = 0;
		int len = s.length();
		while (true) {
			while (i < len && s.charAt(i) <= ' ') {
				i++;
			}
			if (i == len) {
				break;
			}
			int tri = (decode(s.charAt(i)) << 18) 
					+ (decode(s.charAt(i + 1)) << 12) 
					+ (decode(s.charAt(i + 2)) << 6)
					+ (decode(s.charAt(i + 3)));
			os.write((tri >> 16) & 255);
			if (s.charAt(i + 2) == '=') {
				break;
			}
			os.write((tri >> 8) & 255);
			if (s.charAt(i + 3) == '=') {
				break;
			}
			os.write(tri & 255);
			i += 4;
		}
	}
}