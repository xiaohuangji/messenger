package com.dajie.message.elasticsearch.map.utils.baidu;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;

import com.dajie.message.elasticsearch.map.utils.LoggerManager;
import com.dajie.message.model.map.geocode.AddressComponent;
import com.dajie.message.model.map.geocode.GeoCodingObject;
import com.dajie.message.model.map.geocode.GeoCodingResponseObject;
import com.dajie.message.model.map.geocode.Position;
import com.dajie.message.model.map.place.PlaceObject;
import com.dajie.message.model.map.place.PlaceRequestObject;
import com.dajie.message.model.map.suggestion.SuggestionObject;
import com.dajie.message.model.map.suggestion.SuggestionRequestObject;

import static com.dajie.message.elasticsearch.map.utils.baidu.BaiduHttpRequestUtil.*;

public class BaiduGeoInfoUtil {
	
	private static final Logger logger = LoggerManager.getLogger(BaiduGeoInfoUtil.class);
	
	public static AddressComponent getAddressComponentWithGeoPoint(double latitude,double longitude)
	{
		GeoCodingResponseObject  geoCodingResponseObject = getPOIInfoByLocation(latitude, longitude, false);
		
		if(geoCodingResponseObject == null || geoCodingResponseObject.getStatus() > 0)
		{
			logger.error("get address info from Baidu Error.");
			if(geoCodingResponseObject!=null)
				logger.error("Error code is "+ geoCodingResponseObject.getStatus());
			
			return null;
		}
		
		GeoCodingObject  geoCodingObject = geoCodingResponseObject.getResult();
		
		if(geoCodingObject == null)
		{
			logger.error("get address info from Baidu Error. geoCodingObject is null");
			return null;
		}
		
		AddressComponent  addressComponent = geoCodingObject.getAddressComponent();
		
		return addressComponent;
	}
	
	public static List<Position> getPOIListByLocation(double lat, double lng) {
		// TODO Auto-generated method stub
		
		GeoCodingResponseObject  geoCodingResponseObject = BaiduHttpRequestUtil.getPOIInfoByLocation(lat, lng, true);
		
		if(geoCodingResponseObject == null || geoCodingResponseObject.getStatus() > 0)
		{
			logger.error("get address info from Baidu Error.");
			if(geoCodingResponseObject!=null)
				logger.error("Error code is "+ geoCodingResponseObject.getStatus());
			
			return new ArrayList<Position>();
		}
		

		GeoCodingObject  ceoCodingObject =  geoCodingResponseObject.getResult();
		
		if(ceoCodingObject!=null)
		{
			return ceoCodingObject.getPois();
		}
		else
		{
			return new ArrayList<Position>();
		}

	}
	
	
	public static List<PlaceObject> getPOIListByName(String query, String region,int pageNumber,int pageSize) {
		// TODO Auto-generated method stub
		
		PlaceRequestObject  placeRequestObject = BaiduHttpRequestUtil.getPlaceObjectsByQuery(query, region, pageNumber, pageSize);
		
		if(placeRequestObject == null || placeRequestObject.getStatus() > 0)
		{
			logger.error("get address info from Baidu Error.");
			if(placeRequestObject!=null)
				logger.error("Error code is "+ placeRequestObject.getStatus());
			
			return new ArrayList<PlaceObject>();
		}
		

		List<PlaceObject>   placeObjectList = placeRequestObject.getResults();
		
		if(placeObjectList!=null&& placeObjectList.size()> 0)
		{
			return placeObjectList;
		}
		else
		{
			return new ArrayList<PlaceObject>();
		}

	}
	
	public static List<PlaceObject> getPOIListByLocation(String query, double lat, double lon,int pageNumber,int pageSize) {
		// TODO Auto-generated method stub
		
		PlaceRequestObject  placeRequestObject = BaiduHttpRequestUtil.getPlaceObjectsByQuery(lat, lon, query, pageNumber, pageSize);
		
		if(placeRequestObject == null || placeRequestObject.getStatus() > 0)
		{
			logger.error("get address info from Baidu Error.");
			if(placeRequestObject!=null)
				logger.error("Error code is "+ placeRequestObject.getStatus());
			
			return new ArrayList<PlaceObject>();
		}
		

		List<PlaceObject>  placeObjectList = placeRequestObject.getResults();
		
		if(placeObjectList!=null&& placeObjectList.size() > 0)
		{
			return placeObjectList;
		}
		else
		{
			return new ArrayList<PlaceObject>();
		}

	}
	
	
	public static List<SuggestionObject> getSuggestionByLocation(String query,
			String region) {
		// TODO Auto-generated method stub
		
		SuggestionRequestObject  suggestionRequestObject =  BaiduHttpRequestUtil.getSuggestionByQuery(query, region);
		
		
		
		if(suggestionRequestObject ==null || suggestionRequestObject.getStatus() > 0)
		{
			logger.error("get suggestion info from Baidu Error.");
			if(suggestionRequestObject!=null)
				logger.error("Error code is "+ suggestionRequestObject.getStatus());
			
			return new ArrayList<SuggestionObject>();
		}
		
			return suggestionRequestObject.getResult();


	}
	
	
	public static PlaceObject getPlaceObjectsByUid(String uid)
	{
		PlaceRequestObject  placeRequestObject = BaiduHttpRequestUtil.getPlaceObjectsByUid(uid);
		
		if(placeRequestObject == null || placeRequestObject.getStatus() > 0)
		{
			logger.error("get placeRequestObject info from Baidu Error.");
			if(placeRequestObject!=null)
				logger.error("Error code is "+ placeRequestObject.getStatus());
			
			return null;
		}
		
		return placeRequestObject.getResult();
		
	}

}
