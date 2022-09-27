package com.zifisense.zeta.http.api.client.v1;

import com.alibaba.fastjson2.JSON;
import com.zifisense.zeta.http.api.AbstractZifiHttpClient;
import com.zifisense.zeta.http.api.model.ZiFiException;
import com.zifisense.zeta.http.api.util.AesHelper;
import com.zifisense.zeta.http.api.util.CommonUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ZifiV1ClientUtils {

    /**
     * query参数加密，api_key、signal、access_token不做加密处理
     *
     * @param secretKey 加密密钥
     * @param queryMap  原始query参数
     * @return 加密后的Map集合
     * @throws UnsupportedEncodingException
     * @throws ZiFiException
     */
    public static Map<String, Object> encryptQueryParams(String secretKey, Map<String, Object> queryMap)
            throws ZiFiException {
        Map<String, Object> resultMap = new HashMap<>();
        Set<String> keysSet = queryMap.keySet();
        for (String key : keysSet) {
            Object valueObject = queryMap.get(key);
            // 部分参数不需要二次加密
            if ("api_key".equals(key) || "signal".equals(key) || AbstractZifiHttpClient.ACCESS_TOKEN.equals(key)) {
                resultMap.put(key, valueObject);
            } else {
                String ivS = CommonUtils.getRandomString(16);
                byte[] secretKeyArr = CommonUtils.string2byte(secretKey, AbstractZifiHttpClient.DEFAULT_ENCODE_CHARSET);
                byte[] ivsArr = CommonUtils.string2byte(ivS, AbstractZifiHttpClient.DEFAULT_ENCODE_CHARSET);
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
    public static String encryptBodyParams(String secretKey, Map<String, Object> paramsMap) throws ZiFiException {
        String jsonString = JSON.toJSONString(paramsMap);
        String ivS = CommonUtils.getRandomString(16);
        byte[] secretKeyArr = CommonUtils.string2byte(secretKey,  AbstractZifiHttpClient.DEFAULT_ENCODE_CHARSET);
        byte[] ivsArr = CommonUtils.string2byte(ivS,  AbstractZifiHttpClient.DEFAULT_ENCODE_CHARSET);
        // 避免null值进行加密
        if (paramsMap != null) {
            return AesHelper.encrypt(jsonString, secretKeyArr, ivsArr);
        }
        return "";
    }
}
