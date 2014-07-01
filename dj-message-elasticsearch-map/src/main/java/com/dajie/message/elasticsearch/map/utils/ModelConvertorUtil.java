package com.dajie.message.elasticsearch.map.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.dajie.message.elasticsearch.map.constants.MapObjectConstants;
import com.dajie.message.model.job.JobModel;
import com.dajie.message.model.map.JobMapObject;
import com.dajie.message.model.map.LocationInMapObject;
import com.dajie.message.model.map.PersonMapObject;
import com.dajie.message.model.map.PoiInfoObject;
import com.dajie.message.model.map.geocode.AddressComponent;
import com.dajie.message.model.map.geocode.GeoCodingObject;
import com.dajie.message.model.map.geocode.GeoCodingResponseObject;
import com.dajie.message.model.map.geocode.Position;
import com.dajie.message.model.map.place.PlaceObject;
import com.dajie.message.model.system.PrivacySetting;
import com.dajie.message.model.user.UserBase;
import com.dajie.message.model.user.UserCareer;
import com.dajie.message.model.user.UserEducation;

public class ModelConvertorUtil {
	
	private static Logger logger = LoggerManager.getLogger(ModelConvertorUtil.class);
	
	public static List<JobMapObject> convertJobModelsToJobMapObjects(JobModel jobModel,String tag,List<PoiInfoObject> poiInfoObjectList)
	{
		if(jobModel == null||poiInfoObjectList == null||poiInfoObjectList.size() == 0)
		{
			logger.error("jobModel or poiInfoObjectList is null in convertJobModelToJobMapObject.");
			return null;
		}
		
		JobMapObject original = convertJobModelToJobMapObject(jobModel,tag);
		
		List<JobMapObject> resultList = new ArrayList<JobMapObject>();
		
		
		for(PoiInfoObject p : poiInfoObjectList)
		{
			JobMapObject replica = new JobMapObject();
			
			BeanUtils.copyProperties(original, replica);
			
			replica.setAddress(p.getAddress());
			replica.setCity(p.getCity());
			replica.setDistrict(p.getDistrict());
			replica.setProvince(p.getProvince());
			
			LocationInMapObject l = new LocationInMapObject();
			l.setLat(p.getLat());
			l.setLon(p.getLon());
			replica.setLocation(l);
			replica.setNameOfPOI(p.getName());
			replica.setUidOfPOI(p.getUniqueId());
			
			resultList.add(replica);
		}
		
		
		return resultList;
	}
	
	public static JobMapObject convertJobModelToJobMapObject(JobModel jobModel,String tag)
	{
		if(jobModel == null)
		{
			logger.error("jobModel or poiInfoObjectList is null in convertJobModelToJobMapObject.");
			return null;
		}
		
		JobMapObject original = new JobMapObject();
		
		original.setObjectType(MapObjectConstants.OBJECT_TYPE_JOB);
		
		original.setAuthorId(jobModel.getUserId());
		original.setCompanyId(jobModel.getCorpId()+"");
		original.setCompanyName(jobModel.getCorpName());
		original.setStatus(jobModel.getStatus());
	
		original.setCreateTime(jobModel.getCreateDate() == null ? System.currentTimeMillis(): jobModel.getCreateDate().getTime());
		original.setEducation(jobModel.getEducationLevel());
		original.setStartTime(jobModel.getStartDate() == null ? System.currentTimeMillis() : jobModel.getStartDate().getTime());
		original.setEndTime(jobModel.getEndDate() == null ? 0 : jobModel.getEndDate().getTime());
		original.setExperience(jobModel.getWorkExperience());
		original.setJobType(jobModel.getJobType());
		original.setJobId(jobModel.getJobId());
		original.setIndustry(jobModel.getIndustry());
		original.setName(jobModel.getPositionName());
		original.setSalaryMin(jobModel.getSalaryStart());
		original.setSalaryMax(jobModel.getSalaryEnd());
		original.setUpdateTime(jobModel.getUpdateDate() == null ? System.currentTimeMillis():jobModel.getUpdateDate().getTime());
		original.setTag(tag);
		
		return original;
	}
	
	public static PoiInfoObject convertPlaceObjectToPoiInfoObject(PlaceObject placeObject,AddressComponent addressComponent)
	{
		if(placeObject == null||StringUtils.isEmpty(placeObject.getUid())||addressComponent == null)
		{
			logger.error("placeObject is null");
			return null;
		}
		
		PoiInfoObject poiInfoObject = new PoiInfoObject();
		
		poiInfoObject.setAddress(placeObject.getAddress());
		poiInfoObject.setCity(addressComponent.getCity());
		poiInfoObject.setCreateTime(System.currentTimeMillis());
		poiInfoObject.setDistrict(addressComponent.getDistrict());
		poiInfoObject.setLat(placeObject.getLocation().getLat());
		poiInfoObject.setLon(placeObject.getLocation().getLng());
		poiInfoObject.setName(placeObject.getName());
		poiInfoObject.setProvince(addressComponent.getProvince());
		poiInfoObject.setStreet(addressComponent.getStreet());
		poiInfoObject.setStreetNumber(addressComponent.getStreet_number());
		poiInfoObject.setUniqueId(placeObject.getUid());
		poiInfoObject.setUpdateTime(0);
		
		
		return poiInfoObject;
	}
	
