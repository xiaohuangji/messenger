/**
 * 
 */
package com.dajie.message.service;

import java.util.List;

import com.dajie.message.annotation.rest.RestBean;
import com.dajie.message.model.map.PointOnMap;

/**
 * @author tianyuan.zhu
 *
 */
@RestBean
public interface ICampaignService {
	
	
	public List<PointOnMap> getSomeCampaignPoint(int _userId);
	
	public int shareSuccessfullyNotify(int _userId,int campaignId);

}
