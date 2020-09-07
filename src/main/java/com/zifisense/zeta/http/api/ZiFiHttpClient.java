package com.zifisense.zeta.http.api;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.zifisense.zeta.http.api.cache.CacheFactory;
import com.zifisense.zeta.http.api.model.ApiConfig;
import com.zifisense.zeta.http.api.model.HttpMethod;
import com.zifisense.zeta.http.api.model.ZiFiException;
import com.zifisense.zeta.http.api.util.AesHelper;
import com.zifisense.zeta.http.api.util.CommonUtils;
import com.zifisense.zeta.http.api.util.HttpUtils;
import com.zifisense.zeta.http.api.util.MiscUtil;
import com.zifisense.zeta.http.api.util.SignalHelper;

import net.sf.json.JSONObject;

/**
 * Zeta http请求入口
 * 
 * @Title: com.zifisense.zeta.http.api.ZiFiHttpClient.java.java
 * @Description:
 * @author huangdg
 * @date 2020年9月1日
 */
public class ZiFiHttpClient {
	private static final String SIGN_ALGORITHM = "HmacSHA1";
	private static final String ENCODE_CHARSET = "utf8";
	private static final String ACCESS_TOKEN = "access_token";
	private static final String CACHE_ACCESS_TOKEN = "accessTokenCache";
	private static final String URL_ACCESS_TOKEN_STRING = "/teamcms/ws/auth_v1/auth_token/query/getWanAccessToken";

	private ApiConfig apiConfig;

	public ZiFiHttpClient(String apiKey, String secretKey, String hostName) {
		apiConfig = new ApiConfig(apiKey, secretKey, hostName);
	}

	/**
	 * 获取api配置信息
	 * 
	 * @return
	 */
	public ApiConfig getApiConfig() {
		return apiConfig;
	}

	/**
	 * 发送请求 返回Json
	 * 
	 * @param apiConfig
	 * @param url           除hostname外请求路径
	 * @param method        请求方式：get、post
	 * @param bodyParams    请求body参数; 对于get请求，该参数强制转null
	 * @param secondTimeOut 请求超时时间，单位s
	 * @return String Json
	 * @throws ZiFiException
	 * @throws UnsupportedEncodingException
	 */
	public String send(String url, HttpMethod method, Map<String, Object> queryParams, Map<String, Object> bodyParams,
			int secondTimeOut) throws ZiFiException, UnsupportedEncodingException {
		// 请求的uri
		String hostString = apiConfig.getHostName();
		String urlString = joinUrl(hostString, url);
		// 构建query参数
		Map<String, Object> queryMap = new HashMap<>();
		if (queryParams != null) {
			// 参数加密
			queryMap = encryptQueryParams(apiConfig.getSecretKey(), queryParams);
		}
		// 获取access_token,并且传参
		String tokenString = getToken();
		queryMap.put(ACCESS_TOKEN, tokenString);

		String secretString = apiConfig.getSecretKey();
		// get请求不传body参数
		String bodyString = HttpMethod.GET.equals(method) ? null : encryptBodyParams(secretString, bodyParams);

		String resultString = HttpUtils.send(method, urlString, null, queryMap, bodyString, secondTimeOut);
		Map<String, Object> resultMap = CommonUtils.jsonToMap(resultString);
		// token过期或者错误重新获取token,并且重试
		String responseStatuString = resultMap.get("status").toString();
		if ("10003".equals(responseStatuString)) {
			// 重新获取token
			tokenString = tokenRequest();
			queryMap.put(ACCESS_TOKEN, tokenString);
			resultString = HttpUtils.send(method, urlString, null, queryMap, bodyString, secondTimeOut);
		} else if ("-1".equals(responseStatuString)) {
			throw new ZiFiException("Service Error 500!");
		} else if ("10001".equals(responseStatuString)) {
			throw new ZiFiException("Params Error 400!");
		}
		return resultString;
	}

	private String getToken() throws ZiFiException, UnsupportedEncodingException {
		String tokenString = "";
		Object tokenObject = CacheFactory.getInstance().getValue(CACHE_ACCESS_TOKEN);
		if (tokenObject != null) {
			tokenString = tokenObject.toString();
		} else {
			tokenString = tokenRequest();
		}
		return tokenString;

	}

