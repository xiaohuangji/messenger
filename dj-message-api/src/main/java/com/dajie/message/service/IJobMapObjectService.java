/**
 * 
 */
package com.dajie.message.service;

import java.util.List;

import com.dajie.message.model.job.JobModel;
import com.dajie.message.model.map.JobMapObject;

/**
 * @author tianyuan.zhu
 *
 */
public interface IJobMapObjectService {
	
	/**
	 * 新增职位
	 * @param jobModel 职位的模型
	 * @return 职位的多个docid
	 * */
	public List<String> addNewJobMapObject(JobModel jobModel);//添加
	
	/**
	 * 新增职位 内部调用
	 * */
	public List<String> addNewJobMapObject(List<JobMapObject> jobMapObjectList);
	
	/**
	 * 根据jid获取JobMapObject实体
	 * */
	public List<JobMapObject> getJobMapObjectById(int jid);
	
	public List<JobMapObject> getJobMapObjectByIdAndUid(int jid,String uniqueId);
	
	public int updateJobMapObject(JobModel jobModel);//更改
	
	public int deleteJobMapObject(int jid,String uniqueId);
	
	public int deleteJobMapObject(int jid);//删除

}
