package com.dajie.message.model.job;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JobDescriptionRelationInfo {

	private int userId;
	
	private int descriptionId;
	
	private long createDate;
	
	@JsonIgnore
	private long id;
	
	private String description;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getDescriptionId() {
		return descriptionId;
	}

	public void setDescriptionId(int descriptionId) {
		this.descriptionId = descriptionId;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	
}
