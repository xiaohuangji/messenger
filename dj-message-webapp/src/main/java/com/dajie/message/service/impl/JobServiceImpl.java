package com.dajie.message.service.impl;

import com.dajie.message.dao.*;
import com.dajie.message.enums.InformStatusEnum;
import com.dajie.message.enums.JobStatus;
import com.dajie.message.enums.PlatformEnum;
import com.dajie.message.model.MCPInteger;
import com.dajie.message.model.PlatformMap;
import com.dajie.message.model.job.*;
import com.dajie.message.model.map.PoiInfoObject;
import com.dajie.message.model.user.UserBase;
import com.dajie.message.model.user.UserCareer;
import com.dajie.message.service.*;
import com.dajie.message.service.mq.GoudaVerifyEvent;
import com.dajie.message.service.mq.GoudaVerifyType;
import com.dajie.message.service.worker.GoudaVerifyPutter;
import com.dajie.message.service.worker.JobTranslate;
import com.dajie.message.service.worker.MainStationJobDao;
import com.dajie.message.util.EmojiFilterUtil;
import com.dajie.message.util.log.LoggerInformation;
import com.dajie.word.filter.model.FilterWord.FilterLevel;
import com.dajie.word.filter.service.KeywordFilterService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Component("jobServiceImpl")
public class JobServiceImpl implements IJobService {

	@Autowired
	private JobDAO jobDao;

	@Autowired
	private JobDescriptionDAO jobDescriptionDAO;

	@Autowired
	private IUserProfileService userBaseService;

	@Autowired
	private IJobMapObjectService jobMapObjectService;

	@Autowired
	private IPoiInfoService poiIPoiInfoService;

	@Autowired
	private IUserProfileService userProfileService;

	@Autowired
	private JobDescriptionUserRelationDao jobDescriptionUserRelationDao;

	@Autowired
	private JobInformDAO jobInformDao;

	@Autowired
	private JobThirdPartDAO jobThirdPartDAO;

	@Autowired
	private MainStationJobDao mainStationJobDao;

	@Autowired
	private JobTranslate translate;

	@Autowired
	private IPlatformMapService platformMapService;
	
	@Autowired
	private JobDetailCntDAO jobDetailCntDAO;
	
	@Autowired
	private KeywordFilterService keywordFilterService;
	
	public static final int ANTI_ERR = 1015;

	
	//private GoudaVerifyMQPutter verifyMQPutter = GoudaDubboContext.getInstance().getGoudaRabbitMQPutter();

	@Autowired
	private GoudaVerifyPutter goudaVerifyPutter;
	
	private static final Logger logger = Logger.getLogger(IJobService.class);

	private static ThreadLocal<MessageDigest> messageDigest = new ThreadLocal<MessageDigest>() {
		@Override
		protected MessageDigest initialValue() {
			try {
				return MessageDigest.getInstance("md5");
			} catch (NoSuchAlgorithmException e) {
				LoggerInformation.LoggerErr(logger, "md5 create error", e);
				return null;
			}
		}
	};

