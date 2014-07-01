package com.dajie.message.elasticsearch.map.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dajie.message.annotation.mcp.ParamNullable;
import com.dajie.message.dao.UserLabelDAO;
import com.dajie.message.elasticsearch.map.constants.ElasticSearchConstant;
import com.dajie.message.elasticsearch.map.constants.MapObjectConstants;
import com.dajie.message.elasticsearch.map.constants.PointOnMapConstants;
import com.dajie.message.elasticsearch.map.dao.IRegionInfoObjectDAO;
import com.dajie.message.elasticsearch.map.utils.LoggerManager;
import com.dajie.message.elasticsearch.map.utils.PointAggregationUtil;
import com.dajie.message.elasticsearch.map.utils.baidu.BaiduGeoInfoUtil;
import com.dajie.message.elasticsearch.map.utils.baidu.BaiduHttpRequestUtil;
import com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchCampaignUtil;
import com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchJobUtil;
import com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchPersonUtil;
import com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchUtil;
import com.dajie.message.model.map.BaseMapObject;
import com.dajie.message.model.map.JobMapObject;
import com.dajie.message.model.map.LocationInMapObject;
import com.dajie.message.model.map.PersonMapObject;
import com.dajie.message.model.map.PointOnMap;
import com.dajie.message.model.map.RegionInfoObject;
import com.dajie.message.model.campaign.CampaignInfoModel;
import com.dajie.message.model.map.geocode.AddressComponent;
import com.dajie.message.model.map.geocode.GeoCodingObject;
import com.dajie.message.model.map.geocode.GeoCodingResponseObject;
import com.dajie.message.model.map.geocode.Position;
import com.dajie.message.model.system.PrivacySetting;
import com.dajie.message.model.user.UserBase;
import com.dajie.message.model.user.UserCareer;
import com.dajie.message.service.IJobService;
import com.dajie.message.service.IPersonMapObjectService;
import com.dajie.message.service.IPoiInfoService;
import com.dajie.message.service.IPointOnMapService;
import com.dajie.message.service.ISystemService;
import com.dajie.message.service.IUserProfileService;
import com.dajie.message.service.cache.IRedisCache;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component("pointOnMapServiceImpl")
public class PointOnMapServiceImpl implements IPointOnMapService {
	
	private static Logger logger = LoggerManager.getLogger(PointOnMapServiceImpl.class);
	
	@Autowired
	private IPoiInfoService poiInfoService;
	@Autowired
	private IJobService jobService;
	@Autowired
	private IUserProfileService userProfileService;
	@Autowired
	private UserLabelDAO userLabelDAO;
	@Autowired
	private IRegionInfoObjectDAO regionInfoObjectDAO;
	@Autowired
	private IPersonMapObjectService personMapObjectService;
	@Autowired
	private ISystemService systemService;
	@Autowired
	private IRedisCache redisCache;
	
