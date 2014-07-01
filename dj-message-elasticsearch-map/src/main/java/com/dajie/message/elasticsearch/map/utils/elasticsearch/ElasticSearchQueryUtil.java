package com.dajie.message.elasticsearch.map.utils.elasticsearch;

import static com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchClientUtil.countDoc;
import static com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchClientUtil.searchDocWithScan;
import static com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchClientUtil.searchDocs;
import static com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchClientUtil.searchResultWithScrollId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;

import com.dajie.message.elasticsearch.map.constants.ElasticSearchConstant;
import com.dajie.message.elasticsearch.map.constants.MapObjectConstants;
import com.dajie.message.elasticsearch.map.constants.PointOnMapConstants;
import com.dajie.message.elasticsearch.map.utils.LoggerManager;
import com.dajie.message.model.map.RegionInfoObject;
import com.dajie.message.model.system.PrivacySetting;
import com.dajie.message.model.user.UserBase;
import com.dajie.message.model.user.UserCareer;

public class ElasticSearchQueryUtil{
	
	private static Logger logger = LoggerManager.getLogger(ElasticSearchQueryUtil.class);
	
	public static SearchResponse searchJobQuery(String indexName,String indexType,
			double lat,double lon,int distance,int distanceStep,int distanceUnit,
			int pageNumber,int pageSize,String jobName,String companyName,String tag,int salaryMin,int salaryMax,
			int experience,int educationMin,int industry, int jobType,String address,String district,String city,String uniqueId,
			String nameOfPOI){
		//排序
		SortBuilder sortBuilder = ELasticSearchQueryBuilderUtil.sorterBuider(lat, lon);
		
		QueryBuilder queryBuilder = ELasticSearchQueryBuilderUtil.queryListBuilderForJob(lat, lon, distance, 
				distanceStep, distanceUnit, jobName, companyName, tag, 
				salaryMin, salaryMax, experience, educationMin,
				industry, jobType, address, district, city, uniqueId, nameOfPOI);
		
		String[] indexTypes = new String[1];
		indexTypes[0] = indexType;
		
		//脚本
		ScriptModel scriptModel = new ScriptModel();
		scriptModel.setName(ScriptModel.DEFAULT_NAME);
		scriptModel.setScript(ScriptModel.DEFAULT_SCRIPT);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put(ScriptModel.DEFAULT_PARAM_LAT, lat);
		map.put(ScriptModel.DEFAULT_PARAM_LON, lon);
		scriptModel.setParams(map);
		
		SearchResponse searchResponse =  searchDocs(indexName, indexTypes, queryBuilder, null, scriptModel,sortBuilder,pageNumber*pageSize, pageSize);

		logger.debug(searchResponse.toString());
		
		return searchResponse;
	}
	
	
	public static List<SearchResponse> searchJobQueryByScroll(String indexName,String indexType,
			double lat,double lon,int distance,int distanceStep,int distanceUnit,
			String jobName,String companyName,String tag,int salaryMin,int salaryMax,
			int experience,int educationMin,int industry, int jobType,String address,String district,String city,String uniqueId,
			String nameOfPOI){
		
		QueryBuilder queryBuilder = ELasticSearchQueryBuilderUtil.queryListBuilderForJob(lat, lon, distance, 
				distanceStep, distanceUnit, jobName, companyName, tag, 
				salaryMin, salaryMax, experience, educationMin,
				industry, jobType, address, district, city, uniqueId, nameOfPOI);
		
		String[] indexTypes = new String[1];
		indexTypes[0] = indexType;
		
		//脚本
		ScriptModel scriptModel = new ScriptModel();
		scriptModel.setName(ScriptModel.DEFAULT_NAME);
		scriptModel.setScript(ScriptModel.DEFAULT_SCRIPT);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put(ScriptModel.DEFAULT_PARAM_LAT, lat);
		map.put(ScriptModel.DEFAULT_PARAM_LON, lon);
		scriptModel.setParams(map);
		
		SearchResponse searchResponse =  searchDocWithScan(indexName, indexTypes, queryBuilder, null, scriptModel, 0);

		logger.debug(searchResponse.toString());
		
		String scrollId = searchResponse.getScrollId();
		
		if(StringUtils.isNotEmpty(scrollId))
		{
			return searchResultWithScrollId(scrollId);
		}
		
		return new ArrayList<SearchResponse>();
	}
	
	
	public static SearchResponse searchPersonQuery(boolean showAll,UserBase userBase,UserCareer userCareer,PrivacySetting privacySetting,String indexName,String indexType,
			double lat, double lon,
			int distance, int distanceStep,int distanceUnit,
			int pageNumber,int pageSize, String userName,String position,String companyName, 
			int industry, int jobType, int experienceMin,int experienceMax, int educationMin,
			String tag, int gender, int isVerified, int isStudent,String school,String schoolLabel,String major,
			String address,String district,String city)
	{
				//排序
				SortBuilder sortBuilder = ELasticSearchQueryBuilderUtil.sorterBuider(lat, lon);
				
				
				QueryBuilder queryBuilderList = ELasticSearchQueryBuilderUtil.queryListBuilderForPerson(showAll, userCareer == null? 0:userCareer.getCorpId(),
						privacySetting == null? 0:privacySetting.getColleagueVisibility(),lat, lon, distance,
						distanceStep, distanceUnit, userName, position,companyName, industry, jobType, 
						experienceMin,experienceMax, educationMin, tag, gender, isVerified, isStudent, 
						school, schoolLabel, major, address, district, city);
				
				String[] indexTypes = new String[1];
				indexTypes[0] = indexType;
				
				//脚本
				ScriptModel scriptModel = new ScriptModel();
				scriptModel.setName(ScriptModel.DEFAULT_NAME);
				scriptModel.setScript(ScriptModel.DEFAULT_SCRIPT);
				
				Map<String,Object> map = new HashMap<String,Object>();
				map.put(ScriptModel.DEFAULT_PARAM_LAT, lat);
				map.put(ScriptModel.DEFAULT_PARAM_LON, lon);
				scriptModel.setParams(map);
				
				SearchResponse searchResponse =  searchDocs(indexName, indexTypes, queryBuilderList, null, scriptModel,sortBuilder,pageNumber*pageSize, pageSize);

				logger.debug(searchResponse.toString());
				
				
				return searchResponse;
	}
	
	
	public static List<SearchResponse> searchPersonQueryByScroll(boolean showAll,UserBase userBase,UserCareer userCareer,PrivacySetting privacySetting,String indexName,String indexType,
			double lat, double lon,
			int distance, int distanceStep,int distanceUnit,
			String userName,String position, String companyName,
			int industry, int jobType, int experienceMin,int experienceMax, int educationMin,
			String tag, int gender, int isVerified, int isStudent,String school,String schoolLabel,String major,
			String address,String district,String city)
	{
				QueryBuilder queryBuilderList = ELasticSearchQueryBuilderUtil.queryListBuilderForPerson(showAll, userCareer == null? 0:userCareer.getCorpId(),
						privacySetting == null?0:privacySetting.getColleagueVisibility(),lat, lon, distance, distanceStep, 
						distanceUnit, userName, position,companyName, industry, jobType, experienceMin,  experienceMax,
						educationMin, tag, gender, isVerified, isStudent, school, schoolLabel, major, address, 
						district, city);
				
				String[] indexTypes = new String[1];
				indexTypes[0] = indexType;
				
				//脚本
				ScriptModel scriptModel = new ScriptModel();
				scriptModel.setName(ScriptModel.DEFAULT_NAME);
				scriptModel.setScript(ScriptModel.DEFAULT_SCRIPT);
				
				Map<String,Object> map = new HashMap<String,Object>();
				map.put(ScriptModel.DEFAULT_PARAM_LAT, lat);
				map.put(ScriptModel.DEFAULT_PARAM_LON, lon);
				scriptModel.setParams(map);
				
				SearchResponse searchResponse =  searchDocWithScan(indexName, indexTypes, queryBuilderList, null, scriptModel, 0);

				logger.debug(searchResponse.toString());
				
				String scrollId = searchResponse.getScrollId();
				
				if(StringUtils.isNotEmpty(scrollId))
				{
					return searchResultWithScrollId(scrollId);
				}
				
				return new ArrayList<SearchResponse>();
	}
	

	
	public static SearchResponse searchPersonQuery(String indexName,String indexType,
			String userName,String position, String companyName,
			int industry, int jobType, int experienceMin, int educationMin,int educationMax,
			String tag, int gender, int isVerified, int isStudent,String school,String schoolLabel,String major,
			String address,String district,String city,int pageNumber,int pageSize)
	{
				//query
				QueryBuilder query = ELasticSearchQueryBuilderUtil.queryListBuilderForPerson(true, 0, 0,0, 0, 
						0, 0, ElasticSearchConstant.DISTANCE_UNIT_METER_CODE, userName, position, companyName, industry, 
						jobType, experienceMin, educationMin,educationMax, tag, gender, isVerified, isStudent, school, schoolLabel, 
						major, address, district, city);
				
				//排序
				SortBuilder sortBuilder = SortBuilders.fieldSort("userId").order(SortOrder.ASC);
				
				
				String[] indexTypes = new String[1];
				indexTypes[0] = indexType;
				
				SearchResponse searchResponse =  searchDocs(indexName, indexTypes, query, null, null,sortBuilder,pageNumber*pageSize, pageSize);

				logger.debug(searchResponse.toString());
				
				return searchResponse;
	}
	
	
	
