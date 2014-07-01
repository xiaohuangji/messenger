package com.dajie.message.service.worker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dajie.core.hunter.service.HunterCidService;
import com.dajie.core.job.enums.JobRecruitTypeEnum;
import com.dajie.core.job.enums.JobTypeEnum;
import com.dajie.core.job.model.JobTag;
import com.dajie.core.job.model.JobTags;
import com.dajie.core.job.model.POI;
import com.dajie.core.profile.service.ProfileService;
import com.dajie.corp.api.service.CorpService;
import com.dajie.job.model.SimpleJob;
import com.dajie.job.model.SimpleJobQualify;
import com.dajie.job.service.SimpleJobService;
import com.dajie.message.dao.JobDAO;
import com.dajie.message.dao.JobThirdPartDAO;
import com.dajie.message.elasticsearch.map.utils.DictionaryUtils;
import com.dajie.message.elasticsearch.map.utils.ModelConvertorUtil;
import com.dajie.message.enums.JobStatus;
import com.dajie.message.model.MCPInteger;
import com.dajie.message.model.job.JobModel;
import com.dajie.message.model.job.JobQualifyModel;
import com.dajie.message.model.job.JobThirdPartModel;
import com.dajie.message.model.map.place.PlaceObject;
import com.dajie.message.service.IAccountService;
import com.dajie.message.service.IJobService;
import com.dajie.message.service.IMapSupportService;
import com.dajie.message.util.log.JsonMapping;
import com.dajie.message.util.log.LoggerInformation;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component("jobTranslate")
public class JobTranslate {

	@Autowired
	private SimpleJobService simpleJobService;
	
	@Autowired
	private JobThirdPartDAO thirdPartDao;
	
	@Autowired
	private CorpService corpService;

	@Autowired
	private IJobService jobService;
	
	@Autowired
	private IAccountService accountService;
	
	@Autowired
	private JobThirdPartDAO jobThirdPartDAO;
	
	@Autowired
	private JobDAO jobDao;
	
	@Autowired
	private ProfileService profileService;

	@Autowired
	private HunterCidService hunterCidService;


	@Autowired
	private IMapSupportService mapSupportService;

	private static final Logger logger = Logger.getLogger(JobTranslate.class);
	
	public void translateJobWithDB(JobThirdPartModel job,JobQualifyModel qualify) throws Exception{
		String poi = job.getPoi();
		
		if(job.getEnd_date() != null){
			if(job.getEnd_date().getTime() < System.currentTimeMillis()){
				return;
			}
		}
		
		try{
			
			if(job.getRecruit_type() != JobRecruitTypeEnum.SOCIAL_EXCLUDE_FRESH.value()&&
					job.getRecruit_type() != JobRecruitTypeEnum.SOCIAL_INCLUDE_FRESH.value() && 
					job.getRecruit_type() != JobRecruitTypeEnum.DEFAULT.value()){
				return;
			}
			
			if(job.getIs_intern() != null){
				if(job.getIs_intern() == 1)//实习的不要
					return;
			}
		}catch(Exception e){
			LoggerInformation.LoggerErr(logger, "job traslate kind change to int error ", e,job);
		}
		
		if(job.getType() == JobTypeEnum.CRAWL_JOB.value()){
			translateCrawlJobDB(job,qualify);
		}else if(job.getType() == JobTypeEnum.C2C_JOB.value()){
			if(StringUtils.isEmpty(poi)){
				translateCrawlJobDB(job,qualify);
			}else{
				translateC2CJobDB(job,qualify);
			}
		}
		
	}
	
	private void translateCrawlJobDB(JobThirdPartModel job,JobQualifyModel qualify){
		List<String> oldJobs = thirdPartDao.selectJid(job.getJid());
		if(oldJobs != null && oldJobs.size() > 0){
			return;
		}
		if(qualify != null){
			if(!StringUtils.isEmpty(qualify.getDegree())){
				job.setDegree(Integer.valueOf(qualify.getDegree()));
			}
			else
				job.setDegree(0);
			if(qualify.getExperience() != null)
				job.setExperience(ModelConvertorUtil.exchangeMainSiteExperience(qualify.getExperience()));
		}
		
		if(StringUtils.isEmpty(job.getCorp_name())){
			return;
		}
		jobThirdPartDAO.insertJob(job);
	}
	