	@Override
	public MCPInteger pubJob(int _userId, String positionName, String jobType,
			String corpName, int corpId, int salaryStart, int salaryEnd,
			int workExperience, int educationLevel, String descriptionIds,
			String poiUids, long startDate, long endDate, String oldJid,
			int industry,int hunter,long createDate) {
		MCPInteger ret = new MCPInteger();

		if(!keywordFilterService.checkByLevel(corpName, FilterLevel.FIRST)){
			ret.setCode(ANTI_ERR);
			return ret;
		}

		if(!keywordFilterService.checkByLevel(positionName, FilterLevel.FIRST)){
			ret.setCode(ANTI_ERR);
			return ret;
		}

		
		
		UserCareer user = userProfileService.getUserCareer(_userId);
		
		if (!userProfileService.isVerified(_userId)) { // 用户还没有认证通过
			ret.setRet(-1);
			return ret;
		}

		/**
		 * 插入数据库
		 */
		JobModel model = new JobModel();
		model.setCorpId(corpId);
		model.setCorpName(corpName);
		model.setCreateDate(new Date());
		model.setDescriptionIds(descriptionIds);
		model.setEducationLevel(educationLevel);
		model.setUserId(_userId);
		model.setPoiUids(poiUids);
		model.setPositionName(positionName);
		model.setSalaryEnd(salaryEnd);
		model.setSalaryStart(salaryStart);
		model.setUpdateDate(new Date());
		model.setWorkExperience(workExperience);
		model.setJobType(jobType);
		model.setStartDate(new Date(startDate));
		model.setEndDate(new Date(endDate));
		if(createDate > 1){
			model.setCreateDate(new Date(createDate));
		}else{
			model.setCreateDate(new Date());
		}
		model.setHunter(hunter);

		if (industry > 0) {
			model.setIndustry(industry);
		} else {
			if (user != null) {
				model.setIndustry(user.getIndustry());
			}
		}

		if (!StringUtils.isEmpty(oldJid)) {
			model.setOldJid(oldJid);
		}

		model.getDescriptionIds();

		jobDao.insertJob(model);// 插入数据库

		jobMapObjectService.addNewJobMapObject(model);// 添加到doc中

		ret.setRet(model.getJobId());
		

		putJobVerify(model);

		return ret;
	}

	@Override
	public int changeJob(int _userId, int jobId, String positionName,
			String jobType, String corpName, int corpId, int salaryStart,
			int salaryEnd, int workExperience, int educationLevel,
			String descriptionIds, String poiUids, long startDate,
			long endDate, int status,int hunter,int industry) {

		
		if(!keywordFilterService.checkByLevel(corpName, FilterLevel.FIRST)){
			return ANTI_ERR;
		}

		if(!keywordFilterService.checkByLevel(positionName, FilterLevel.FIRST)){
			return ANTI_ERR;
		}

			
		JobModel model = jobDao.selectJobModelByJobId(jobId);

		if (model == null)
			return 1;

		if (corpId > 0)
			model.setCorpId(corpId);
		if (!StringUtils.isEmpty(corpName))
			model.setCorpName(corpName);
		model.setUpdateDate(new Date());
		// model.setCreateDate(new Date().getTime());
		if (!StringUtils.isEmpty(descriptionIds))
			model.setDescriptionIds(descriptionIds);
		model.setEducationLevel(educationLevel);
		if (startDate > 0)
			model.setStartDate(new Date(startDate));
		if (endDate > 0)
			model.setEndDate(new Date(endDate));
		if (_userId > 0)
			model.setUserId(_userId);
		if (!StringUtils.isEmpty(positionName))
			model.setPositionName(positionName);
		model.setSalaryEnd(salaryEnd);
		model.setSalaryStart(salaryStart);
		model.setUpdateDate(new Date());
		model.setWorkExperience(workExperience);
		if (!StringUtils.isEmpty(jobType))
			model.setJobType(jobType);
		if (!StringUtils.isEmpty(poiUids))
			model.setPoiUids(poiUids);
		if (status >= 0 && status < 3)
			model.setStatus(status);
		if(hunter > -1){
			model.setHunter(hunter);
		}
		
		if(industry > -1){
			model.setIndustry(industry);
		}
		jobDao.updateJobModel(model);
		jobMapObjectService.updateJobMapObject(model);
		
		if(status == JobStatus.JOB_OK.getCode())
			putJobVerify(model);
		
		return 0;
	}

	@Override
	public List<SimpleJobInfo> listJobsByUserId(int jobUserId, long timestamp,
			int pageSize) {

		if (timestamp == 0)
			timestamp = new Date().getTime();

		List<SimpleJobInfo> jobInfos = new ArrayList<SimpleJobInfo>();
		List<JobModel> models = jobDao.selectJobModelByUserId(jobUserId,
				new Date(timestamp), pageSize);
		UserBase userBase = userBaseService.getUserBase(jobUserId);
		for (JobModel jm : models) {
			jobInfos.add(new SimpleJobInfo(jm, userBase));
		}
		return jobInfos;
	}

