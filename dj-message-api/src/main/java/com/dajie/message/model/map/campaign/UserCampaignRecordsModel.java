package com.dajie.message.model.map.campaign;

public class UserCampaignRecordsModel {
	
	private int id;
	
	private int campaignId;
	
	private int userId;
	
	private double pointsBalance;
	
	private int chancesCount;
	
	private int hadShared;
	
	private long lastJoinTime;
	
	private long lastShareTime;

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getHadShared() {
		return hadShared;
	}

	public void setHadShared(int hadShared) {
		this.hadShared = hadShared;
	}

	public long getLastJoinTime() {
		return lastJoinTime;
	}

	public void setLastJoinTime(long lastJoinTime) {
		this.lastJoinTime = lastJoinTime;
	}

	public long getLastShareTime() {
		return lastShareTime;
	}

	public void setLastShareTime(long lastShareTime) {
		this.lastShareTime = lastShareTime;
	}

	public int getChancesCount() {
		return chancesCount;
	}

	public void setChancesCount(int chancesCount) {
		this.chancesCount = chancesCount;
	}

	public double getPointsBalance() {
		return pointsBalance;
	}

	public void setPointsBalance(double pointsBalance) {
		this.pointsBalance = pointsBalance;
	}

}
