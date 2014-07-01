package com.dajie.message.model.map;

import java.io.Serializable;

/**
 * @author tianyuan.zhu
 *
 *代表地图上的一个点，一个点可能是聚合的点，也可能是单个信息的点，
 *也可能是一个表示聚合的数字
 */

public class PointOnMap implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
//	/**
//	 * 如果点为3级缩放聚合点，则内涵点的实际列表
//	 * */
//	private List<BaseMapObject> baseMapObjectList;
	
	
	private String ponitInfo;
	
	
	/**
	 * 该实体是职位还是用户
	 * */
	private int objectType;
	
	/**
	 * 该点类型
	 * */
	private int pointLevel;
	
	
	/**
	 * 显示数字，list如果不为空
	 * 则为list的size
	 */
	private int objectCount;
	
	/**
	 * 区名
	 * */
	private String district;
	
	/**
	 * 城市名称
	 */
	private String city;
	
	private String url;
	
	private String iconUrl;
	
	private String shareUrl;
	
	private double longitude;
	
	private double latitude;
	
	
	/**
	 * 当前查询点距离该点距离
	 * */
	private long distance;
	
	/**
	 * 该点的地址，从百度获取
	 * */
	private String pointAddress;
	
	
	/**
	 * 从百度获取的POI信息，
	 * 仅当该点隶属于poi时才会有值
	 * */
	private String nameOfPOI;
	
	private String uidOfPOI;
	
	private int campaignId;
	
	public String getPonitInfo() {
		return ponitInfo;
	}
	public void setPonitInfo(String ponitInfo) {
		this.ponitInfo = ponitInfo;
	}
	public int getObjectType() {
		return objectType;
	}
	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}
	public int getObjectCount() {
		return objectCount;
	}
	public void setObjectCount(int objectCount) {
		this.objectCount = objectCount;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public String getPointAddress() {
		return pointAddress;
	}
	public void setPointAddress(String pointAddress) {
		this.pointAddress = pointAddress;
	}
	public String getNameOfPOI() {
		return nameOfPOI;
	}
	public void setNameOfPOI(String nameOfPOI) {
		this.nameOfPOI = nameOfPOI;
	}
	public String getUidOfPOI() {
		return uidOfPOI;
	}
	public void setUidOfPOI(String uidOfPOI) {
		this.uidOfPOI = uidOfPOI;
	}
	public long getDistance() {
		return distance;
	}
	public void setDistance(long distance) {
		this.distance = distance;
	}
	public int getPointLevel() {
		return pointLevel;
	}
	public void setPointLevel(int pointLevel) {
		this.pointLevel = pointLevel;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public int getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}
	public String getShareUrl() {
		return shareUrl;
	}
	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}
	
	

}
