package com.dajie.message.model.map.campaign;

import java.io.Serializable;

import com.dajie.message.model.map.LocationInMapObject;

public class CampaignChanceModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String _id;
	
	private int campaignId;
	
	private int userId;
	
	private LocationInMapObject location;
	
	private long expireTime;
	
	private long createTime;
	
	private double lotteryRate;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public int getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public LocationInMapObject getLocation() {
		return location;
	}

	public void setLocation(LocationInMapObject location) {
		this.location = location;
	}

	public double getLotteryRate() {
		return lotteryRate;
	}

	public void setLotteryRate(double lotteryRate) {
		this.lotteryRate = lotteryRate;
	}
	
	

}
