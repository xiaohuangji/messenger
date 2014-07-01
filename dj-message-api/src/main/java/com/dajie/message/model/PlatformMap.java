package com.dajie.message.model;

import java.io.Serializable;
import java.util.Date;

public class PlatformMap implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3851604763123099965L;

	// 自增id
	private int mapId;

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	// message帐号
	private int userId;

	// 第三方平台类别
	private int PlatformType;

	// 第三方平台帐号
	private String PlatformUid;

	// OAuth的accessToken
	private String accessToken;

	// OaAuth的secretToken
	private String secretToken;

	// 认证类型
	private int tokenType;

	// 来源，如wap,app
	private String source;

	// 创建时间
	private Date createTime;

	// 接入状态
	private int status;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getPlatformType() {
		return PlatformType;
	}

	public void setPlatformType(int platformType) {
		PlatformType = platformType;
	}

	public String getPlatformUid() {
		return PlatformUid;
	}

	public void setPlatformUid(String platformUid) {
		PlatformUid = platformUid;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getSecretToken() {
		return secretToken;
	}

	public void setSecretToken(String secretToken) {
		this.secretToken = secretToken;
	}

	public int getTokenType() {
		return tokenType;
	}

	public void setTokenType(int tokenType) {
		this.tokenType = tokenType;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
