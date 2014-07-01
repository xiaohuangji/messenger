/**
 * 
 */
package com.dajie.message.elasticsearch.map.utils.elasticsearch;


import static com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchQueryUtil.countJobByMapQuery;
import static com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchQueryUtil.searchByMapQuery;
import static com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchQueryUtil.searchJobQuery;
import static com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchQueryUtil.searchJobQueryByScroll;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
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
import com.dajie.message.model.map.JobMapObject;
import com.dajie.message.model.map.RegionInfoObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author tianyuan.zhu
 *
 */
public class ElasticSearchJobUtil {
	
	private static Logger logger = LoggerManager.getLogger(ElasticSearchJobUtil.class);
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	
	public static List<JobMapObject> getJobByJid(int jid)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("jobId", jid);
		
		SearchResponse  searchResponse = searchByMapQuery(ElasticSearchConstant.INDEX_NAME, 
				ElasticSearchConstant.JOB_TYPE_NAME, map, 0, 500);
		
		if(searchResponse.status() != RestStatus.OK)
		{
			logger.error("there is something wrong when search jobs. error status is " +searchResponse.status().toString());
			return null;
		}
		
		SearchHit[] searchHits = searchResponse.getHits().getHits();
		List<JobMapObject> resultList = new ArrayList<JobMapObject>();
		
		try {
		if(searchHits.length > 0)
		{
			for(SearchHit s : searchHits)
			{
				String source = s.getSourceAsString();
				String _id = s.getId();
				
				
				JobMapObject j= objectMapper.readValue(source, JobMapObject.class);
				j.set_id(_id);
				resultList.add(j);
			}
		}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			logger.error("ElasticSearchUtil.getJobByJid",e);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			logger.error("ElasticSearchUtil.getJobByJid",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("ElasticSearchUtil.getJobByJid",e);
		}
		
		return resultList;
		
	}
	
	public static List<JobMapObject> getJobByJidAndUid(int jid,String uniqueId)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("jobId", jid);
		map.put("uidOfPOI", uniqueId);
		
		SearchResponse  searchResponse = searchByMapQuery(ElasticSearchConstant.INDEX_NAME, 
				ElasticSearchConstant.JOB_TYPE_NAME, map, 0, 500);
		
		if(searchResponse.status() != RestStatus.OK)
		{
			logger.error("there is something wrong when search jobs. error status is " +searchResponse.status().toString());
			return null;
		}
		
		SearchHit[] searchHits = searchResponse.getHits().getHits();
		List<JobMapObject> resultList = new ArrayList<JobMapObject>();
		
