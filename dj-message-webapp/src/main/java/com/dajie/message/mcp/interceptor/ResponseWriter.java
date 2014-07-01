package com.dajie.message.mcp.interceptor;

import static com.dajie.message.mcp.model.DispatchInterceptorConstant.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;


import com.dajie.message.mcp.model.ErrorModel;
import com.dajie.message.util.log.JsonMapping;
import com.dajie.message.util.log.LoggerInformation;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseWriter {

	
	public static boolean writeToResponse(Object obj, HttpServletResponse response) {
		
		ObjectMapper mapper = JsonMapping.getMapper();
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			String ret = "";
			if(obj.getClass().isEnum()){
				@SuppressWarnings("rawtypes")
				Enum e = (Enum) obj;
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", e.name());
				map.put("code", String.valueOf(e.ordinal()));
				ret = mapper.writeValueAsString(map);
			}else{
				ret = mapper.writeValueAsString(obj);
			}
			//logger.info("response write : "+ret);
			byte[] bytes = ret.getBytes(Charset.forName("utf-8"));
			response.setContentLength(bytes.length);
			out.write(bytes);
			out.flush();
			return true;
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					//e.printStackTrace();
				}
			}
		}
	}
	
	public static void writeErrResponse(HttpServletResponse response, int code, String msg) {
		LoggerInformation.LoggerErr(msg);
		writeToResponse(new ErrorModel(code, msg), response);
	}

	public static void writeResponseHeader(HttpServletResponse response){
		response.setContentType(DEFAULT_CONTENT_TYPE);
		response.setCharacterEncoding(DEFAULT_CHAR_SET);
	}
	
}
