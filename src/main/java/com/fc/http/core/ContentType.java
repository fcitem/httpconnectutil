package com.fc.http.core;

public enum ContentType {

	APPLICATION_JSON("application/json;charset=utf-8"),
	APPLICATION_XML("application/x-javascript text/xml;charset=utf-8"),
	APPLICATION_FORM("application/x-www-form-urlencoded;charset=utf-8");
	private String value;
	private ContentType(String value) {
		this.value=value;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
