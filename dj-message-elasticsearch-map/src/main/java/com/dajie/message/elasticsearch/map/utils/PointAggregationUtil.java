package com.dajie.message.elasticsearch.map.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import com.dajie.message.elasticsearch.map.constants.MapObjectConstants;
import com.dajie.message.elasticsearch.map.constants.PointOnMapConstants;
import com.dajie.message.model.map.BaseMapObject;
import com.dajie.message.model.map.LocationInMapObject;
import com.dajie.message.model.map.PointOnMap;

public class PointAggregationUtil {
	
	private static final Logger logger = LoggerManager.getLogger(PointAggregationUtil.class);

	/**
	 * 只有地图详细级才会使用
	 * @param baseMapObjectList 
	 * */
	public static List<PointOnMap>  aggregateMapObjectToPointOnMap(List<BaseMapObject> baseMapObjectList)
	{
		if(baseMapObjectList == null || baseMapObjectList.size() ==0)
		{
			return new ArrayList<PointOnMap>();
		}
		
		Map<String,PointOnMap> pointOnMapMap = new HashMap<String,PointOnMap>();
		
		Map<String,Integer> pointOnMapCount =  new HashMap<String,Integer>();
		
		
		for(BaseMapObject b : baseMapObjectList)
		{
			int count = 0;
			PointOnMap pointOnMap = convertBaseMapObjectToPointOnMap(b);
			
			if(pointOnMap == null) continue;
			
			String location = pointOnMap.getLatitude() + "," + pointOnMap.getLongitude()+","+pointOnMap.getObjectType();
			
			if(pointOnMapCount.get(location)!=null)
			{
				count = pointOnMapCount.get(location);
			}
			
			if(count == 0)
			{
				pointOnMapMap.put(location, pointOnMap);
				pointOnMapCount.put(location, 1);
			}else
			{
				count ++;
				pointOnMapCount.put(location, count);
			}
		}
		
		List<PointOnMap> pointOnMapList = new ArrayList<PointOnMap>();
		
		for(Entry<String, PointOnMap> e : pointOnMapMap.entrySet())
		{
			PointOnMap p = e.getValue();
			p.setObjectCount(pointOnMapCount.get(e.getKey()));

			pointOnMapList.add(p);
		}
		
		
		return pointOnMapList;
	}

	
	public static PointOnMap convertBaseMapObjectToPointOnMap(BaseMapObject baseMapObject)
	{
		if(baseMapObject == null) return null;
		
		if(StringUtils.isEmpty(baseMapObject.getUidOfPOI())&&
				baseMapObject.getObjectType() == MapObjectConstants.OBJECT_TYPE_JOB)
			return null;

		PointOnMap p = new PointOnMap();
		
		p.setCity(baseMapObject.getCity());
		p.setDistrict(baseMapObject.getDistrict());
		LocationInMapObject l = baseMapObject.getLocation();
		if(l!=null)
		{
			p.setLatitude(l.getLat());
			p.setLongitude(l.getLon());
		}
		p.setPonitInfo(baseMapObject.getName());
		p.setNameOfPOI(baseMapObject.getNameOfPOI());
		p.setUidOfPOI(baseMapObject.getUidOfPOI());
		p.setObjectType(baseMapObject.getObjectType());
		p.setPointAddress(baseMapObject.getAddress());
		p.setDistance(baseMapObject.getDistance());
		p.setPointLevel(PointOnMapConstants.POINT_TYPE_COUNT_AT_POINT);
		p.setUrl(baseMapObject.getUrl());
		p.setIconUrl(baseMapObject.getIconUrl());
		p.setShareUrl(baseMapObject.getShareUrl());

		
		return p;
	}

	
}
