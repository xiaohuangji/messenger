package com.dajie.message.model.job;

public class JobThirdPartPOI {
	  /**
     * 名称
     */
    private String name;
    /**
     * 纬度值
     */
    private float lat;

    /**
     * 经度值
     */
    private float lng;

    /**
     * poi地址信息
     */
    private String address;

    /**
     * poi电话信息
     */
    private String telephone;

    /**
     * poi的唯一标示
     */
    private String uid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLng() {
		return lng;
	}

	public void setLng(float lng) {
		this.lng = lng;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
    
    
}
