/**
 * 
 */
package com.dajie.message.model.map;

import java.io.Serializable;



/**
 * @author tianyuan.zhu
 *
 */

public class BaseMapObject  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 搜索用主键主键
	 * */
	private String _id;
	
	
	private String name;
	
	/**
	 * 该实体是职位还是用户
	 * */
	private int objectType;
	
	/**
	 *该职位的地址 
	 */
	private String address;
	
	/**
	 * 该职位所属区域
	 * */
	private String district;
	
	/**
	 * 该职位所属城市
	 * */
	private String city;
	
	private String url;
	
	private String iconUrl;
	
	private String shareUrl;
	
	private int campaignId;
	
	private String province;
	
	private LocationInMapObject location;
	
	/**
	 * 从百度获取的POI信息，
	 * 仅当该点隶属于poi时才会有值
	 * */
	private String nameOfPOI;
	private String uidOfPOI;
	
	private long createTime;
	
	private long updateTime;
	
	private long distance;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getObjectType() {
		return objectType;
	}

	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public LocationInMapObject getLocation() {
		return location;
	}

	public void setLocation(LocationInMapObject location) {
		this.location = location;
	}

	public long getDistance() {
		return distance;
	}

	public void setDistance(long distance) {
		this.distance = distance;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
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
