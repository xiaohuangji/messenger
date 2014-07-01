package com.dajie.message.service;

import java.util.List;

import com.dajie.message.annotation.mcp.ParamNullable;
import com.dajie.message.annotation.rest.RestBean;
import com.dajie.message.enums.JobStatus;
import com.dajie.message.model.MCPInteger;
import com.dajie.message.model.job.JobDescriptionModel;
import com.dajie.message.model.job.JobDescriptionRelationInfo;
import com.dajie.message.model.job.JobInfo;
import com.dajie.message.model.job.JobThirdPartInfo;
import com.dajie.message.model.job.SimpleJobInfo;

/**
 * 职位信息信息服务
 * @author xinquan.guan
 *
 */
@RestBean
public interface IJobService {

	/**
	 * 发布Job
	 * @param _userId
	 * @param positionName
	 * @param corpName
	 * @param corpId
	 * @param salaryStart
	 * @param salaryEnd
	 * @param workExperience
	 * @param educationLevel
	 * @param descriptions
	 * @param poies
	 * @param expire
	 */
	public MCPInteger pubJob(int _userId,String positionName,String jobType,String corpName,int corpId,int salaryStart,int salaryEnd,int workExperience,int educationLevel,String descriptionIds,String poiUids,long startDate,long endDate,@ParamNullable(true)String oldJid,@ParamNullable(value=true,defaultValue="-1")int industry,@ParamNullable(value=true,defaultValue="0")int hunter,@ParamNullable(value=true,defaultValue="0")long createDate);
	
	/**
	 * 带有描述的发布职位
	 * @param _userId
	 * @param positionName
	 * @param jobType
	 * @param corpName
	 * @param corpId
	 * @param salaryStart
	 * @param salaryEnd
	 * @param workExperience
	 * @param educationLevel
	 * @param descriptionIds
	 * @param poiUids
	 * @param startDate
	 * @param endDate
	 * @param oldJid
	 * @param industry
	 * @param hunter
	 * @param createDate
	 * @return
	 */
	public MCPInteger pubJobWithDesc(int _userId,String positionName,String jobType,String corpName,int corpId,int salaryStart,int salaryEnd,int workExperience,int educationLevel,String descriptions,String poiUids,long startDate,long endDate,@ParamNullable(true)String oldJid,@ParamNullable(value=true,defaultValue="-1")int industry,@ParamNullable(value=true,defaultValue="0")int hunter,@ParamNullable(value=true,defaultValue="0")long createDate);
	
	
	
	/**
	 * 修改job信息
	 * @param jobId
	 * @param positionName
	 * @param jobType
	 * @param corpName
	 * @param corpId
	 * @param salaryStart
	 * @param salaryEnd
	 * @param workExperience
	 * @param educationLevel
	 * @param descriptions
	 * @param poies
	 * @param expire
	 * @return
	 */
	public int changeJob(int _userId,int jobId,String positionName,String jobType,@ParamNullable(true)String corpName,@ParamNullable(true)int corpId,int salaryStart,int salaryEnd,int workExperience,int educationLevel,String descriptionIds,String poiUids,long startDate,long endDate,@ParamNullable(true)int status,@ParamNullable(value=true,defaultValue="-1")int hunter,@ParamNullable(value=true,defaultValue="-1")int industry);
	
	/**
	 * 根据HRId 获取其相关的职位信息
	 * @param jobUserId
	 * @param timestamp
	 * @param page
	 * @return
	 */
	public List<SimpleJobInfo> listJobsByUserId(int jobUserId,long timestamp,int pageSize);
	
	/**
	 * 获取当前用户发布的JobInfo信息
	 * @param jobUserId
	 * @param timestamp
	 * @param page
	 * @return
	 */
	public List<SimpleJobInfo> listJobs(int _userId,long timestamp,int pageSize);
	
	/**
	 * 获取单个Job的详情
	 * @param jobId
	 */
	public JobInfo getJobDetail(int jobId);
	
	/**
	 * 删除job
	 * @param jobId
	 */
	public int deleteJob(int jobId);
	

	
	
	
	/**
	 * 发送职位描述
	 * @param description
	 * @return
	 */
	public MCPInteger pubJobDescription(int _userId,String description);
	
	/**
	 * 根据拼接的desrIds，批量获取描述信息
	 * @param desrIds
	 * @return
	 */
	public List<JobDescriptionModel> listJobDescription(String desrIds);
	
	/**
	 * 根据用户信息，获取job描述
	 * @param _userId
	 * @return
	 */
	public List<JobDescriptionRelationInfo> listJobDescriptionsByUserId(int _userId,long timestamp,int pageSize);
	
	/**
	 * 将描述与用户解除关联
	 * @param _userId
	 * @param descriptionId
	 * @return
	 */
	public int deleteJobDescription(int _userId,int descriptionId);
	
	/**
	 * 举报job
	 * @param _userId
	 * @param jobId
	 * @param description
	 * @return
	 */
	public int informJob(int _userId,int jobId,@ParamNullable(true)String description);
	
	/**
	 * 更改job的状态
	 * @param jobId
	 * @param status
	 * @return
	 */
	public int changeJobStatus(int jobId,JobStatus status);
	
	/**
	 * 根据corpName获取三大网站职位的列表信息
	 * @param corpName
	 * @return
	 */
	public List<JobThirdPartInfo> listJobThirdPartInfo(String corpName,int seq,int pageSize);
	
	/**
	 * 同步主站的第三方职位到本地的第三方职位
	 * @param pageSize
	 */
	public void synchronousDB(int start,int pageSize);
	
	/**
	 * hr 同步大街网数据
	 * @param _userId
	 */
	public List<JobThirdPartInfo> listDajieSynchronous(int _userId,int seq,int pageSize);
	
	/**
	 * 
	 * 将某个用户发布的所有职位，设置成非法的
	 * @param userId
	 * @return
	 */
	public int changeJobStatusToInformByUserId(int userId);
	
	/**
	 * 通过jobIds获取job信息
	 * @param jobIds
	 * @return
	 */
	public List<SimpleJobInfo> listJobsByJobIds(String jobIds);
	
}
