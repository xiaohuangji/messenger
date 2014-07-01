package com.dajie.message.model.map;

import java.io.Serializable;

public class RegionInfoObject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	
	private String regionCode;
	
	private String regionName;
	
	private int parentId;
	
	private int regionLevel;
	
	private int regionOrder;
	
	private double regionLat;
	
	private double regionLon;
	
	private String regionUniqueId;
	
	private String regionNameEn;
	
	private String regionShortNameEn;
	
	private long createTime;
	
	private long updateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getRegionLevel() {
		return regionLevel;
	}

	public void setRegionLevel(int regionLevel) {
		this.regionLevel = regionLevel;
	}

	public int getRegionOrder() {
		return regionOrder;
	}

	public void setRegionOrder(int regionOrder) {
		this.regionOrder = regionOrder;
	}

	public String getRegionNameEn() {
		return regionNameEn;
	}

	public void setRegionNameEn(String regionNameEn) {
		this.regionNameEn = regionNameEn;
	}

	public String getRegionShortNameEn() {
		return regionShortNameEn;
	}

	public void setRegionShortNameEn(String regionShortNameEn) {
		this.regionShortNameEn = regionShortNameEn;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public double getRegionLat() {
		return regionLat;
	}

	public void setRegionLat(double regionLat) {
		this.regionLat = regionLat;
	}

	public double getRegionLon() {
		return regionLon;
	}

	public void setRegionLon(double regionLon) {
		this.regionLon = regionLon;
	}

	public String getRegionUniqueId() {
		return regionUniqueId;
	}

	public void setRegionUniqueId(String regionUniqueId) {
		this.regionUniqueId = regionUniqueId;
	}


}
