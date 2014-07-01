package com.dajie.message.elasticsearch.map.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerManager {
	
	private static final  Logger loggerForService = LoggerFactory.getLogger("logger_for_service");
	
	private static final  Logger loggerForUtils = LoggerFactory.getLogger("logger_for_utils");
	
	private static final  Logger loggerForNoPrefix = LoggerFactory.getLogger("logger_for_no_prefix");

	private static final Logger loggerForDefault = LoggerFactory.getLogger(LoggerManager.class);
	
	public static Logger getNoPeifixLogger()
	{
		return loggerForNoPrefix;
	}
	
	public static Logger getLogger(Class<?> clazz)
	{
		if(clazz.getName().endsWith("Impl"))
		{
			return loggerForService;
		}
		else if(clazz.getName().endsWith("Utils")||clazz.getName().endsWith("Util"))
		{
			return loggerForUtils;
		}
		else
			return loggerForDefault;
	}
	
	
	
}
