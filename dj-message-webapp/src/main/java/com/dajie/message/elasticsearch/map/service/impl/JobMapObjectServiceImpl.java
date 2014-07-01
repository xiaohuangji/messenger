package com.dajie.message.elasticsearch.map.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dajie.message.elasticsearch.map.constants.MapObjectConstants;
import com.dajie.message.elasticsearch.map.utils.LoggerManager;
import com.dajie.message.elasticsearch.map.utils.ModelConvertorUtil;
import com.dajie.message.elasticsearch.map.utils.baidu.BaiduGeoInfoUtil;
import com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchJobUtil;
import com.dajie.message.elasticsearch.map.utils.elasticsearch.ElasticSearchUtil;
import com.dajie.message.model.job.JobDescriptionModel;
import com.dajie.message.model.job.JobModel;
import com.dajie.message.model.map.JobMapObject;
import com.dajie.message.model.map.PoiInfoObject;
import com.dajie.message.model.map.geocode.AddressComponent;
import com.dajie.message.model.map.place.PlaceObject;
import com.dajie.message.service.IJobMapObjectService;
import com.dajie.message.service.IJobService;
import com.dajie.message.service.IPoiInfoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component("jobMapObjectServiceImpl")
public class JobMapObjectServiceImpl implements IJobMapObjectService {

	private static Logger logger = LoggerManager.getLogger(JobMapObjectServiceImpl.class);

	@Autowired
	private IPoiInfoService poiInfoService;
	@Autowired
	private IJobService jobService;
	
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public List<String> addNewJobMapObject(JobModel jobModel) {
		// TODO Auto-generated method stub
		
		if(jobModel == null)
		{
			logger.error("jobModel  is null in addNewJobMapObject");
			return null;
		}
		
		
		String uniqueIds = jobModel.getPoiUids();
		
		if(StringUtils.isEmpty(uniqueIds))
		{
			logger.error("uniqueIds  is null in addNewJobMapObject");
			return null;
		}
			
		String[] uniqueIdArray = uniqueIds.split(",");
		
		if(uniqueIdArray == null || uniqueIdArray.length == 0)
		{
			logger.error("uniqueIds  is null in addNewJobMapObject");
			return null;
		}
		
		//获取poi详细列表
		List<PoiInfoObject> poiInfoObjectList = convertuniqueIdArrayToPoiInfoObjects(uniqueIdArray);
		
		//拼接tag
		String tag = convertDescriptionsIdsToTag(jobModel.getDescriptionIds());
		
		//转换模型
		List<JobMapObject> jobMapObjectList = ModelConvertorUtil.convertJobModelsToJobMapObjects(jobModel, tag, poiInfoObjectList);
		
		return this.addNewJobMapObject(jobMapObjectList);
	}


	@Override
	public List<String> addNewJobMapObject(List<JobMapObject> jobMapObjectList) {
		// TODO Auto-generated method stub
		
		if(jobMapObjectList == null || jobMapObjectList.size() == 0)
		{
			logger.error("addNewJobMapObject jobMapObjectList is null");
			return null;
		}
		
		List<String>  jsons = new ArrayList<String>();
		//转换成json串
		try {
		if(jobMapObjectList!=null && jobMapObjectList.size() > 0)
		{
			for(JobMapObject j : jobMapObjectList)
			{
					jsons.add(objectMapper.writeValueAsString(j));
			}
		}
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			logger.error("PointOnMapServiceImpl.addNewJobMapObject",e);
		}
		
		
		List<String> resultList = ElasticSearchUtil.addDoc(MapObjectConstants.OBJECT_TYPE_JOB,0,jsons.toArray(new String[0]));
		
		return resultList;
	}

	
	@Override
	public List<JobMapObject> getJobMapObjectById(int jid) {
		// TODO Auto-generated method stub
		
		return ElasticSearchJobUtil.getJobByJid(jid);
	}

	@Override
	public List<JobMapObject> getJobMapObjectByIdAndUid(int jid, String uniqueId) {
		// TODO Auto-generated method stub
		return ElasticSearchJobUtil.getJobByJidAndUid(jid, uniqueId);
	}

