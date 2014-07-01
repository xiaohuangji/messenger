package com.dajie.message.model.map.suggestion;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SuggestionRequestObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int status;
	
	private String message;
	
	private List<SuggestionObject> result;

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

	public List<SuggestionObject> getResult() {
		return result;
	}

	public void setResult(List<SuggestionObject> result) {
		this.result = result;
	}

	

}
