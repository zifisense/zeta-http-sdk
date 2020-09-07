package com.zifisense.zeta.http.api.model;

/**
 * 异常信息
 * 
 * @Title: com.zifisense.zeta.http.api.model.ZiFiException.java
 * @Description:
 * @author huangdg
 * @date 2020年9月4日
 */
public class ZiFiException extends Exception {
	private static final long serialVersionUID = 1L;

	public ZiFiException(String message, Throwable e) {
		super(message, e);
	}

	public ZiFiException(String message) {
		super(message);
	}
}
