package com.zifisense.zeta.http.api;

import com.alibaba.fastjson2.JSON;
import com.zifisense.zeta.http.api.model.ApiResponse;
import com.zifisense.zeta.http.api.model.HttpMethod;
import com.zifisense.zeta.http.api.util.ZifiHttpVersion;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ZifiNewHttpClientTest {
    private static final Logger log = LoggerFactory.getLogger(ZifiNewHttpClientTest.class);
    private static final String HOST_NAME = "";
    private static final String API_KEY = "";
    private static final String SECRET_KEY = "";

    @Test
    public void testV2()throws Exception{

        log.info("####################");
        log.info("#  终端API测试 V2    #");
        log.info("####################");
        //定义一个终端设备ID
        //String msDeviceId = "11110009";
        String msDeviceId = "70000001";
        Map<String,Object> params = new HashMap<>();
        Map<String,String>header = new HashMap<>();
        header.put("Content-Type","application/json");

        log.info("----------获取设备列表-------------");
        IZifiHttpClient client = ZifiHttpClientFactory.getClient(HOST_NAME, API_KEY, SECRET_KEY);

        String uri = String.format("/teamcms/ws/zeta_v2/wan_ms/query/%s/getMsList",API_KEY);
        ApiResponse apiResponse = client.requestUrl(HttpMethod.GET, uri, header, params);
        log.info(JSON.toJSONString(apiResponse));

        log.info("----------根据时间获取上行数据-------------");
        uri = String.format("/teamcms/ws/zeta_v2/wan_ms/query/%s/getMsCtlHistoryByDate",msDeviceId);
        params.clear();
        params.put("starttime","1659283200");
        params.put("endtime","1661875200");
        apiResponse = client.requestUrl(HttpMethod.POST,uri,header,params);
        log.info(JSON.toJSONString(apiResponse));

        log.info("----------获取设备详情-------------");
        uri = String.format("/teamcms/ws/zeta_v2/wan_ms/query/%s/getMsDetail",API_KEY);
        apiResponse = client.requestUrl(HttpMethod.GET, uri, header, params);
        log.info(JSON.toJSONString(apiResponse));


        log.info("----------获取设备状态-------------");
        uri = String.format("/teamcms/ws/zeta_v2/wan_ms/query/%s/getMsStatus",API_KEY);
        apiResponse = client.requestUrl(HttpMethod.GET, uri, header, params);
        log.info(JSON.toJSONString(apiResponse));

        log.info("----------根据时间获取心跳数据-------------");
        uri = String.format("/teamcms/ws/zeta_v2/wan_ms/query/%s/getMsHeartBeatByDate",msDeviceId);
        params.clear();
        params.put("starttime","1656604800");
        params.put("endtime","1659196800");
        apiResponse = client.requestUrl(HttpMethod.POST,uri,header,params);
        log.info(JSON.toJSONString(apiResponse));


        log.info("----------根据时间应用上行数据-------------");
        uri = String.format("/teamcms/ws/zeta_v2/wan_ms/query/%s/getAppDataByDate",msDeviceId);
        params.clear();
        params.put("starttime","1661961600");
        params.put("endtime","1664467200");
        apiResponse = client.requestUrl(HttpMethod.POST,uri,header,params);
        log.info(JSON.toJSONString(apiResponse));


    }

    /**
     * v1版本的api测试已不再维护,不推荐使用,推荐使用testV2
     * @throws Exception
     */
    @Test
    public void testV1()throws Exception{
        log.info("####################");
        log.info("#  终端API测试 V1    #");
        log.info("####################");
        Map<String,Object> params = new HashMap<>();
        Map<String,String>header = new HashMap<>();
        header.put("Content-Type","application/json");

        String msDeviceId = "11110009";

        String uri = String.format("/teamcms/ws/zeta_v1/wan_ms/query/%s/getMsList",API_KEY);
        IZifiHttpClient client = ZifiHttpClientFactory.getClient(HOST_NAME, API_KEY, SECRET_KEY, ZifiHttpVersion.V1);
        ApiResponse apiResponse = client.requestUrl(HttpMethod.GET, uri, null, null);
        log.info(JSON.toJSONString(apiResponse));

        log.info("----------根据时间获取上行数据-------------");
        uri = String.format("/teamcms/ws/zeta_v1/wan_ms/query/%s/getMsCtlHistoryByDate",msDeviceId);
        params.clear();
        params.put("starttime","1659283200");
        params.put("endtime","1661875200");
        apiResponse = client.requestUrl(HttpMethod.POST,uri,header,params);
        log.info(JSON.toJSONString(apiResponse));

        log.info("----------获取设备详情-------------");
        uri = String.format("/teamcms/ws/zeta_v1/wan_ms/query/%s/getMsDetail",API_KEY);
        apiResponse = client.requestUrl(HttpMethod.GET, uri, header, params);
        log.info(JSON.toJSONString(apiResponse));


        log.info("----------获取设备状态-------------");
        uri = String.format("/teamcms/ws/zeta_v1/wan_ms/query/%s/getMsStatus",API_KEY);
        apiResponse = client.requestUrl(HttpMethod.GET, uri, header, params);
        log.info(JSON.toJSONString(apiResponse));

        log.info("----------根据时间获取心跳数据-------------");
        uri = String.format("/teamcms/ws/zeta_v1/wan_ms/query/%s/getMsHeartBeatByDate",msDeviceId);
        params.clear();
        params.put("starttime","1656604800");
        params.put("endtime","1659196800");
        apiResponse = client.requestUrl(HttpMethod.POST,uri,header,params);
        log.info(JSON.toJSONString(apiResponse));
    }
}