	@Override
	public int updateJobMapObject(JobModel jobModel) {
		// TODO Auto-generated method stub
		
		if(jobModel == null)
		{
			logger.error("PointOnMapServiceImpl.updateJobMapObject : jobSearchModel is null.");
			return 0;
		}
		
		int jid = jobModel.getJobId();
		
		List<JobMapObject> listInES = ElasticSearchJobUtil.getJobByJid(jid);
		
		if(listInES == null || listInES.size() == 0)
		{
			logger.info("PointOnMapServiceImpl.updateJobMapObject : JobMapObjectList is null.");
			return 0;
		}
		
		String uniqueIds = jobModel.getPoiUids();
		String[] uniqueIdArray = uniqueIds.split(",");
		
		if(uniqueIdArray == null || uniqueIdArray.length == 0)
		{
			logger.info("uniqueIds  is null in updateJobMapObject.");
			return 0;
		}
		
		int result = 0;
		
		for(JobMapObject j : listInES)
		{
			ElasticSearchUtil.deleteDocById(MapObjectConstants.OBJECT_TYPE_JOB, j.get_id());
		}
		
		//获取poi详细列表
		List<PoiInfoObject> poiInfoObjectList = convertuniqueIdArrayToPoiInfoObjects(uniqueIdArray);
		String tag = convertDescriptionsIdsToTag(jobModel.getDescriptionIds());

		List<JobMapObject> listToUpdate = ModelConvertorUtil.convertJobModelsToJobMapObjects(jobModel, tag, poiInfoObjectList);
		List<String> addResult = this.addNewJobMapObject(listToUpdate);
		
		if(addResult!=null) result +=addResult.size();
		
		return result;
	}

	@Override
	public int deleteJobMapObject(int jid, String uniqueId) {
		// TODO Auto-generated method stub
		if(jid == 0|| StringUtils.isEmpty(uniqueId))
		{
			logger.error("deleteJobMapObject jid = 0 or uniqueId = null");
			return 0;
		}
		List<JobMapObject> list = ElasticSearchJobUtil.getJobByJidAndUid(jid, uniqueId);
		
		int result = 0;
		if(list !=null && list.size() > 0)
		{
			
			for(JobMapObject j : list)
			{
				result += ElasticSearchUtil.deleteDocById(MapObjectConstants.OBJECT_TYPE_JOB, j.get_id());
			}
		}
		
		return result;
	}
	
	@Override
	public int deleteJobMapObject(int jid) {
		// TODO Auto-generated method stub
		if(jid == 0)
		{
			logger.error("deleteJobMapObject jid = 0");
			return 0;
		}
		List<JobMapObject> list = ElasticSearchJobUtil.getJobByJid(jid);
		
		int result = 0;
		if(list !=null && list.size() > 0)
		{
			
			for(JobMapObject j : list)
			{
				result += ElasticSearchUtil.deleteDocById(MapObjectConstants.OBJECT_TYPE_JOB, j.get_id());
			}
		}
		
		return result;
	}

	

	private String convertDescriptionsIdsToTag(String descriptionIds)
	{
		if(StringUtils.isEmpty(descriptionIds))
		{
			logger.error("convertDescriptionsIdsToTag fail. descriptionIds is null.");
			return null;
		}
		
		List<JobDescriptionModel> discriptions = jobService.listJobDescription(descriptionIds);
		String tag = "";
		if(discriptions!=null && discriptions.size() > 0)
		{
			for(JobDescriptionModel d:discriptions)
			{
				tag += d.getDescription()+" ";
			}
			tag= tag.substring(0, tag.length() -1);
		}
		
		return tag;
		
	}
	
	/**
	 * 获取poi信息并存入数据库
	 * */
	private List<PoiInfoObject> convertuniqueIdArrayToPoiInfoObjects(
			String[] uniqueIdArray) {
		// TODO Auto-generated method stub

			List<PoiInfoObject> poiInfoObjectList =  new ArrayList<PoiInfoObject>();
			for(String s : uniqueIdArray)
			{
				
				PoiInfoObject  poiInfoObject = poiInfoService.getPoiInfoObjectById(s);
				
				if(poiInfoObject!=null)
				{
					poiInfoObjectList.add(poiInfoObject);
					continue;
				}
				
				PlaceObject  placeObject = BaiduGeoInfoUtil.getPlaceObjectsByUid(s);
				if(placeObject == null ) continue;
				AddressComponent addressComponent = BaiduGeoInfoUtil.getAddressComponentWithGeoPoint(placeObject.getLocation().getLat(), placeObject.getLocation().getLng());
				PoiInfoObject p = ModelConvertorUtil.convertPlaceObjectToPoiInfoObject(placeObject, addressComponent);
				
				int result = poiInfoService.insertPoiInfoObject(p);
				
				logger.info("add new PoiInfoObject with result = "+result);
				
				if(p!=null) poiInfoObjectList.add(p);
			}
		
		return poiInfoObjectList;
		
	}


	public IPoiInfoService getPoiInfoService() {
		return poiInfoService;
	}


	public void setPoiInfoService(IPoiInfoService poiInfoService) {
		this.poiInfoService = poiInfoService;
	}


	public IJobService getJobService() {
		return jobService;
	}


	public void setJobService(IJobService jobService) {
		this.jobService = jobService;
	}
	
	
}