	private void translateC2CJobDB(JobThirdPartModel job,JobQualifyModel qualify) throws Exception{
		List<String> oldJobs = jobDao.selectOldJid(job.getJid());
		if(oldJobs != null && oldJobs.size() > 0){
			return;
		}
		
		//大街创建
		JobModel jobModel = new JobModel();
		int corpId = corpService.getCorpIdByCid(job.getCid());
		jobModel.setCorpId(corpId);
		jobModel.setCorpName(job.getCorp_name());
		jobModel.setCreateDate(job.getCreate_date());
		//boolean isHunterJob = hunterCidService.getHunterCidByCid(job.getCid()) != null;
		jobModel.setHunter(checkHunter(job.getCid()));
		int userId = accountService.importDJUser(job.getUid());
		if(userId < 0){
			throw new Exception("import user from dajie error , the import interface return -1");
		}
		jobModel.setUserId(userId);
		
		/**
		 * propare for description
		 */
		
		
		
		StringBuffer sb = new StringBuffer();
		String feature = job.getFeature();
		
		if(!StringUtils.isEmpty(feature)){
			String[] arr = feature.split(",");
			if(arr != null){
				for(String a : arr){
					if(!StringUtils.isEmpty(a)){
						MCPInteger mi = jobService.pubJobDescription(jobModel.getUserId(),a);
						sb.append(String.valueOf(mi.getRet()));
						sb.append(",");
					}
				}
			}
		}
		
		if(job.getHead_count() > 0){
			MCPInteger mi = jobService.pubJobDescription(jobModel.getUserId(), "招聘人数："+String.valueOf(job.getHead_count()));
			sb.append(String.valueOf(mi.getRet()));
			sb.append(",");
		}

	
		String intro = job.getIntro();
		if(!StringUtils.isEmpty(intro)){
			MCPInteger mi = jobService.pubJobDescription(jobModel.getUserId(), intro);
			sb.append(String.valueOf(mi.getRet()));
		}else{
			if(sb.length() > 0){
				if(sb.charAt(sb.length() - 1) == ','){
					sb.deleteCharAt(sb.length()-1);
				}
			}
		}
	
		
		String desc = sb.toString();
		if(!StringUtils.isEmpty(desc))
			jobModel.setDescriptionIds(desc);
		
		
		if(qualify != null){
			if(!StringUtils.isEmpty(qualify.getDegree()))
				jobModel.setEducationLevel(Integer.valueOf(qualify.getDegree()));//教育经历和工作经验没有
			else
				jobModel.setEducationLevel(0);
		}
		jobModel.setEndDate(job.getEnd_date());
		
		if(!StringUtils.isEmpty(job.getIndustry()))
		{
			String[] industries = job.getIndustry().split(",");
			if(industries.length>0){
				if(!StringUtils.isEmpty(industries[0])){
					jobModel.setIndustry(Integer.valueOf(industries[0]));
				}
			}
		}
		
		jobModel.setJobType(job.getPosition_function());
		jobModel.setPositionName(job.getName());
		
		jobModel.setSalaryEnd(DictionaryUtils.exchangeSalaryCharToSalaryPrise(job.getSalary_end()));//salaryEnd和salaryStart的枚举含义
		jobModel.setSalaryStart(DictionaryUtils.exchangeSalaryCharToSalaryPrise(job.getSalary()));//
		jobModel.setStartDate(job.getStart_date());
		jobModel.setStatus(JobStatus.JOB_OK.ordinal());
		jobModel.setUpdateDate(new Date());
		
/*		boolean isHunterJob = hunterCidService.getHunterCidByCid(job.getCid()) != null;
		jobModel.setHunter(isHunterJob?1:0);
*/
		
		String poi = job.getPoi();
		if(!StringUtils.isEmpty(poi)){
			try{
				POI p = JsonMapping.getMapper().readValue(poi, POI.class);
				if(p!= null){
					if(!StringUtils.isEmpty(p.getUid())){
						jobModel.setPoiUids(p.getUid());
					}else if(!StringUtils.isEmpty(p.getName())){
						
						String cities = DictionaryUtils.exchageCityIdsToCityNames(job.getCity());
						String[] citiesArray = cities.split(",");
						List<PlaceObject> places = new ArrayList<PlaceObject>();
						if(citiesArray != null && citiesArray.length > 0)
							places = mapSupportService.getPOIListByName(p.getName(), citiesArray[0], 0, 1);
						else
							places = mapSupportService.getPOIListByName(p.getName(),"全国", 0, 1);
						if(places == null || places.size() < 1){
							translateCrawlJobDB(job,qualify);
							return;
						}else{
							if(!StringUtils.isEmpty(places.get(0).getUid())){
								jobModel.setPoiUids(places.get(0).getUid());
							}else{
								translateCrawlJobDB(job,qualify);
								return;
							}
								
						}
						
					}else{
						translateCrawlJobDB(job,qualify);
						return;
					}
				}else{
					translateCrawlJobDB(job,qualify);
					//System.out.println("c2c job to crawlJob"+job.getJid());
					return;
				}
			}catch(Exception e){
				LoggerInformation.LoggerErr(logger, "poi change error", e);
				translateCrawlJobDB(job,qualify);
				//System.out.println("c2c job to crawlJob"+job.getJid());
				return;
			}
			
		}
		
		if(StringUtils.isEmpty(jobModel.getPoiUids())){
			translateCrawlJobDB(job,qualify);
			return;
		}
		if(qualify != null){
			if(qualify.getExperience() != null)
				jobModel.setWorkExperience(ModelConvertorUtil.exchangeMainSiteExperience(qualify.getExperience()));
		}
		
		if(job.getCreate_date() != null){
			jobModel.setCreateDate(job.getCreate_date());
		}else{
			jobModel.setCreateDate(new Date());
		}
		
		jobService.pubJob(jobModel.getUserId(), jobModel.getPositionName(), jobModel.getJobType(), jobModel.getCorpName(), jobModel.getCorpId(),
				jobModel.getSalaryStart(), jobModel.getSalaryEnd(), jobModel.getWorkExperience(), jobModel.getEducationLevel(), jobModel.getDescriptionIds(), jobModel.getPoiUids(), 
				jobModel.getStartDate() == null ? System.currentTimeMillis():jobModel.getStartDate().getTime(),
						jobModel.getEndDate() == null ? System.currentTimeMillis():jobModel.getEndDate().getTime(),job.getJid(),-1,0,jobModel.getCreateDate().getTime());

	}
	
