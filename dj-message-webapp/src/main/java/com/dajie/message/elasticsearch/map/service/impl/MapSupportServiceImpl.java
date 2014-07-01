package com.dajie.message.elasticsearch.map.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.dajie.message.elasticsearch.map.utils.baidu.BaiduGeoInfoUtil;
import com.dajie.message.elasticsearch.map.utils.baidu.BaiduHttpRequestUtil;
import com.dajie.message.model.map.geocode.GeoCodingObject;
import com.dajie.message.model.map.geocode.GeoCodingResponseObject;
import com.dajie.message.model.map.geocode.Position;
import com.dajie.message.model.map.place.PlaceObject;
import com.dajie.message.model.map.suggestion.SuggestionObject;
import com.dajie.message.service.IMapSupportService;

@Component("mapSupportServiceImpl")
public class MapSupportServiceImpl implements IMapSupportService {

	@Override
	public List<Position> getPOIListByLocation(double lat, double lng) {
		// TODO Auto-generated method stub
		
		return BaiduGeoInfoUtil.getPOIListByLocation(lat, lng);
	}

	
	@Override
	public List<PlaceObject> getPOIListByName(String query, String region,
			int pageNumber, int pageSize) {
		// TODO Auto-generated method stub
		return BaiduGeoInfoUtil.getPOIListByName(query, region, pageNumber, pageSize);
	}


	@Override
	public List<PlaceObject> getPOIListByLocationInPages(String query, double lat,
			double lon, int pageNumber, int pageSize) {
		// TODO Auto-generated method stub
		return BaiduGeoInfoUtil.getPOIListByLocation(query, lat, lon, pageNumber, pageSize);
	}


	@Override
	public List<SuggestionObject> getSuggestionByLocation(String query,
			double lat,double lon) {
		// TODO Auto-generated method stub
		
		String region = "全国";
		
		GeoCodingResponseObject  geoCodingResponseObject = BaiduHttpRequestUtil.getPOIInfoByLocation(lat, lon, false);
		
		if(geoCodingResponseObject!=null&&geoCodingResponseObject.getStatus() == 0)
		{
			GeoCodingObject  geoCodingObject = geoCodingResponseObject.getResult();
			
			region = geoCodingObject.getCityCode()+"";
		}
		
		return BaiduGeoInfoUtil.getSuggestionByLocation(query, region);
	}

}
