package com.dajie.message.mcp.interceptor;

import com.dajie.message.mcp.model.AppType;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.dajie.message.mcp.model.DispatchInterceptorConstant.*;

/**
 * 验证appId信息,目前仅支持三种类型的appId，分别为test、ios、android
 * @author xinquan.guan
 *
 */
public class VerifyAppInfoInterceptor  extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
//		if (DefaultHandler.class.isAssignableFrom(handler.getClass())) {//仅有DefaultHandler的消息才处理
        String appId = InterceptorUtil.getRequestParam(request, "_appId");
        String password = InterceptorUtil.getRequestParam(request, "_password");
        if(StringUtils.isEmpty(appId) || StringUtils.isEmpty(password)){//请求缺少appId
            ResponseWriter.writeErrResponse(response, ACK_ERR_APP_ID, ACK_ERR_APP_ID_MSG);
            return false;
        }

        if(appId.equals(TEST_APP_ID)){
            if(password.equals(TEST_PASSWORD)){
                request.setAttribute("appType", AppType.test);
                return true;
            }else{
                ResponseWriter.writeErrResponse(response, ACK_ERR_APP_ID_PASSWORD, ACK_ERR_APP_ID_PASSWORD_MSG);
                return false;
            }
        }else if(appId.equals(IOS_APP_ID)){
            if(password.equals(IOS_PASSWORD)){
                request.setAttribute("appType", AppType.ios);
                return true;
            }else{
                ResponseWriter.writeErrResponse(response, ACK_ERR_APP_ID_PASSWORD, ACK_ERR_APP_ID_PASSWORD_MSG);
                return false;
            }
        }else if(appId.equals(ANDROID_APP_ID)){
            if(password.equals(ANDROID_APP_PASSWORD)){
                request.setAttribute("appType", AppType.andriod);
                return true;
            }else{
                ResponseWriter.writeErrResponse(response, ACK_ERR_APP_ID_PASSWORD, ACK_ERR_APP_ID_PASSWORD_MSG);
                return false;
            }
        }else if(appId.equals(QA_APP_ID)){
            if(password.equals(QA_APP_PASSWORD)){
                request.setAttribute("appType", AppType.qa);
                return true;
            }else{
                ResponseWriter.writeErrResponse(response, ACK_ERR_APP_ID_PASSWORD, ACK_ERR_APP_ID_PASSWORD_MSG);
                return false;
            }
        }
        else{
            ResponseWriter.writeErrResponse(response, ACK_ERR_APP_ID_NAME, ACK_ERR_APP_ID_NAME_MSG);
            return false;
        }
//		}else{
//			return true;
//		}

	}
	
}