	private static ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public List<PointOnMap> getPointsOnMap(int _userId,double latitude, double longitude,
			int zoomLevel, int pointType,int distance,int distanceStep) {
		// TODO Auto-generated method stub
		
		if(_userId==0) return null;
		
		UserBase userBase = userProfileService.getUserBase(_userId);
		UserCareer userCareer = userProfileService.getUserCareer(_userId);
		PrivacySetting privacySetting = systemService.getPrivacySetting(_userId);
		
		
		if(zoomLevel == PointOnMapConstants.POINT_TYPE_COUNT_AT_CITY)
		{//城市级别
			Map<Integer,Map<RegionInfoObject, Integer>> map = new HashMap<Integer,Map<RegionInfoObject, Integer>>();
			
			if(pointType == MapObjectConstants.OBJECT_TYPE_JOB)
			{
				
				Map<RegionInfoObject, Integer> m = getCachedRegionInfoCountMap(MapObjectConstants.OBJECT_TYPE_JOB);
				if(m == null || m.size() == 0)
				{
					m = ElasticSearchUtil
							.getCountByCity(MapObjectConstants.OBJECT_TYPE_JOB);
					setRegionInfoCountMapToCache(MapObjectConstants.OBJECT_TYPE_JOB, 
							MapObjectConstants.MAP_OBJECT_CACHE_EXPRIE, m);
				}
				
				map.put(MapObjectConstants.OBJECT_TYPE_JOB, m);
			}
			else if(pointType == MapObjectConstants.OBJECT_TYPE_PERSON)
			{
				Map<RegionInfoObject, Integer> m = getCachedRegionInfoCountMap(MapObjectConstants.OBJECT_TYPE_PERSON);
						
				if(m == null || m.size() == 0)
				{
					m = ElasticSearchUtil
							.getCountByCity(MapObjectConstants.OBJECT_TYPE_PERSON);
					setRegionInfoCountMapToCache(MapObjectConstants.OBJECT_TYPE_PERSON, 
							MapObjectConstants.MAP_OBJECT_CACHE_EXPRIE, m);
				}
				
				map.put(MapObjectConstants.OBJECT_TYPE_PERSON, m);
			}else
			{
				Map<RegionInfoObject, Integer> m1 = getCachedRegionInfoCountMap(MapObjectConstants.OBJECT_TYPE_JOB);
				if(m1 == null || m1.size() == 0)
				{
					m1 = ElasticSearchUtil
							.getCountByCity(MapObjectConstants.OBJECT_TYPE_JOB);
					setRegionInfoCountMapToCache(MapObjectConstants.OBJECT_TYPE_JOB, 
							MapObjectConstants.MAP_OBJECT_CACHE_EXPRIE, m1);
				}
				
				Map<RegionInfoObject, Integer> m2  = getCachedRegionInfoCountMap(MapObjectConstants.OBJECT_TYPE_PERSON);
				if(m2 == null || m2.size() == 0)
				{
					m2 = ElasticSearchUtil
							.getCountByCity(MapObjectConstants.OBJECT_TYPE_PERSON);
					setRegionInfoCountMapToCache(MapObjectConstants.OBJECT_TYPE_PERSON, 
							MapObjectConstants.MAP_OBJECT_CACHE_EXPRIE, m2);
				}
				
				map.put(MapObjectConstants.OBJECT_TYPE_JOB, m1);
				map.put(MapObjectConstants.OBJECT_TYPE_PERSON, m2);
			}
			
			if(map == null || map.size() == 0)
			{
				logger.error("There is no result when get count by cities");
				return new ArrayList<PointOnMap>();
			}
			
			List<PointOnMap> result = new ArrayList<PointOnMap>();
			
			for(Entry<Integer, Map<RegionInfoObject, Integer>> e : map.entrySet())
			{
				
				if(e.getValue() == null || e.getValue().size() == 0)
				{
					logger.error("There is no result when get count by cities with objectType"+ e.getKey());
					continue;
				}
				
				for(Entry<RegionInfoObject, Integer> f : e.getValue().entrySet())
				{
					PointOnMap p = new PointOnMap();
					
					if(f.getValue() == null)
					{
						logger.error("there is no RegionInfoObject for the city" );
						continue;
					}
					
					p.setPointLevel(PointOnMapConstants.POINT_TYPE_COUNT_AT_CITY);
					p.setObjectType(e.getKey());
					p.setCity(f.getKey().getRegionName());
					p.setPonitInfo(f.getKey().getRegionName());
					p.setObjectCount(f.getValue());
					p.setLatitude(f.getKey().getRegionLat());
					p.setLongitude(f.getKey().getRegionLon());
					
					if(p.getLatitude()!=0||p.getLongitude()!=0)
					result.add(p);
				}
			}
			return result;
		}
		else if(zoomLevel == PointOnMapConstants.POINT_TYPE_COUNT_AT_DISTRICT)
		{//地区级别
			AddressComponent addressComponent = BaiduGeoInfoUtil.getAddressComponentWithGeoPoint(latitude, longitude);
			
			if(addressComponent ==null)
			{
				logger.error("get geo infomation from Baidu error.");
				return new ArrayList<PointOnMap>();
			}
			
			String cityName = addressComponent.getCity();
			
			Map<Integer,Map<RegionInfoObject, Integer>> map = new HashMap<Integer,Map<RegionInfoObject, Integer>>();
			
			if(pointType == MapObjectConstants.OBJECT_TYPE_JOB)
			{
				Map<RegionInfoObject, Integer> m = ElasticSearchUtil.getCountByDistrict(MapObjectConstants.OBJECT_TYPE_JOB,
					cityName);
				
				map.put(MapObjectConstants.OBJECT_TYPE_JOB, m);
			}
			else if(pointType == MapObjectConstants.OBJECT_TYPE_PERSON)
			{
				Map<RegionInfoObject, Integer> m = ElasticSearchUtil.getCountByDistrict(MapObjectConstants.OBJECT_TYPE_PERSON,
					cityName);
				
				map.put(MapObjectConstants.OBJECT_TYPE_PERSON, m);
			}else
			{
				Map<RegionInfoObject, Integer> m1 = ElasticSearchUtil.getCountByDistrict(MapObjectConstants.OBJECT_TYPE_JOB,
						cityName);
				Map<RegionInfoObject, Integer> m2 = ElasticSearchUtil.getCountByDistrict(MapObjectConstants.OBJECT_TYPE_PERSON,
						cityName);
				
				map.put(MapObjectConstants.OBJECT_TYPE_PERSON, m2);
				map.put(MapObjectConstants.OBJECT_TYPE_JOB, m1);
			}
			
			
			if(map == null || map.size() == 0)
			{
				logger.error("There is no result when get count by district. The city is " + cityName);
				return new ArrayList<PointOnMap>();
			}
			
			List<PointOnMap> result = new ArrayList<PointOnMap>();
			
			for(Entry<Integer, Map<RegionInfoObject, Integer>> e : map.entrySet())
			{
				
				if(e.getValue() == null || e.getValue().size() == 0)
				{
					logger.error("There is no result when get count by cities with objectType"+ e.getKey());
					continue;
				}
				
				for(Entry<RegionInfoObject, Integer> f : e.getValue().entrySet())
				{
					PointOnMap p = new PointOnMap();
					
					if(f.getKey()==null)
					{
						logger.error("regionInfo is null");
						continue;
					}
					
					p.setPointLevel(PointOnMapConstants.POINT_TYPE_COUNT_AT_DISTRICT);
					p.setObjectType(e.getKey());
					p.setCity(cityName);
					p.setDistrict(f.getKey().getRegionName());
					p.setPonitInfo(f.getKey().getRegionName());
					p.setObjectCount(f.getValue());
					p.setLatitude(f.getKey().getRegionLat());
					p.setLongitude(f.getKey().getRegionLon());
				
					
					if(p.getLatitude()!=0||p.getLongitude()!=0)
						result.add(p);
				}
			}
			return result;

		}else if(zoomLevel == PointOnMapConstants.POINT_TYPE_COUNT_AT_POINT)
		{//详细级别
			
//			if(distanceStep == 0) distanceStep = ElasticSearchConstant.STEP_DISTANCE_MAX_METER;
			
			List<BaseMapObject> baseMapObjectList = new ArrayList<BaseMapObject>();
			
			
			
			if(pointType == MapObjectConstants.OBJECT_TYPE_JOB)
			{
					List<JobMapObject> jList = ElasticSearchJobUtil.searchJob(_userId,
							latitude, longitude, 
							distance, distanceStep,ElasticSearchConstant.DISTANCE_UNIT_METER_CODE,0, 0, 
							null,null, null, 0, ElasticSearchConstant.DEFAULT_SALARY_MAX, 
							0, 0,
							0, 0, null, null, null, null, null);
					
					baseMapObjectList.addAll(jList);
			}
			else if(pointType == MapObjectConstants.OBJECT_TYPE_PERSON)
			{
				List<PersonMapObject> pList = ElasticSearchPersonUtil.searchPerson(userBase,userCareer,privacySetting,latitude, longitude, distance, distanceStep, 
						ElasticSearchConstant.DISTANCE_UNIT_METER_CODE, 
						0,0, null, null,null, 0, 0, 0, 0, 0, 
						null, -1, -1, -1, null, null,null,null,null, null);
				baseMapObjectList.addAll(pList);
			}else
			{
				List<PersonMapObject> pList = ElasticSearchPersonUtil.searchPerson(userBase,userCareer,privacySetting,latitude, longitude, distance, distanceStep, 
						ElasticSearchConstant.DISTANCE_UNIT_METER_CODE, 
						0,0, null, null,null, 0, 0, 0, 0, 0,
						null, -1, -1, -1, null, null,null,null,null, null);
				List<JobMapObject> jList = ElasticSearchJobUtil.searchJob(_userId,
						latitude, longitude, 
						distance, distanceStep,ElasticSearchConstant.DISTANCE_UNIT_METER_CODE,0, 0, 
						null,null, null, 0, ElasticSearchConstant.DEFAULT_SALARY_MAX, 
						0, 0,
						0, 0, null, null, null, null, null);
				baseMapObjectList.addAll(jList);
				baseMapObjectList.addAll(pList);
			}
			
			return PointAggregationUtil.aggregateMapObjectToPointOnMap(baseMapObjectList);
			
		}
		
		
		return new ArrayList<PointOnMap>();
	}
	
