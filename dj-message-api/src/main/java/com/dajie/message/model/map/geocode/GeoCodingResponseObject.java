package com.dajie.message.model.map.geocode;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoCodingResponseObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int status;
	
	private GeoCodingObject result;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public GeoCodingObject getResult() {
		return result;
	}

	public void setResult(GeoCodingObject result) {
		this.result = result;
	}
	
	

}
