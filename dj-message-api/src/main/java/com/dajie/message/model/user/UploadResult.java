package com.dajie.message.model.user;

import java.io.Serializable;

public class UploadResult implements Serializable {

    private static final long serialVersionUID = -6981692631121804763L;

	/**
	 * 原始文件地址
	 */
	private String fileUrl;
	
	/**
	 * 聊天窗口 需要返回小图
	 */
	private String smallImgUrl;

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getSmallImgUrl() {
		return smallImgUrl;
	}

	public void setSmallImgUrl(String smallImgUrl) {
		this.smallImgUrl = smallImgUrl;
	}
		
}
