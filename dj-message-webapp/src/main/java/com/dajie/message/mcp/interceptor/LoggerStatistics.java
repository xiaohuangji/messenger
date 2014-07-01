package com.dajie.message.mcp.interceptor;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import org.apache.log4j.Logger;

import com.dajie.message.constants.returncode.CommonResultCode;
import com.dajie.message.model.wrapper.LoginReturn;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;


public class LoggerStatistics {


	private static final Logger logger = Logger.getLogger(LoggerStatistics.class);
	private static final Set<String> WHITE_LIST = ImmutableSet.of(
				"updateUserDeviceInfo".toLowerCase(),
				"registerByDJ".toLowerCase(),
				"register".toLowerCase(),
				"registerBy3rdPlatform".toLowerCase(),
				"pubJob".toLowerCase(),
				"changeJob".toLowerCase(),
				"searchJob".toLowerCase(),
				"getJobDetail".toLowerCase(),
				"searchPerson".toLowerCase(),
				"get".toLowerCase(),
				"searchJobOnMap".toLowerCase(),
				"searchPersonOnMap".toLowerCase(),
				"getPointsOnMap".toLowerCase(),
				"acceptOrIgnore".toLowerCase(),
				"verifyCorpEmail".toLowerCase(),
				"addLabel".toLowerCase(),
				"modifyCareer".toLowerCase(),
				"modifyEducation".toLowerCase(),
				"likeLabel".toLowerCase(),
				"modifyCareer".toLowerCase(),
				"updateUserDeviceInfo".toLowerCase(),
				"share".toLowerCase(),
				"startToChat".toLowerCase(),
				"sendFriendRequest".toLowerCase()
				
			);
	
	private static final String SEPARATOR = "||";
	
	public static void loggerInfo(String requestMethod,String appType,String deviceNumber,String userId,String version,String identity,Object ret,String market,String from,Object ...objects){
		
		if(ret == null)
			return;
	
		if(!WHITE_LIST.contains(requestMethod.toLowerCase())){
			return;
		}
		
		if(requestMethod.toLowerCase().equals("registerByDJ".toLowerCase())
				|| requestMethod.toLowerCase().equals("register".toLowerCase()) 
				||requestMethod.toLowerCase().equals("registerBy3rdPlatform".toLowerCase())
				|| requestMethod.toLowerCase().equals("login".toLowerCase())){
			LoginReturn loginReturn = (LoginReturn) ret;
			userId = String.valueOf(loginReturn.getUserId());
			if(loginReturn.getCode()!=CommonResultCode.OP_SUCC||loginReturn.getUserId() == 0)
				return;
			objects = null;
		}
		
		String statistics = Joiner.on(SEPARATOR).useForNull("null").join(requestMethod,
				appType,
				deviceNumber,
				userId,
				version,
				identity,
				new Date().getTime(),
				market,
				Arrays.toString(objects),
				from
				);

		logger.info(statistics);
		
	}
	
	
	
}
