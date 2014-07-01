package com.dajie.message.elasticsearch.map.utils.elasticsearch;

import static com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchQueryUtil.countPersonByMapQuery;
import static com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchQueryUtil.searchByMapQuery;
import static com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchQueryUtil.searchPersonQuery;
import static com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchQueryUtil.searchPersonQueryByScroll;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;

import com.dajie.message.elasticsearch.map.constants.DefaultPositionTreeUtils;
import com.dajie.message.elasticsearch.map.constants.ElasticSearchConstant;
import com.dajie.message.elasticsearch.map.constants.MapObjectConstants;
import com.dajie.message.elasticsearch.map.constants.PointOnMapConstants;
import com.dajie.message.elasticsearch.map.constants.model.DefaultPositionTreeNode;
import com.dajie.message.elasticsearch.map.utils.LoggerManager;
import com.dajie.message.elasticsearch.map.utils.spring.MyApplicationContextUtil;
import com.dajie.message.model.map.PersonMapObject;
import com.dajie.message.model.map.RegionInfoObject;
import com.dajie.message.model.system.PrivacySetting;
import com.dajie.message.model.user.UserBase;
import com.dajie.message.model.user.UserCareer;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ElasticSearchPersonUtil {
	
	private static Logger logger = LoggerManager.getLogger(ElasticSearchPersonUtil.class);
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	
	public static List<PersonMapObject> searchPerson(UserBase userBase,UserCareer userCareer,PrivacySetting privacySetting,double lat, double lon,
			int distance, int distanceStep,int distanceUnit,
			int pageNumber,int pageSize, String userName, String position,String companyName,
			int industry, int jobType, int experienceMin,int experienceMax, int education,
			String tag, int gender, int isVerified, int isStudent,String school,String schoolLabel,String major,
			String address,String district,String city)
	{
		if(pageSize == 0)
		{
			return searchAllPerson( userBase, userCareer,privacySetting,lat, lon, distance, distanceStep, distanceUnit, 
					userName, position,companyName, industry, jobType, experienceMin,experienceMax, education, tag, gender, isVerified,
					isStudent, school, schoolLabel, major, address, district, city);
		}
		
		SearchResponse searchResponse = searchPersonQuery(false, userBase,userCareer,privacySetting,ElasticSearchConstant.INDEX_NAME, ElasticSearchConstant.PERSON_TYPE_NAME, 
				lat, lon, distance, distanceStep, distanceUnit, pageNumber,pageSize, userName,position, companyName, industry, jobType, experienceMin,experienceMax, education, tag, 
				gender, isVerified, isStudent, school,schoolLabel,major,address, district, city);
		
		if(searchResponse.status() != RestStatus.OK)
		{
			logger.error("there is something wrong when search Persons. error status is " +searchResponse.status().toString());
			return null;
		}
		
		
		SearchHit[] searchHits = searchResponse.getHits().getHits();
		List<PersonMapObject> resultList = new ArrayList<PersonMapObject>();
		
		try {
		if(searchHits.length > 0)
		{
			for(SearchHit s : searchHits)
			{
				String source = s.getSourceAsString();
				if(StringUtils.isEmpty(source))
				{
					continue;
				}
				String _id = s.getId();
				Double distanceFromCenter = new Double(0);
				if(s.getFields()!=null)
				{
					Map<String, SearchHitField> map = s.getFields();
					
					SearchHitField searchHitField = map.get(ScriptModel.DEFAULT_NAME);
					
					distanceFromCenter = searchHitField.getValue();
					
				}
				PersonMapObject p= objectMapper.readValue(source, PersonMapObject.class);
				p.set_id(_id);
				p.setDistance(distanceFromCenter.longValue());
				resultList.add(p);
			}
		}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			logger.error("ElasticSearchUtil.searchPerson",e);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			logger.error("ElasticSearchUtil.searchPerson",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("ElasticSearchUtil.searchPerson",e);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("ElasticSearchUtil.searchPerson",e);
		}
		
		return resultList;
	}
	
	public static List<PersonMapObject> searchAllPerson(UserBase userBase,UserCareer userCareer,PrivacySetting privacySetting,double lat, double lon,
			int distance, int distanceStep,int distanceUnit,
			String userName,String position, String companyName,
			int industry, int jobType, int experienceMin, int experienceMax, int education,
			String tag, int gender, int isVerified, int isStudent,String school,String schoolLabel,String major,
			String address,String district,String city)
	{
		List<SearchResponse>  searchResponseList = searchPersonQueryByScroll(false,userBase,userCareer,privacySetting,ElasticSearchConstant.INDEX_NAME, ElasticSearchConstant.PERSON_TYPE_NAME, 
				lat, lon, distance, distanceStep, distanceUnit, userName, position,companyName, industry, jobType, experienceMin,experienceMax, education, tag, 
				gender, isVerified, isStudent, school,schoolLabel,major,address, district, city);
		
		List<PersonMapObject> resultList = new ArrayList<PersonMapObject>();
		
		if(searchResponseList!=null&&searchResponseList.size() > 0)
		{
			
			for(SearchResponse  searchResponse : searchResponseList)
			{
				
				if(searchResponse.status() != RestStatus.OK)
				{
					logger.error("there is something wrong when search Persons. error status is " +searchResponse.status().toString());
					continue;
				}
				
				SearchHit[] searchHits = searchResponse.getHits().getHits();
				
				try {
				if(searchHits.length > 0)
				{
					for(SearchHit s : searchHits)
					{
						String source = s.getSourceAsString();
						if(StringUtils.isEmpty(source))
						{
							continue;
						}
						String _id = s.getId();
						Double distanceFromCenter = new Double(0);
						if(s.getFields()!=null)
						{
							Map<String, SearchHitField> map = s.getFields();
							SearchHitField searchHitField = map.get(ScriptModel.DEFAULT_NAME);
							distanceFromCenter = searchHitField.getValue();
						}
						PersonMapObject p= objectMapper.readValue(source, PersonMapObject.class);
						p.set_id(_id);
						p.setDistance(distanceFromCenter.longValue());
						resultList.add(p);
					}
				}
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					logger.error("ElasticSearchUtil.searchPerson",e);
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					logger.error("ElasticSearchUtil.searchPerson",e);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error("ElasticSearchUtil.searchPerson",e);
				} catch (Exception e) {
					// TODO: handle exception
					logger.error("ElasticSearchUtil.searchPerson",e);
				}
			}
		}
		return resultList;
	}
	
	public static List<Integer> searchPersonWithoutGeoInfo(String userName,String position,
			String companyName, int industry, int jobType,
			int experienceMin, int experienceMax,int educationMin, String tag, int gender,
			int isVerified, int isStudent, String school,String schoolLabel,String major,
			String address, String district,
			String city, int pageNumber, int pageSize) {
		// TODO Auto-generated method stub
		
		SearchResponse searchResponse = ElasticSearchQueryUtil.searchPersonQuery(
				ElasticSearchConstant.INDEX_NAME, ElasticSearchConstant.PERSON_TYPE_NAME,
				userName, position, companyName,
				 industry,  jobType,  experienceMin, experienceMax, educationMin,
				 tag,  gender,  isVerified,  isStudent,school,schoolLabel,major,
				 address, district, city, pageNumber, pageSize);
		
		if(searchResponse.status() != RestStatus.OK)
		{
			logger.error("there is something wrong when search Persons. error status is " +searchResponse.status().toString());
			return null;
		}
		
		SearchHit[] searchHits = searchResponse.getHits().getHits();
		List<Integer> resultList = new ArrayList<Integer>();
		
		try {
		if(searchHits.length > 0)
		{
			for(SearchHit s : searchHits)
			{
				String source = s.getSourceAsString();
				
				PersonMapObject p= objectMapper.readValue(source, PersonMapObject.class);
				
				resultList.add(p.getUserId());
			}
		}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			logger.error("ElasticSearchUtil.searchPerson",e);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			logger.error("ElasticSearchUtil.searchPerson",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("ElasticSearchUtil.searchPerson",e);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("ElasticSearchUtil.searchPerson",e);
		}
		
		return resultList;
	}
	
	public static Map<RegionInfoObject, Integer> searchPersonDistrictCount(UserBase userBase,UserCareer userCareer,PrivacySetting privacySetting,
			String userName, String position,String companyName,
			int industry, int jobType, int experienceMin,int experienceMax,
			int educationMin, String tag, int gender, int isVerified,
			int isStudent,String school,String schoolLabel,String major,String city,int zoomLevel)
	{
		
		List<RegionInfoObject>  regionList = null;
		
		Map<RegionInfoObject, Integer> resultMap = new HashMap<RegionInfoObject, Integer>();
		
		Map<RegionInfoObject, SearchResponse>  countResultMap = new HashMap<RegionInfoObject, SearchResponse>();;
		
		if(zoomLevel == PointOnMapConstants.POINT_TYPE_COUNT_AT_DISTRICT)
		{
		
		if(StringUtils.isNotEmpty(city))
		{
			DefaultPositionTreeNode  cityNode = DefaultPositionTreeUtils.getByName(DefaultPositionTreeUtils.getInstance(), city);
			RegionInfoObject regionInfoObject = null;
			
			if(cityNode!=null)
			{
				regionInfoObject = cityNode.getRegionInfoObject();
			}
			
			if(regionInfoObject!=null)
				regionList = ElasticSearchUtil.getSubInfoBySuper(regionInfoObject.getId());
			
		}else
		{
			logger.error("geopoint and city are both null!");
			return new HashMap<RegionInfoObject, Integer>();
		}
		
		
		if(regionList == null||regionList.size() == 0)
		{
			logger.info("there is no ditrict under city :" + city+", so turn to city count.");
			return ElasticSearchUtil.getCountByCity(MapObjectConstants.OBJECT_TYPE_JOB);
		}
		
			resultMap = new HashMap<RegionInfoObject, Integer>();
		
			countResultMap = countPersonByMapQuery(userBase,userCareer,privacySetting,userName, position, companyName,
					industry, jobType, experienceMin,experienceMax, educationMin, tag, gender, isVerified, isStudent, 
					school, schoolLabel, major, regionList,zoomLevel,city);
			
		}else if(zoomLevel == PointOnMapConstants.POINT_TYPE_COUNT_AT_CITY)
		{
			List<DefaultPositionTreeNode> citySet = DefaultPositionTreeUtils.getInstance().getNodes();
			
			if(citySet == null || citySet.size() == 0)
			{
				logger.error("cityList is null.");
				return null;
			}
			
			regionList = new ArrayList<RegionInfoObject>();
			for(DefaultPositionTreeNode r : citySet)
			{
				regionList.add(r.getRegionInfoObject());
			}
			
			countResultMap = countPersonByMapQuery(userBase,userCareer,privacySetting,userName, position, companyName,
					industry, jobType, experienceMin,experienceMax, educationMin, tag, gender, isVerified, isStudent, 
					school, schoolLabel, major, regionList,zoomLevel,null);
		}
			
			
			for(Entry<RegionInfoObject, SearchResponse> e : countResultMap.entrySet())
			{
				
				SearchResponse countResult = e.getValue();
				
				if(countResult!=null&&countResult.status() == RestStatus.OK)
				{
					if(countResult.getHits().getTotalHits() > 0)
					{
						SearchHits searchhits = countResult.getHits();
						
						if(searchhits!=null&&searchhits.getHits().length>0)
						{
							SearchHit[] searchhit = searchhits.getHits();
							String json = searchhit[0].getSourceAsString();
							try {
								JsonNode jn = objectMapper.readTree(json);
								JsonNode locationNode = jn.get("location");
								if(locationNode!=null)
								{
									double lat = locationNode.get("lat").asDouble();
									double lon = locationNode.get("lon").asDouble();
									
									if(lat!=0&&lon!=0)
									{
										e.getKey().setRegionLat(lat);
										e.getKey().setRegionLon(lon);
									}
								}
							
						} catch (JsonProcessingException e1) {
							// TODO Auto-generated catch block
							logger.error("ElasticSearchUtil.searchPersonDistrictCount",e1);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							logger.error("ElasticSearchUtil.searchPersonDistrictCount",e1);
						}
					
				}
				
				}
				resultMap.put(e.getKey(), (int)countResult.getHits().getTotalHits());
			}
		}
		
		return resultMap;
		
	}
	
	
	public static List<PersonMapObject> getPersonByUserId(int userId)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		
		SearchResponse  searchResponse = searchByMapQuery(ElasticSearchConstant.INDEX_NAME, 
				ElasticSearchConstant.PERSON_TYPE_NAME, map, 0, 500);
		
		if(searchResponse.status() != RestStatus.OK)
		{
			logger.error("there is something wrong when search persons. error status is " +searchResponse.status().toString());
			return null;
		}
		
		SearchHit[] searchHits = searchResponse.getHits().getHits();
		List<PersonMapObject> resultList = new ArrayList<PersonMapObject>();
		
		try {
		if(searchHits.length > 0)
		{
			for(SearchHit s : searchHits)
			{
				String source = s.getSourceAsString();
				String _id = s.getId();
				
				
				PersonMapObject p= objectMapper.readValue(source, PersonMapObject.class);
				p.set_id(_id);
				resultList.add(p);
			}
		}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			logger.error("ElasticSearchUtil.getPersonByUserId",e);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			logger.error("ElasticSearchUtil.getPersonByUserId",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("ElasticSearchUtil.getPersonByUserId",e);
		}
		
		return resultList;
	}


}
