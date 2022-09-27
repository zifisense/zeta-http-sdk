package com.zifisense.zeta.http.api.client.v2;

import com.alibaba.fastjson2.JSONObject;
import com.zifisense.zeta.http.api.AbstractZifiHttpClient;
import com.zifisense.zeta.http.api.model.*;
import com.zifisense.zeta.http.api.util.*;

import java.util.HashMap;
import java.util.Map;

public class ZifiV2HttpClient extends AbstractZifiHttpClient {

    public ZifiV2HttpClient(String hostName, String apiKey, String secretKey) throws Exception {
        super(hostName, apiKey, secretKey, ZifiHttpVersion.V2);
    }

    /**
     * V2已经在父类中实现
     *
     * @return
     * @throws ZiFiException
     */
    @Override
    public String getTokenRequest() throws ZiFiException {
        return super.getTokenRequest();
    }

    @Override
    public ApiResponse requestUrl(HttpMethod httpMethod, String url, Map<String, String> header, Map<String, Object> mapParams) throws ZiFiException {
        if (mapParams == null) {
            mapParams = new HashMap<>();
        }
        ApiConfig apiConfig = getApiConfig();
        url = HttpUtils.joinUrl(MiscUtil.urlCheckEnd(apiConfig.getHostName()), url);
        String accessTokenValue = HttpUtils.getUrlParameter(url, ACCESS_TOKEN);
        if(StringUtils.isEmpty(accessTokenValue) && !mapParams.containsKey(ACCESS_TOKEN)){
            mapParams.put(ACCESS_TOKEN, getToken());
        }
        String resultStr = HttpUtils.simpleSend(httpMethod, url, header, mapParams);
        if (StringUtils.isEmpty(resultStr)) {
            throw new ZiFiException("response content is empty");
        }
        ApiResponse apiResponse = JSONObject.parseObject(resultStr, ApiResponse.class);
        if ("0".equals(apiResponse.getStatus())) {
            return apiResponse;
        } else if ("10003".equals(apiResponse.getStatus())) {
            // token过期,需要重新获取token
            mapParams.put(ACCESS_TOKEN, getTokenRequest());
            resultStr = HttpUtils.simpleSend(httpMethod, url, header, mapParams);
            apiResponse = JSONObject.parseObject(resultStr, ApiResponse.class);
        }
        return apiResponse;
    }
}