	@Override
	public List<SimpleJobInfo> listJobs(int _userId, long timestamp,
			int pageSize) {

		return listJobsByUserId(_userId, timestamp, pageSize);
	}

	@Override
	public JobInfo getJobDetail(int jobId) {
		JobModel jobModel = jobDao.selectJobModelByJobId(jobId);

		if(jobModel == null){
			return null;
		}
		
		UserBase userBase = userBaseService.getUserBase(jobModel.getUserId());

		UserCareer userCareer = userProfileService.getUserCareer(jobModel
				.getUserId());

		List<PoiInfoObject> poiInfoObjects = new ArrayList<PoiInfoObject>();

		List<String> poiUids = changeStringToList(jobModel.getPoiUids());

		for (String pioUid : poiUids) {
			PoiInfoObject poiInfo = poiIPoiInfoService
					.getPoiInfoObjectById(pioUid);
			if (poiInfo != null)
				poiInfoObjects.add(poiInfo);
		}

		// 分析descriptions
/*		List<JobDescriptionModel> descriptionModels = new ArrayList<JobDescriptionModel>();
		if (!StringUtils.isEmpty(jobModel.getDescriptionIds()))
			descriptionModels = jobDescriptionDAO
					.selectDescriptionsByIds(jobModel.getDescriptionIds());
*/
		List<JobDescriptionModel> descriptionModels = getDescriptions(jobModel.getDescriptionIds());
		
		JobInfo jobInfo = new JobInfo(jobModel, userBase, userCareer,
				poiInfoObjects, descriptionModels);

		
		Integer cnt = jobDetailCntDAO.selectJobDetailCnt(jobId);
		if(cnt == null){
			jobDetailCntDAO.insertJobDetailCnt(jobId, 1, new Date());
		}else{
			jobDetailCntDAO.updateJobDetailCnt(jobId, cnt+1);
		}
		
		return jobInfo;
	}

	private List<String> changeStringToList(String str) {
		List<String> rets = new ArrayList<String>();
		if (!StringUtils.isEmpty(str)) {
			String[] strarr = str.split(",");
			for (String s : strarr) {
				rets.add(s);
			}
		}
		return rets;
	}

	@Override
	public int deleteJob(int jobId) {
		jobDao.deleteJobModel(jobId);
		jobMapObjectService.deleteJobMapObject(jobId);
		return 0;

	}