	@Override
	public List<PointOnMap> searchJobOnMap(int _userId,double lat, double lon, int zoomLevel,
			int distance,int distanceStep,String jobName, String companyName,
			String tag, int salaryMin, int salaryMax, int experience,
			int educationMin, int industry, int jobType, String address,
			String district, String city, String uniqueId, String nameOfPOI) {
		// TODO Auto-generated method stub
		
		if(zoomLevel == PointOnMapConstants.POINT_TYPE_COUNT_AT_CITY)
		{
			
			Map<RegionInfoObject, Integer>  map = ElasticSearchJobUtil.searchJobCount(jobName, companyName, tag, salaryMin, 
					salaryMax, experience, educationMin, industry, jobType, city,zoomLevel);
			
			List<PointOnMap> result = new ArrayList<PointOnMap>();

			for(Entry<RegionInfoObject, Integer> f : map.entrySet())
			{
				
					PointOnMap p = new PointOnMap();
					
					if(f.getValue() == null)
					{
						logger.error("there is no RegionInfoObject for the city" );
						continue;
					}
					
					p.setPointLevel(PointOnMapConstants.POINT_TYPE_COUNT_AT_DISTRICT);
					p.setObjectType(MapObjectConstants.OBJECT_TYPE_JOB);
					p.setCity(f.getKey().getRegionName());
					p.setDistrict(null);
					p.setPonitInfo(f.getKey().getRegionName());
					p.setObjectCount(f.getValue());
					p.setLatitude(f.getKey().getRegionLat());
					p.setLongitude(f.getKey().getRegionLon());
					
					if(p.getLatitude()!=0||p.getLongitude()!=0)
					result.add(p);
				
			}
			return result;
			
		}else if(zoomLevel == PointOnMapConstants.POINT_TYPE_COUNT_AT_DISTRICT)
		{
			AddressComponent addressComponent = BaiduGeoInfoUtil.getAddressComponentWithGeoPoint(lat, lon);
			
			if(addressComponent ==null)
			{
				logger.error("get geo infomation from Baidu error.");
				return new ArrayList<PointOnMap>();
			}
			
			String cityName = addressComponent.getCity();
			
			Map<RegionInfoObject, Integer>  map = ElasticSearchJobUtil.searchJobCount(jobName, companyName, tag, salaryMin, 
					salaryMax, experience, educationMin, industry, jobType, cityName,zoomLevel);
			
			List<PointOnMap> result = new ArrayList<PointOnMap>();

			for(Entry<RegionInfoObject, Integer> f : map.entrySet())
			{
				
					PointOnMap p = new PointOnMap();
					
					if(f.getValue() == null)
					{
						logger.error("there is no RegionInfoObject for the city" );
						continue;
					}
					
					p.setPointLevel(PointOnMapConstants.POINT_TYPE_COUNT_AT_CITY);
					p.setObjectType(MapObjectConstants.OBJECT_TYPE_JOB);
					p.setCity(cityName);
					p.setDistrict(f.getKey().getRegionName());
					p.setPonitInfo(f.getKey().getRegionName());
					p.setObjectCount(f.getValue());
					p.setLatitude(f.getKey().getRegionLat());
					p.setLongitude(f.getKey().getRegionLon());
					
					if(p.getLatitude()!=0||p.getLongitude()!=0)
					result.add(p);
				
			}
			return result;
			
		}else
		{
			List<BaseMapObject> baseMapObjectList = new ArrayList<BaseMapObject>();
			
			List<JobMapObject> jList = ElasticSearchJobUtil.searchJob(_userId,
					lat, lon, 
					distance, distanceStep,ElasticSearchConstant.DISTANCE_UNIT_METER_CODE, 0,0, 
					jobName,companyName, tag, salaryMin, salaryMax, 
							experience, educationMin,
							industry, jobType, address, district, city, uniqueId, nameOfPOI);
			
			baseMapObjectList.addAll(jList);
			
			return PointAggregationUtil.aggregateMapObjectToPointOnMap(baseMapObjectList);
		}
	}

