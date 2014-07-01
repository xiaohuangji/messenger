package com.dajie.message.elasticsearch.map.constants.model;

import java.util.ArrayList;
import java.util.List;


public final class DefaultPositionConstantModel {
	
	private String name;
	
	private double lat;
	
	private double lon;
	
	private String uid;
	
	private List<DefaultPositionConstantModel> positionList;

	public DefaultPositionConstantModel(String name, double lat, double lon,String uid,
			DefaultPositionConstantModel... positionList) {
		super();
		this.name = name;
		this.lat = lat;
		this.lon = lon;
		this.uid = uid;
		if(positionList!=null&&positionList.length > 0)
		{
			this.positionList = new ArrayList<DefaultPositionConstantModel>();
			
			for(DefaultPositionConstantModel d : positionList)
			{
				this.positionList.add(d);
			}
		}
		
	}
	
	
	public DefaultPositionConstantModel(){}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public List<DefaultPositionConstantModel> getPositionList() {
		return positionList;
	}

	public void setPositionList(List<DefaultPositionConstantModel> positionList) {
		this.positionList = positionList;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public void addDefaultPositionConstantModel(DefaultPositionConstantModel d)
	{
		if(d == null)
			return;
		
		this.positionList.add(d);
	}
	
	
}