	public static SearchResponse searchByMapQuery(String indexName,String indexType,Map<String,Object> map,
			int page,int pageSize)
	{
		List<String> indexTypes = new ArrayList<String>();
		indexTypes.add(indexType);
		
		return searchByMapQuery(indexName, indexTypes, map, page, pageSize);
	}
	
	public static SearchResponse searchByMapQuery(String indexName,List<String> indexTypes,Map<String,Object> map,
			int page,int pageSize)
	{
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		
		for(Entry<String, Object> e : map.entrySet())
		{
			MatchQueryBuilder KVMatcher= QueryBuilders.matchQuery(e.getKey(), e.getValue());
			boolQuery.must(KVMatcher);
		}
		
		SearchResponse sr = searchDocs(indexName, indexTypes.toArray(new String[0]), boolQuery, 
				null, null,null, page*pageSize , pageSize);
		
		return sr;

	}

	public static Map<RegionInfoObject,SearchResponse> countJobByRegionInfoObject(List<RegionInfoObject> list,int zoomLevel,String cityName)
	{
		
		Map<RegionInfoObject,QueryBuilder> queryMap = new HashMap<RegionInfoObject,QueryBuilder>();

		Map<RegionInfoObject,SortBuilder> sortBuilderMap = new HashMap<RegionInfoObject,SortBuilder>();
		
		for(RegionInfoObject r : list)
		{
				QueryBuilder  query = null;
				if(zoomLevel == PointOnMapConstants.POINT_TYPE_COUNT_AT_CITY)
				{
					query = ELasticSearchQueryBuilderUtil.queryListBuilderForJob(r.getRegionLat(), r.getRegionLon(), 
							0, 0,
							ElasticSearchConstant.DISTANCE_UNIT_METER_CODE, null, null, null, 0, 0, 0, 
							0, 0, 0, null, null, r.getRegionName(), null, null);
					
				}else if(zoomLevel == PointOnMapConstants.POINT_TYPE_COUNT_AT_DISTRICT )
				{
					query = ELasticSearchQueryBuilderUtil.queryListBuilderForJob(r.getRegionLat(), r.getRegionLon(), 
							0, 0,
							ElasticSearchConstant.DISTANCE_UNIT_METER_CODE, null, null, null, 0, 0, 0, 
							0, 0, 0, null, r.getRegionName(),cityName , null, null);
				}
				if(query!=null)
				{
					queryMap.put(r, query);
					sortBuilderMap.put(r, ELasticSearchQueryBuilderUtil.sorterBuider(r.getRegionLat(), r.getRegionLon()));
				}
		}

		String [] indexTypes = new String[1];
		indexTypes[0] = ElasticSearchConstant.JOB_TYPE_NAME;

		return countDoc(ElasticSearchConstant.INDEX_NAME, indexTypes, queryMap,sortBuilderMap);
	}
	