	public void translateJob(String jid) throws Exception{
		
		
		
		SimpleJob job = simpleJobService.getSimpleJob(jid);
		try{
			if(job.getRecruitType() != JobRecruitTypeEnum.SOCIAL_EXCLUDE_FRESH.value()&&
					job.getRecruitType() != JobRecruitTypeEnum.SOCIAL_INCLUDE_FRESH.value()){
				return;
			}
			if(job.getIsIntern() != null){
				if(job.getIsIntern() == 1)//实习的不要
					return;
			}
		}catch(Exception e){
			
		}


		SimpleJobQualify qualify = simpleJobService.getSimpleJobQualifyByJid(job.getJid());
		String intro = simpleJobService.getJobIntroByJid(jid);
		POI poi = job.getPoi();
		
		if(job.getType() == JobTypeEnum.CRAWL_JOB.value()){//抓取
			crawlJob(job, qualify, intro);
			
		}else if(job.getType() == JobTypeEnum.C2C_JOB.value()){
			if(poi == null){
				crawlJob(job, qualify, intro);
			}else{
				c2cJob(job,qualify,intro);
			}
			
		}
	}
	
	private void crawlJob(SimpleJob job,SimpleJobQualify qualify,String intro){
		
		List<String> oldJobs = thirdPartDao.selectJid(job.getJid());
		if(oldJobs != null && oldJobs.size() > 0){
			return;
		}

		
		JobThirdPartModel model = new JobThirdPartModel();
		model.setAddress(job.getAddress());
		model.setCid(job.getCid());
		model.setCity(job.getCity());
		model.setCorp_name(job.getCorpName());
		model.setCorp_quality(job.getCorpQuality());
		model.setCorp_rank(job.getCorpRank());
		model.setCorp_scale(job.getCorpScale());
		model.setCorpad(job.getCorpad());
		model.setCreate_date(job.getCreateDate());
		model.setCv_eng(job.getCvEng());
		
		if(qualify != null){
			if(!StringUtils.isEmpty(qualify.getDegree()))
				model.setDegree(Integer.valueOf(qualify.getDegree()));
			else
				model.setDegree(0);
			model.setExperience(ModelConvertorUtil.exchangeMainSiteExperience(qualify.getExperience()));
		}
		
		model.setDepartment(job.getDepartment());
		model.setDisplay_type(job.getDisplayType());
		model.setEmail(job.getEmail());
		model.setEnd_date(job.getEndDate());
		
		JobTags tags = job.getFeature();
		if(tags != null){
			List<JobTag> jobTags = tags.getTags();
			StringBuffer sb = new StringBuffer();				
			for(JobTag jt : jobTags){
				if(StringUtils.isEmpty(jt.getName()))
						continue;
				sb.append(jt.getName());
				sb.append(",");
			}
			
			if(sb.length() > 1)
				sb.deleteCharAt(sb.length() -1);
			model.setFeature(sb.toString());
		}
		
		model.setHead_count(job.getHeadCount());
		model.setIndustry(job.getIndustry());
		model.setInternship_days(job.getInternshipDays());
		model.setInternship_period(job.getInternshipPeriod());
		model.setIntro(intro);
		model.setIs_intern(job.getIsIntern());
		model.setJid(job.getJid());
		model.setKind(job.getKind());
		model.setName(job.getName());
		if(model.getPoi() != null){
			try {
				model.setPoi(JsonMapping.getMapper().writeValueAsString(job.getPoi()));
			} catch (JsonProcessingException e) {
			}
		}
		model.setPosition_exper(ModelConvertorUtil.exchangeMainSiteExperience(job.getPositionExperience()));
		model.setPosition_function(job.getPositionFunction());
		model.setPosition_industry(job.getPositionIndustry());
		model.setPost_status(job.getPostStatus());
		model.setProject_tag(job.getProjectTag());
		model.setRecruit_type(job.getRecruitType());
		model.setSalary(job.getSalary());
		model.setSalary_end(job.getSalaryEnd());
		model.setSeq(job.getSeq());
		model.setStart_date(job.getStartDate());
		model.setStatus(job.getStatus());
		model.setTitle(job.getTitle());
		model.setUid(job.getUid());
		model.setType(job.getType());
		model.setUpdate_date(job.getUpdateDate());
		model.setValidity(job.getValidity());
		model.setWelfare(job.getWelfare());
		
		jobThirdPartDAO.insertJob(model);
	}
	