	@Override
	public List<PointOnMap> searchPersonOnMap(int _userId,double lat, double lon,int zoomLevel,
			int distance,int distanceStep,String userName, String position,String companyName,
			int industry, int jobType, int experienceMin,int experienceMax,
			int educationMin, String tag, int gender, int isVerified,
			int isStudent,String school,String schoolLabel,String major,
			String address, String district, String city) {
		// TODO Auto-generated method stub
		if(_userId==0) return null;
		
		UserBase userBase = userProfileService.getUserBase(_userId);
		UserCareer userCareer = userProfileService.getUserCareer(_userId);
		PrivacySetting privacySetting = systemService.getPrivacySetting(_userId);
		
		if(zoomLevel == PointOnMapConstants.POINT_TYPE_COUNT_AT_CITY)
		{
			Map<RegionInfoObject, Integer>  map = ElasticSearchPersonUtil.searchPersonDistrictCount(userBase,userCareer,privacySetting,
					userName, position, companyName,
					industry, jobType, experienceMin,experienceMax, educationMin, tag, gender, isVerified, isStudent, school, schoolLabel,
					major, city,zoomLevel);
			
			List<PointOnMap> result = new ArrayList<PointOnMap>();

			for(Entry<RegionInfoObject, Integer> f : map.entrySet())
			{
				
					PointOnMap p = new PointOnMap();
					
					if(f.getValue() == null)
					{
						logger.error("there is no RegionInfoObject for the city" );
						continue;
					}
					
					p.setPointLevel(PointOnMapConstants.POINT_TYPE_COUNT_AT_DISTRICT);
					p.setObjectType(MapObjectConstants.OBJECT_TYPE_PERSON);
					p.setCity(f.getKey().getRegionName());
					p.setDistrict(null);
					p.setPonitInfo(f.getKey().getRegionName());
					p.setObjectCount(f.getValue());
					p.setLatitude(f.getKey().getRegionLat());
					p.setLongitude(f.getKey().getRegionLon());
					
					if(p.getLatitude()!=0||p.getLongitude()!=0)
					result.add(p);
				
			}
			return result;
			
			
		}
		else if(zoomLevel == PointOnMapConstants.POINT_TYPE_COUNT_AT_DISTRICT)
		{
				AddressComponent addressComponent = BaiduGeoInfoUtil.getAddressComponentWithGeoPoint(lat, lon);
				
				if(addressComponent ==null)
				{
					logger.error("get geo infomation from Baidu error.");
					return new ArrayList<PointOnMap>();
				}
				
				String cityName = addressComponent.getCity();
				
				Map<RegionInfoObject, Integer>  map = ElasticSearchPersonUtil.searchPersonDistrictCount(userBase,userCareer,privacySetting,
						userName, position, companyName,
						industry, jobType, experienceMin,experienceMax, educationMin, tag, gender, isVerified, isStudent, school, schoolLabel,
						major, cityName,zoomLevel);
				
				List<PointOnMap> result = new ArrayList<PointOnMap>();

				for(Entry<RegionInfoObject, Integer> f : map.entrySet())
				{
					
						PointOnMap p = new PointOnMap();
						
						if(f.getValue() == null)
						{
							logger.error("there is no RegionInfoObject for the city" );
							continue;
						}
						
						p.setPointLevel(PointOnMapConstants.POINT_TYPE_COUNT_AT_CITY);
						p.setObjectType(MapObjectConstants.OBJECT_TYPE_PERSON);
						p.setCity(cityName);
						p.setDistrict(f.getKey().getRegionName());
						p.setPonitInfo(f.getKey().getRegionName());
						p.setObjectCount(f.getValue());
						p.setLatitude(f.getKey().getRegionLat());
						p.setLongitude(f.getKey().getRegionLon());
						
						if(p.getLatitude()!=0||p.getLongitude()!=0)
							result.add(p);
					
				}
				return result;
				
			
		}
		else
		{
			
			List<BaseMapObject> baseMapObjectList = new ArrayList<BaseMapObject>();
			
			List<PersonMapObject> pList = ElasticSearchPersonUtil.searchPerson(userBase,userCareer,privacySetting,lat, lon, distance, distanceStep, 
					ElasticSearchConstant.DISTANCE_UNIT_METER_CODE,
					0,0, userName, position,companyName, industry, jobType, experienceMin,experienceMax, educationMin, 
					tag, gender, isVerified, isStudent, school, schoolLabel, major,address, district, city);
			
			baseMapObjectList.addAll(pList);
			
			return PointAggregationUtil.aggregateMapObjectToPointOnMap(baseMapObjectList);
		}
	}






