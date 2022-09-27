package com.zifisense.zeta.http.api;

import com.zifisense.zeta.http.api.client.v1.ZifiV1HttpClient;
import com.zifisense.zeta.http.api.client.v2.ZifiV2HttpClient;
import com.zifisense.zeta.http.api.model.ZiFiException;
import com.zifisense.zeta.http.api.util.ZifiHttpVersion;


/**
 * @author PomZWJ
 * @desc This is new zifisense zeta sdk http client
 * @date 2022-09-25
 */
public class ZifiHttpClientFactory {
    public static IZifiHttpClient getClient(String hostName, String apiKey, String secretKey)throws Exception{
        return getClient(hostName,apiKey,secretKey,ZifiHttpVersion.V2);
    }
    public static IZifiHttpClient getClient(String hostName, String apiKey, String secretKey,ZifiHttpVersion version)throws Exception{
        if(version.equals(ZifiHttpVersion.V1)){
            return new ZifiV1HttpClient(hostName,apiKey,secretKey);
        }else if(version.equals(ZifiHttpVersion.V2)){
            return new ZifiV2HttpClient(hostName,apiKey,secretKey);
        }
        throw new ZiFiException("zifiHttpVersion is not support");
    }
}