	public static SearchResponse countJobByMapQuery(Map<String,Object> map)
	{
		
		BoolQueryBuilder  boolQuery = QueryBuilders.boolQuery();
		
		for(Entry<String, Object> e : map.entrySet())
		{
				QueryBuilder KVMatcher= QueryBuilders.matchQuery(e.getKey(), e.getValue());
				boolQuery.must(KVMatcher);
		}
		
		boolQuery.must(QueryBuilders.rangeQuery("endTime").gte(System.currentTimeMillis()));
		boolQuery.must(QueryBuilders.termQuery("status", 0));
		
		String [] indexTypes = new String[1];
		
		indexTypes[0] = ElasticSearchConstant.JOB_TYPE_NAME;
		
		return countDoc(ElasticSearchConstant.INDEX_NAME, indexTypes, boolQuery,null);
		


	}
	
	public static Map<RegionInfoObject, SearchResponse> countJobByMapQuery(
			String jobName, String companyName,
			String tag, int salaryMin, int salaryMax, int experience,
			int educationMin, int industry, int jobType,List<RegionInfoObject> regionList,int zoomLevel,String cityName)
	{

		
		if(regionList == null || regionList.size() ==0) return null;
		
		Map<RegionInfoObject,QueryBuilder> queryBuilderMap = new HashMap<RegionInfoObject,QueryBuilder>();
		
		Map<RegionInfoObject,SortBuilder> sortBuilderMap = new HashMap<RegionInfoObject,SortBuilder>();
		
		for(RegionInfoObject r : regionList)
		{
		
			QueryBuilder queryBuilder = null;
			if(zoomLevel == PointOnMapConstants.POINT_TYPE_COUNT_AT_CITY)
			{
				queryBuilder = ELasticSearchQueryBuilderUtil.queryListBuilderForJob(r.getRegionLat(), r.getRegionLon(), 0, 
						0, ElasticSearchConstant.DISTANCE_UNIT_METER_CODE, jobName, companyName, tag, 
						salaryMin, salaryMax, experience, educationMin,
						industry, jobType, null, null, r.getRegionName(), null, null);
			}else if(zoomLevel == PointOnMapConstants.POINT_TYPE_COUNT_AT_DISTRICT)
			{
				queryBuilder = ELasticSearchQueryBuilderUtil.queryListBuilderForJob(r.getRegionLat(), r.getRegionLon(), 0, 
				0, ElasticSearchConstant.DISTANCE_UNIT_METER_CODE, jobName, companyName, tag, 
				salaryMin, salaryMax, experience, educationMin,
				industry, jobType, null, r.getRegionName(), cityName, null, null);
			}
			
			if(queryBuilder!=null)
			{
				queryBuilderMap.put(r, queryBuilder);
				sortBuilderMap.put(r, ELasticSearchQueryBuilderUtil.sorterBuider(r.getRegionLat(), r.getRegionLon()));
			}
		}
		
		String [] indexTypes = new String[1];
		
		indexTypes[0] = ElasticSearchConstant.JOB_TYPE_NAME;
		
		Map<RegionInfoObject, SearchResponse> sr = countDoc(ElasticSearchConstant.INDEX_NAME, indexTypes, queryBuilderMap,sortBuilderMap);
		
		return sr;
	}
	