	@Override
	public List<JobMapObject> searchJob(int _userId,double lat, double lon, int distance,int distanceStep,
			int pageNumber,int pageSize, String jobName,String companyName, String tag,
			int salaryMin, int salaryMax, int experience, int educationMin,
			int industry, int jobType,String address, String district, String city,
			String uniqueId, String nameOfPOI) {
		// TODO Auto-generated method stub
		
		List<JobMapObject> list =  ElasticSearchJobUtil.searchJob( _userId,
				lat, lon, 
				distance, distanceStep,ElasticSearchConstant.DISTANCE_UNIT_METER_CODE,pageNumber, pageSize, 
				jobName,companyName, tag, salaryMin, salaryMax, experience, educationMin,
				industry, jobType,address, district, city, uniqueId, nameOfPOI);
		
		List<JobMapObject> resultList =  new ArrayList<JobMapObject>();
		
		if(list == null ||list.size() == 0) return resultList;
		
		for(JobMapObject j : list)
		{
			int authorId = j.getAuthorId();
			
			UserBase ub = userProfileService.getUserBase(authorId);
			UserCareer uc = userProfileService.getUserCareer(authorId);
			
			if(ub == null) 
			{
				logger.error("there is no user with id :" + authorId);
				continue;
			}
			
			j.setAuthorName(ub.getName());
			
			
			if(authorId == _userId||userProfileService.isFriend(_userId, authorId))
				j.setAuthorAvatar(ub.getAvatar());
			else
				j.setAuthorAvatar(ub.getAvatarMask());
			
			if(uc !=null)
				j.setAuthorPosition(uc.getPositionName());
			
			resultList.add(j);
			
		}
		
		return resultList;
	}

	
	
