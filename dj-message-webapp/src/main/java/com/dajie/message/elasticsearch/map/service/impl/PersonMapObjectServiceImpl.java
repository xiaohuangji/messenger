package com.dajie.message.elasticsearch.map.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dajie.message.dao.UserLabelDAO;
import com.dajie.message.elasticsearch.map.constants.MapObjectConstants;
import com.dajie.message.elasticsearch.map.utils.LoggerManager;
import com.dajie.message.elasticsearch.map.utils.ModelConvertorUtil;
import com.dajie.message.elasticsearch.map.utils.baidu.BaiduHttpRequestUtil;
import com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchJobUtil;
import com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchPersonUtil;
import com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchUtil;
import com.dajie.message.model.map.LocationInMapObject;
import com.dajie.message.model.map.PersonMapObject;
import com.dajie.message.model.map.geocode.GeoCodingResponseObject;
import com.dajie.message.model.system.PrivacySetting;
import com.dajie.message.model.user.UserBase;
import com.dajie.message.model.user.UserCareer;
import com.dajie.message.model.user.UserEducation;
import com.dajie.message.model.user.UserLabel;
import com.dajie.message.service.IPersonMapObjectService;
import com.dajie.message.service.ISystemService;
import com.dajie.message.service.IUserProfileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component("personMapObjectServiceImpl")
public class PersonMapObjectServiceImpl implements IPersonMapObjectService{
	
	
	private static Logger logger = LoggerManager.getLogger(PersonMapObjectServiceImpl.class);
	
	
	@Autowired
	private IUserProfileService userProfileService;
	@Autowired
	private UserLabelDAO userLabelDAO;
	@Autowired
	private ISystemService systemService;
	
	private static ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String addNewPersonMapObject(UserBase userBase,double lat,double lon) {
		// TODO Auto-generated method stub
		
		if(userBase == null || userBase.getUserId() == 0)
		{
			logger.error("PointOnMapServiceImpl.addNewPersonMapObject userBase is null");
			return null;
		}
		
		List<UserLabel> userLabelList = userLabelDAO.getLabels(userBase.getUserId());
		String tag = "";
		if(userLabelList != null && userLabelList.size() > 0)
		{
			for(UserLabel ul : userLabelList)
			{
				tag += (ul.getContent().replace(" ", "")+" ");
			}
			tag = tag.substring(0, tag.length()-1);
		}
		
		GeoCodingResponseObject  geoCodingResponseObject = null;
		if(lat >0 || lon > 0)
			geoCodingResponseObject =  BaiduHttpRequestUtil.getPOIInfoByLocation(lat, lon, true);
		
		UserCareer userCareer = userProfileService.getUserCareer(userBase.getUserId());
		
		UserEducation userEducation = userProfileService.getUserEducation(userBase.getUserId());
		
		PrivacySetting privacySetting = systemService.getPrivacySetting(userBase.getUserId());
		
		PersonMapObject personMapObject = ModelConvertorUtil.convertUserBaseToPersonMapObject(userBase, userCareer,userEducation,privacySetting ,tag,
				geoCodingResponseObject, lat, lon);
		
		return this.addNewPersonMapObject(personMapObject);
	}
	
	

	@Override
	public String addNewPersonMapObject(PersonMapObject personMapObject) {
		// TODO Auto-generated method stub
		
		if(personMapObject == null)
		{
			logger.error("when addNewPersonMapObject, personMapObject is null");
			return "";
		}
		
		String result = "";
		String json = null;
		try {
			json = objectMapper.writeValueAsString(personMapObject);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			logger.error("PointOnMapServiceImpl.addNewPersonMapObject",e);
		}
		
		List<PersonMapObject> list = ElasticSearchPersonUtil.getPersonByUserId(personMapObject.getUserId());
		
		if(list!=null&&list.size() > 0)
		{
			for(PersonMapObject p : list)
			{
				ElasticSearchUtil.deleteDocById(MapObjectConstants.OBJECT_TYPE_PERSON, p.get_id());
			}
		}
		
		
		List<String> resultList = ElasticSearchUtil.addDoc(MapObjectConstants.OBJECT_TYPE_PERSON,0,json);
		
		if(resultList!=null&resultList.size()>0)
		{
			for(String s : resultList)
			{
				result+=(s+",");
			}
			return result.substring(0, result.length()-1);
		}
		
		return result;
	}

