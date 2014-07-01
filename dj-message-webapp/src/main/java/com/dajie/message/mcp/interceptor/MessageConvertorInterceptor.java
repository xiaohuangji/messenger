package com.dajie.message.mcp.interceptor;

import com.dajie.message.util.log.JsonMapping;
import com.dajie.message.util.log.LoggerInformation;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.dajie.message.mcp.model.DispatchInterceptorConstant.*;
/**
 * 解析消息体，如果http-content的内容是application/json，将http body 内的消息
 * 转换为request attribute中的属性。支持更多的转换类型，可以在下面添加。当前只支持
 * http get、http post（application/json和application/x-www-form-urlencoded)
 * @author xinquan.guan
 *
 */
public class MessageConvertorInterceptor  extends HandlerInterceptorAdapter{

	private static final Logger logger = Logger.getLogger(MessageConvertorInterceptor.class);
	
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
//		if (DefaultHandler.class.isAssignableFrom(handler.getClass())) {
        ResponseWriter.writeResponseHeader(response);

        String contentType = request.getContentType();
        if (StringUtils.isEmpty(contentType)) {//仅有get请求可以缺少content-type
            if(request.getMethod().equals("GET"))//request type is get use application/x-www-form-urlencoded
            {
                contentType = FORM_CONTENT_TYPE;
            }else{//缺少content type，如果是个post请求，将被拒绝
                ResponseWriter.writeErrResponse(response, ACK_ERR_CONTENT_TYPE, ACK_ERR_CONTENT_TYPE_MSG);
                return false;
            }
        }

        contentType = contentType.toLowerCase().trim();

        if(contentType.length() < JSON_CONTENT_TYPE.length()){//JSON CONTENT length是最短的，不满足，这不支持这个请求
            ResponseWriter.writeErrResponse(response, ACK_ERR_CONTENT_TYPE, ACK_ERR_CONTENT_TYPE_MSG);
            return false;
        }

        if (JSON_CONTENT_TYPE.equals(contentType.substring(0,DEFAULT_CONTENT_TYPE.length()))){
            if(!jsonContentParser(request,response))
                return false;
        }else if(FORM_CONTENT_TYPE.equals(contentType.substring(0,FORM_CONTENT_TYPE.length()))){
        	 //nop application/x-www-form-urlencoded
        	if(!formContentParser(request, response))
        		return false;
        }else{
        	
            //error no suport content 目前只支持json和form两种content类型
            ResponseWriter.writeErrResponse(response, ACK_ERR_CONTENT_TYPE, ACK_ERR_CONTENT_TYPE_MSG);
            return false;
        }
        return super.preHandle(request, response, handler);
//		}
//		return true;
	}
	
	public boolean formContentParser(HttpServletRequest request,HttpServletResponse response){
		
		@SuppressWarnings("unchecked")
		Enumeration<String> iter = request.getParameterNames();
		Map<String,Object> maps = new HashMap<String, Object>();
		while(iter.hasMoreElements()){
			String i = iter.nextElement();
			maps.put(i, request.getParameter(i));
		}
		
		InterceptorUtil.setRequestParam(request, maps);
		return true;
	}
	
	/**
	 * 如果content的内容是application/json，将http消息体内的消息转换到request的attribute中
	 * @param request
	 * @param response
	 * @return true 解析正常返回，false 解析出现异常
	 */
	private boolean jsonContentParser(HttpServletRequest request,
			HttpServletResponse response){
		
		ObjectMapper mapper = JsonMapping.getMapper();
		InputStream inputStream = null;
		try {
			inputStream = request.getInputStream();
			@SuppressWarnings("unchecked")
			Map<String, String> paraMapper = mapper.readValue(inputStream,
					Map.class);
			
			InterceptorUtil.setRequestParam(request, paraMapper);
			return true;
		} catch (IOException e) {
			ResponseWriter.writeErrResponse(response, ACK_ERR_SERVICE_PARSE_JSON, ACK_ERR_SERVICE_PARSE_JSON_MSG);
			LoggerInformation.LoggerErr(logger,ACK_ERR_SERVICE_PARSE_JSON_MSG, e);
			return false;
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				LoggerInformation.LoggerErr(logger,"close request input stream error ",e);
			}
		}

	}

}
