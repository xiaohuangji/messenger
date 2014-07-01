package com.dajie.message.model.user;

import java.io.Serializable;

public class SchoolLabel implements Serializable {
	
	private static final long serialVersionUID = 6633580668964027718L;

	private String school;
	
	private String label;

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

}
