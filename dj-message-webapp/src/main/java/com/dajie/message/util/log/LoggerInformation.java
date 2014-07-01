package com.dajie.message.util.log;


import org.apache.log4j.Logger;

import com.dajie.message.util.type.TypeUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoggerInformation {

	private static final ObjectMapper mapper = JsonMapping.getMapper();
	private static final Logger logger = Logger.getLogger(LoggerInformation.class);
	
	public static void LoggerErr(String prompt){
		logger.error(new Exception(prompt));
	}
	
	public static void LoggerErr(String prompt,Exception e){
		logger.error(prompt,e);
	}
	
	public static void LoggerErr(Logger logger,String prompt,Exception e){
		logger.error(prompt, e);
	}
	
	public static void LoggerErr(Logger logger,String prompt,Exception e,Object ...objects){
		prompt = prompt + objectsToString(objects);
		logger.error(prompt,e);
	}
	
	public static void LoggerErrMessage(Logger logger,String prompt){
		logger.error(prompt);
	}
	
	public static void LoggerInfo(Logger logger,String prompt){
		if(logger.isInfoEnabled()){
			logger.info(prompt);
		}
	}
	
	public static void LoggerInfo(Logger logger,String prompt,Exception e){
		if(logger.isInfoEnabled()){
			logger.info(prompt,e);
		}
	}
	
	public static void LoggerInfo(Logger logger,String prompt,Object ...objects){
		prompt = prompt + objectsToString(objects);
		if(logger.isInfoEnabled()){
			logger.info(prompt);
		}
	}
	
	public static void LoggerInfo(Logger logger,String prompt,Exception e,Object ...objects){
		prompt = prompt + objectsToString(objects);
		if(logger.isInfoEnabled()){
			logger.info(prompt,e);
		}
	}
	
	private static String objectsToString(Object ...objects){
		StringBuilder builder = new StringBuilder();
		for(Object obj : objects){
			if(obj ==  null)
			{
				builder.append("null");
				builder.append("::");
				continue;
			}
			if(TypeUtils.isBaseType(obj.getClass())){
				builder.append(obj);
			}else{
				try {
					builder.append(mapper.writeValueAsString(obj));
				} catch (JsonProcessingException e1) {
//					e1.printStackTrace();
					return "";
				}
			}
			builder.append("::");
		}
		return builder.toString();
	}
}