	@Override
	public List<PersonMapObject> searchPerson(int _userId,double lat, double lon,
			int distance,int distanceStep,
			int pageNumber,int pageSize, String userName, String position,String companyName,
			int industry, int jobType, int experienceMin,int experienceMax, int educationMin,
			String tag, int gender, int isVerified, int isStudent,String school,
			String schoolLabel,String major,
			String address,String district,String city) {
		// TODO Auto-generated method stub
		
		if(_userId==0) return null;
		
		UserBase userBase = userProfileService.getUserBase(_userId);
		UserCareer userCareer = userProfileService.getUserCareer(_userId);
		PrivacySetting privacySetting = systemService.getPrivacySetting(_userId);
		
		List<PersonMapObject> list = ElasticSearchPersonUtil.searchPerson(userBase,userCareer,privacySetting,lat, lon, distance, distanceStep, 
				ElasticSearchConstant.DISTANCE_UNIT_METER_CODE,pageNumber, pageSize, userName,position, companyName, industry, jobType,
				experienceMin,experienceMax, educationMin, tag, gender, isVerified, isStudent,
				school,schoolLabel,major,
				address, district, 
				city);
		
		
		if(list!=null)
		{
			
			for(PersonMapObject p : list)
			{
				int userId = p.getUserId();
				
				UserBase userbase = userProfileService.getUserBase(userId);
				
				if(userbase != null)
				{
						p.setAvatar(userbase.getAvatar());
						p.setAvatarMask(userbase.getAvatarMask());
				}
			}

			return list;
		}
		else
			return new ArrayList<PersonMapObject>();
	}

	@Override
	public List<Integer> searchPersonWithoutGeoInfo(String userName,String position,
			String companyName, int industry, int jobType,
			int experienceMin,int experienceMax, int educationMin, String tag, int gender,
			int isVerified, int isStudent,String school,String schoolLabel,String major,
			String address, String district,
			String city, int pageNumber, int pageSize) {
		// TODO Auto-generated method stub
		return ElasticSearchPersonUtil.searchPersonWithoutGeoInfo(userName,  position,companyName,
				 industry,  jobType,  experienceMin, experienceMax, educationMin,
				 tag,  gender,  isVerified,  isStudent,school,schoolLabel,major,
				 address, district, city, pageNumber, pageSize);
	}

