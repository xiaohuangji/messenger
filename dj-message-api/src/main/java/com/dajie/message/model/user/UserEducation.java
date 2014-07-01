package com.dajie.message.model.user;

import java.io.Serializable;

/**
 * Created by John on 4/15/14.
 */
public class UserEducation implements Serializable {

	private static final long serialVersionUID = -3351618948724895036L;

	/**
	 * userId
	 */
	private int userId;
	
	/**
	 * 专业
	 */
	private String major;
	
	/**
	 * 学校
	 */
	private String school;
	
	/**
	 * 学校标签
	 */
	private String label;
	
	/**
	 * 学位
	 */
	private int degree;
	
	public UserEducation() {
	}

	public UserEducation(int userId, String major, String school, String label, int degree) {
		this.userId = userId;
		this.major = major;
		this.school = school;
		this.label = label;
		this.degree = degree;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

}
