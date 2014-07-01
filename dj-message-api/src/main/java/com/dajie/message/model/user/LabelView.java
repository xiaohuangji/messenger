package com.dajie.message.model.user;

import java.io.Serializable;

/**
 * Created by John on 4/29/14.
 */
public class LabelView extends UserLabel implements Serializable {
	
	private static final long serialVersionUID = -3442944438245076545L;
	
	/**
	 * 赞的数量
	 */
	private int likeCounts;
	
	/**
	 * 用户是否赞过
	 */
	private int isliked;
	
	public LabelView() {
		super();
	}
	
	public LabelView(UserLabel userLabel) {
		super(userLabel);
	}

	public int getLikeCounts() {
		return likeCounts;
	}

	public void setLikeCounts(int likeCounts) {
		this.likeCounts = likeCounts;
	}

	public int getIsliked() {
		return isliked;
	}

	public void setIsliked(int isliked) {
		this.isliked = isliked;
	}

}
