package com.dajie.message.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dajie.common.bean.PageIterator;
import com.dajie.common.dictionary.DictEnum;
import com.dajie.common.dictionary.model.Dict;
import com.dajie.common.dictionary.model.DictItem;
import com.dajie.common.dictionary.service.DictService;
import com.dajie.common.util.StringUtil;
import com.dajie.corp.api.service.CorpService;
import com.dajie.corp.info.model.CorpBase;
import com.dajie.message.model.corp.SimpleCorpBase;
import com.dajie.message.service.IAuxService;
import com.dajie.sphsearch.service.JobSearchService;
import com.dajie.sphsearch.service.W3SearchService;

/**
 * Created by John on 4/28/14.
 */
@Component("auxService")
public class AuxServiceImpl implements IAuxService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AuxServiceImpl.class);
	
	@Autowired
	private DictService dictService;

	@Autowired
	private CorpService corpService;
	
	@Override
	public List<String> getSchoolNames(int _userId, String keyword) {
		Dict dict = dictService.getDict(DictEnum.valueOf("DICT_SCHOOL"));
		List<String> schoolNames = new ArrayList<String>();
        PageIterator<Integer> pi = W3SearchService.searchDict("school", keyword, 1, 6);
        if (pi != null) {
            List<Integer> list = pi.getData();
            for (Integer id : list) {
                DictItem item = dict.value(id);
                if (item != null) {
                	schoolNames.add(item.getName());
                }
            }
        }
        return schoolNames;
	}
	
	@Override
	public List<SimpleCorpBase> getCorpBaseList(int _userId, String keyword) {
		List<SimpleCorpBase> corps = new ArrayList<SimpleCorpBase>();
		final int RELATED_COMPANY_SIZE = 5;// 默认显示5个

		if (!StringUtil.isEmpty(keyword)) {
			PageIterator<Integer> corpIdIterator = JobSearchService
					.relatedCompany(keyword, RELATED_COMPANY_SIZE);

			if (corpIdIterator == null || corpIdIterator.getData() == null
					|| corpIdIterator.getData().size() == 0) {
				LOGGER.warn("JobSearchService.relatedCompany return empty!!!");
			}
			if (corpIdIterator != null && corpIdIterator.getData() != null) {
				if (!corpIdIterator.getData().isEmpty()) {
					Map<Integer, CorpBase> corpBaseMap = corpService
							.getCorpBaseSimpleWithCorpIds(corpIdIterator
									.getData());
					LOGGER.info(
							"corpService.getCorpBaseSimpleWithCorpIds(corpIdIterator.getData()) return: {}",
							corpBaseMap.keySet());
					final Map<Integer, Integer> employeeCountMap = corpService
							.getEmployeeCountsByCorpIds(new ArrayList<Integer>(
									corpBaseMap.keySet()));
					List<Map.Entry<Integer, CorpBase>> entries = new ArrayList<Map.Entry<Integer, CorpBase>>(
							corpBaseMap.entrySet());
					// 以公司人数倒叙排序
					Collections.sort(entries,
							new Comparator<Map.Entry<Integer, CorpBase>>() {
								@Override
								public int compare(
										Map.Entry<Integer, CorpBase> entryOne,
										Map.Entry<Integer, CorpBase> entryTwo) {
									Integer numOne = employeeCountMap
											.get(entryOne.getKey()) == null ? 0
											: employeeCountMap.get(entryOne
													.getKey());
									Integer numTwo = employeeCountMap
											.get(entryTwo.getKey()) == null ? 0
											: employeeCountMap.get(entryTwo
													.getKey());
									return (numTwo - numOne);
								}
							});
					LOGGER.info(
							"List<Map.Entry<Integer, CorpBase>> entries size: {}",
							entries.size());
					for (Map.Entry<Integer, CorpBase> corpBaseEntry : entries) {
						CorpBase corpBase = corpBaseEntry.getValue();
						SimpleCorpBase simpleCorpBase = new SimpleCorpBase();
						simpleCorpBase.setCorpId(corpBase.getId());
						simpleCorpBase.setCorpName(corpBase.getName());
						corps.add(simpleCorpBase);
					}
				}
			}
		}
		return corps;
	}

}
