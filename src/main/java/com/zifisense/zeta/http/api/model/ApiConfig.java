package com.zifisense.zeta.http.api.model;

/**
 * Api连接认证信息
 * @Title: com.zifisense.zeta.http.api.model.ApiConfig.java
 * @Description: 
 * @author huangdg
 * @date 2020年8月28日
 */
public class ApiConfig {
	/**
	 * api_key: 管理平台web页面->系统管理->企业管理->企业编码
	 */
	private String apiKey;
	/**
	 * secret_key: 管理平台web页面->系统管理->企业管理->企业密钥
	 */
	private String secretKey;
	/**
	 * hostname: 访问地址
	 * 如：
	 * http://127.0.0.1:25455
	 * http://abc.com
	 * https://abd.com:25455
	 * https://apc.com
	 * https://apc.com:25455
	 */
	private String hostName;
	
	public ApiConfig(String apiKey, String secretKey, String hostName) {
		this.apiKey = apiKey;
		this.secretKey = secretKey;
		this.hostName = hostName;
	}
	
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	@Override
	public String toString() {
		return "ApiConfig [apiKey=" + apiKey + ", secretKey=" + secretKey + ", hostName=" + hostName + "]";
	}
}
