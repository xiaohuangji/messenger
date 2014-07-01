package com.dajie.message.model.map.geocode;

import java.io.Serializable;
import java.util.List;

import com.dajie.message.model.map.LocationObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoCodingObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LocationObject location;
	
	private String formatted_address;
	
	private String business;
	
	private AddressComponent addressComponent;
	
	private List<Position> pois;
	
	private int cityCode;

	public LocationObject getLocation() {
		return location;
	}

	public void setLocation(LocationObject location) {
		this.location = location;
	}

	public String getFormatted_address() {
		return formatted_address;
	}

	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public AddressComponent getAddressComponent() {
		return addressComponent;
	}

	public void setAddressComponent(AddressComponent addressComponent) {
		this.addressComponent = addressComponent;
	}

	public List<Position> getPois() {
		return pois;
	}

	public void setPois(List<Position> pois) {
		this.pois = pois;
	}

	public int getCityCode() {
		return cityCode;
	}

	public void setCityCode(int cityCode) {
		this.cityCode = cityCode;
	}



}
