package com.dajie.message.elasticsearch.map.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dajie.message.elasticsearch.map.dao.IPoiInfoObjectDAO;
import com.dajie.message.elasticsearch.map.utils.ModelConvertorUtil;
import com.dajie.message.elasticsearch.map.utils.baidu.BaiduGeoInfoUtil;
import com.dajie.message.model.map.PoiInfoObject;
import com.dajie.message.model.map.geocode.AddressComponent;
import com.dajie.message.model.map.place.PlaceObject;
import com.dajie.message.service.IPoiInfoService;

@Component("poiInfoServiceImpl")
public class PoiInfoServiceImpl implements IPoiInfoService {
	
	@Autowired
	private IPoiInfoObjectDAO poiInfoObjectDAO;

	@Override
	public PoiInfoObject getPoiInfoObjectById(String uniqueId) {
		// TODO Auto-generated method stub
		PoiInfoObject p =  poiInfoObjectDAO.select(uniqueId);
		
		if(p == null)
		{
			
			PlaceObject  placeObject = BaiduGeoInfoUtil.getPlaceObjectsByUid(uniqueId);
			if(placeObject == null ) return null;
			
			AddressComponent addressComponent = BaiduGeoInfoUtil.getAddressComponentWithGeoPoint(placeObject.getLocation().getLat(), placeObject.getLocation().getLng());
			p = ModelConvertorUtil.convertPlaceObjectToPoiInfoObject(placeObject, addressComponent);
			
			this.insertPoiInfoObject(p);
			
		}

		return p;
	}

	@Override
	public int delPoiInfoObjectById(String uniqueId) {
		// TODO Auto-generated method stub
		return poiInfoObjectDAO.delete(uniqueId);
	}

	@Override
	public int insertPoiInfoObject(PoiInfoObject poiInfoObject) {
		// TODO Auto-generated method stub
		
		if(poiInfoObject == null) return 0;
		
		PoiInfoObject p = poiInfoObjectDAO.select(poiInfoObject.getUniqueId());
		
		if(p == null)
			return poiInfoObjectDAO.insert(poiInfoObject);
		else
			return poiInfoObjectDAO.update(poiInfoObject);
	}

	@Override
	public int updatePoiInfoObject(PoiInfoObject poiInfoObject) {
		// TODO Auto-generated method stub
		
		if(poiInfoObject == null) return 0;
		
		poiInfoObject.setUpdateTime(System.currentTimeMillis());
		
		return poiInfoObjectDAO.update(poiInfoObject);
	}

	public IPoiInfoObjectDAO getPoiInfoObjectDAO() {
		return poiInfoObjectDAO;
	}

	public void setPoiInfoObjectDAO(IPoiInfoObjectDAO poiInfoObjectDAO) {
		this.poiInfoObjectDAO = poiInfoObjectDAO;
	}

}