	public static Map<RegionInfoObject, SearchResponse> countPersonByMapQuery(UserBase userBase,UserCareer userCareer,PrivacySetting privacySetting,
			String userName,String position,String companyName,
			int industry, int jobType, int experienceMin,int experienceMax,
			int educationMin, String tag, int gender, int isVerified,
			int isStudent,String school,String schoolLabel,String major,List<RegionInfoObject> regionList,int zoomLevel,String cityName)
	{

		if(regionList == null || regionList.size() ==0) return null;
		
		Map<RegionInfoObject,QueryBuilder> queryBuilderMap = new HashMap<RegionInfoObject,QueryBuilder>();
		
		Map<RegionInfoObject,SortBuilder> sortBuilderMap = new HashMap<RegionInfoObject,SortBuilder>();

		QueryBuilder queryBuilder = null;
		
		for(RegionInfoObject r : regionList)
		{
			if(zoomLevel == PointOnMapConstants.POINT_TYPE_COUNT_AT_CITY)
			{
				queryBuilder = ELasticSearchQueryBuilderUtil.queryListBuilderForPerson(false,userCareer == null? 0:userCareer.getCorpId(),
						privacySetting == null ? 0 : privacySetting.getColleagueVisibility(),r.getRegionLat(), r.getRegionLon(), 0, 0, 
						ElasticSearchConstant.DISTANCE_UNIT_METER_CODE, userName, position, 
						companyName, industry, jobType, experienceMin,experienceMax,
						educationMin, tag, gender, isVerified, isStudent, school, schoolLabel, major, 
						null,null , r.getRegionName());
			}
			else if(zoomLevel == PointOnMapConstants.POINT_TYPE_COUNT_AT_DISTRICT)
			{
				queryBuilder = ELasticSearchQueryBuilderUtil.queryListBuilderForPerson(false,userCareer == null? 0:userCareer.getCorpId(),
						privacySetting == null ? 0 : privacySetting.getColleagueVisibility(),r.getRegionLat(), r.getRegionLon(), 0, 0, 
						ElasticSearchConstant.DISTANCE_UNIT_METER_CODE, userName, position, 
						companyName, industry, jobType, experienceMin,experienceMax,
						educationMin, tag, gender, isVerified, isStudent, school, schoolLabel, major, 
						null, r.getRegionName(), cityName);
			}
			
			if(queryBuilder!= null)
			{
				queryBuilderMap.put(r, queryBuilder);
				sortBuilderMap.put(r, ELasticSearchQueryBuilderUtil.sorterBuider(r.getRegionLat(), r.getRegionLon()));
			}
		}
		
		String [] indexTypes = new String[1];
		
		indexTypes[0] = ElasticSearchConstant.PERSON_TYPE_NAME;
		
		Map<RegionInfoObject, SearchResponse> sr = countDoc(ElasticSearchConstant.INDEX_NAME, indexTypes, queryBuilderMap,sortBuilderMap);
		
		return sr;
	}
	
