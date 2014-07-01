package com.dajie.message.model.map.geocode;

import java.io.Serializable;

import com.dajie.message.model.map.LocationObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Position implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String addr;
	
	private String cp;
	
	private String distance;
	
	private String name;
	
	private String poiType;
	
	private LocationObject point;
	
	private String tel;
	
	private String uid;
	
	private String zip;

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPoiType() {
		return poiType;
	}

	public void setPoiType(String poiType) {
		this.poiType = poiType;
	}

	public LocationObject getPoint() {
		return point;
	}

	public void setPoint(LocationObject point) {
		this.point = point;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	
	
}
