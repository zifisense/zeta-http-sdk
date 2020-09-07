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
import java.util.Map;
import javax.net.ssl.*;

import com.zifisense.zeta.http.api.model.HttpMethod;
import com.zifisense.zeta.http.api.model.ZiFiException;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 简单的http、https操作工具类
 * 
 * @Title: com.zifisense.zeta.http.api.util.HttpUtils.java
 * @Description:
 * @author huangdg
 * @date 2020年8月28日
 */
public class HttpUtils {
	private static final String HTTPS = "https";
	private static final String CONTENT_TYPE = "application/json";
	private static final String CHARSET = "utf8";

	private HttpUtils() {

	}

	/**
	 * 发送http/https请求
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
		TrustManager[] trustManagers = { new MyX509TrustManager() };
		// 初始化
		sslContext.init(null, trustManagers, new SecureRandom());
		return sslContext.getSocketFactory();

	}

	/**
	 * 证书校验类
	 * 
	 * @Title: com.zifisense.zeta.http.api.util.HttpUtils.java
	 * @Description:
	 * @author huangdg
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

}