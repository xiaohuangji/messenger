package com.dajie.message.controller;

import org.apache.log4j.Logger;

public class WebStatisticLogger {
	private static final Logger logger = Logger.getLogger(WebStatisticLogger.class);
	
	public static void logPV(String info){
		logger.info("pv statistic : " +info);
	}
	
	public static void log(String info){
		logger.info(info);
	}
}