	public static PersonMapObject convertUserBaseToPersonMapObject(UserBase userBase,UserCareer userCareer,UserEducation userEducation,PrivacySetting privacySetting,
			String tag,
			GeoCodingResponseObject  geoCodingResponseObject,double lat,double lon)
	{
		
		if(userBase == null) return null;
		
		PersonMapObject personMapObject= new PersonMapObject();
		
		personMapObject.setAvatar(userBase.getAvatar());
		personMapObject.setCreateTime(userBase.getCreateTime() == null ? System.currentTimeMillis() : userBase.getCreateTime().getTime());
		personMapObject.setTag(tag);
		personMapObject.setGender(userBase.getGender());
		personMapObject.setUserId(userBase.getUserId());
		personMapObject.setUpdateTime(0);
		personMapObject.setName(userBase.getName());
		personMapObject.setObjectType(MapObjectConstants.OBJECT_TYPE_PERSON);
		personMapObject.setIsShowable(userBase.getAudit());
		
		if(userCareer!=null)
		{
			personMapObject.setCompanyId(userCareer.getCorpId()+"");
			personMapObject.setCompanyName(userCareer.getCorpName());
			personMapObject.setEducation(userCareer.getEducation());
			personMapObject.setExperience(userCareer.getExperience());
			personMapObject.setPosition(userCareer.getPositionName());
			personMapObject.setIsVerified(userCareer.getVerification());
			personMapObject.setIndustry(userCareer.getIndustry());
			personMapObject.setJobType(userCareer.getPositionType());
		}
		
		if(userEducation != null)
		{
			personMapObject.setSchool(userEducation.getSchool());
			personMapObject.setMajor(userEducation.getMajor());
			personMapObject.setSchoolLabel(userEducation.getLabel());
			
			if(userEducation.getDegree()!=0)
				personMapObject.setEducation(userEducation.getDegree());
		}
		
		if(privacySetting!=null)
		{
			personMapObject.setIsColleagueVisibility(privacySetting.getColleagueVisibility());
			personMapObject.setIsVisibility(privacySetting.getVisibility());
		}
		
		if(geoCodingResponseObject!=null&&geoCodingResponseObject.getResult()!=null)
		{
			GeoCodingObject  g = geoCodingResponseObject.getResult();
			
			List<Position> posList = g.getPois();
			
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
				
				AddressComponent  a = g.getAddressComponent();
				
				personMapObject.setNameOfPOI(p.getName());
				personMapObject.setUidOfPOI(p.getUid());
				personMapObject.setLocation(new LocationInMapObject(p.getPoint().getLat(),p.getPoint().getLng()));
				personMapObject.setAddress(p.getAddr());
				personMapObject.setCity(a.getCity());
				personMapObject.setDistrict(a.getDistrict());
				personMapObject.setProvince(a.getProvince());
			}
			else
			{
				AddressComponent  a = g.getAddressComponent();
			
				personMapObject.setLocation(new LocationInMapObject(lat,lon));
				personMapObject.setAddress(g.getFormatted_address());
				personMapObject.setCity(a.getCity());
				personMapObject.setDistrict(a.getDistrict());
				personMapObject.setProvince(a.getProvince());
			}
		}
		
		
		return personMapObject;
	}
	
	
	public static Position convertPlaceObjectToPosition(PlaceObject placeObject)
	{
		
		if(placeObject == null) return null;
		
		Position position = new Position();
		position.setPoint(placeObject.getLocation());
		position.setAddr(placeObject.getAddress());
		position.setName(placeObject.getName());
		position.setUid(placeObject.getUid());
		position.setTel(placeObject.getTelephone());
		
		return position;
	}
	
	
	public static int exchangeMainSiteExperience(int experience)
	{
		if(experience == 9999)
		{
			return 0;
		}
		
		if(experience < 7)
		{
			return 10;
		}
		
		if(experience >=7&& experience <=20)
		{
			return 20;
		}
		
		if(experience >= 30)
		{
			return 30;
		}
		
		if(experience >= 50)
		{
			return 40;
		}
		
		if(experience >= 80&& experience <1000)
		{
			return 50;
		}
		
		return 0;
	}

}
