/**
 * 
 */
package com.dajie.message.service;

import com.dajie.message.model.map.PoiInfoObject;

/**
 * @author tianyuan.zhu
 *
 */
public interface IPoiInfoService {
	
	public PoiInfoObject getPoiInfoObjectById(String uniqueId);//search
	
	public int delPoiInfoObjectById(String uniqueId);
	
	public int insertPoiInfoObject(PoiInfoObject poiInfoObject);
	
	public int updatePoiInfoObject(PoiInfoObject poiInfoObject);

}
