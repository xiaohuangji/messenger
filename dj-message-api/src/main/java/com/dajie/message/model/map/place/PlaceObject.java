package com.dajie.message.model.map.place;

import java.io.Serializable;

import com.dajie.message.model.map.LocationObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private String address;
	
	private String street_id;
	
	private String telephone;
	
	private String uid;
	
	private LocationObject location;
	
	private PlaceDetailObject detail_info;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStreet_id() {
		return street_id;
	}

	public void setStreet_id(String street_id) {
		this.street_id = street_id;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public LocationObject getLocation() {
		return location;
	}

	public void setLocation(LocationObject location) {
		this.location = location;
	}

	public PlaceDetailObject getDetail_info() {
		return detail_info;
	}

	public void setDetail_info(PlaceDetailObject detail_info) {
		this.detail_info = detail_info;
	}
	

}