	@Override
	public List<PersonMapObject> getPersonMapObjectById(int userId) {
		// TODO Auto-generated method stub
		List<PersonMapObject> result = ElasticSearchPersonUtil.getPersonByUserId(userId);
		if(result!=null)
			return result;
		else
			return new ArrayList<PersonMapObject>();
	}

	@Override
	public int updatePersonMapObject(int userId) {
		// TODO Auto-generated method stub
		
		if(userId == 0)
		{
			logger.error("PointOnMapServiceImpl.updatePersonMapObject : userId is 0.");
			return 0;
		}
		
		
		List<PersonMapObject> personMapObjectList = ElasticSearchPersonUtil.getPersonByUserId(userId);
		
		if(personMapObjectList == null || personMapObjectList.size() == 0)
		{
			logger.error("No data in ES for updating when User id = "+ userId +".");
			return 0;
		}
		
		
		Map<String,String> map = new HashMap<String,String>();
		
		for(PersonMapObject p :  personMapObjectList)
		{
			
			LocationInMapObject l = p.getLocation();
			
			double lat = l == null? 0 : l.getLat();
			double lon = l == null? 0 : l.getLon();
			
			List<UserLabel> userLabelList = userLabelDAO.getLabels(userId);
			String tag = "";
			if(userLabelList != null && userLabelList.size() > 0)
			{
				for(UserLabel ul : userLabelList)
				{
					tag += (ul.getContent().replace(" ", "")+" ");
				}
				tag = tag.substring(0, tag.length()-2);
			}
			
			GeoCodingResponseObject  geoCodingResponseObject =  BaiduHttpRequestUtil.getPOIInfoByLocation(lat, lon, false);
			
			UserBase userBase = userProfileService.getUserBase(userId);
			
			UserCareer userCareer = userProfileService.getUserCareer(userId);
			
			UserEducation userEducation = userProfileService.getUserEducation(userId);
			
			PrivacySetting privacySetting = systemService.getPrivacySetting(userId);
			
			PersonMapObject personMapObject = ModelConvertorUtil.convertUserBaseToPersonMapObject(userBase, userCareer,userEducation,privacySetting,tag,
					geoCodingResponseObject, lat, lon);
			
			personMapObject.setUpdateTime(System.currentTimeMillis());

			String json = null;
			
			
			try {
				json = objectMapper.writeValueAsString(personMapObject);
				
				map.put(p.get_id(), json);
				
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				logger.error("PointOnMapServiceImpl.updatePersonMapObject",e);
			}
		}
		

		return ElasticSearchUtil.updateDocs(MapObjectConstants.OBJECT_TYPE_PERSON, map);

	}
	
	
	@Override
	public int deletePersonMapObject(int userId) {
		// TODO Auto-generated method stub
		
		List<PersonMapObject> personMapObjectList = ElasticSearchPersonUtil.getPersonByUserId(userId);
		
		if(personMapObjectList == null || personMapObjectList.size() == 0)
		{
			logger.error("No data in ES for deleting when User id = "+ userId +".");
			return 0;
		}
		
		int result = 0;
		
		for(PersonMapObject p :  personMapObjectList)
		{
			result += ElasticSearchUtil.deleteDocById(MapObjectConstants.OBJECT_TYPE_PERSON, p.get_id());
		}
		
		return result;
	}



	public IUserProfileService getUserProfileService() {
		return userProfileService;
	}



	public void setUserProfileService(IUserProfileService userProfileService) {
		this.userProfileService = userProfileService;
	}



	public UserLabelDAO getUserLabelDAO() {
		return userLabelDAO;
	}



	public void setUserLabelDAO(UserLabelDAO userLabelDAO) {
		this.userLabelDAO = userLabelDAO;
	}



	public ISystemService getSystemService() {
		return systemService;
	}



	public void setSystemService(ISystemService systemService) {
		this.systemService = systemService;
	}

}