	public String tokenRequest() throws ZiFiException, UnsupportedEncodingException {
		Map<String, Object> queryMap = new HashMap<>();
		String apiKeyString = apiConfig.getApiKey();
		String secretKeyString = apiConfig.getSecretKey();
		String hostString = apiConfig.getHostName();
		// 请求的uri
		String urlString = joinUrl(hostString, URL_ACCESS_TOKEN_STRING);

		// 组装query参数
		queryMap.put("api_key", apiKeyString);
		byte[] apiKeyArr = CommonUtils.string2byte(apiKeyString, ENCODE_CHARSET);
		byte[] secretKeyArr = CommonUtils.string2byte(secretKeyString, ENCODE_CHARSET);
		String signalString = SignalHelper.getSignature(apiKeyArr, secretKeyArr, SIGN_ALGORITHM);
		queryMap.put("signal", signalString);

		String tokenString = "";
		String resultString = HttpUtils.send(HttpMethod.GET, urlString, null, queryMap, null, 10);
		Map<String, Object> resultMap = CommonUtils.jsonToMap(resultString);
		if ("0".equals(resultMap.get("status").toString())) {
			tokenString = CommonUtils.json2List(resultMap.get("data").toString()).get(0).get(ACCESS_TOKEN).toString();
			// 更新缓存，有效期5分钟
			CacheFactory.getInstance().addCache(CACHE_ACCESS_TOKEN, tokenString, 5 * 60);
		} else {
			throw new ZiFiException("Authentication failed!");
		}
		return tokenString;
	}

	/**
	 * uri组装
	 * 
	 * @param hostName
	 * @param url
	 * @return
	 */
	private String joinUrl(String hostName, String url) {
		String urlString = MiscUtil.urlCheckEnd(hostName) + "/";
		urlString += MiscUtil.urlCheckStart(url);
		return urlString;
	}

	/**
	 * query参数加密，api_key、signal、access_token不做加密处理
	 * 
	 * @param secretKey 加密密钥
	 * @param queryMap  原始query参数
	 * @return 加密后的Map集合
	 * @throws UnsupportedEncodingException
	 * @throws ZiFiException
	 */
	private Map<String, Object> encryptQueryParams(String secretKey, Map<String, Object> queryMap)
			throws ZiFiException, UnsupportedEncodingException {
		Map<String, Object> resultMap = new HashMap<>();
		Set<String> keysSet = queryMap.keySet();
		for (String key : keysSet) {
			Object valueObject = queryMap.get(key);
			// 部分参数不需要二次加密
			if ("api_key".equals(key) || "signal".equals(key) || ACCESS_TOKEN.equals(key)) {
				resultMap.put(key, valueObject);
			} else {
				String ivS = CommonUtils.getRandomString(16);
				byte[] secretKeyArr = CommonUtils.string2byte(secretKey, ENCODE_CHARSET);
				byte[] ivsArr = CommonUtils.string2byte(ivS, ENCODE_CHARSET);
				String encryptString = AesHelper.encrypt(valueObject.toString(), secretKeyArr, ivsArr);
				resultMap.put(key, encryptString);
			}
		}
		return resultMap;
	}

	/**
	 * body参数由Map转json后，加密
	 * 
	 * @param secretKey 加密密钥
	 * @param paramsMap body参数Map集合
	 * @return 加密后字符串
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	private String encryptBodyParams(String secretKey, Map<String, Object> paramsMap) throws ZiFiException {
		String jsonString = JSONObject.fromObject(paramsMap).toString();
		String ivS = CommonUtils.getRandomString(16);
		byte[] secretKeyArr = CommonUtils.string2byte(secretKey, ENCODE_CHARSET);
		byte[] ivsArr = CommonUtils.string2byte(ivS, ENCODE_CHARSET);
		// 避免null值进行加密
		if (paramsMap != null) {
			return AesHelper.encrypt(jsonString, secretKeyArr, ivsArr);
		}
		return "";
	}
}