		try {
		if(searchHits.length > 0)
		{
			for(SearchHit s : searchHits)
			{
				String source = s.getSourceAsString();
				String _id = s.getId();
				
				
				JobMapObject j= objectMapper.readValue(source, JobMapObject.class);
				j.set_id(_id);
				resultList.add(j);
			}
		}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			logger.error("ElasticSearchUtil.searchJob",e);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			logger.error("ElasticSearchUtil.searchJob",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("ElasticSearchUtil.searchJob",e);
		}
		
		return resultList;
		
	}
	
	
	public static List<JobMapObject> searchJob(int _userId,double lat,double lon,int distance,int distanceStep,int distanceUnit,
			int pageNumber,int pageSize,String jobName,String companyName,String tag,int salaryMin,int salaryMax,
			int experience,int educationMin,int industry, int jobType,
			String address,String district,String city,String uniqueId,String nameOfPOI){
		
		if(pageSize <= 0)
		{
			return searchAllJob(_userId,lat, lon, distance, distanceStep, distanceUnit, jobName, 
					companyName, tag, salaryMin, salaryMax, experience, educationMin, 
					industry, jobType, address, district, city, uniqueId, nameOfPOI);
		}
		
		SearchResponse searchResponse = searchJobQuery(ElasticSearchConstant.INDEX_NAME, ElasticSearchConstant.JOB_TYPE_NAME,
				lat, lon, distance,distanceStep,
				distanceUnit, pageNumber,pageSize, jobName,
				companyName, tag, salaryMin, salaryMax, experience, 
				educationMin, industry, jobType, address, district, city, uniqueId, nameOfPOI);
		
		if(searchResponse.status() != RestStatus.OK)
		{
			logger.error("there is something wrong when search jobs. error status is " +searchResponse.status().toString());
			return null;
		}
		
		
		SearchHit[] searchHits = searchResponse.getHits().getHits();
		List<JobMapObject> resultList = new ArrayList<JobMapObject>();
		
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
				JobMapObject j= objectMapper.readValue(source, JobMapObject.class);
				j.set_id(_id);
				j.setDistance(distanceFromCenter.longValue());
				resultList.add(j);
			}
		}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			logger.error("ElasticSearchUtil.searchJob",e);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			logger.error("ElasticSearchUtil.searchJob",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("ElasticSearchUtil.searchJob",e);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("ElasticSearchUtil.searchJob",e);
		}
		
		return resultList;
	}
	
	public static List<JobMapObject> searchAllJob(int _userId,double lat,double lon,int distance,int distanceStep,int distanceUnit,
			String jobName,String companyName,String tag,int salaryMin,int salaryMax,
			int experience,int educationMin,int industry, int jobType,
			String address,String district,String city,String uniqueId,String nameOfPOI)
	{
		
		List<SearchResponse> searchResponseList = searchJobQueryByScroll(ElasticSearchConstant.INDEX_NAME, ElasticSearchConstant.JOB_TYPE_NAME,
				lat, lon, distance, distanceStep, distanceUnit, jobName, companyName, tag, salaryMin, salaryMax,
				experience, educationMin, industry, jobType, address, district, city, uniqueId, nameOfPOI);
		
		List<JobMapObject> resultList = new ArrayList<JobMapObject>();
		
		if(searchResponseList!=null&&searchResponseList.size()>0)
		{
			for(SearchResponse searchResponse : searchResponseList)
			{
				if(searchResponse.status() != RestStatus.OK)
				{
					logger.error("there is something wrong when search jobs. error status is " +searchResponse.status().toString());
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
						JobMapObject j= objectMapper.readValue(source, JobMapObject.class);
						j.set_id(_id);
						j.setDistance(distanceFromCenter.longValue());
						resultList.add(j);
					}
				}
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					logger.error("ElasticSearchUtil.searchJob",e);
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					logger.error("ElasticSearchUtil.searchJob",e);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error("ElasticSearchUtil.searchJob",e);
				} catch (Exception e) {
					// TODO: handle exception
					logger.error("ElasticSearchUtil.searchJob",e);
				}
			}
		}
		return resultList;
	}
	

	
	public static Map<RegionInfoObject, Integer> searchJobCount(
			String jobName, String companyName,
			String tag, int salaryMin, int salaryMax, int experience,
			int educationMin, int industry, int jobType,String city,int zoomLevel)
	{
		
		List<RegionInfoObject>  regionList = null;
		
		Map<RegionInfoObject, Integer> resultMap = new HashMap<RegionInfoObject, Integer>();
		
		Map<RegionInfoObject, SearchResponse>  countResultMap = new HashMap<RegionInfoObject, SearchResponse>();
		
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
			
			countResultMap = countJobByMapQuery(jobName, companyName, tag, salaryMin, 
						salaryMax, experience, educationMin, industry, jobType, regionList,PointOnMapConstants.POINT_TYPE_COUNT_AT_DISTRICT,city);
		}
		else if(zoomLevel == PointOnMapConstants.POINT_TYPE_COUNT_AT_CITY)
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
			
			
			countResultMap = countJobByMapQuery(jobName, companyName, tag, salaryMin, 
					salaryMax, experience, educationMin, industry, jobType, regionList,PointOnMapConstants.POINT_TYPE_COUNT_AT_CITY,null);
		}
		
		
			for(Entry<RegionInfoObject, SearchResponse> c : countResultMap.entrySet())
			{
			
				SearchResponse countResult = c.getValue();
				
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
									c.getKey().setRegionLat(lat);
									c.getKey().setRegionLon(lon);
								}
							}
							
						} catch (JsonProcessingException e) {
							// TODO Auto-generated catch block
							logger.error("ElasticSearchUtil.searchPersonDistrictCount",e);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							logger.error("ElasticSearchUtil.searchPersonDistrictCount",e);
						}
						
					}
					
				}
				
				resultMap.put(c.getKey(), (int)countResult.getHits().getTotalHits());
			}
		}
		
		return resultMap;
		
	}

}
