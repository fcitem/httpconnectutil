package com.fc.http;

/**请求方式枚举
 * @author fengchao
 *
 */
public enum HttpMethordEnum {

	GET("GET"),
	POST("POST");
	private String value;
	private HttpMethordEnum(String value) {
		this.value=value;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
