package com.dajie.message.model.corp;

import java.io.Serializable;

/**
 * Created by John on 5/4/14.
 */
public class SimpleCorpBase implements Serializable{
	
	private static final long serialVersionUID = -2721521923672607370L;

	/**
	 * 公司id
	 */
	private int corpId;
	
	/**
	 * 公司名称
	 */
	private String corpName;

	public int getCorpId() {
		return corpId;
	}

	public void setCorpId(int corpId) {
		this.corpId = corpId;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

}
