package com.dajie.message.service.worker;


import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dajie.framework.app.TaskHandler;
import com.dajie.job.queue.enums.JobOperationEnum;
import com.dajie.job.queue.model.JobOperationBean;
import com.dajie.message.dao.JobDAO;
import com.dajie.message.dao.JobThirdPartDAO;
import com.dajie.message.util.log.LoggerInformation;


@Component("jobReciever")
public class JobReciever implements TaskHandler<JobOperationBean>{
	
	@Autowired
	private JobTranslate translate;
	
	private static final Logger logger = Logger.getLogger(JobReciever.class);
		
	@Autowired
	private JobDAO jobDAO;
	
	@Autowired
	private JobThirdPartDAO jobThirdPartDAO;
	
	@Override
	public boolean handle(JobOperationBean task) {
		try{		
			if(task.getOperation() != JobOperationEnum.CREATE && task.getOperation() != JobOperationEnum.UPDATE)
				return true;
			
			if(JobOperationEnum.UPDATE == task.getOperation()){
				List<String> oldJids = jobDAO.selectOldJid(task.getJid());
				if(oldJids == null || oldJids.size() < 1){//表示这个职位还没有在job_table中
					translate.translateJob(task.getJid());//这个职位有可能被添加到job_table或者添加到了job_third_part中
					oldJids = jobDAO.selectOldJid(task.getJid());//如果这个职位被添加到了job_table中，那么需要将它从job_third_part中删除
					if(oldJids != null && oldJids.size() > 0){
						jobThirdPartDAO.deleteJobThirdPartInfo(task.getJid());
					}
					
				}else{
					return true;
				}
				return true;
			}
			
			String[] jids = task.getJid().split(",");
			for(String jid : jids){
				translate.translateJob(jid);
			}
		}catch(Exception e){
			LoggerInformation.LoggerErr(logger, "job worker error ", e);
			
			return true;
		}
		return true;
	}

}