	@Override
	public int updatePersonGeoPoint(int _userId, double lat, double lon) {
		// TODO Auto-generated method stub
		
		List<PersonMapObject> personMapObjectList = ElasticSearchPersonUtil.getPersonByUserId(_userId);
		
		if(personMapObjectList == null || personMapObjectList.size() == 0)
		{
			UserBase userBase = userProfileService.getUserBase(_userId);

			String docId = personMapObjectService.addNewPersonMapObject(userBase, lat, lon);
			
			if(StringUtils.isEmpty(docId))
			{
				logger.error("No data in ES for updating geopoint and fail to add a new one when User id = "+ _userId +".");
				return 1;
			}
			return 0;
		}
		
		String fomattdAddress = "";
		String district = "";
		String city = "";
		String nameOfPOI = null;
		String uidOfPOI = null;
		double newLat = lat;
		double newLon = lon;
		
		if(lat >0 || lon > 0)
		{
			GeoCodingResponseObject geoCodingResponseObject =  BaiduHttpRequestUtil.getPOIInfoByLocation(lat, lon, true);
			if(geoCodingResponseObject != null)
			{
				GeoCodingObject  geoCodingObject = geoCodingResponseObject.getResult();
				if(geoCodingObject!=null)
				{
					
					List<Position> posList = geoCodingObject.getPois();
					
					if(posList!=null&&posList.size() > 0)
					{
						Position p = null;
						for(Position position : posList)
						{
							if(p==null||Integer.valueOf(p.getDistance())>Integer.valueOf(position.getDistance()))
							{
								p = position;
							}
						}
						
						
						nameOfPOI = p.getName();
						uidOfPOI = p.getUid();
						newLat = p.getPoint().getLat();
						newLon = p.getPoint().getLng();
						
						AddressComponent addressComponent = geoCodingObject.getAddressComponent();
						if(addressComponent!=null)
						{
							district = addressComponent.getDistrict();
							city = addressComponent.getCity();
						}
						
						
					}else
					{
						fomattdAddress = geoCodingObject.getFormatted_address();
						AddressComponent addressComponent = geoCodingObject.getAddressComponent();
						if(addressComponent!=null)
						{
							district = addressComponent.getDistrict();
							city = addressComponent.getCity();
						}
					}
				}
			}
		}
		
		Map<String,String> map = new HashMap<String,String>();
		
		for(PersonMapObject p :  personMapObjectList)
		{
			
			PrivacySetting privacySetting = systemService.getPrivacySetting(p.getUserId());
			
			p.setAddress(fomattdAddress);
			p.setDistrict(district);
			p.setCity(city);
			p.setNameOfPOI(nameOfPOI);
			p.setUidOfPOI(uidOfPOI);
			p.setLocation(new LocationInMapObject(newLat, newLon));
			p.setIsColleagueVisibility(privacySetting.getColleagueVisibility());
			p.setIsVisibility(privacySetting.getVisibility());
			p.setUpdateTime(System.currentTimeMillis());
			
			String json = null;
			
			try {
				json = objectMapper.writeValueAsString(p);
				
				map.put(p.get_id(), json);
				
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				logger.error("PointOnMapServiceImpl.updatePersonMapObject",e);
			}
		}
		
		return ElasticSearchUtil.updateDocs(MapObjectConstants.OBJECT_TYPE_PERSON, map)>0 ? 0:1;
	}
	

	public IJobService getJobService() {
		return jobService;
	}

	public void setJobService(IJobService jobService) {
		this.jobService = jobService;
	}

	public IUserProfileService getUserProfileService() {
		return userProfileService;
	}

	public void setUserProfileService(IUserProfileService userProfileService) {
		this.userProfileService = userProfileService;
	}
	
	public IPoiInfoService getPoiInfoService() {
		return poiInfoService;
	}

	public void setPoiInfoService(IPoiInfoService poiInfoService) {
		this.poiInfoService = poiInfoService;
	}
	
	public UserLabelDAO getUserLabelDAO() {
		return userLabelDAO;
	}

	public void setUserLabelDAO(UserLabelDAO userLabelDAO) {
		this.userLabelDAO = userLabelDAO;
	}
	
	public IRegionInfoObjectDAO getRegionInfoObjectDAO() {
		return regionInfoObjectDAO;
	}

	public void setRegionInfoObjectDAO(IRegionInfoObjectDAO regionInfoObjectDAO) {
		this.regionInfoObjectDAO = regionInfoObjectDAO;
	}
	
	public IPersonMapObjectService getPersonMapObjectService() {
		return personMapObjectService;
	}

