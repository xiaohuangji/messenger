package com.dajie.message.mcp.interceptor;

import com.dajie.message.mcp.model.AppType;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.dajie.message.mcp.model.DispatchInterceptorConstant.*;
/**
 * 验证ip、mobile number和version
 * @author xinquan.guan
 *
 */
public class VerifyIpAndMobileNumberInterceptor  extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

        String type = InterceptorUtil.getRequestParam(request, "appType");
        String number = InterceptorUtil.getRequestParam(request, "_deviceNumber");
        String version = InterceptorUtil.getRequestParam(request, "_v");
        if(type.equals(AppType.test.name())){
            return true;
        }else if(type.equals(AppType.ios.name()) || type.equals(AppType.andriod.name()) || type.equals(AppType.qa)){
            String ip = request.getHeader("X-Forwarded-For");//通过代理获取ip
            if(StringUtils.isEmpty(ip)){//如果请求没有经过代理，那么直接获取ip
                ip = request.getRemoteAddr();
            }

            if(StringUtils.isEmpty(version)){//缺少version信息
                ResponseWriter.writeErrResponse(response, ACK_ERR_VERSION, ACK_ERR_VERSION_MSG);
                return false;
            }

            if(StringUtils.isEmpty(ip) || StringUtils.isEmpty(number)){//缺少deviceNumber信息
                ResponseWriter.writeErrResponse(response, ACK_ERR_IP_NUMBER, ACK_ERR_IP_NUMBER_MSG);
                return false;
            }else{
                request.setAttribute("_ip", ip);
                return true;
            }
        }
        return true;

	}
	
	
	
}
