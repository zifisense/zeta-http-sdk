package com.zifisense.zeta.http.api.model;

import java.util.List;
import java.util.Map;

/**
 * 接口应答信息
 * @Title: com.zifisense.zeta.http.api.model.ApiResponse.java
 * @Description: 
 * @author huangdg
 * @date 2020年8月27日
 */
public class ApiResponse{
	/**
	 * 接口执行，返回码
	 * -1	服务器内部错误，请稍后重试。如果提示内容包含json异常信息，一般为加解密错误和post请求中的body，加密前不满足json格式 sdk转异常500
	 *	0	请求成功，正确返回
	 *	10000	认证失败，用户名或者密码错误
	 *	10001	请求参数错误 sdk转异常400
	 *	10002	不包含access_token参数
	 *	10003	Access_token过时或者错误
	 *	10004	指令下发失败 sdk转异常404
	 */
	private Integer status;
	/**
	 * 执行错误时的错误说明
	 */
	private String errmsg;
	/**
	 * 返回时间：秒级unix时间戳
	 */
	private Long ts;
	/**
	 * 执行正确时的返回数据
	 * 查询操作：返回数据结果json集合
	 * 控制操作：返回指令的消息序号
	 */
	private List<Map<String, Object>> data;
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public Long getTs() {
		return ts;
	}
	public void setTs(Long ts) {
		this.ts = ts;
	}
	
	public List<Map<String, Object>> getData() {
		return data;
	}
	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "ApiResponse [status=" + status + ", errmsg=" + errmsg + ", ts=" + ts + ", data=" + data + "]";
	}
}
