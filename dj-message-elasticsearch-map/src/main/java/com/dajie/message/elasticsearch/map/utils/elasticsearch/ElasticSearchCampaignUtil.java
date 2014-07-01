package com.dajie.message.elasticsearch.map.utils.elasticsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.dajie.message.elasticsearch.map.constants.MapObjectConstants;
import com.dajie.message.elasticsearch.map.utils.LoggerManager;
import com.dajie.message.elasticsearch.map.utils.baidu.BaiduHttpRequestUtil;
import com.dajie.message.model.campaign.CampaignInfoModel;
import com.dajie.message.model.map.BaseMapObject;
import com.dajie.message.model.map.LocationInMapObject;
import com.dajie.message.model.map.campaign.CampaignChanceModel;
import com.dajie.message.model.map.geocode.AddressComponent;
import com.dajie.message.model.map.geocode.GeoCodingObject;
import com.dajie.message.model.map.geocode.GeoCodingResponseObject;
import com.dajie.message.model.map.geocode.Position;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ElasticSearchCampaignUtil {
	
	private static Logger logger = LoggerManager.getLogger(ElasticSearchCampaignUtil.class);
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	public static List<BaseMapObject> getSomeCampainPoint(double lat,double lon,int userId,CampaignInfoModel mapCampaignInfoModel)
	{
		if(userId == 0||mapCampaignInfoModel == null||
				(lat ==0&&lon == 0)||mapCampaignInfoModel.getIsOnline()<=0||
				mapCampaignInfoModel.getEndTime()<System.currentTimeMillis())
		{
			logger.error("userId == 0||mapCampaignInfoModel == null||"+
				"(lat ==0&&lon == 0)||mapCampaignInfoModel.getIsOnline()<=0||"+
				"mapCampaignInfoModel.getEndTime()<System.currentTimeMillis()");
			
			return null;
		}
		
		//获取活动举办城市
		List<String> cityNames = null;
		if(StringUtils.isNotEmpty(mapCampaignInfoModel.getCityNames()))
		{
			cityNames = Arrays.asList(mapCampaignInfoModel.getCityNames().split(","));
			if(cityNames == null || cityNames.size() == 0) 
			{
				logger.error("cityNames is null in mapCampaignInfoModel");
				return null;
			}
		}
		 
		
		List<BaseMapObject> resultList = new ArrayList<BaseMapObject>();
		
		//利用坐标获取地理信息
		GeoCodingResponseObject geoCodingResponseObject = BaiduHttpRequestUtil.getPOIInfoByLocation(lat, lon, true);
		GeoCodingObject  geoCodingObject = geoCodingResponseObject.getResult();
		AddressComponent addressComponent = geoCodingObject.getAddressComponent();
		
		if(addressComponent!=null)
		{
			//判断用户是否身在活动举办城市
			String cityName = addressComponent.getCity();
			if(!cityNames.contains(cityName))
			{
				logger.info(cityName + " is not a city in campaign " + mapCampaignInfoModel.getId());
				return null;
			}
		}
		
		List<Position> poisList = geoCodingObject.getPois();
		
		//如果该用户身边没有poi则无点
		if(poisList == null) 
		{
			logger.error("baidu returns no points with this geopoin to generate campaign points");
			return null;
		}
		
		//如果限制数为0则设定为默认值
		int pointCount = mapCampaignInfoModel.getPointsCountLimit();
		if(pointCount == 0) pointCount = MapObjectConstants.DEFAULT_GIFT_COUNT;
		
		if(poisList.size() < pointCount) pointCount = poisList.size();
		
		int size = pointCount;
		if(mapCampaignInfoModel.getIsPointsRandom() > 0)
		{
			Random r = new Random(System.currentTimeMillis());
			size = Math.abs(r.nextInt() % pointCount);
			
			if(size == 0) size = pointCount;
		}
		
		for(int i = 0; i < size; i++)
		{
			Position p = poisList.get(i);
			
			String campaignUrl = generateSomeCampaignChance(userId, lat, lon, mapCampaignInfoModel);
			
			BaseMapObject g = new BaseMapObject();
			g.setObjectType(MapObjectConstants.OBJECT_TYPE_CAMPAIGN);
			
			g.setAddress(p.getAddr());
			g.setLocation(new LocationInMapObject(p.getPoint().getLat(), p.getPoint().getLng()));
			g.setName(mapCampaignInfoModel.getName());
			g.setNameOfPOI(p.getName());
			g.setUidOfPOI(p.getUid());
			g.setUrl(campaignUrl);
			g.setIconUrl(mapCampaignInfoModel.getIconUrl());
			g.setShareUrl(mapCampaignInfoModel.getShareUrl());
			
			resultList.add(g);
		}
		
		return resultList;
	}

	
	public static String generateSomeCampaignChance(int userId,double lat,double lon,CampaignInfoModel mapCampaignInfoModel)
	{
		
		if(mapCampaignInfoModel == null) return null;
		
		String baseUrl = mapCampaignInfoModel.getBaseUrl();
		
		if(StringUtils.isNotEmpty(baseUrl))
		{
			
			CampaignChanceModel campaignChanceModel = new CampaignChanceModel();
			
			campaignChanceModel.setCampaignId(mapCampaignInfoModel.getId());
			campaignChanceModel.setCreateTime(System.currentTimeMillis());
			campaignChanceModel.setExpireTime(mapCampaignInfoModel.getExpireTime());
			campaignChanceModel.setUserId(userId);
			campaignChanceModel.setLocation(new LocationInMapObject(lat, lon));
			campaignChanceModel.setLotteryRate(mapCampaignInfoModel.getLotteryRate());
			
			try {
				String json = objectMapper.writeValueAsString(campaignChanceModel);
				
				List<String> resultList = ElasticSearchUtil.addDoc(MapObjectConstants.OBJECT_TYPE_CAMPAIGN, mapCampaignInfoModel.getExpireTime(), json);
				
				if(resultList!=null&&resultList.size() > 0)
				{
					String _id =  resultList.get(0);
					
					return baseUrl + _id;
				}
				
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				logger.error("ElasticSearchCampaignUtil.generateSomeCampaignChance",e);
			}
			return baseUrl +"error";
		}
		return "error";
	}
	
	public static CampaignChanceModel getCampaignChanceModelById(String _id)
	{
		if(StringUtils.isEmpty(_id))
		{
			logger.error("_id is null in getCampaignChanceModelById(String _id)");
			return null;
		}
		
		String json = ElasticSearchUtil.getDoc(MapObjectConstants.OBJECT_TYPE_CAMPAIGN, _id);
		
		if(StringUtils.isEmpty(json))
		{
			logger.error("json is null in getCampaignChanceModelById(String _id)");
			return null;
		}
		
		try {
			
			CampaignChanceModel campaignChanceModel = objectMapper.readValue(json, CampaignChanceModel.class);
			
			campaignChanceModel.set_id(_id);
			
			return campaignChanceModel;
			
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			logger.error("ElasticSearchCampaignUtil.getCampaignChanceModelById",e);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			logger.error("ElasticSearchCampaignUtil.getCampaignChanceModelById",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("ElasticSearchCampaignUtil.getCampaignChanceModelById",e);
		}
		
		return null;
	}
	
	public static int deleteCampaignChanceModelById(String _id)
	{
		if(StringUtils.isEmpty(_id))
		{
			logger.error("_id is null in deleteCampaignChanceModelById(String _id)");
			return 0;
		}
		
		return ElasticSearchUtil.deleteDocById(MapObjectConstants.OBJECT_TYPE_CAMPAIGN, _id);
	}
	
	
	public static int getResultOfLottery(double rate)
	{
		int result = 0;
		
		if(rate == 0) return result;
		
		double bigNumber = 1/rate;
		
		Random r = new Random(System.currentTimeMillis());
		
		int number = r.nextInt((int) bigNumber);
		
		if(number == 0)
		{
			result = 1;
		}
				
		return result;
	}
	

}
