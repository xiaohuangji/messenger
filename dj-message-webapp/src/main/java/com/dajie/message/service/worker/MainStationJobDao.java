package com.dajie.message.service.worker;

import java.util.List;
import java.util.Map;

import com.dajie.message.model.job.JobQualifyModel;
import com.dajie.message.model.job.JobThirdPartModel;


public interface MainStationJobDao {
	public List<String> listJobThirdPartModel(Map<String, Object> map);
	
	public List<JobThirdPartModel> listJobThirdPartModels(Map<String, Object> map);
	
	public JobQualifyModel selectSimpleJobQualify(String jid);
}
