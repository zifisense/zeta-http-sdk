package com.zifisense.zeta.http.api.client.v1;

import com.alibaba.fastjson2.JSONObject;
import com.zifisense.zeta.http.api.AbstractZifiHttpClient;
import com.zifisense.zeta.http.api.model.ApiConfig;
import com.zifisense.zeta.http.api.model.ApiResponse;
import com.zifisense.zeta.http.api.model.HttpMethod;
import com.zifisense.zeta.http.api.model.ZiFiException;
import com.zifisense.zeta.http.api.util.HttpUtils;
import com.zifisense.zeta.http.api.util.MiscUtil;
import com.zifisense.zeta.http.api.util.ZifiHttpVersion;

import java.util.HashMap;
import java.util.Map;

public class ZifiV1HttpClient extends AbstractZifiHttpClient {

    public ZifiV1HttpClient(String hostName, String apiKey, String secretKey)throws Exception {
        super(hostName, apiKey, secretKey,ZifiHttpVersion.V1);
    }

    /**
     * V1已经在父类中实现
     * @return
     * @throws ZiFiException
     */
    @Override
    public String getTokenRequest() throws ZiFiException {
        return super.getTokenRequest();
    }

    @Override
    public ApiResponse requestUrl(HttpMethod httpMethod, String url, Map<String, String> header, Map<String, Object> mapParams) throws ZiFiException {
        if(mapParams == null){
            mapParams = new HashMap<>();
        }
        // 请求的uri
        ApiConfig apiConfig = getApiConfig();
        String hostString = apiConfig.getHostName();
        String urlString = HttpUtils.joinUrl(MiscUtil.urlCheckEnd(hostString), url);
        // 构建query参数
        mapParams.remove(ACCESS_TOKEN);
        Map<String, Object> queryMap = new HashMap<>();
        if (mapParams != null) {
            // 参数加密
            queryMap = ZifiV1ClientUtils.encryptQueryParams(apiConfig.getSecretKey(), mapParams);
        }
        // 获取access_token,并且传参
        String tokenString = getToken();
        queryMap.put(ACCESS_TOKEN, tokenString);
        String secretString = apiConfig.getSecretKey();
        // get请求不传body参数
        String bodyString = HttpMethod.GET.equals(httpMethod) ? null : ZifiV1ClientUtils.encryptBodyParams(secretString, mapParams);

        String resultString = HttpUtils.send(httpMethod, urlString, null, queryMap, bodyString);
        ApiResponse apiResponse = JSONObject.parseObject(resultString, ApiResponse.class);
        if ("10003".equals(apiResponse.getStatus())) {
            // 重新获取token
            tokenString = getTokenRequest();
            queryMap.put(ACCESS_TOKEN, tokenString);
            resultString = HttpUtils.send(httpMethod, urlString, null, queryMap, bodyString);
            return apiResponse = JSONObject.parseObject(resultString, ApiResponse.class);
        } else {
            return apiResponse;
        }
    }
}
