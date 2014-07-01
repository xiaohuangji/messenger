package com.dajie.message.mcp.model;

import java.util.ArrayList;
import java.util.List;


public class ParamParse {
	private ErrorModel error;
	private List<Object> objs = new ArrayList<Object>();

	public ErrorModel getError() {
		return error;
	}

	public void setError(ErrorModel error) {
		this.error = error;
	}

	public List<Object> getObjs() {
		return objs;
	}

	public void setObjs(List<Object> objs) {
		this.objs = objs;
	}
}