	public static Map<RegionInfoObject, SearchResponse> countPersonByRegionInfoObject(List<RegionInfoObject> list,int zoomLevel,String cityName)
	{
		
		Map<RegionInfoObject,QueryBuilder> queryMap = new HashMap<RegionInfoObject,QueryBuilder>();
		
		Map<RegionInfoObject,SortBuilder> sortBuilderMap = new HashMap<RegionInfoObject,SortBuilder>();
		
		for(RegionInfoObject r : list)
		{
			QueryBuilder query = null;
			
				if(zoomLevel == PointOnMapConstants.POINT_TYPE_COUNT_AT_CITY)
				{
					query = ELasticSearchQueryBuilderUtil.queryListBuilderForPerson(false, 0,0, 
							r.getRegionLat(), r.getRegionLon(), 0, 0, ElasticSearchConstant.DISTANCE_UNIT_METER_CODE, 
							null, null, null, 
							0, 0, 0, 0, 0, null, 0, 0, 0, null, 
							null, null, null, null, r.getRegionName());
				}else if(zoomLevel == PointOnMapConstants.POINT_TYPE_COUNT_AT_DISTRICT )
				{
					query = ELasticSearchQueryBuilderUtil.queryListBuilderForPerson(false, 0, 0,
							r.getRegionLat(), r.getRegionLon(), 0, 0, ElasticSearchConstant.DISTANCE_UNIT_METER_CODE, 
							null, null, null, 
							0, 0, 0, 0, 0, null, 0, 0, 0, null, 
							null, null, null, r.getRegionName(), cityName);
				}
				
				if(query!=null)
				{
					queryMap.put(r, query);
					sortBuilderMap.put(r, ELasticSearchQueryBuilderUtil.sorterBuider(r.getRegionLat(), r.getRegionLon()));
				}
		}

		String [] indexTypes = new String[1];
		indexTypes[0] = ElasticSearchConstant.PERSON_TYPE_NAME;
		return  countDoc(ElasticSearchConstant.INDEX_NAME, indexTypes, queryMap,sortBuilderMap);
	}

}
