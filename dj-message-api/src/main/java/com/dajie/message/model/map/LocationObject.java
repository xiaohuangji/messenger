package com.dajie.message.model.map;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	//纬度
	private double lat;
	
	//经度
	private double lng;
	
	public double getX() {
		return lng;
	}

	public void setX(double lng) {
		this.lng = lng;
	}

	public double getY() {
		return lat;
	}

	public void setY(double lat) {
		this.lat = lat;
	}

	public LocationObject(){}
	
	public LocationObject(double lat, double lng) {
		super();
		this.lat = lat;
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}
	

}
