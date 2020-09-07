package com.zifisense.zeta.http.api.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zifisense.zeta.http.api.model.ZiFiException;

/**
 * url参数构建
 * 
 * @Title: com.zifisense.zeta.http.api.util.MiscUtil.java
 * @Description:
 * @author huangdg
 * @date 2020年8月27日
 */
public class MiscUtil {
	private MiscUtil() {

	}

	private static final Map<String, Object> ENCODING_RULES;
	private static final Pattern PATTERN_START = Pattern.compile("^/+");
	private static final Pattern PATTERN_END = Pattern.compile("/+$");

	static {
		HashMap<String, Object> rules = new HashMap<>();
		rules.put("*", "%2A");
		rules.put("%7E", "~");
		ENCODING_RULES = Collections.unmodifiableMap(rules);
	}

	/**
	 * 去除请求地址url中末尾的"/"
	 * 
	 * @param urlString
	 * @return
	 */
	public static String urlCheckEnd(String urlString) {
		Matcher matcher = PATTERN_END.matcher(urlString);
		return matcher.replaceAll("");
	}

	/**
	 * 去除请求地址url中开头的"/"
	 * 
	 * @param urlString
	 * @return
	 */
	public static String urlCheckStart(String urlString) {
		Matcher matcher = PATTERN_START.matcher(urlString);
		return matcher.replaceAll("");

	}

	/**
	 * url特殊字符转码
	 * 
	 * @param plain
	 * @return
	 * @throws ZiFiException
	 */
	public static String encodeUrl(String plain, String chartSet) throws ZiFiException {
		String encoded = "";
		try {
			encoded = URLEncoder.encode(plain, chartSet);
		} catch (UnsupportedEncodingException e) {
			throw new ZiFiException("The Character Encoding is not supported:" + chartSet, e);
		}

		for (Map.Entry<String, Object> rule : ENCODING_RULES.entrySet()) {
			encoded = applyRule(encoded, rule.getKey(), rule.getValue().toString());
		}
		return encoded;
	}

	private static String applyRule(String encoded, String toReplace, String replacement) {
		return encoded.replaceAll(Pattern.quote(toReplace), replacement);
	}

	/**
	 * http url参数构建 特殊处理： "*" 转 "%2A" "%7E" 转 "~"
	 * 
	 * @param queryParamMap
	 * @param encode        是否需要转码
	 * @return
	 */
	public static String queryParamMap2UrlString(Map<String, Object> queryParamMap, String charSet, boolean encode)
			throws ZiFiException {
		if (queryParamMap == null) {
			return null;
		}
		StringBuilder content = new StringBuilder();
		int i = 0;
		int size = queryParamMap.size();
		for (Entry<String, Object> entry : queryParamMap.entrySet()) {
			if (entry.getValue() == null) {
				return null;
			}
			if (encode) {
				content.append(String.format("%s=%s", encodeUrl(entry.getKey(), charSet),
						encodeUrl(entry.getValue().toString(), charSet)));
			} else {
				content.append(String.format("%s=%s", entry.getKey(), entry.getValue().toString()));
			}
			i++;
			if (i == size) {
				break;
			}
			content.append("&");
		}
		return content.toString();
	}
}
