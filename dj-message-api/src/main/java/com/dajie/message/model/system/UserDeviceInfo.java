package com.dajie.message.model.system;

import java.util.Date;

public class UserDeviceInfo {

	// 用户id
	private int userId;

	// 用户手机系统平台
	private int system;

	// 手机平台版本
	private String systemVersion;

	// 手机品牌
	private String mobileBrand;

	// 手机型号
	private String mobileModel;

	// 渠道
	private String channel;

	// 客户端版本
	private String clientVersion;

	// 手机分辨率
	private String mobileResolution;

	private Date createTime;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getSystem() {
		return system;
	}

	public void setSystem(int system) {
		this.system = system;
	}

	public String getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	public String getMobileBrand() {
		return mobileBrand;
	}

	public void setMobileBrand(String mobileBrand) {
		this.mobileBrand = mobileBrand;
	}

	public String getMobileModel() {
		return mobileModel;
	}

	public void setMobileModel(String mobileModel) {
		this.mobileModel = mobileModel;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getClientVersion() {
		return clientVersion;
	}

	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}

	public String getMobileResolution() {
		return mobileResolution;
	}

	public void setMobileResolution(String mobileResolution) {
		this.mobileResolution = mobileResolution;
	}

	public UserDeviceInfo(int userId, int system, String systemVersion,
			String mobileBrand, String mobileModel, String channel,
			String clientVersion, String mobileResolution) {
		this.userId = userId;
		this.system = system;
		this.systemVersion = systemVersion;
		this.mobileBrand = mobileBrand;
		this.mobileModel = mobileModel;
		this.channel = channel;
		this.clientVersion = clientVersion;
		this.mobileResolution = mobileResolution;
		this.createTime = new Date();
	}

    public UserDeviceInfo() {
    }

    public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
