package com.dajie.message.elasticsearch.map.utils.elasticsearch;

import java.util.Map;

public class ScriptModel {
	
	public static final String DEFAULT_NAME = "distance";
	
	public static final String DEFAULT_SCRIPT = "doc['location'].distance(param_lat,param_lon)";
	
	public static final String DEFAULT_PARAM_LAT = "param_lat";
	
	public static final String DEFAULT_PARAM_LON = "param_lon";
	
	private String name;
	
	private String script;
	
	private Map<String,Object> params;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
}
