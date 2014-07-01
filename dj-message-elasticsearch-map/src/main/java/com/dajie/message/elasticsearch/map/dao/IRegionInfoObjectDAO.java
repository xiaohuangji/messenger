package com.dajie.message.elasticsearch.map.dao;

import java.util.List;

import com.dajie.message.model.map.RegionInfoObject;

public interface IRegionInfoObjectDAO {
	
	public RegionInfoObject select(int id);
	
	public List<RegionInfoObject> getByName(String regionName);
	
	public List<RegionInfoObject> getByParentId(int parentId);
	
	public int updateRegionInfo(RegionInfoObject regionInfoObject);
	
}
