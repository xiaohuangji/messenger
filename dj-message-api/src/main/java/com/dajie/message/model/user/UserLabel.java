package com.dajie.message.model.user;

import java.io.Serializable;

/**
 * Created by John on 4/29/14.
 */
public class UserLabel implements Serializable {

	private static final long serialVersionUID = -8559253612723417182L;

	/**
	 * 数据库主键
	 */
	private int id;

	/**
	 * userId
	 */
	private int userId;

	/**
	 * 标签的描述
	 */
	private String content;
	
	public UserLabel() {		
	}
	
	public UserLabel(UserLabel userLabel) {
		id = userLabel.id;
		userId = userLabel.userId;
		content = userLabel.content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
