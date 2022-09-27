package com.zifisense.zeta.http.api;

import com.alibaba.fastjson2.JSONObject;
import com.zifisense.zeta.http.api.cache.CacheFactory;
import com.zifisense.zeta.http.api.model.ApiConfig;
import com.zifisense.zeta.http.api.model.ApiResponse;
import com.zifisense.zeta.http.api.model.HttpMethod;
import com.zifisense.zeta.http.api.model.ZiFiException;
import com.zifisense.zeta.http.api.util.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author PomZWJ
 * @desc abstract zeta http client
 * @date 2022-09-25
 */
public abstract class AbstractZifiHttpClient implements IZifiHttpClient {
    /**
     * 签名算法
     */
    public static final String DEFAULT_SIGN_ALGORITHM = "HmacSHA1";
    /**
     * 编码
     */
    public static final String DEFAULT_ENCODE_CHARSET = "UTF-8";

    /**
     * access_token名称定义
     */
    public static final String ACCESS_TOKEN = "access_token";

    /**
     * accessTokenCache缓存的key名+版本号
     */
    public String CACHE_ACCESS_TOKEN = "accessTokenCache";

    /**
     * 缓存超时时长
     */
    public final static int CACHE_TIME_OUT = 5 * 60;

    public final static String API_KEY_PARAM = "api_key";

    public final static String SIGNAL_PARAM = "signal";

    public final static String REQUEST_TIME_PARAM = "request_time";

    /**
     * 这个url需要format才能使用
     */
    public final static String DEFAULT_REQUEST_TOKEN_URL = "/teamcms/ws/auth_%s/auth_token/query/getWanAccessToken";
    /**
     * sdk http接口版本
     */
    public ZifiHttpVersion zifiHttpVersion = null;


    /**
     * api配置信息
     */
    private ApiConfig apiConfig;


    public AbstractZifiHttpClient(String hostName, String apiKey, String secretKey, ZifiHttpVersion zifiHttpVersion) throws Exception {
        if (StringUtils.isEmpty(hostName)) {
            throw new ZiFiException("hostName is null");
        }
        if (StringUtils.isEmpty(apiKey)) {
            throw new ZiFiException("apiKey is null");
        }
        if (StringUtils.isEmpty(secretKey)) {
            throw new ZiFiException("secretKey is null");
        }
        if (zifiHttpVersion == null) {
            throw new ZiFiException("zifiHttpVersion is null");
        }
        this.zifiHttpVersion = zifiHttpVersion;
        CACHE_ACCESS_TOKEN = CACHE_ACCESS_TOKEN + "_" + zifiHttpVersion.getVersion();
        apiConfig = new ApiConfig(apiKey, secretKey, hostName);
    }

    @Override
    public String getTokenRequest() throws ZiFiException {
        Map<String, Object> queryMap = new HashMap<>();
        if (zifiHttpVersion == ZifiHttpVersion.V1) {
            this.packageRequestV1TokenParams(queryMap);
        } else if (zifiHttpVersion == ZifiHttpVersion.V2) {
            this.packageRequestV2TokenParams(queryMap);
        } else {
            throw new ZiFiException("your customized version is not implemented getTokenRequest,need you to implement this method");
        }
        // 请求的uri
        String urlString = HttpUtils.joinUrl(apiConfig.getHostName(), String.format(DEFAULT_REQUEST_TOKEN_URL, zifiHttpVersion.getVersion()));
        String tokenString = "";
        String resultString = HttpUtils.simpleSend(HttpMethod.GET, urlString, null, queryMap);
        ApiResponse apiResponse = JSONObject.parseObject(resultString, ApiResponse.class);
        Integer status = apiResponse.getStatus();
        if (apiResponse != null && status.compareTo(0) == 0) {
            List<Map<String, Object>> data = apiResponse.getData();
            tokenString = data.get(0).get(ACCESS_TOKEN).toString();
            // 更新缓存，有效期5分钟
            CacheFactory.getInstance().addCache(CACHE_ACCESS_TOKEN, tokenString, CACHE_TIME_OUT);
        } else {
            if (apiResponse == null) {
                throw new ZiFiException("AUTHENTICATION FAILURE!");
            } else {
                throw new ZiFiException(apiResponse.getErrmsg());
            }
        }
        return tokenString;
    }

    /**
     * 包装v1请求token的参数
     *
     * @param queryMap
     * @throws ZiFiException
     */
    private void packageRequestV1TokenParams(Map<String, Object> queryMap) throws ZiFiException {
        String apiKey = apiConfig.getApiKey();
        String secretKey = apiConfig.getSecretKey();
        // 组装query参数
        queryMap.put(API_KEY_PARAM, apiKey);
        byte[] apiKeyArr = CommonUtils.string2byte(apiKey, DEFAULT_ENCODE_CHARSET);
        byte[] secretKeyArr = CommonUtils.string2byte(secretKey, DEFAULT_ENCODE_CHARSET);
        String signalString = SignalHelper.getSignature(apiKeyArr, secretKeyArr, DEFAULT_SIGN_ALGORITHM);
        queryMap.put(SIGNAL_PARAM, signalString);
    }

    /**
     * 包装v2请求token的参数
     *
     * @param queryMap
     * @throws ZiFiException
     */
    private void packageRequestV2TokenParams(Map<String, Object> queryMap) throws ZiFiException {
        String apiKey = apiConfig.getApiKey();
        String secretKey = apiConfig.getSecretKey();
        // 组装query参数
        queryMap.put(API_KEY_PARAM, apiKey);
        byte[] apiKeyArr = CommonUtils.string2byte(apiKey, DEFAULT_ENCODE_CHARSET);
        Long requestTime = System.currentTimeMillis() / 1000;
        byte[] secretKeyArr = CommonUtils.string2byte(secretKey + requestTime, DEFAULT_ENCODE_CHARSET);
        String signalString = SignalHelper.getV2Signature(apiKeyArr, secretKeyArr, DEFAULT_SIGN_ALGORITHM);
        queryMap.put(SIGNAL_PARAM, signalString);
        queryMap.put(REQUEST_TIME_PARAM, requestTime);
    }

    public String getToken() throws ZiFiException {
        String tokenString = "";
        Object tokenObject = CacheFactory.getInstance().getValue(CACHE_ACCESS_TOKEN);
        if (tokenObject != null) {
            tokenString = tokenObject.toString();
        } else {
            tokenString = getTokenRequest();
        }
        return tokenString;
    }

    public ApiConfig getApiConfig() {
        return apiConfig;
    }
}
