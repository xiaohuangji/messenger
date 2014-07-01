package com.dajie.messageadmin.interceptor;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wills on 5/17/14.
 */
public class IpInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER= LoggerFactory.getLogger(IpInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        String ip = request.getHeader("X-Forwarded-For");//通过代理获取ip
        if(StringUtils.isEmpty(ip)){//如果请求没有经过代理，那么直接获取ip
            ip = request.getRemoteAddr();
        }

        if(ip.startsWith("192.168")||ip.startsWith("10.10")){
//            LOGGER.info("visit pass ,intranet ip:"+ip);
            return true;
        }else{
            LOGGER.warn("internet-ip is visiting admin console:"+ip);
            response.getWriter().write("you can only visit admin console in intranet !! ");
            return false;
        }

    }
}