	private void c2cJob(SimpleJob job,SimpleJobQualify qualify,String intro) throws Exception{
		List<String> oldJobs = jobDao.selectOldJid(job.getJid());
		if(oldJobs != null && oldJobs.size() > 0){
			return;
		}

		
		//大街创建
		JobModel jobModel = new JobModel();
		int corpId = corpService.getCorpIdByCid(job.getCid());
		jobModel.setCorpId(corpId);
		jobModel.setCorpName(job.getCorpName());
		jobModel.setCreateDate(job.getCreateDate());
		jobModel.setHunter(checkHunter(job.getCid()));
		int userId = accountService.importDJUser(job.getUid());
		if(userId < 0){
			throw new Exception("import user from dajie error , the import interface return -1");
		}
		jobModel.setUserId(userId);
		
		/**
		 * propare for description
		 */
		StringBuffer sb = new StringBuffer();
		JobTags tags = job.getFeature();
		
		if(tags != null){
			List<JobTag> jobTags = tags.getTags();
			if(jobTags != null){
				for(JobTag jt : jobTags){
					if(StringUtils.isEmpty(jt.getName()))
							continue;
					MCPInteger mi = jobService.pubJobDescription(jobModel.getUserId(), jt.getName());
					sb.append(String.valueOf(mi.getRet()));
					sb.append(",");
				}
			}
		}
		
		if(job.getHeadCount() > 0){
			MCPInteger mi = jobService.pubJobDescription(jobModel.getUserId(), "招聘人数："+String.valueOf(job.getHeadCount()));
			sb.append(String.valueOf(mi.getRet()));
			sb.append(",");
		}
	
		if(!StringUtils.isEmpty(intro)){
			MCPInteger mi = jobService.pubJobDescription(jobModel.getUserId(), intro);
			sb.append(String.valueOf(mi.getRet()));
		}else{
			if(sb.length() > 0){
				if(sb.charAt(sb.length() - 1) == ','){
					sb.deleteCharAt(sb.length()-1);
				}
			}

		}
		
		String desc = sb.toString();
		if(!StringUtils.isEmpty(desc))
			jobModel.setDescriptionIds(desc);
		
		
		if(qualify != null){
			if(!StringUtils.isEmpty(qualify.getDegree()))
				jobModel.setEducationLevel(Integer.valueOf(qualify.getDegree()));//教育经历和工作经验没有
			else
				jobModel.setEducationLevel(0);
		}
		jobModel.setEndDate(job.getEndDate());
		
		if(!StringUtils.isEmpty(job.getIndustry()))
		{
			String[] industries = job.getIndustry().split(",");
			if(industries.length>0){
				if(!StringUtils.isEmpty(industries[0])){
					try{
						jobModel.setIndustry(Integer.valueOf(industries[0]));
					}catch(Exception e){
						LoggerInformation.LoggerErr(logger,"change industry to integer error ",e);
						return;
					}
				}
			}
		}
		
		jobModel.setJobType(job.getPositionFunction());
		jobModel.setPositionName(job.getName());
		
		jobModel.setSalaryEnd(DictionaryUtils.exchangeSalaryCharToSalaryPrise(job.getSalaryEnd()));//salaryEnd和salaryStart的枚举含义
		jobModel.setSalaryStart(DictionaryUtils.exchangeSalaryCharToSalaryPrise(job.getSalary()));//
		jobModel.setStartDate(job.getStartDate());
		jobModel.setStatus(JobStatus.JOB_OK.ordinal());
		jobModel.setUpdateDate(new Date());
		
		
		if(job.getPoi() != null){
			if(!StringUtils.isEmpty(job.getPoi().getUid())){
				jobModel.setPoiUids(job.getPoi().getUid());
			}else{
				if(!StringUtils.isEmpty(job.getPoi().getName())){
					
					String cities = DictionaryUtils.exchageCityIdsToCityNames(job.getCity());
					String[] citiesArray = cities.split(",");
					List<PlaceObject> places = new ArrayList<PlaceObject>();
					
					if(citiesArray != null && citiesArray.length > 0){
						places = mapSupportService.getPOIListByName(job.getPoi().getName(), citiesArray[0], 0, 1);
					}
					else{
						places = mapSupportService.getPOIListByName(job.getPoi().getName(),"全国", 0, 1);
					}
					
					
					if(places == null || places.size() < 1){
						crawlJob(job, qualify, intro);
						return;
					}else{
						if(!StringUtils.isEmpty(places.get(0).getUid())){
							jobModel.setPoiUids(places.get(0).getUid());
						}
						else{
							
							crawlJob(job, qualify, intro);
							return;
						}
							
					}
					
				}else{
					crawlJob(job, qualify, intro);
					return;
				}
			}
		}
		if(qualify != null)
			jobModel.setWorkExperience(ModelConvertorUtil.exchangeMainSiteExperience(qualify.getExperience()));
		
		if(StringUtils.isEmpty(jobModel.getPoiUids())){
			crawlJob(job, qualify, intro);
			return;
		}
		
		if(job.getCreateDate() != null){
			jobModel.setCreateDate(job.getCreateDate());
		}else{
			jobModel.setCreateDate(new Date());
		}
		
		jobService.pubJob(jobModel.getUserId(), jobModel.getPositionName(), jobModel.getJobType(), jobModel.getCorpName(), jobModel.getCorpId(),
				jobModel.getSalaryStart(), jobModel.getSalaryEnd(), jobModel.getWorkExperience(), jobModel.getEducationLevel(), jobModel.getDescriptionIds(), jobModel.getPoiUids(), 
				jobModel.getStartDate() == null ? System.currentTimeMillis():jobModel.getStartDate().getTime(),
						jobModel.getEndDate() == null ? System.currentTimeMillis():jobModel.getEndDate().getTime(),job.getJid(),-1,0,jobModel.getCreateDate().getTime());
	}
	
	private int checkHunter(String cid){
		
		
		boolean isHunterJob = hunterCidService.getHunterCidByCid(cid) != null;
		
		return isHunterJob ? 1 : 0;

	}
	
	public static void main(String[] args) {
		/*String cities = DictionaryUtils.exchageCityIdsToCityNames("110000");
		String[] citiesArray = cities.split(",");
		List<PlaceObject> places = new ArrayList<PlaceObject>();
		System.out.println(citiesArray);*/
	}
}
