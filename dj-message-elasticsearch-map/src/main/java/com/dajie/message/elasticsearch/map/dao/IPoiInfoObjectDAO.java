/**
 * 
 */
package com.dajie.message.elasticsearch.map.dao;

import com.dajie.message.model.map.PoiInfoObject;


/**
 * @author tianyuan.zhu
 *
 */
public interface IPoiInfoObjectDAO {
	
	int insert(PoiInfoObject poiInfoObject);
	
	int update(PoiInfoObject poiInfoObject);
	
	int delete(String uniqueId);
	
	PoiInfoObject select(String uniqueId);
	
}
