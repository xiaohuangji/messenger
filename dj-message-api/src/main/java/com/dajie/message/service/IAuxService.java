package com.dajie.message.service;

import java.util.List;

import com.dajie.message.annotation.rest.RestBean;
import com.dajie.message.model.corp.SimpleCorpBase;

@RestBean
public interface IAuxService {

	/**
	 * 根据用户输入，联想学校名称
	 * 
	 * @param _userId
	 * @param keyword
	 * @return
	 */
	List<String> getSchoolNames(int _userId, String keyword);
	
	/**
	 * 根据用户输入，联想公司名称和id
	 * 
	 * @param _userId
	 * @param keyword
	 * @return
	 */
	List<SimpleCorpBase> getCorpBaseList(int _userId, String keyword);

}