	@Override
	public MCPInteger pubJobDescription(int _userId, String description) {
		/**
		 * 先判断description是否已经存在，如果已经存在，就直接返回id
		 */
		MCPInteger ret = new MCPInteger();
		if(!keywordFilterService.checkByLevel(description, FilterLevel.FIRST)){
			ret.setCode(ANTI_ERR);
			return ret;
		}
		
		description = EmojiFilterUtil.emojiKiller(description);
		
		byte[] md5 = null;
		
		try {
			md5 = messageDigest.get().digest(description.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			LoggerInformation.LoggerErr(logger, "md5 error", e);
		}
		if (md5 == null) {
			ret.setRet(-1);
			return ret;
		}

		Integer id = jobDescriptionDAO.selectDescriptionByMd5(md5);

		if (id != null && id > 0) {
			addJobDescriptionUserRelation(_userId, id);// 存储
			ret.setRet(id);
			return ret;
		}
		/**
		 * 如果description 不存在，那么就将description存入数据库，并且返回id
		 */
		JobDescriptionModel model = new JobDescriptionModel();
		model.setCreateDate(new Date());
		model.setDescription(description);
		model.setMd5Description(md5);

		if (md5 != null) {
			jobDescriptionDAO.insertDescription(model);
			addJobDescriptionUserRelation(_userId, model.getId());// 存储
		}
		ret.setRet(model.getId());
		
		
		
		return ret;
	}

	@Override
	public List<JobDescriptionModel> listJobDescription(String desrIds) {
		return jobDescriptionDAO.selectDescriptionsByIds(desrIds);
	}

	@Override
	public List<JobDescriptionRelationInfo> listJobDescriptionsByUserId(
			int _userId, long timestamp, int pageSize) {
		if (timestamp == 0)
			timestamp = new Date().getTime();
		List<JobDescriptionRelationInfo> jobDescriptionRelationInfos = jobDescriptionUserRelationDao
				.selectRelationByUserId(_userId, new Date(timestamp), pageSize);

		StringBuilder builder = new StringBuilder();
		Map<Integer, JobDescriptionRelationInfo> relationMap = new HashMap<Integer, JobDescriptionRelationInfo>();
		List<JobDescriptionModel> descModels = new ArrayList<JobDescriptionModel>();

		for (JobDescriptionRelationInfo jdri : jobDescriptionRelationInfos) {
			relationMap.put(jdri.getDescriptionId(), jdri);
			builder.append(jdri.getDescriptionId());
			builder.append(",");
		}
		if (builder.length() > 0) {
			builder.deleteCharAt(builder.length() - 1);
			descModels = listJobDescription(builder.toString());
		}

		if (descModels != null) {
			for (JobDescriptionModel jdm : descModels) {
				JobDescriptionRelationInfo jdr = relationMap.get(jdm.getId());
				if (jdr != null)
					jdr.setDescription(jdm.getDescription());
			}
		}

		return jobDescriptionRelationInfos;
	}

	private void addJobDescriptionUserRelation(int userId, int descriptionId) {

		JobDescriptionRelationInfo info = jobDescriptionUserRelationDao
				.selectRelationByUserIdAndDesrId(userId, descriptionId);

		if (info == null)
			jobDescriptionUserRelationDao.addRelation(userId, descriptionId,
					new Date());
	}

	@Override
	public int deleteJobDescription(int _userId, int descriptionId) {
		// jobDescriptionUserRelationDao.sel

		jobDescriptionUserRelationDao.deleteRelation(_userId, descriptionId);
		return 0;
	}

	@Override
	public int informJob(int _userId, int jobId, String description) {
		JobModel jobModel = jobDao.selectJobModelByJobId(jobId);
		if (jobModel == null) {
			return 0;
		}
		JobInformModel jobInform = new JobInformModel();
		jobInform.setCreateTime(new Date());
		jobInform.setDescription(description);
		jobInform.setInformUserId(_userId);
		jobInform.setJobId(jobId);
		jobInform.setJobUserId(jobModel.getUserId());
		jobInform.setStatus(InformStatusEnum.NO_PROCESS.getCode());
		jobInform.setPositionName(jobModel.getPositionName());
		jobInformDao.insertJobInform(jobInform);
		jobModel.setStatus(JobStatus.JOB_INFROM.getCode());
		return 0;
	}

	public int changeJobStatus(int jobId, JobStatus status) {
		JobModel jobModel = jobDao.selectJobModelByJobId(jobId);

		if (jobModel != null) {
			long startDate = 0;
			if (jobModel.getStartDate() != null) {
				startDate = jobModel.getStartDate().getTime();
			}

			long endDate = 0;
			if (jobModel.getEndDate() != null) {
				endDate = jobModel.getEndDate().getTime();
			}
			changeJob(jobModel.getUserId(), jobModel.getJobId(),
					jobModel.getPositionName(), jobModel.getJobType(),
					jobModel.getCorpName(), jobModel.getCorpId(),
					jobModel.getSalaryStart(), jobModel.getSalaryEnd(),
					jobModel.getWorkExperience(), jobModel.getEducationLevel(),
					jobModel.getDescriptionIds(), jobModel.getPoiUids(),
					startDate, endDate, status.getCode(),-1,-1);

//			if (status == JobStatus.JOB_INFROM) {
//				if (!StringUtils.isEmpty(description))
//					pushService.sendTextMessage(jobModel.getUserId(),
//							"抱歉，您的机会" + jobModel.getPositionName() + "由于"
//									+ description
//									+ "被用户举报，经审核后已下线，重新编辑后可以再次发布");
//			}
			return 0;
		}
		return -1;
	}

	@Override
	public void synchronousDB(int start,int pageSize) {
		while (true) {
			Map<String, Object> map = new HashMap<String, Object>();
			if(start < 0)
			{
				start = 0;
			}
			
			map.put("start", start);
			map.put("pagesize", pageSize);
			List<JobThirdPartModel> jobs = mainStationJobDao
					.listJobThirdPartModels(map);
			if (jobs == null || jobs.size() == 0) {
				return;
			}
			for (JobThirdPartModel job : jobs) {
				if (StringUtils.isEmpty(job.getJid())) {
					continue;
				}
				JobQualifyModel qualify = mainStationJobDao
						.selectSimpleJobQualify(job.getJid());

				try {

					translate.translateJobWithDB(job, qualify);
				} catch (Exception e) {
					LoggerInformation.LoggerErr(logger, "translate job error",
							e);
				}
			}
			try{
				start = jobs.get(jobs.size() -1 ).getSeq();
			}catch(Exception e){
				start -= pageSize;
			}
			LoggerInformation.LoggerInfo(logger, "transform job " + start);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				LoggerInformation.LoggerErr(logger, "sleep synchronous error",
						e);
			}

		}
	}

