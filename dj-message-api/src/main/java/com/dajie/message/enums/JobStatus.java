package com.dajie.message.enums;

public enum JobStatus implements CodedEnum{
	/**
	 * 一个正常的job
	 */
	JOB_OK(0),
	/**
	 * 一个被举报的job
	 */
	JOB_INFROM(1),
	
	/**
	 * 一个被审核后台确认非法的job
	 */
	JOB_VERIFY_ERR(2);
	
	
	private int code;
	private JobStatus(int code) {
		this.code = code;
	}
	
	@Override
	public int getCode() {
		return code;
	}

}
