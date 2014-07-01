package com.dajie.message.mcp.interceptor;

import com.dajie.message.mcp.model.*;
import com.dajie.message.util.log.LoggerInformation;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dajie.message.mcp.model.DispatchInterceptorConstant.*;

public class InterceptorUtil {

	private static final Logger logger = Logger.getLogger(InterceptorUtil.class);
	
	/**
	 * 根据paramName获取在request中的值（首先从parameter中获取，如果不存在，从attribute中获取
	 * 
	 * @param request
	 * @param paramName
	 * @return
	 */
	public static String getRequestParam(HttpServletRequest request,
			String paramName) {
		Object obj = request.getAttribute(DispatchInterceptorConstant.REQUEST_PARAM_NAME);
		if(obj != null){
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) obj;
			if(map.containsKey(paramName)){
				if(map.get(paramName) == null)
					return "";
				return String.valueOf(map.get(paramName));
			}
		}
		
		if(!StringUtils.isEmpty(request.getParameter(paramName))){
			if(request.getParameter(paramName) == null)
				return "";
			return String.valueOf(request.getParameter(paramName));
		}
		
		if(!StringUtils.isEmpty(String.valueOf(request.getAttribute(paramName)))){
			if(request.getAttribute(paramName) == null)
				return "";
			return String.valueOf(request.getAttribute(paramName));
		}
		return "";
	}
	
	@SuppressWarnings("unchecked")
	public static void setRequestParam(HttpServletRequest request,Map<String,?> maps){
		Object obj = request.getAttribute(DispatchInterceptorConstant.REQUEST_PARAM_NAME);
		Map<String,Object> map = null;
		if(obj == null){
			map = new HashMap<String, Object>();
			request.setAttribute(DispatchInterceptorConstant.REQUEST_PARAM_NAME, map);
		}else{
			map = (Map<String, Object>) obj;
		}
		
		for(Map.Entry<String, ?> m : maps.entrySet()){
			map.put(m.getKey(), m.getValue());
		}
		
	}
	
	public static void setRequestParam(HttpServletRequest request,String paramName,Object obj){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(paramName, obj);
		setRequestParam(request, map);
	}

	/**
	 * 从url中获取到类名字和方法名字
	 * @param path
	 * @return
	 */
	public static ClassMethodName getPathClassName(String path) {
		ClassMethodName cmn = new ClassMethodName();
		if (path == null || "".equals(path))
			return null;
        /**
         * /api/ISystemService/echo
         * 从/api/后面开始解析
         */
		if (path.startsWith("/api"))
			path = path.substring(5);
		int index = path.indexOf('/');
		if (index == -1) {
			cmn.setClassName(path);
		} else {
			cmn.setClassName(path.substring(0, index));
			path = path.substring(index + 1, path.length());
			int end = path.indexOf('/');
			if (end == -1)
				cmn.setMethodName(path);
			else
				cmn.setMethodName(path.substring(0, end));
		}
		return cmn;
	}
	
	/**
	 * 分析方法对应参数
	 * @param methodMeta
	 * @param request
	 * @return
	 */
	public static ParamParse getInvokeParam(MethodMeta methodMeta,
			HttpServletRequest request) {
		ParamParse parse = new ParamParse();
		List<ParamMeta> paramMetas = methodMeta.getParamMetas();// 方法名字
		Class<?>[] clazz = methodMeta.getMethod().getParameterTypes();// 方法类型
		for (int i = 0; i < paramMetas.size(); i++) {
			String value = getRequestParam(request, paramMetas.get(i)
					.getParamName());// 获取value
			if (StringUtils.isEmpty(value)) {// value的值是空
				if (paramMetas.get(i).isNullable()) {// 如果允许为空
					if(!StringUtils.isEmpty(paramMetas.get(i).getDefaultValue())){
						//设置了默认参数
						parse.getObjs().add(changeToBaseType(clazz[i],paramMetas.get(i).getDefaultValue()));
					}else{//没有设置默认参数
						parse.getObjs().add(getDefaultBaseTypeValue(clazz[i]));
					}
				} else {// 返回不能为空的提示
				
					parse.setError(new ErrorModel(ACK_ERR_REQUIRE_METHOD_PARAM,
							ACK_ERR_REQUIRE_METHOD_PARAM_MSG.replace(
									"{parameter}", request.getRequestURI()+"."+paramMetas.get(i)
											.getParamName())));
					return parse;
				}
			} else {//value的值不为null

				if (request.getMethod().equals("GET")) {// get的方法需要转码
					try {
						String clientCoding = request.getCharacterEncoding();
						if (StringUtils.isEmpty(clientCoding))
							value = new String(value.getBytes("ISO-8859-1"));
						else
							value = new String(value.getBytes("ISO-8859-1"),
									clientCoding);
					} catch (UnsupportedEncodingException e) {
						LoggerInformation.LoggerErr(logger,"change get value encoding error", e);
					}
				}

				Object obj = null;
				obj = changeToBaseType(clazz[i], value);//转换成基础类型
				if (obj == null) {
					parse.setError(new ErrorModel(ACK_ERR_SERVICE_PARAM_TYPE,
							ACK_ERR_SERVICE_PARAM_TYPE_MSG));
					return parse;
				}
				parse.getObjs().add(obj);
			}
		}
		return parse;

	}

	/**
	 * 将值转换成基础类型
	 * @param type
	 * @param value
	 * @return
	 */
	private static Object changeToBaseType(Class<?> type, String value) {
		if (value == null)
			return null;
		if (type == String.class) {
			return value;
		} else if (type == Integer.class || type == int.class) {
			return Integer.valueOf(value);
		} else if (type == Boolean.class || type == boolean.class) {
			return Boolean.valueOf(value);
		} else if (type == Long.class || type == long.class) {
			return Long.valueOf(value);
		} else if (type == Double.class || type == double.class) {
			return Double.valueOf(value);
		} else if (type == Float.class || type == float.class) {
			return Float.valueOf(value);
		} else if (type == Short.class || type == short.class) {
			return Short.valueOf(value);
		} else if (type == Byte.class || type == byte.class) {
			return Byte.valueOf(value);
		}
		return null;
	}
	
	/**
	 * 获取基础类型的默认值
	 * @param type
	 * @return
	 */
	private static Object getDefaultBaseTypeValue(Class<?> type){
		if (type == String.class) {
			return "";
		} else if (type == Integer.class || type == int.class) {
			return Integer.valueOf(0);
		} else if (type == Boolean.class || type == boolean.class) {
			return Boolean.FALSE;
		} else if (type == Long.class || type == long.class) {
			return Long.valueOf(0);
		} else if (type == Double.class || type == double.class) {
			return Double.valueOf(0);
		} else if (type == Float.class || type == float.class) {
			return Float.valueOf(0);
		} else if (type == Short.class || type == short.class) {
			return Short.valueOf("0");
		} else if (type == Byte.class || type == byte.class) {
			return Byte.valueOf("0");
		}
		return null;
	}

}
