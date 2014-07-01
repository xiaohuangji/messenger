package com.dajie.message.elasticsearch.map.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dajie.message.elasticsearch.map.constants.model.DefaultPositionConstantModel;
import com.dajie.message.elasticsearch.map.dao.IRegionInfoObjectDAO;
import com.dajie.message.elasticsearch.map.utils.LoggerManager;
import com.dajie.message.model.map.RegionInfoObject;


public class DefaultPositionMapUtil {
//	
//	/**
//	 * 默认地理位置，主要提供城市级别和地区被级别的显示
//	 * */
//	private static Map<String,DefaultPositionConstantModel> defaultCityPositionMap = null;
//	
//	private static Logger logger = LoggerManager.getLogger(DefaultPositionMapUtil.class);
//	
//	private static byte[] lock = new byte[1];
//
//	private static ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:mappers_config/*.xml",
//			"classpath:spring/*.xml");
//	
//	private static IRegionInfoObjectDAO initGeoInfoObjectDAO;
//	
//	public static Map<String,DefaultPositionConstantModel> getInstance()
//	{
//		if (null == defaultCityPositionMap) {
//			synchronized (lock) {
//				if (null == defaultCityPositionMap){
//					try{
//						
//						initGeoInfoObjectDAO = (IRegionInfoObjectDAO) applicationContext.getBean("initGeoInfoObjectDAO");
//
//						init();
//					}catch(Exception e)
//					{
//						logger.error("Get defaultCityPositionMap INSTANCE Error. \n", e);
//					}
//				}
//			}
//		}
//		return defaultCityPositionMap;
//	}
//
//	
//	private static void init() throws Exception
//	{
//		
//		//第三级
//		List<RegionInfoObject> thirdLevelList = initGeoInfoObjectDAO.getByLevel(3);
//		
//		HashMap<String, List<DefaultPositionConstantModel>> thirdLevelMap = new HashMap<String,List<DefaultPositionConstantModel>>();
//		
//		if(thirdLevelList != null && thirdLevelList.size() != 0)
//		{
//			for(RegionInfoObject l : thirdLevelList)
//			{
//				String name = l.getName();
//				String belongTo = l.getBelongTo();
//				
//				if(StringUtils.isEmpty(belongTo)) continue;
//				
//				String uniqueId = l.getUniqueId();
//				double lat = l.getLat();
//				double lon = l.getLon();
//				
//				List<DefaultPositionConstantModel> thirdLevel =  thirdLevelMap.get(belongTo);
//				
//				if(thirdLevel == null)
//				{
//					thirdLevel = new ArrayList<DefaultPositionConstantModel>();
//					thirdLevel.add(new DefaultPositionConstantModel(name,lat,lon,uniqueId));
//				}
//
//				thirdLevel.add(new DefaultPositionConstantModel(name,lat,lon,uniqueId));
//				thirdLevelMap.put(belongTo, thirdLevel);
//			}
//		}
//		
//		//第二级
//		List<RegionInfoObject> secondLevelList = initGeoInfoObjectDAO.getByLevel(2);
//		
//		HashMap<String, List<DefaultPositionConstantModel>> secondLevelMap = new HashMap<String,List<DefaultPositionConstantModel>>();
//		
//		if(secondLevelList == null || secondLevelList.size() == 0)
//		{
//			for(RegionInfoObject l : secondLevelList)
//			{
//				String name = l.getName();
//				if(StringUtils.isEmpty(name)) continue;
//				String belongTo = l.getBelongTo();
//				if(StringUtils.isEmpty(belongTo)) continue;
//				String uniqueId = l.getUniqueId();
//				double lat = l.getLat();
//				double lon = l.getLon();
//				
//				List<DefaultPositionConstantModel> thirdLevel = thirdLevelMap.get(name);
//				
//				List<DefaultPositionConstantModel> secondLevel = secondLevelMap.get(belongTo);
//				
//				if(secondLevel == null)
//				{
//					secondLevel = new ArrayList<DefaultPositionConstantModel>();
//				}
//				
//				if(thirdLevel == null || thirdLevel.size() == 0)
//				{
//					secondLevel.add(new DefaultPositionConstantModel(name,lat,lon,uniqueId));
//				}
//				else
//				{
//					secondLevel.add(new DefaultPositionConstantModel(name,lat,lon,uniqueId,
//							thirdLevel.toArray(new DefaultPositionConstantModel[0])));
//				}
//				
//				secondLevelMap.put(belongTo, secondLevel);
//			}
//		}
//		
//		//第一级
//		List<RegionInfoObject> firstLevelList = initGeoInfoObjectDAO.getByLevel(1);
//		
//		if(firstLevelList == null || firstLevelList.size() == 0)
//		{
//			throw new Exception("no geo data to init!");
//		}
//		
//		defaultCityPositionMap = new HashMap<String,DefaultPositionConstantModel>();
//		
//		for(RegionInfoObject l : firstLevelList)
//		{
//			String name = l.getName();
//			String belongTo = l.getBelongTo();
//			if(StringUtils.isEmpty(belongTo)) continue;
//			String uniqueId = l.getUniqueId();
//			double lat = l.getLat();
//			double lon = l.getLon();
//			
//			List<DefaultPositionConstantModel> secondLevel = secondLevelMap.get(name);
//			
//			if(secondLevel == null || secondLevel.size() == 0)
//			{
//				defaultCityPositionMap.put(name, new DefaultPositionConstantModel(name,lat,lon,uniqueId));
//			}
//			else
//			{
//				defaultCityPositionMap.put(name, new DefaultPositionConstantModel(name,lat,lon,uniqueId
//						,secondLevel.toArray(new DefaultPositionConstantModel[0])));
//			}
//		}
//		
//	}



	
	
	
	
//	public static final String DEFAULT_CITY_NAME_BEIJING = "北京市";
//	private static final double DEFAULT_CITY_NAME_BEIJING_LAT = 116.42784;
//	private static final double DEFAULT_CITY_NAME_BEIJING_LON = 39.926002;
	
//	public static final String DEFAULT_CITY_NAME_SHANGHAI = "上海市";
//	private static final double DEFAULT_CITY_NAME_SHANGHAI_LAT = 121.473299;
//	private static final double DEFAULT_CITY_NAME_SHANGHAI_LON = 31.248899;
	
//	public static final String DEFAULT_CITY_NAME_SHENZHEN = "深圳市";
//	private static final double DEFAULT_CITY_NAME_SHENZHEN_LAT = 114.062638;
//	private static final double DEFAULT_CITY_NAME_SHENZHEN_LON = 22.55313;
	
//	public static final String DEFAULT_DISTRICT_NAME_BEIJING_HAIDIAN = "海淀区";
//	private static final double DEFAULT_DISTRICT_NAME_BEIJING_HAIDIAN_LAT = 116.304233;
//	private static final double DEFAULT_DISTRICT_NAME_BEIJING_HAIDIAN_LON = 39.966272;
	
//	public static final String DEFAULT_DISTRICT_NAME_BEIJING_CHAOYANG = "朝阳区";
//	private static final double DEFAULT_DISTRICT_NAME_BEIJING_CHAOYANG_LAT = 116.477858;
//	private static final double DEFAULT_DISTRICT_NAME_BEIJING_CHAOYANG_LON = 39.963175;
	
	
	
//	static{
//		
//		PlaceRequestObject placeRequestObject = BaiduHttpRequestUtil.getPlaceObjectsByQuery(DEFAULT_DISTRICT_NAME_BEIJING_CHAOYANG, DEFAULT_CITY_NAME_BEIJING, 0, 1);
//		PlaceObject  placeObject =  placeRequestObject.getResults().get(0);
//		placeObject.getLocation().getLat();
//		placeObject.getLocation().getLng();
//		
//		
//		
//		PlaceRequestObject placeRequestObject = BaiduHttpRequestUtil.getPlaceObjectsByQuery(DEFAULT_CITY_NAME_BEIJING, "全国", 0, 1);
//		PlaceObject  placeObject =  placeRequestObject.getResults().get(0);
//		placeObject.getLocation().getLat();
//		placeObject.getLocation().getLng();
//		
//		DefaultPositionConstantModel chaoyang = new DefaultPositionConstantModel(DEFAULT_DISTRICT_NAME_BEIJING_CHAOYANG,
//				DEFAULT_DISTRICT_NAME_BEIJING_CHAOYANG_LAT,
//				DEFAULT_DISTRICT_NAME_BEIJING_CHAOYANG_LON,null);
//		DefaultPositionConstantModel haidian = new DefaultPositionConstantModel(DEFAULT_DISTRICT_NAME_BEIJING_HAIDIAN,
//				DEFAULT_DISTRICT_NAME_BEIJING_HAIDIAN_LAT,
//				DEFAULT_DISTRICT_NAME_BEIJING_HAIDIAN_LON);
//		
//		DefaultPositionConstantModel beijing = new DefaultPositionConstantModel(DEFAULT_CITY_NAME_BEIJING,
//				DEFAULT_CITY_NAME_BEIJING_LAT,
//				DEFAULT_CITY_NAME_BEIJING_LON,
//				chaoyang,haidian);
//		
//		DefaultPositionConstantModel shanghai = new DefaultPositionConstantModel(DEFAULT_CITY_NAME_SHANGHAI,
//				DEFAULT_CITY_NAME_SHANGHAI_LAT,
//				DEFAULT_CITY_NAME_SHANGHAI_LON);
//		
//		DefaultPositionConstantModel shenzhen = new DefaultPositionConstantModel(DEFAULT_CITY_NAME_SHENZHEN,
//				DEFAULT_CITY_NAME_SHENZHEN_LAT,
//				DEFAULT_CITY_NAME_SHENZHEN_LON);
//		
//		DEFAULT_CITY_POSITION_MAP.put(DEFAULT_CITY_NAME_BEIJING, beijing);
//		DEFAULT_CITY_POSITION_MAP.put(DEFAULT_CITY_NAME_SHANGHAI, shanghai);
//		DEFAULT_CITY_POSITION_MAP.put(DEFAULT_CITY_NAME_SHENZHEN, shenzhen);
//	}

}