	@Override
	public List<JobThirdPartInfo> listJobThirdPartInfo(String corpName,
			int seq, int pageSize) {
		List<JobThirdPartInfo> ret = null;
		
		if(seq < 0)
		{
			seq = 0;
		}
		ret = jobThirdPartDAO.selectThreeSiteJobThirdPartInfo(corpName, seq,
				pageSize);
		if (ret == null) {
			ret = new ArrayList<JobThirdPartInfo>();
		}

		return ret;
	}

	@Override
	public List<JobThirdPartInfo> listDajieSynchronous(int _userId,
			int seq, int pageSize) {

		if(seq < 0)
			seq = 0;
		PlatformMap pf = platformMapService.getPlatformMapByUserIdAndType(
				_userId, PlatformEnum.DAJIE);

		if (pf == null) {
			return new ArrayList<JobThirdPartInfo>();
		}
		int djUserId = Integer.valueOf(pf.getPlatformUid());
/*		if(seq <= 0)
		{
			seq = Integer.MAX_VALUE;
		}
*/		List<JobThirdPartInfo> jtpis = jobThirdPartDAO
				.selectDaJieThirdPartInfoByUserId(djUserId, seq, pageSize);
		if (jtpis == null) {
			jtpis = new ArrayList<JobThirdPartInfo>();
		}
		return jtpis;
	}

	
	private void putJobVerify(JobModel model){
		
		if(model == null)
			return;
		UserBase userBase = userBaseService.getUserBase(model.getUserId());
		/**
		 * 审核消息
		 */
		GoudaVerifyEvent jobVerifyBean = new GoudaVerifyEvent();
		jobVerifyBean.setCorpName(model.getCorpName());
		jobVerifyBean.setDescriptions(changeJobDescriptionIdsToStr(model.getDescriptionIds()));
		jobVerifyBean.setUserId(model.getUserId());
		jobVerifyBean.setUserName(userBase.getName());
		jobVerifyBean.setId(model.getJobId());
		jobVerifyBean.setPositionName(model.getPositionName());
		jobVerifyBean.setVerifyType(GoudaVerifyType.VERIFY_JOB.getCode());
		jobVerifyBean.setUpdateDate(System.currentTimeMillis());
		goudaVerifyPutter.addVerifyEvent(jobVerifyBean);
	}
	
