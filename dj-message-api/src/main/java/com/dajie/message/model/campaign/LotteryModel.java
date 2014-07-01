package com.dajie.message.model.campaign;

import java.io.Serializable;

public class LotteryModel implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int type;
	
	private int isUsed;
	
	private String name;
	
	private String description;
	
	private String exchangeUrl;
	
	private long expireTime;
	
	private double amount;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExchangeUrl() {
		return exchangeUrl;
	}

	public void setExchangeUrl(String exchangeUrl) {
		this.exchangeUrl = exchangeUrl;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(int isUsed) {
		this.isUsed = isUsed;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
