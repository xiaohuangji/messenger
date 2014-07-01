package com.dajie.message.elasticsearch.map;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dajie.message.elasticsearch.map.constants.ElasticSearchConstant;
import com.dajie.message.elasticsearch.map.dao.IRegionInfoObjectDAO;
import com.dajie.message.elasticsearch.map.utils.LoggerManager;
import com.dajie.message.elasticsearch.map.utils.baidu.BaiduHttpRequestUtil;
import com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchClientUtil;
import com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchConnectionUtil;
import com.dajie.message.model.map.LocationObject;
import com.dajie.message.model.map.RegionInfoObject;
import com.dajie.message.model.map.place.PlaceObject;
import com.dajie.message.model.map.place.PlaceRequestObject;

public class ManualInitialize {

	static Logger logger = LoggerManager.getLogger(ManualInitialize.class);
	
	private static Client client = ElasticSearchConnectionUtil.getInstance();
	
	
	public static void main(String[] args) {
		
		//deleteAllJobs();
	//	 initRegionData();
		
		
//		PlaceRequestObject  p = BaiduHttpRequestUtil.getPlaceObjectsByQuery("清华同方科技广场D座5层", "北京", 0, 10);
//		
//		PlaceRequestObject  p1 = BaiduHttpRequestUtil.getPlaceObjectsByQuery(p.getResults().get(0).getLocation().getLat(),p.getResults().get(0).getLocation().getLng(), "北京市房山区良乡拱辰北大街3号", 0, 10);
//		
//		System.out.println();
	}
	
	
	public static void initRegionData() {
		// TODO Auto-generated method stub
		
		  
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:mybatis-connection.xml",
				"classpath:spring/*.xml",
				"classpath:mappers_config/*.xml"
				);
		
		IRegionInfoObjectDAO regionInfoObjectDAO = (IRegionInfoObjectDAO) applicationContext.getBean("regionInfoObjectDAO");
		
		if(regionInfoObjectDAO == null)
		{
			logger.error("regionInfoObjectDAO == null");
			return;
		}
		
		RegionInfoObject r = new RegionInfoObject();
		
		r.setRegionName("全国");
		
		List<RegionInfoObject> firstLevelList  = regionInfoObjectDAO.getByParentId(1);
		
		start(r,firstLevelList,regionInfoObjectDAO);
		}
		
	public static void start(RegionInfoObject parentRegion,List<RegionInfoObject> nameList,IRegionInfoObjectDAO regionInfoObjectDAO)
	{
		if(regionInfoObjectDAO == null)
		{
			logger.error("regionInfoObjectDAO == null");
			return;
		}
		
		String region = parentRegion.getRegionName();
		
		if(region.equals("市辖区")||region.equals("县")||
				region.equals("省直辖县级行政区划")||region.equals("郊区"))
		{
			region = regionInfoObjectDAO.select(parentRegion.getParentId()).getRegionName();
		}
		
		
		if(nameList!=null && nameList.size() >0)
		{
			
			for(RegionInfoObject r : nameList)
			{
				
				
//				RegionInfoObject regionInfo = regionInfoObjectDAO.select(r.getId());

				
				logger.info("start to process with cityname "+r.getRegionName());
				
				PlaceRequestObject  placeRequestObject =  BaiduHttpRequestUtil.getPlaceObjectsByQuery(r.getRegionName(), region, 0, 1);
				
				
				if(r.getRegionName().equals("市辖区")||r.getRegionName().equals("县")||
						r.getRegionName().equals("省直辖县级行政区划")||r.getRegionName().equals("郊区"))
				{
					RegionInfoObject regionInfoObject = new RegionInfoObject();
					regionInfoObject.setId(r.getId());
					regionInfoObject.setRegionLat(0);
					regionInfoObject.setRegionLon(0);
					regionInfoObject.setRegionUniqueId("");
					
					
					regionInfoObjectDAO.updateRegionInfo(regionInfoObject);
				}
//				else if(StringUtils.isNotEmpty(regionInfo.getRegionUniqueId()))
//				{
//					
//					logger.info(r.getRegionName() +"'s info was already exist"); 
//					
//					
//				}
				else if(placeRequestObject!=null && placeRequestObject.getStatus() == 0)
				{
					List<PlaceObject>  placeObjectList = placeRequestObject.getResults();
					
					if(placeObjectList != null && placeObjectList.size() >0)
					{
						PlaceObject  placeObject = BaiduHttpRequestUtil.getPlaceObjectsByQuery(r.getRegionName(), region, 0, 1).getResults().get(0);
						
						if(placeObject != null)
						{
							LocationObject l = placeObject.getLocation();
							if(l!=null)
							{
								double lat = l.getLat();
								double lon = l.getLng();
								String uid = placeObject.getUid();
								
								RegionInfoObject regionInfoObject = new RegionInfoObject();
								regionInfoObject.setId(r.getId());
								regionInfoObject.setRegionLat(lat);
								regionInfoObject.setRegionLon(lon);
								regionInfoObject.setRegionUniqueId(uid);
								
								logger.info("update result = "+ regionInfoObjectDAO.updateRegionInfo(regionInfoObject));
								
							}
							else
							{
								logger.error("LocationObject is null for "+ r.getRegionName());
								
							}
						}else
						{
							logger.error("placeRequestObject is error for "+ r.getRegionName());
							if(placeRequestObject!=null)
							{
								logger.error("errorcode is  "+placeRequestObject.getStatus());
							}
						}
					}
					else
					{
						logger.error("placeObjectList is null for "+ r.getRegionName());
					}
				}else
				{
					
					logger.error("placeObjectList is null for "+ r.getRegionName());
				}
				
				List<RegionInfoObject> districtLevelNameList = regionInfoObjectDAO.getByParentId(r.getId());
				
				start(r,districtLevelNameList,regionInfoObjectDAO);
			}
		}
		return;
	}
	
	
	public static void deleteAllJobs()
	{
		
		SearchRequestBuilder requestBuilder = client
				.prepareSearch(ElasticSearchConstant.INDEX_NAME).setTypes(ElasticSearchConstant.JOB_TYPE_NAME)
				.setSearchType(SearchType.SCAN)
				.setSize(500).setScroll(new TimeValue(6000));
		
		QueryBuilder q = QueryBuilders.matchAllQuery();
		
		requestBuilder.setQuery(q);
		
		logger.debug(requestBuilder.toString());
		SearchResponse response = requestBuilder.execute().actionGet();
		logger.debug(response.toString());
		
		String scrollId = response.getScrollId();
		
		SearchResponse scrollResp = null;
		
		if(StringUtils.isNotEmpty(scrollId))
		{
			while(true)
			{
				SearchScrollRequestBuilder searchScrollRequestBuilder = 
						client.prepareSearchScroll(scrollId).setScroll(new TimeValue(60000));
				
				scrollResp = searchScrollRequestBuilder.execute().actionGet();
				 
				 if (scrollResp.getHits().getHits().length == 0) {
				        break;
				 }
				 
				 logger.debug(scrollResp.toString());

				 SearchHit[] searchhits = scrollResp.getHits().getHits();
				 
				 
				 if(searchhits!=null &&searchhits.length>0)
				 {
					 
					 for(SearchHit s : searchhits)
					 {
					 
					 ElasticSearchClientUtil.deleteDoc(ElasticSearchConstant.INDEX_NAME,
							 ElasticSearchConstant.JOB_TYPE_NAME,
							 s.getId());
					 
					 logger.info("delete id "+s.getId());
					 }
				 }
			
				 
				 
			}
		}
		
	}
	

}
