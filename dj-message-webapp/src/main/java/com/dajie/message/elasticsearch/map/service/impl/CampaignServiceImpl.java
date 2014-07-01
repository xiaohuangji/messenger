package com.dajie.message.elasticsearch.map.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dajie.message.model.campaign.CampaignInfoModel;
import com.dajie.message.model.map.PointOnMap;
import com.dajie.message.service.ICampaignService;
import com.dajie.message.service.IPointOnMapService;

@Component("campaignServiceImpl")
public class CampaignServiceImpl implements ICampaignService {

	@Autowired
	IPointOnMapService pointOnMapService;
	
	@Override
	public List<PointOnMap> getSomeCampaignPoint(int _userId) {
		// TODO Auto-generated method stub
		return pointOnMapService.getSomeCampaignPoint(_userId);
	}

	@Override
	public int shareSuccessfullyNotify(int _userId, int campaignId) {
		// TODO Auto-generated method stub
		
		if(_userId ==0||campaignId == 0)
		{
			return -1;
		}
		
		//获取活动详情，并确定初次邀请增加的参与次数
		CampaignInfoModel campaignInfoModel = new CampaignInfoModel();
		
		campaignInfoModel.getExtraChancesCount();
		
		//获取用户的记录，判断是否已经邀请
		int isShared = 0;
		
		if(isShared == 0)
		{//初次邀请
			
			//从数据库获取用户参与活动信息，没有则创建
			
			//为用户参与信息中的参与剩余次数增加对应次数
			
			//将已分享置为1
			
			//更新最后分享时间
			
			return 1;
			
		}else
		{
			//更新最后分享时间
			return 0;
		}
	}

	public IPointOnMapService getPointOnMapService() {
		return pointOnMapService;
	}

	public void setPointOnMapService(IPointOnMapService pointOnMapService) {
		this.pointOnMapService = pointOnMapService;
	}

	
	
	
}
