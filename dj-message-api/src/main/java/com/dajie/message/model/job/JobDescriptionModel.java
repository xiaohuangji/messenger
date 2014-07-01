package com.dajie.message.model.job;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JobDescriptionModel {
	
	private int id;
	
	private String description;
	
	@JsonIgnore
	private Date createDate;
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@JsonIgnore
	private byte[] md5Description;

	public byte[] getMd5Description() {
		return md5Description;
	}

	public void setMd5Description(byte[] md5Description) {
		this.md5Description = new byte[md5Description.length];
		for(int i = 0 ; i < md5Description.length ; i++){
			this.md5Description[i] = md5Description[i];
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
