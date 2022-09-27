package com.zifisense.zeta.http.api.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.*;

import com.alibaba.fastjson2.JSON;
import com.zifisense.zeta.http.api.AbstractZifiHttpClient;
import com.zifisense.zeta.http.api.model.HttpMethod;
import com.zifisense.zeta.http.api.model.ZiFiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简单的http、https操作工具类
 *
 * @author huangdg
 * @Title: com.zifisense.zeta.http.api.util.HttpUtils.java
 * @Description:
 * @date 2020年8月28日
 */
public class HttpUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);
    private static final String HTTPS = "https";
    private static final String CONTENT_TYPE = "application/json";
    private static final String CHARSET = "utf8";
    //单位s
    public static final int DEFAULT_TIME_OUT = 10;

    private HttpUtils() {

    }

    /**
     * 发送http/https请求
     * @param httpMethod
     * @param urlString
     * @param header
     * @param queryMapParams
     * @param bodyMapParams
     * @return
     * @throws ZiFiException
     */
    public static String send(HttpMethod httpMethod, String urlString, Map<String, String> header,
                              Map<String, Object> queryMapParams, String bodyMapParams)throws ZiFiException{
        return send(httpMethod,urlString,header,queryMapParams,bodyMapParams,DEFAULT_TIME_OUT);
    }
    /**
     * 发送http/https请求 带超时时间
     *
     * @param httpMethod
     * @param urlString
     * @param header
     * @param queryMapParams
     * @param bodyMapParams
     * @param secondTimeOut
     * @return
     * @throws ZiFiException
     */
    public static String send(HttpMethod httpMethod, String urlString, Map<String, String> header,
                              Map<String, Object> queryMapParams, String bodyMapParams, int secondTimeOut) throws ZiFiException {
        String resultString = "";
        // 构建完整请求url
        String urlNameString = MiscUtil.urlCheckEnd(urlString);
        String queryString = queryParams(queryMapParams);
        urlNameString += queryString;
        // 发送请求
        if (urlString.contains(HTTPS)) {
            resultString = sendHttps(httpMethod, urlNameString, header, bodyMapParams, secondTimeOut * 1000);
        } else {
            resultString = sendHttp(httpMethod, urlNameString, header, bodyMapParams, secondTimeOut * 1000);
        }
        return resultString;
    }

    public static String simpleSend(HttpMethod httpMethod, String urlString, Map<String, String> header, Map<String, Object> mapParams) throws ZiFiException {
        return simpleSend(httpMethod, urlString, header, mapParams, DEFAULT_TIME_OUT);
    }

    public static String simpleSend(HttpMethod httpMethod, String urlString, Map<String, String> header,
                                    Map<String, Object> mapParams, int secondTimeOut) throws ZiFiException {
        String resultString = "";
        String bodyParams = null;
        // 构建完整请求url
        String urlNameString = MiscUtil.urlCheckEnd(urlString);
        if (HttpMethod.GET == httpMethod) {
            String queryString = queryParams(mapParams);
            urlNameString += queryString;
            mapParams.remove(AbstractZifiHttpClient.ACCESS_TOKEN);
        } else if (HttpMethod.POST == httpMethod) {
            Map<String, Object> getParams = new HashMap<>();
            getParams.put(AbstractZifiHttpClient.ACCESS_TOKEN, mapParams.get(AbstractZifiHttpClient.ACCESS_TOKEN));
            String queryString = queryParams(getParams);
            urlNameString += queryString;
            mapParams.remove(AbstractZifiHttpClient.ACCESS_TOKEN);
            bodyParams = JSON.toJSONString(mapParams);
        }
        // 发送请求
        Long startTime = System.currentTimeMillis();
        log.info("request url = {}",urlNameString);
        log.info("request http method = {}",httpMethod.toString());
        log.info("request header = {}",JSON.toJSONString(header));
        log.info("request bodyParams = {}",JSON.toJSONString(bodyParams));
        if (urlString.contains(HTTPS)) {
            resultString = sendHttps(httpMethod, urlNameString, header, bodyParams, secondTimeOut * 1000);
        } else {
            resultString = sendHttp(httpMethod, urlNameString, header, bodyParams, secondTimeOut * 1000);
        }
        Long endTime = System.currentTimeMillis();
        log.info("request time = {}(millisecond)",(endTime-startTime));
        log.info("response result = {}",resultString);
        return resultString;
    }

    /**
     * 获取url中参数的值
     *
     * @param url
     * @return
     */
    public static String getUrlParameter(String url, String name) {
        url += "&";
        String pattern = "(\\?|&){1}#{0,1}" + name + "=[a-zA-Z0-9]*(&{1})";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(url);
        if (matcher.find()) {
            return matcher.group(0).split("=")[1].replace("&", "");
        } else {
            return null;
        }
    }

    /**
     * url上的请求参数构建
     *
     * @param queryMapParams
     * @return
     * @throws ZiFiException
     */
    private static String queryParams(Map<String, Object> queryMapParams) throws ZiFiException {
        StringBuilder queryString = new StringBuilder();
        queryString.append("?");
        if (queryMapParams != null && !queryMapParams.isEmpty()) {
            String queryParam = MiscUtil.queryParamMap2UrlString(queryMapParams, CHARSET, true);
            queryString.append(queryParam);
        }
        return queryString.toString();
    }

    /**
     * 向指定 URL发送http
     *
     * @param httpMethod    请求类型
     * @param urlString     完整请求url
     * @param header
     * @param bodyString
     * @param secondTimeOut
     * @return
     * @throws ZiFiException
     */
    private static String sendHttp(HttpMethod httpMethod, String urlString, Map<String, String> header,
                                   String bodyString, int secondTimeOut) throws ZiFiException {
        HttpURLConnection connect = (HttpURLConnection) createConnection(urlString, header, secondTimeOut);
        try {
            connect.setRequestMethod(httpMethod.name());
        } catch (ProtocolException e) {
        }

        try {
            connect.connect();
        } catch (IOException e) {
            throw new ZiFiException("Http connect error", e);
        }
        return getResponse(connect, bodyString);
    }

    /**
     * https请求是在http请求的基础上加上一个ssl层
     *
     * @param httpMethod
     * @param urlString
     * @param header
     * @param bodyString
     * @param secondTimeOut
     * @return
     * @throws ZiFiException
     */
    private static String sendHttps(HttpMethod httpMethod, String urlString, Map<String, String> header,
                                    String bodyString, int secondTimeOut) throws ZiFiException {
        HttpsURLConnection connect = (HttpsURLConnection) createConnection(urlString, header, secondTimeOut);
        try {
            connect.setRequestMethod(httpMethod.name());
        } catch (ProtocolException e) {
        }
        try {
            connect.setSSLSocketFactory(getSsl());
        } catch (Exception e) {
            throw new ZiFiException("Https certificate error", e);
        }

        try {
            connect.connect();
        } catch (IOException e) {
            throw new ZiFiException("Https connect error", e);
        }
        return getResponse(connect, bodyString);
    }

    /**
     * 读写内容
     *
     * @param connect
     * @param bodyString
     * @return
     * @throws ZiFiException
     */
    private static String getResponse(URLConnection connect, String bodyString) throws ZiFiException {
        if (null != bodyString && !bodyString.isEmpty()) {
            try (OutputStream outputStream = connect.getOutputStream();) {
                outputStream.write(bodyString.getBytes(CHARSET));
            } catch (IOException e) {
                throw new ZiFiException("Http stream write error", e);
            }
        }
        StringBuilder stringBuffer = new StringBuilder();
        try (InputStream inputStream = connect.getInputStream();
             InputStreamReader streamReader = new InputStreamReader(inputStream, CHARSET);
             BufferedReader bufferedReader = new BufferedReader(streamReader);) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
        } catch (Exception e) {
            throw new ZiFiException("Http stream read error", e);
        }

        return stringBuffer.toString();
    }

    /**
     * 创建连接连接
     *
     * @param urlString
     * @param secondTimeOut
     * @return
     * @throws IOException
     * @throws ZiFiException
     */
    private static URLConnection createConnection(String urlString, Map<String, String> header, int secondTimeOut)
            throws ZiFiException {
        try {
            URL url = new URL(urlString);
            URLConnection connect = url.openConnection();
            connect.setUseCaches(false);
            connect.setDoOutput(true);
            connect.setDoInput(true);
            connect.setConnectTimeout(secondTimeOut / 2);
            connect.setReadTimeout(secondTimeOut);
            // 构建请求头
            connect.setRequestProperty("Accept-Charset", CHARSET);
            connect.setRequestProperty("accept", CONTENT_TYPE);
            connect.setRequestProperty("Content-Type", CONTENT_TYPE);
            connect.setRequestProperty("user-agent",
                    "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/10000 Safari/10000");
            if (header != null && !header.isEmpty()) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    connect.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            return connect;
        } catch (MalformedURLException e) {
            throw new ZiFiException("a malformed URL", e);
        } catch (IOException e) {
            throw new ZiFiException("Http connection io error", e);
        }
    }

    /**
     * 配置ssl请求信息
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     * @throws Exception
     */
    private static SSLSocketFactory getSsl() throws Exception {
        // 创建SSLContext
        SSLContext sslContext = SSLContext.getInstance("SSL");
        TrustManager[] trustManagers = {new MyX509TrustManager()};
        // 初始化
        sslContext.init(null, trustManagers, new SecureRandom());
        return sslContext.getSocketFactory();

    }

    /**
     * 证书校验类
     *
     * @author huangdg
     * @Title: com.zifisense.zeta.http.api.util.HttpUtils.java
     * @Description:
     * @date 2020年9月4日
     */
    private static class MyX509TrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // 信任所有

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // 信任所有

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

    }
    /**
     * uri组装
     *
     * @param hostName
     * @param url
     * @return
     */
    public static String joinUrl(String hostName, String url) {
        String urlString = MiscUtil.urlCheckEnd(hostName) + "/";
        urlString += MiscUtil.urlCheckStart(url);
        return urlString;
    }
}