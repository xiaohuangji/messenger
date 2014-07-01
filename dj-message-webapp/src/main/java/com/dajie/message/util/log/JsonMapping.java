package com.dajie.message.util.log;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapping {

	private static ObjectMapper mapper = new ObjectMapper();
	
	public static ObjectMapper getMapper(){
		return mapper;
	}
	
	
}
