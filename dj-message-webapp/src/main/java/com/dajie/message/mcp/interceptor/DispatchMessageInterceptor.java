package com.dajie.message.mcp.interceptor;

import com.dajie.message.mcp.model.*;
import com.dajie.message.mcp.pool.InterfaceMethodPool;
import com.dajie.message.mcp.service.IPassportService;
import com.dajie.message.model.MCPInteger;
import com.dajie.message.util.log.LoggerInformation;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.perf4j.commonslog.CommonsLogStopWatch;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.dajie.message.mcp.model.DispatchInterceptorConstant.*;

/**
 * 分析request uri的信息得出调用的方法名，组织方法参数，完成方法调用，并返回json结果
 * 
 * @author xinquan.guan
 * 
 */
public class DispatchMessageInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = Logger.getLogger(DispatchMessageInterceptor.class);
	//private static final Logger stastisticLogger = Logger.getLogger("com.dajie.mobile.mcp.log.LoggerStatistics");
	//private static final Logger accessLogger = Logger.getLogger("com.dajie.mobile.mcp.interceptor.DispatchMessageInterceptor.access");
	
	private InterfaceMethodPool interfacePool;
	private IPassportService passportService;
	
	public static final int NO_TICKET = 1;
	
	public static final int TICKET_OUT_DATE = 2;
	
	public static final int TICKET_OK = 0;

	public static final String secretKey = "1qaz2wsx";
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

        ClassMethodName cmn = InterceptorUtil.getPathClassName(request
                .getRequestURI());
        if (cmn == null) {// no class name
            ResponseWriter.writeErrResponse(response, ACK_ERR_CLASS_NAME,
                    ACK_ERR_CLASS_NAME_MSG);
            return false;
        } else if (cmn.getMethodName() == null) {// no method name
            ResponseWriter.writeErrResponse(response, ACK_ERR_CLASS_METHOD,
                    ACK_ERR_CLASS_METHOD_MSG);
            return false;
        } else {// 获取class和method
            ClassHandler ch = interfacePool.getClassHandler(cmn
                    .getClassName());
            if (ch == null) {// error class name error
                ResponseWriter.writeErrResponse(response,
                        ACK_ERR_CLASS_NAME_NO_MAP,
                        ACK_ERR_CLASS_NAME_NO_MAP_MSG);
                return false;
            }
            MethodMeta methodMeta = ch.getMethodMeta(cmn.getMethodName());
            if (methodMeta == null) {// error method name error
                ResponseWriter.writeErrResponse(response,
                        ACK_ERR_CLASS_METHOD_NO_MAP,
                        ACK_ERR_CLASS_METHOD_NO_MAP_MSG);
                return false;
            }

            if (methodMeta.isTiket()) {//是否验证ticket
                int vret = verifyTicket(request);

                if (vret == NO_TICKET) {// 异常
                    ResponseWriter.writeErrResponse(response, ACK_ERR_TICKET, ACK_ERR_TICKET_MSG);
                    
                    LoggerInformation.LoggerErr(logger, "user no ticket invoke",new Exception(), request.getRequestURI(),getRequestString(request));
                    return false;
                }else if(vret == TICKET_OUT_DATE){
                    ResponseWriter.writeErrResponse(response, ACK_ERR_TICKET_OUT_DATE, ACK_ERR_TICKET_OUT_DATE_MSG);
                    LoggerInformation.LoggerErr(logger, "user out date invoke",new Exception(), request.getRequestURI(),getRequestString(request));
                    return false;
                }else if(vret == TICKET_OK){

                }
            }
            
            if(!checkSigString(request,methodMeta.isTiket())){//验证sig
            	ResponseWriter.writeErrResponse(response, ACK_ERR_SIG, ACK_ERR_SIG_MSG);
            	return false;
            }
            
            if(!checkFreq(request, cmn)){//验证访问频次
            	ResponseWriter.writeErrResponse(response, ACK_ERR_FREQ, ACK_ERR_FREQ_MSG);
            	return false;
            }
            
            ParamParse paramParse = InterceptorUtil.getInvokeParam(
                    methodMeta, request);

            if (paramParse.getError() != null) {
                ResponseWriter.writeErrResponse(response, paramParse
                        .getError().getCode(), paramParse.getError()
                        .getMsg());
                return false;
            }

            CommonsLogStopWatch watch = null;
            if(logger.isInfoEnabled()){
                watch = new CommonsLogStopWatch("perform4j");
            }

            Object ret = null;
            try{
                ret = invokeHandler(ch.getClassObject(),
                    methodMeta.getMethod(), paramParse);
            }catch(Exception e){
                ResponseWriter.writeErrResponse(response, ACK_ERR_SERVICE_INVOKE, ACK_ERR_SERVICE_INVOKE_MSG);
                return false;
            }
            if(watch != null){
                watch.lap(request.getRequestURI());
            }
            LoggerInvokeInfo(request,ch.getClassObject().getClass(),methodMeta.getMethod(),paramParse,ret);


            if (methodMeta.getMethod().getReturnType() == void.class
                    || methodMeta.getMethod().getReturnType() == Void.class) {
                return false;
            }

            if (ret == null) {// invoke method return null
                ResponseWriter.writeErrResponse(response,
                        ACK_ERR_RET_NULL, ACK_ERR_RET_NULL_MSG);
                return false;
            }

            MCPResponse mcp = new MCPResponse();

            if (ret.getClass() == Integer.class || ret.getClass() == int.class) {
                mcp.setCode((Integer)ret);
            }else if(ret.getClass() == MCPInteger.class){
            	MCPInteger r = (MCPInteger) ret;
            	if(r.getCode() != 0)
            		mcp.setCode(r.getCode());
            	else
            		mcp.setCode(ACK_OK);
                mcp.setRet(((MCPInteger)ret).getRet());
            }else{

                mcp.setCode(ACK_OK);
                mcp.setRet(ret);
            }

            ResponseWriter.writeToResponse(mcp, response);

        }
        return false;

	}

	private Object invokeHandler(Object classObject, Method method,
			ParamParse paramModel) throws Exception {
		try {
			if (paramModel.getObjs() == null || paramModel.getObjs().size() < 1)// 没有参数
			{
				return method.invoke(classObject);
			} else {
				return method.invoke(classObject, paramModel.getObjs()
						.toArray());// 有参数
			}
		} catch (Exception e) {
			LoggerInformation.LoggerErr(logger, "method invoke exception", e,classObject.getClass().getSigners(),method.getName(),paramModel);
			throw e;
		}
		//return null;
	}


	/**
	 * 验证过ticket的用户，如果参数中需要，那么参数的名字必须叫_userId
	 * 
	 * @param request
	 * @return
	 */
	
	private int verifyTicket(HttpServletRequest request) {
		String t = InterceptorUtil.getRequestParam(request, "_t");
		String type = InterceptorUtil.getRequestParam(request, "appType");

		if (type.equals(AppType.test.name())) {
			return TICKET_OK;//成功
		} else {
			if (StringUtils.isEmpty(t)) {
				LoggerInformation.LoggerErr(logger, "user no ticket invoke",new Exception(), request.getRequestURI(),getRequestString(request));
				return NO_TICKET;//没传入t票
			}else{//这里需要添加验证用户token的信息
				UserPassport passport = passportService.getPassportByTicket(t);
				
				
					
				if(passport == null || passport.getUserId() == 0 || StringUtils.isEmpty(passport.getTicket()) ||
						!passport.getTicket().equals(t)){
					
					if(passport == null){
						LoggerInformation.LoggerErr(logger, "user ticket in cache is empty, ticket is out off dates "+t,new Exception(), request.getRequestURI(),getRequestString(request));
					}else if(!passport.getTicket().equals(t)){
						LoggerInformation.LoggerErr(logger, "user ticket not maped ,user ticket relogin by others "+passport.getTicket(),new Exception(), request.getRequestURI(),getRequestString(request));
					}
					
					return TICKET_OUT_DATE;//t票过期
				}else{
					request.setAttribute("_userId", passport.getUserId());
					return TICKET_OK;//成功
				}
			}
		}
	}
	
	public boolean checkSigString(HttpServletRequest request,boolean isTicket){
		String sig = InterceptorUtil.getRequestParam(request, "_sig");
		String v = InterceptorUtil.getRequestParam(request, "_v");
		String type = InterceptorUtil.getRequestParam(request, "appType");
		String t = InterceptorUtil.getRequestParam(request, "_t");
		
		if(StringUtils.isEmpty(type)){
			return false;
		}
		
		if("test".equals(type))
			return true;
		
		if(StringUtils.isEmpty(v)){
			return false;
		}
		
/*		if(v.equals("1.0") || v.equals("1.0.0"))
			return true;
*/		
		if(StringUtils.isEmpty(sig)){
			return false;
		}	
		
		Object obj = request.getAttribute(DispatchInterceptorConstant.REQUEST_PARAM_NAME);
		if(obj == null)
			return false;
		
		@SuppressWarnings("unchecked")
		Map<String, Object> params = (Map<String, Object>) obj;
		
		List<String> sortedParams = new ArrayList<String>();
		
		for(Map.Entry<String, Object> p : params.entrySet()){
			String k = p.getKey();
			if(!k.equals("_sig")){
				sortedParams.add(String.valueOf(p.getKey()));
			}
		}
		
		StringBuilder sb = new StringBuilder();
		Collections.sort(sortedParams);
		
		for (String paramKey : sortedParams) {
            sb.append(paramKey).append('=').append(StringUtils.defaultString(InterceptorUtil.getRequestParam(request,paramKey)));
		}
		
		String sk = secretKey;
		
		if(isTicket){
			UserPassport passport = passportService.getPassportByTicket(t);
			if(passport == null)
				return false;
			sk += passport.getUserSecretKey();
		}
		
		String gsig = DigestUtils.md5Hex(sb.toString() + sk).toLowerCase();
		
		if(gsig.equals(sig))
			return true;

		return false;
	}
	
	public boolean checkFreq(HttpServletRequest request,ClassMethodName cmn){
		String sig = InterceptorUtil.getRequestParam(request, "_sig");
		String v = InterceptorUtil.getRequestParam(request, "_v");
		if(v.equals("1.0") || v.equals("1.0.0"))
			return true;

		if(StringUtils.isEmpty(sig)){
			return true;
		}
		
		String newSig = DigestUtils.md5Hex(sig+cmn.getClassName()+cmn.getMethodName()).toLowerCase();
		
		Integer sigValue = passportService.getSig(newSig);
		
		if(sigValue == null){
			passportService.addSig(newSig, 1);
			return true;
		}
		
		int value = sigValue;
		
		if(value != 0){//已经请求过了
			return false;
			
		}else{//这次请求是新的
			passportService.addSig(newSig, 1);
			return true;
		}
		
	}

	private void LoggerInvokeInfo(HttpServletRequest request,Class<?> clazz,Method method,ParamParse parse,Object retObj){
		String type = InterceptorUtil.getRequestParam(request, "appType");
		String number = InterceptorUtil.getRequestParam(request, "_number");
		String ip = InterceptorUtil.getRequestParam(request, "_ip");
		String userId = InterceptorUtil.getRequestParam(request, "_userId");
		String version = InterceptorUtil.getRequestParam(request, "_v");
		String ticket = InterceptorUtil.getRequestParam(request, "_t");
		String deviceNumber = InterceptorUtil.getRequestParam(request, "_deviceNumber");
		String identity = InterceptorUtil.getRequestParam(request, "_identity");
		String market =  InterceptorUtil.getRequestParam(request, "_market");
		String requestMethod = request.getMethod();
		String contentType = request.getContentType();
		String encoder = request.getCharacterEncoding();
		String from = InterceptorUtil.getRequestParam(request, "_from");
		
		if(method.getName().toLowerCase().equals("login") ||
				method.getName().toCharArray().equals("register")||
				method.getName().toLowerCase().equals("registerByDJ")||
				method.getName().toLowerCase().equals("registerBy3rdPlatform")){
			LoggerInformation.LoggerInfo(logger, "invoke success", 
					"requestMethod",requestMethod,
					"contentType",contentType,
					"characterEncoding",encoder,
					"appType",type,
					"phoneNumber",number,
					"ip",ip,
					"userId",userId,
					"version",version,
					"ticket",ticket,
					"class",clazz.getSimpleName(),
					"method",method.getName(),
					"deviceNumber",deviceNumber,
					/*"params",parse.getObjs(),*/
					"retObj",retObj);
		}
		else{
			LoggerInformation.LoggerInfo(logger, "invoke success", 
					"requestMethod",requestMethod,
					"contentType",contentType,
					"characterEncoding",encoder,
					"appType",type,
					"phoneNumber",number,
					"ip",ip,
					"userId",userId,
					"version",version,
					"ticket",ticket,
					"class",clazz.getSimpleName(),
					"method",method.getName(),
					"params",parse.getObjs(),
					"deviceNumber",deviceNumber
					/*,
					"retObj",retObj*/);
		}
		
		
		LoggerStatistics.loggerInfo(method.getName(),type,deviceNumber,userId,version,identity,retObj,market,from,parse.getObjs());
	}
	
	private String getRequestString(HttpServletRequest request){
		Object obj = request.getAttribute(DispatchInterceptorConstant.REQUEST_PARAM_NAME);
		if(obj == null)
			return "";
		
		@SuppressWarnings("unchecked")
		Map<String, Object> params = (Map<String, Object>) obj;
		
		List<String> sortedParams = new ArrayList<String>();
		
		for(Map.Entry<String, Object> p : params.entrySet()){
			String k = p.getKey();
			if(!k.equals("_sig")){
				sortedParams.add(String.valueOf(p.getKey()));
			}
		}
		
		StringBuilder sb = new StringBuilder();
		Collections.sort(sortedParams);
		
		for (String paramKey : sortedParams) {
            sb.append(paramKey).append('=').append(StringUtils.defaultString(InterceptorUtil.getRequestParam(request,paramKey)));
		}
		return sb.toString();
	}
	
	public IPassportService getPassportService() {
		return passportService;
	}

	public void setPassportService(IPassportService passportService) {
		this.passportService = passportService;
	}

	public InterfaceMethodPool getInterfacePool() {
		return interfacePool;
	}

	public void setInterfacePool(InterfaceMethodPool interfacePool) {
		this.interfacePool = interfacePool;
	}

}
