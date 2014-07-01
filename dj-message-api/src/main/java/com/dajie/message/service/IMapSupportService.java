/**
 * 
 */
package com.dajie.message.service;

import java.util.List;

import com.dajie.message.annotation.rest.RestBean;
import com.dajie.message.model.map.geocode.Position;
import com.dajie.message.model.map.place.PlaceObject;
import com.dajie.message.model.map.suggestion.SuggestionObject;

/**
 * @author tianyuan.zhu
 *
 */
@RestBean
public interface IMapSupportService {
	
	
	/**
	 * 通过坐标点获取POI的列表
	 * @param lat 纬度
	 * @param lon 经度
	 * 
	 * */
	List<Position> getPOIListByLocation(double lat,double lon);
	
	/**
	 * 通过POI名称和区域获取POI的列表
	 * @param lat 纬度
	 * @param lon 经度
	 * 
	 * */
	List<PlaceObject> getPOIListByName(String query,String region,int pageNumber,int pageSize);
	
	/**
	 * 通过POI名称和区域获取POI的列表
	 * @param lat 纬度
	 * @param lon 经度
	 * 
	 * */
	List<PlaceObject> getPOIListByLocationInPages(String query,double lat,double lon,int pageNumber,int pageSize);
	
	/**
	 * 通过区域与关键词获取输入提示
	 * @param 查询关键字
	 * @param 用户现在所在区域（城市，区域等）
	 * 
	 * */
	List<SuggestionObject> getSuggestionByLocation(String query,double lat,double lon);

}
