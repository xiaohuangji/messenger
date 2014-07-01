package com.dajie.message.elasticsearch.map.utils.baidu;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;

import com.dajie.message.elasticsearch.map.utils.LoggerManager;
import com.dajie.message.elasticsearch.map.utils.spring.MyApplicationContextUtil;
import com.dajie.message.model.map.LocationObject;
import com.dajie.message.model.map.geocode.GeoCodingObject;
import com.dajie.message.model.map.geocode.GeoCodingResponseObject;
import com.dajie.message.model.map.geocode.Position;
import com.dajie.message.model.map.place.PlaceObject;
import com.dajie.message.model.map.place.PlaceRequestObject;
import com.dajie.message.model.map.suggestion.SuggestionRequestObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BaiduHttpRequestUtil {
	
	private static final String applicationKey = "Odl6k2og5ITOnEfb29l29Que";

	private static final String geoCodingURL = "http://api.map.baidu.com/geocoder/v2/";
	
	private static final String suggestionURL = "http://api.map.baidu.com/place/v2/suggestion";
	
	private static final String placeURL = "http://api.map.baidu.com/place/v2/search";
	
	private static final String uidURL = "http://api.map.baidu.com/place/v2/detail";

	private static MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
	private static HttpClient client = new HttpClient(connectionManager);

	private static final Logger logger = LoggerManager.getLogger(BaiduHttpRequestUtil.class);
	
	private static ObjectMapper objectMapper = new ObjectMapper();

	static {
		client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
		client.getHttpConnectionManager().getParams().setSoTimeout(30000);
		client.getHttpConnectionManager().getParams().setDefaultMaxConnectionsPerHost(8);
		client.getHttpConnectionManager().getParams().setMaxTotalConnections(48);
		client.getHttpConnectionManager().getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		client.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
	}
	
	
	public static GeoCodingResponseObject getPOIInfoByLocation(double lat,double lng,boolean needPOI)
	{
		String url = geoCodingURL + "?";
		
		Map<String, String> m = new HashMap<String, String>();

		m.put("ak", applicationKey);
		m.put("location", lat+","+lng);
		m.put("output", "json");
		if(needPOI)
			m.put("pois", "1");
		else
			m.put("pois", "0");
		
		boolean start = true;
		for (Map.Entry<String, String> e : m.entrySet()) {
			if (start == true) {
				url += e.getKey() + "=" + e.getValue();
				start = false;
			} else {
				url += "&" + e.getKey() + "=" + e.getValue();
			}
		}
		
		
		GetMethod getMethod = new GetMethod(url);

		String retJsonStr = null;

		GeoCodingResponseObject b = null;
		
		try {
			client.executeMethod(getMethod);
			
			retJsonStr = getMethod.getResponseBodyAsString();

			b = objectMapper.readValue(retJsonStr,GeoCodingResponseObject.class);
			} catch (JsonParseException e1) {
				// TODO Auto-generated catch block
				logger.error("BaiduHttpRequestUtil.getPOIInfoByLocation",e1);
			} catch (JsonMappingException e1) {
				// TODO Auto-generated catch block
				logger.error("BaiduHttpRequestUtil.getPOIInfoByLocation",e1);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				logger.error("BaiduHttpRequestUtil.getPOIInfoByLocation",e1);
			}
			


		return b;
	}
	
	
	
	public static SuggestionRequestObject getSuggestionByQuery(String query,String region)
	{
		String url = suggestionURL + "?";
		
		Map<String, String> m = new HashMap<String, String>();

		m.put("ak", applicationKey);
		m.put("query", URLEncoder.encode(query));
		m.put("output", "json");
		m.put("region",URLEncoder.encode(region));

		
		boolean start = true;
		for (Map.Entry<String, String> e : m.entrySet()) {
			if (start == true) {
				url += e.getKey() + "=" + e.getValue();
				start = false;
			} else {
				url += "&" + e.getKey() + "=" + e.getValue();
			}
		}
		
		
		GetMethod getMethod = new GetMethod(url);

		String retJsonStr = null;

		SuggestionRequestObject s = null;
		

		try {
			client.executeMethod(getMethod);
			
			retJsonStr = getMethod.getResponseBodyAsString();

			s = objectMapper.readValue(retJsonStr,SuggestionRequestObject.class);
			
			} catch (JsonParseException e1) {
				// TODO Auto-generated catch block
				logger.error("BaiduHttpRequestUtil.getSuggestionByQuery",e1);
			} catch (JsonMappingException e1) {
				// TODO Auto-generated catch block
				logger.error("BaiduHttpRequestUtil.getSuggestionByQuery",e1);				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				logger.error("BaiduHttpRequestUtil.getSuggestionByQuery",e1);
			}


		return s;
	}
	
	
	public static PlaceRequestObject getPlaceObjectsByQuery(String query,String region,int pageNumber,int pageSize)
	{
		
		String url = placeURL + "?";
		
		Map<String, Object> m = new HashMap<String, Object>();

		m.put("ak", applicationKey);
		m.put("query", URLEncoder.encode(query));
		m.put("output", "json");
		m.put("scope","2");
		m.put("region",  URLEncoder.encode(region));
		m.put("page_size", pageSize);
		m.put("page_num", pageNumber);
		
		boolean start = true;
		for (Map.Entry<String, Object> e : m.entrySet()) {
			if (start == true) {
				url += e.getKey() + "=" + e.getValue();
				start = false;
			} else {
				url += "&" + e.getKey() + "=" + e.getValue();
			}
		}
		
		
		GetMethod getMethod = new GetMethod(url);

		String retJsonStr = null;

		PlaceRequestObject p = null;
		
		try {
			client.executeMethod(getMethod);
			
			retJsonStr = getMethod.getResponseBodyAsString();

			logger.debug(retJsonStr);
			
				p = objectMapper.readValue(retJsonStr,PlaceRequestObject.class);
			} catch (JsonParseException e1) {
				// TODO Auto-generated catch block
				logger.error("BaiduHttpRequestUtil.getPlaceObjectsByQuery",e1);
			} catch (JsonMappingException e1) {
				// TODO Auto-generated catch block
				logger.error("BaiduHttpRequestUtil.getPlaceObjectsByQuery",e1);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				logger.error("BaiduHttpRequestUtil.getPlaceObjectsByQuery",e1);
			}
			

		return p;
	}
	
	
	public static PlaceRequestObject getPlaceObjectsByQuery(double lat,double lng,String query,int pageNumber,int pageSize)
	{
		
		String url = placeURL + "?";
		
		Map<String, Object> m = new HashMap<String, Object>();

		m.put("ak", applicationKey);
		m.put("query", URLEncoder.encode(query));
		m.put("output", "json");
		m.put("scope","2");
		m.put("location", lat+","+lng);
		m.put("radius", "5000");
		m.put("page_size", pageSize);
		m.put("page_num", pageNumber);
		
		boolean start = true;
		for (Map.Entry<String, Object> e : m.entrySet()) {
			if (start == true) {
				url += e.getKey() + "=" + e.getValue();
				start = false;
			} else {
				url += "&" + e.getKey() + "=" + e.getValue();
			}
		}
		
		
		GetMethod getMethod = new GetMethod(url);

		String retJsonStr = null;

		PlaceRequestObject p = null;
		
		try {
			client.executeMethod(getMethod);
			
			retJsonStr = getMethod.getResponseBodyAsString();

			p = objectMapper.readValue(retJsonStr,PlaceRequestObject.class);
			} catch (JsonParseException e1) {
				// TODO Auto-generated catch block
				logger.error("BaiduHttpRequestUtil.getPlaceObjectsByQuery",e1);
			} catch (JsonMappingException e1) {
				// TODO Auto-generated catch block
				logger.error("BaiduHttpRequestUtil.getPlaceObjectsByQuery",e1);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				logger.error("BaiduHttpRequestUtil.getPlaceObjectsByQuery",e1);
			}


		return p;
	}
	
	
	public static PlaceRequestObject getPlaceObjectsByUid(String uid)
	{
		
		String url = uidURL + "?";
		
		Map<String, Object> m = new HashMap<String, Object>();

		m.put("ak", applicationKey);
		m.put("uid", uid);
		m.put("output", "json");
		m.put("scope","2");


		
		boolean start = true;
		for (Map.Entry<String, Object> e : m.entrySet()) {
			if (start == true) {
				url += e.getKey() + "=" + e.getValue();
				start = false;
			} else {
				url += "&" + e.getKey() + "=" + e.getValue();
			}
		}
		
		
		GetMethod getMethod = new GetMethod(url);

		String retJsonStr = null;

		PlaceRequestObject p = null;
		
		try {
			client.executeMethod(getMethod);
			
			retJsonStr = getMethod.getResponseBodyAsString();
			
			JsonNode jn = objectMapper.readTree(retJsonStr);
			
			JsonNode result = jn.get("result");
			
			if(result!=null && result.isArray())
			{
				p = new PlaceRequestObject();
				
				Iterator<JsonNode> jni = result.elements();
				
				if(jni.hasNext())
				{
					PlaceObject placeObject = objectMapper.readValue(jni.next().asText(), PlaceObject.class);
					
					p.setResult(placeObject);
				}
				
			}else
			{
				p = objectMapper.readValue(retJsonStr,PlaceRequestObject.class);
			}
			
			} catch (JsonParseException e1) {
				// TODO Auto-generated catch block
				logger.error("BaiduHttpRequestUtil.getPlaceObjectsByUid",e1);
			} catch (JsonMappingException e1) {
				// TODO Auto-generated catch block
				logger.error("BaiduHttpRequestUtil.getPlaceObjectsByUid",e1);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				logger.error("BaiduHttpRequestUtil.getPlaceObjectsByUid",e1);
			}


		return p;
	}

}
