package com.dajie.message.mcp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MCPResponse {

	private int code;
	
	private Object ret;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getRet() {
		return ret;
	}

	public void setRet(Object ret) {
		this.ret = ret;
	}
	
}