	private String changeJobDescriptionIdsToStr(String jobDescriptionIds){
		if(StringUtils.isEmpty(jobDescriptionIds))
			return "";
		List<JobDescriptionModel> descriptionModels = jobDescriptionDAO.selectDescriptionsByIds(jobDescriptionIds);

		StringBuffer buffer = new StringBuffer();
		
		for(JobDescriptionModel jdm : descriptionModels){
			buffer.append(jdm.getDescription());
			buffer.append(";");
		}
		buffer.deleteCharAt(buffer.length()-1);
		return buffer.toString();
		
	}

	@Override
	public int changeJobStatusToInformByUserId(int userId) {
		Date date = new Date();
		int pageSize = 10;
		List<JobModel> jobModels = jobDao.selectJobModelByUserId(userId, date, pageSize);
		changeJobsToInform(jobModels);
		
		while(jobModels.size() > pageSize-1){
			jobModels = jobDao.selectJobModelByUserId(userId, jobModels.get(pageSize -1).getUpdateDate(), pageSize);
			changeJobsToInform(jobModels);
		}
		return 0;
	}
	
	private void changeJobsToInform(List<JobModel> jobModels){
		
		if(jobModels != null){
			for(JobModel jm : jobModels){
				changeJobStatus(jm.getJobId(), JobStatus.JOB_INFROM);
			}
		}
		
	}

	@Override
	public List<SimpleJobInfo> listJobsByJobIds(String jobIds) {
		List<JobModel> jobModels = jobDao.selectJobModelByJobIds(jobIds);
		List<SimpleJobInfo> jobInfos = new ArrayList<SimpleJobInfo>();
		
		for (JobModel jm : jobModels) {
			try{
				UserBase userBase = userBaseService.getUserBase(jm.getUserId());
				jobInfos.add(new SimpleJobInfo(jm, userBase));
			}catch(Exception e){
				
			}
		}
		return jobInfos;
		
	}
	
	
	private List<JobDescriptionModel> getDescriptions(String descriptionIds){
		
		if(StringUtils.isEmpty(descriptionIds)){
			return new ArrayList<JobDescriptionModel>();
		}
		
		String[] desces = descriptionIds.split(",");
		
		if(desces == null || desces.length <1){
			return new ArrayList<JobDescriptionModel>();
		}
		
		List<JobDescriptionModel> ret = new ArrayList<JobDescriptionModel>();
		
		for(String d : desces){
			try{
				if(StringUtils.isEmpty(d))
					continue;
				JobDescriptionModel m = jobDescriptionDAO.selectDescriptionById(Integer.valueOf(d));
				if(m != null){
					ret.add(m);
				}
			}catch(Exception e){
				LoggerInformation.LoggerErr("get description error", e);
			}
		}
		return ret;
		
		
	}

	@Override
	public MCPInteger pubJobWithDesc(int _userId, String positionName,
			String jobType, String corpName, int corpId, int salaryStart,
			int salaryEnd, int workExperience, int educationLevel,
			String descriptions, String poiUids, long startDate,
			long endDate, String oldJid, int industry, int hunter,
			long createDate) {
		
		String ids = "";
		
		if(!StringUtils.isEmpty(descriptions)){
			String[] deses = descriptions.split("-djgouda-");
			
			if(deses != null){
				for(String str : deses){
					if(StringUtils.isEmpty(str))
						continue;
					MCPInteger mcpret = pubJobDescription(_userId,str);
					ids += mcpret.getRet();
					ids += ",";
				}
			}
		}
		
		if(!StringUtils.isEmpty(ids)){
			if(ids.charAt(ids.length() -1) == ','){
				ids = ids.substring(0, ids.length()-1);
			}
		}
		
		return pubJob(_userId, positionName, jobType, corpName, corpId, salaryStart, salaryEnd, workExperience, educationLevel, ids, poiUids, startDate, endDate, oldJid, industry, hunter, createDate);
		
	}
}