	public void setPersonMapObjectService(
			IPersonMapObjectService personMapObjectService) {
		this.personMapObjectService = personMapObjectService;
	}

	
	private  Map<RegionInfoObject, Integer> getCachedRegionInfoCountMap(int ObjectType)
	{
		
		Map<RegionInfoObject, Integer> m = null;
		
		String jobJson = null;
		
		if(ObjectType == MapObjectConstants.OBJECT_TYPE_JOB)
		{
			 jobJson = redisCache.get(MapObjectConstants.MAP_OBJECT_CACHE_PREFIX, 
					MapObjectConstants.COUNTRY_LEVEL_CACHE_KEY_JOB, String.class);
		}
		
		if(ObjectType == MapObjectConstants.OBJECT_TYPE_PERSON)
		{
			 jobJson = redisCache.get(MapObjectConstants.MAP_OBJECT_CACHE_PREFIX, 
						MapObjectConstants.COUNTRY_LEVEL_CACHE_KEY_PERSON, String.class);
		}
		
		
		if(StringUtils.isNotEmpty(jobJson))
		{
			try {
				m = new HashMap<RegionInfoObject, Integer>();
				
				Map<?,?> cacheMap = objectMapper.readValue(jobJson, Map.class);
				
				if(cacheMap!=null&&cacheMap.size() > 0)
				{
					for(Entry<?, ?> e : cacheMap.entrySet())
					{
						String key = (String) e.getKey();
						Integer value = (Integer) e.getValue();
						RegionInfoObject regionInfo = objectMapper.readValue(key, RegionInfoObject.class);
						
						m.put(regionInfo, value);
					}
				}
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				logger.error("PointOnMapServiceImpl.getPointsOnMap",e1);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				logger.error("PointOnMapServiceImpl.getPointsOnMap",e1);
			}
		}
		
		return m;
	}
	
	private void setRegionInfoCountMapToCache(int objectType,int expireTime,Map<RegionInfoObject, Integer> m)
	{
		if(m!=null&&m.size() > 0)
		{
			try {
				
				Map<String,Integer> cacheMap = new HashMap<String,Integer>();
				
				for(Entry<RegionInfoObject, Integer> e: m.entrySet())
				{
					cacheMap.put(objectMapper.writeValueAsString(e.getKey()), e.getValue());
				}
				
				String json = objectMapper.writeValueAsString(cacheMap);
				
				if(objectType == MapObjectConstants.OBJECT_TYPE_JOB)
				{
					redisCache.set(MapObjectConstants.MAP_OBJECT_CACHE_PREFIX, 
							MapObjectConstants.COUNTRY_LEVEL_CACHE_KEY_JOB, json, 
							expireTime);
				}else if(objectType == MapObjectConstants.OBJECT_TYPE_PERSON)
				{
					redisCache.set(MapObjectConstants.MAP_OBJECT_CACHE_PREFIX, 
							MapObjectConstants.COUNTRY_LEVEL_CACHE_KEY_PERSON, json, 
							expireTime);
				}
				
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				logger.error("PointOnMapServiceImpl.getPointsOnMap",e1);
			}
		}
	}
	
	
	@Override
	public List<PointOnMap> getSomeCampaignPoint(int _userId) {
		// TODO Auto-generated method stub
		
		return new ArrayList<PointOnMap>();
//		if(_userId == 0) return new ArrayList<PointOnMap>();
//		
//		List<PersonMapObject>  userList = ElasticSearchPersonUtil.getPersonByUserId(_userId);
//		
//		if(userList == null || userList.size() ==0)return new ArrayList<PointOnMap>();
//		
//		PersonMapObject p = userList.get(0);
//		
//		LocationInMapObject l = p.getLocation();
//				
//		if(l==null) return new ArrayList<PointOnMap>();
//		
//		List<String> citys = new ArrayList<String>();
//		citys.add("北京市");
//		
//		CampaignInfoModel m = new CampaignInfoModel();
//		
//		m.setCityNames("北京市,天津市");
//		m.setBaseUrl("http://www.baidu.com/");
//		m.setIsOnline(1);
//		m.setShareUrl("http://www.sina.com.cn/");
//		m.setIconUrl("http://bbs.saraba1st.com/2b/static/image/s1/hot_1.gif");
//		m.setExpireTime(360000);
//		m.setIsPointsRandom(1);
//		m.setName("红包抽奖");
//		m.setEndTime(System.currentTimeMillis() + 1000000);
//		m.setFreeChancesCount(1);
//		m.setExtraChancesCount(3);
//		m.setLotteryRate(0.3);
//		
//		return PointAggregationUtil.aggregateMapObjectToPointOnMap(
//				ElasticSearchCampaignUtil.getSomeCampainPoint(l.getLat(), l.getLon(), _userId,m));
	}

	public ISystemService getSystemService() {
		return systemService;
	}

	public void setSystemService(ISystemService systemService) {
		this.systemService = systemService;
	}

	public IRedisCache getRedisCache() {
		return redisCache;
	}

	public void setRedisCache(IRedisCache redisCache) {
		this.redisCache = redisCache;
	}

}
