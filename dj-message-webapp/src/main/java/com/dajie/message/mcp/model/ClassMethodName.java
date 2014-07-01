package com.dajie.message.mcp.model;

public class ClassMethodName {
	private String className;
	private String methodName;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className.toLowerCase();
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName.toLowerCase();
	}
}
