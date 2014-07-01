package com.dajie.message.model.campaign;

/**
 * 活动的整体定义
 * 一个活动对应一个模型
 * */
public class CampaignInfoModel {
	
	
	//活动id
	private int id;
	
	//活动名称
	private String name;
	
	//开始时间
	private long startTime;
	
	//结束时间
	private long endTime;
	
	//每次机会的有效时间
	private long expireTime;
	
	/**
	 * 多个城市,逗号隔开
	 * */
	private String cityNames;

	//基础url 要求以“/”结尾
	private String baseUrl;
	
	//分享用url
	private String shareUrl;
	
	//自定义地图图标
	private String iconUrl;
	
	//为每个用户在地图上显示的点的个数
	private int pointsCountLimit;
	
	//点是否随机出现
	private int isPointsRandom;
	
	//每人不分享状态下参加活动次数上限
	private int freeChancesCount;
	
	//每人已分享状态下参加活动次数上限
	private int extraChancesCount;
	
	//是否正在进行中
	private int isOnline;
	
	//中奖率
	private double lotteryRate;
	
	//奖品类型
	private int giftType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getCityNames() {
		return cityNames;
	}

	public void setCityNames(String cityNames) {
		this.cityNames = cityNames;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public int getPointsCountLimit() {
		return pointsCountLimit;
	}

	public void setPointsCountLimit(int pointsCountLimit) {
		this.pointsCountLimit = pointsCountLimit;
	}

	public int getIsPointsRandom() {
		return isPointsRandom;
	}

	public void setIsPointsRandom(int isPointsRandom) {
		this.isPointsRandom = isPointsRandom;
	}

	public int getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(int isOnline) {
		this.isOnline = isOnline;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public int getFreeChancesCount() {
		return freeChancesCount;
	}

	public void setFreeChancesCount(int freeChancesCount) {
		this.freeChancesCount = freeChancesCount;
	}

	public int getExtraChancesCount() {
		return extraChancesCount;
	}

	public void setExtraChancesCount(int extraChancesCount) {
		this.extraChancesCount = extraChancesCount;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public double getLotteryRate() {
		return lotteryRate;
	}

	public void setLotteryRate(double lotteryRate) {
		this.lotteryRate = lotteryRate;
	}

	public int getGiftType() {
		return giftType;
	}

	public void setGiftType(int giftType) {
		this.giftType = giftType;
	}
	

}
