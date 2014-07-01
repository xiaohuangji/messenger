package com.dajie.message.model.job;

import java.util.Date;

public class JobInformModel {

    public static final int STATUS_PENDING=0;

    public static final int STATUS_DELETED=1;

    public static final int STATUS_IGNORED=2;

	private int id;
	
	private int informUserId;
	
	private int jobId;
	
	private int jobUserId;
	
	private String description;
	
	private int status;
	
	private Date updateTime;
	
	private Date createTime;
	
	private String positionName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getInformUserId() {
		return informUserId;
	}

	public void setInformUserId(int informUserId) {
		this.informUserId = informUserId;
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public int getJobUserId() {
		return jobUserId;
	}

	public void setJobUserId(int jobUserId) {
		this.jobUserId = jobUserId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	
}
