package com.dajie.message.service;

import com.dajie.message.annotation.rest.RestBean;

@RestBean
public interface IShareStatistics {

	public int share(int _userId,int shareType,int id,int social);
	
}
