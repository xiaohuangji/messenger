package com.dajie.message.model.map.place;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceRequestObject implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int status;
	
	private String message;
	
	private int total;
	
	private List<PlaceObject> results;
	
	
	//仅当使用uid获取时才使用
	private PlaceObject result;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<PlaceObject> getResults() {
		return results;
	}

	public void setResults(List<PlaceObject> results) {
		this.results = results;
	}

	public PlaceObject getResult() {
		return result;
	}

	public void setResult(PlaceObject result) {
		this.result = result;
	}

	
}
