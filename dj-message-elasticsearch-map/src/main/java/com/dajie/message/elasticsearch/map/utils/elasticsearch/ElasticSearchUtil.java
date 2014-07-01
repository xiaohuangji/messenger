package com.dajie.message.elasticsearch.map.utils.elasticsearch;

import static com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchClientUtil.deleteDoc;
import static com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchClientUtil.updateDoc;
import static com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchQueryUtil.countJobByRegionInfoObject;
import static com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchQueryUtil.countPersonByRegionInfoObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;

import com.dajie.message.elasticsearch.map.constants.DefaultPositionTreeUtils;
import com.dajie.message.elasticsearch.map.constants.ElasticSearchConstant;
import com.dajie.message.elasticsearch.map.constants.MapObjectConstants;
import com.dajie.message.elasticsearch.map.constants.PointOnMapConstants;
import com.dajie.message.elasticsearch.map.constants.model.DefaultPositionTreeNode;
import com.dajie.message.elasticsearch.map.utils.LoggerManager;
import com.dajie.message.model.map.RegionInfoObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ElasticSearchUtil {
	
	private static Logger logger = LoggerManager.getLogger(ElasticSearchUtil.class);
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	public static Map<RegionInfoObject,Integer> getCountByCity(int objectType)
	{
		
		List<DefaultPositionTreeNode> citySet = DefaultPositionTreeUtils.getInstance().getNodes();
		
		if(citySet == null || citySet.size() == 0)
		{
			logger.error("cityList is null.");
			return null;
		}
		
		Map<RegionInfoObject,Integer> resultMap = new HashMap<RegionInfoObject,Integer>();
		
		List<RegionInfoObject> regionInfoObjectList = new ArrayList<RegionInfoObject>();
		
		for(DefaultPositionTreeNode r : citySet)
		{
			regionInfoObjectList.add(r.getRegionInfoObject());
		}
		
		Map<RegionInfoObject, SearchResponse> countResponse = null;
		
		if(objectType == MapObjectConstants.OBJECT_TYPE_PERSON)
		{
			countResponse = countPersonByRegionInfoObject(regionInfoObjectList,PointOnMapConstants.POINT_TYPE_COUNT_AT_CITY,null);
		}
		else if(objectType == MapObjectConstants.OBJECT_TYPE_JOB)
		{
			countResponse = countJobByRegionInfoObject(regionInfoObjectList,PointOnMapConstants.POINT_TYPE_COUNT_AT_CITY,null);
		}
		
		int count = 0;
		
		if(countResponse!=null&&countResponse.size()>0)
		{
			for(Entry<RegionInfoObject, SearchResponse> e:countResponse.entrySet())
			{
				RegionInfoObject r = e.getKey();
				SearchHits searchHits = e.getValue().getHits();
				
				if(searchHits != null)
				{
					count = (int) searchHits.getTotalHits();
					
					SearchHit[] searchHit = searchHits.getHits();
					
					if(searchHit!=null&&searchHit.length > 0)
					{
						try {
							JsonNode  jn = objectMapper.readTree(searchHit[0].getSourceAsString());
							JsonNode locationNode = jn.get("location");
							
							r.setRegionLat(locationNode.get("lat").asDouble());
							r.setRegionLon(locationNode.get("lon").asDouble());

						} catch (JsonProcessingException e1) {
							// TODO Auto-generated catch block
							logger.error("ElasticSearchUtil.getCountByCity",e1);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							logger.error("ElasticSearchUtil.getCountByCity",e1);
						}
					}
				}
				
				if(count!=0)
					resultMap.put(r, count);
			}
		}
		
		return resultMap;
	}

	public static Map<RegionInfoObject,Integer> getCountByDistrict(int objectType,String cityName)
	{
		
		DefaultPositionTreeNode cityNode = DefaultPositionTreeUtils.getByName(DefaultPositionTreeUtils.getInstance(), cityName);
		
		if(cityNode == null)
		{
			logger.error("there is no city name called "+ cityName);
			return null;
		}
		
		RegionInfoObject city = cityNode.getRegionInfoObject();
		
		List<RegionInfoObject> districtLevelList = getSubInfoBySuper(city.getId());
		
		Map<RegionInfoObject,Integer> resultMap = new HashMap<RegionInfoObject,Integer>();
		
		Map<RegionInfoObject, SearchResponse> countResponse = null;
		
		if(objectType == MapObjectConstants.OBJECT_TYPE_PERSON)
		{
			countResponse = countPersonByRegionInfoObject(districtLevelList,PointOnMapConstants.POINT_TYPE_COUNT_AT_DISTRICT,city.getRegionName());
		}
		else if(objectType == MapObjectConstants.OBJECT_TYPE_JOB)
		{
			countResponse = countJobByRegionInfoObject(districtLevelList,PointOnMapConstants.POINT_TYPE_COUNT_AT_DISTRICT,city.getRegionName());
		}
		
		int count = 0;
		
		if(countResponse!=null&&countResponse.size()>0)
		{
			for(Entry<RegionInfoObject, SearchResponse> e:countResponse.entrySet())
			{
				RegionInfoObject r = e.getKey();
				SearchHits searchHits = e.getValue().getHits();
				
				if(searchHits != null)
				{
					count = (int) searchHits.getTotalHits();
					
					SearchHit[] searchHit = searchHits.getHits();
					
					if(searchHit!=null&&searchHit.length > 0)
					{
						try {
							JsonNode  jn = objectMapper.readTree(searchHit[0].getSourceAsString());
							JsonNode locationNode = jn.get("location");
							
							r.setRegionLat(locationNode.get("lat").asDouble());
							r.setRegionLon(locationNode.get("lon").asDouble());

						} catch (JsonProcessingException e1) {
							// TODO Auto-generated catch block
							logger.error("ElasticSearchUtil.getCountByCity",e1);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							logger.error("ElasticSearchUtil.getCountByCity",e1);
						}
					}
				}
				
				if(count!=0)
					resultMap.put(r, count);	
			}
		}
		
		return resultMap;
	}
	
	public static List<RegionInfoObject> getSubInfoBySuper(int superId)
	{
		DefaultPositionTreeNode superNode = DefaultPositionTreeUtils.getById(DefaultPositionTreeUtils.getInstance(), superId);
		
		if(superNode == null)
		{
			logger.error("there is no superNode for id  "+ superId);
			return null;
		}
		
		if(superNode.getNodes()==null||superNode.getNodes().size()==0)
		{
			return null;
		}
		
		List<RegionInfoObject> districtList = new ArrayList<RegionInfoObject>();
		
		for(DefaultPositionTreeNode r : superNode.getNodes())
		{
			if(StringUtils.isNotEmpty(r.getRegionInfoObject().getRegionUniqueId()))
			{
				districtList.add(r.getRegionInfoObject());
			}else
			{
				
				List<RegionInfoObject> list = getSubInfoBySuper(r.getRegionInfoObject().getId());
				
				districtList.addAll(list==null? new ArrayList<RegionInfoObject>():list);
			}
		}
		
		return districtList;
	}
	
	public static List<String> addDoc(int objectType,long ttl,String... jsons)
	{
		
		List<IndexResponse> list = null;
		
		if(objectType == MapObjectConstants.OBJECT_TYPE_PERSON)
		{
			list = ElasticSearchClientUtil.addDoc(ElasticSearchConstant.INDEX_NAME, 
					ElasticSearchConstant.PERSON_TYPE_NAME,0,jsons);
		}
		else if(objectType == MapObjectConstants.OBJECT_TYPE_JOB)
		{
			list = ElasticSearchClientUtil.addDoc(ElasticSearchConstant.INDEX_NAME, 
					ElasticSearchConstant.JOB_TYPE_NAME,0,jsons);
		}else if(objectType == MapObjectConstants.OBJECT_TYPE_CAMPAIGN)
		{
			list = ElasticSearchClientUtil.addDoc(ElasticSearchConstant.INDEX_NAME_CAMPAIGN, 
					ElasticSearchConstant.CAMPAIGN_TYPE_NAME,ttl,jsons);
		}
		
		List<String> resultList = new ArrayList<String>();
		
		if(list!=null && list.size() > 0)
		{
			for(IndexResponse i : list)
			{
				if(i.isCreated())
					resultList.add(i.getId());
				else
				{
					logger.error("create new doc fail.");
				}
			}
		}
		
		
		return resultList;
	}
	
	
	public static String getDoc(int objectType,String docId)
	{
		if(docId == null)
		{
			return null;
		}
		
		String index = null;
		String type = null;
		if(objectType == MapObjectConstants.OBJECT_TYPE_PERSON)
		{
			index = ElasticSearchConstant.INDEX_NAME;
			type = ElasticSearchConstant.PERSON_TYPE_NAME;
		}else if(objectType == MapObjectConstants.OBJECT_TYPE_JOB)
		{
			index = ElasticSearchConstant.INDEX_NAME;
			type = ElasticSearchConstant.JOB_TYPE_NAME;
		}else if(objectType == MapObjectConstants.OBJECT_TYPE_CAMPAIGN)
		{
			index = ElasticSearchConstant.INDEX_NAME_CAMPAIGN;
			type = ElasticSearchConstant.CAMPAIGN_TYPE_NAME;
		}
		else
		{
			logger.error("objectType is unkown. "+objectType );
			return null;
		}
		
		
		GetResponse getResponse = ElasticSearchClientUtil.getDocById(index, type, docId);
		
		if(getResponse!=null&&StringUtils.isNotEmpty(getResponse.getId()))
		{
			return getResponse.getSourceAsString();
		}else
			return null;
	}
	
	
	/**
	 * key docId
	 * 
	 * */
	public static int updateDocs(int objectType,Map<String,String> docIdAndJson)
	{
		if(docIdAndJson == null || docIdAndJson.size() ==0)
		{
			return 0;
		}
		
		int result = 0;
		
		String index = null;
		String type = null;
		if(objectType == MapObjectConstants.OBJECT_TYPE_PERSON)
		{
			index = ElasticSearchConstant.INDEX_NAME;
			type = ElasticSearchConstant.PERSON_TYPE_NAME;
		}else if(objectType == MapObjectConstants.OBJECT_TYPE_JOB)
		{
			index = ElasticSearchConstant.INDEX_NAME;
			type = ElasticSearchConstant.JOB_TYPE_NAME;
		}else if(objectType == MapObjectConstants.OBJECT_TYPE_CAMPAIGN)
		{
			index = ElasticSearchConstant.INDEX_NAME_CAMPAIGN;
			type = ElasticSearchConstant.CAMPAIGN_TYPE_NAME;
		}
		else
		{
			logger.error("objectType is unkown. "+objectType );
			return 0;
		}
		
		
		for(Entry<String, String> e : docIdAndJson.entrySet())
		{

			IndexResponse  i = updateDoc(index, type,
					e.getKey(), e.getValue());
			
			if(i!=null&&StringUtils.isNotEmpty(i.getId()))
			{
				logger.debug(i.toString());
				result ++;
			}else
			{
				logger.error("update job error. _id is "+ e.getKey() +", source is :"+e.getValue());
			}
		}
		return result;
		
	}

	
	public static int deleteDocById(int objectType,String docId)
	{
		if(StringUtils.isEmpty(docId))
		{
			return 0;
		}

		if(objectType == MapObjectConstants.OBJECT_TYPE_PERSON)
		{
			 if(deleteDoc(ElasticSearchConstant.INDEX_NAME, ElasticSearchConstant.PERSON_TYPE_NAME, docId))
			{
				return 1; 
			}
		}
		else if(objectType == MapObjectConstants.OBJECT_TYPE_JOB)
		{
			if(deleteDoc(ElasticSearchConstant.INDEX_NAME, ElasticSearchConstant.JOB_TYPE_NAME, docId))
			{
				return 1;
			}
		}else if(objectType == MapObjectConstants.OBJECT_TYPE_CAMPAIGN)
		{
			if(deleteDoc(ElasticSearchConstant.INDEX_NAME_CAMPAIGN, ElasticSearchConstant.CAMPAIGN_TYPE_NAME, docId))
			{
				return 1;
			}
		}

		return 0;
	}
	
	

	public static void main(String[] args) {
		
		
//		List<PlaceObject> i = BaiduGeoInfoUtil.getPOIListByName("芍药居-地铁站", "北京市", 0, 10);
//		 Map<RegionInfoObject, Integer> map = ElasticSearchJobUtil.searchJobCount(
//					null, null,
//					null, 0, 0,0,
//					0, 0, 0,"天津市",2);
//		 
//		 System.out.println();
////		
//		Map<RegionInfoObject, Integer> map = ElasticSearchJobUtil.searchJobCount(null, null,
//				null, 9000, 10000, 0,
//				0, 0, 0, "北京市", 1);
//		GeoCodingResponseObject  g= BaiduHttpRequestUtil.getPOIInfoByLocation(31.606921, 117.894665, false);
//		
//		System.out.println("asdasd");
//		URL logPath = MyApplicationContextUtil.class.getClassLoader().getResource("log4j.poperties");
//		PropertyConfigurator.configure(logPath.getPath());
//		
//		UserCareer userCareer = new UserCareer();
//		
//		userCareer.setCorpName("大街网");
//		
//		List<PersonMapObject>  list = ElasticSearchPersonUtil.searchPerson(null, userCareer, 40.002632600798, 116.32824589411, 
//				0, 5000, 0, 0, 0, null, null, null, 0, 0, 0, 0, null, 0, 0, 0, null, null, null, null, null, null);

//		MapCampaignInfoModel m = new MapCampaignInfoModel();
//		
//		m.setCityNames("北京市,天津市");
//		m.setBaseUrl("http://www.baidu.com/");
//		m.setIsOnline(1);
//		m.setShareUrl("http://www.sina.com.cn/");
//		m.setExpireTime(100000);
//		m.setIsPointsRandom(1);
//		m.setName("红包抽奖");
//		m.setEndTime(System.currentTimeMillis() + 1000000);
//		m.setFreeChancesCount(1);
//		m.setExtraChancesCount(3);
//		m.setId(3);
//		
//		 List<BaseMapObject> list = ElasticSearchCampaignUtil.getSomeCampainPoint(40.004124857128, 116.34592454655, 10000644, m);
//	
//		for(BaseMapObject p : list)
//		{
//			System.out.println(p.getUrl() + "_" + p.getName());
//		}
//		
//		CampaignChanceModel  c = ElasticSearchCampaignUtil.getCampaignChanceModelById("54Ud727cTPKLYzcY5GouHw");
//		
//		System.out.println(c.getLat()+"_"+c.getLon());
		
//		
//		int i = 0;
//		for(Entry<RegionInfoObject, Integer> j : map.entrySet())
//		{
//			i += j.getValue();
//			System.out.println(j.getKey().getRegionName() + "    " + j.getValue());
//		}
//		System.out.println("总和:"+i);
//		
//		Map<RegionInfoObject, Integer> map = getCountByCity(2);
//		for(Entry<RegionInfoObject, Integer> j : map.entrySet())
//		{
//			System.out.println(j.getKey().getRegionName() + "    " + j.getValue());
//		}
//		
//		System.out.println(System.currentTimeMillis());
//				sear+-chJob(
//						39.99764,116.345386, 0,5000, 0, 31, "工程师", 
//						"大街网", "美女多", 100, 50000, 3, 2, "互联网", "研发", "北京市海淀区王庄路1号",
//						"海淀区", "北京市", "6ad35c13c680de21364aae46", "清华同方科技广场D座");
				
//				searchPersonWithoutGeoInfo(null,
//						null, null, null,
//						0, 0, null, 0,
//						0, 0, null,null,null,
//						null, null,
//						"北京",0, 100);
//		long l2 = System.currentTimeMillis();
//				List<JobMapObject>  ll = ElasticSearchUtil.searchJob(39.99764, 116.3328, 100000, 0, 0, 50, null, null, null, 0, 1000000, 0, 0, null, null, null, null, null, null, null);
//		Field[] fields = BaseMapObject.class.getDeclaredFields();
//
//			
//		System.out.println(searchResponse.getTookInMillis());
//		
//		SearchHit[] sh =searchResponse.getHits().getHits();
//		
//		for(SearchHit s : sh)
//		{
//			
//			Map<String,Object> m = s.getSource();
//			
//			for(Entry<String, Object> e : m.entrySet())
//			{
//				System.out.println(e.getKey() + "__" + e.getValue());
//			}
//		}
//		
//		System.out.println();
//		List<JobMapObject> searchResponse = getJobByJid(100);/*searchJob("dajie_message_index", "dajie_message_type",
//				116.345427, 40.005313, 0, 0, 31, "这么屌", null,
//				"制药", "一天", 100, 20000, 4, 4, "精神", "清华同方", 
//				"海淀区", "北京市", "asdwqr324rfsvweve", "清华同方科技广场");*/
//		
//		for(JobMapObject j : searchResponse)
//		{
//			logger.info(j.getName());
//			logger.info(j.getDistrict());
//			logger.info(j.getDistance()+"");
//		}
//		
//		logger.info("time:" + (l2-l1));
//		Map<String,Integer> m =  getCountByDistrict("dajie_message_index", "dajie_message_type","北京市");
//		
//		for(Entry<String, Integer> e : m.entrySet())
//		{
//			System.out.println(e.getKey() + " : " + e.getValue());
//		}
	}
}
