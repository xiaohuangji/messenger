package com.dajie.message.mcp.model;

public class ParamMeta {
	private String paramName;
	private boolean nullable;
	private String defaultValue = null;

	public ParamMeta(String paramName, boolean nullable) {
		this.paramName = paramName;
		this.nullable = nullable;
	}
	
	public ParamMeta(String paramName, boolean nullable,String defaultValue){
		this.paramName = paramName;
		this.nullable = nullable;
		this.defaultValue = defaultValue;
	}

	public ParamMeta() {
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
}
