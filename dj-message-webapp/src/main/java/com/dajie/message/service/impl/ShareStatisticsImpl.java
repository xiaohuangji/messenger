package com.dajie.message.service.impl;

import org.springframework.stereotype.Component;

import com.dajie.message.service.IShareStatistics;

@Component("shareStatistics")
public class ShareStatisticsImpl implements IShareStatistics{

	@Override
	public int share(int _userId, int shareType, int id, int social) {
		return 0;
	}

}
