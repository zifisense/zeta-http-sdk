package com.zifisense.zeta.http.api;

import com.zifisense.zeta.http.api.model.ApiResponse;
import com.zifisense.zeta.http.api.model.HttpMethod;
import com.zifisense.zeta.http.api.model.ZiFiException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface IZifiHttpClient {
    /**
     * 请求token
     * @return 返回access_token
     * @throws ZiFiException
     * @throws UnsupportedEncodingException
     */
    String getTokenRequest() throws ZiFiException;

    ApiResponse requestUrl(HttpMethod httpMethod,String url,Map<String,String>header, Map<String,Object> mapParams)throws ZiFiException;
}
