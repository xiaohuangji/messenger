package com.dajie.messageadmin.controller;

import com.dajie.message.annotation.rest.RestBean;
import com.dajie.message.util.scan.InterfaceScan;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wills on 5/4/14.
 */
@Controller
@RequestMapping("/admin/service")
public class ServiceTestController {

    private static final Logger LOGGER= LoggerFactory.getLogger(ServiceTestController.class);

    private static List<String> restPackage=new ArrayList<String>();

    private RestTemplate rest = new RestTemplate();

    static {
        restPackage.add("com.dajie.message.service");
    }

    @RequestMapping("index")
    public ModelAndView index(){
        ModelAndView mv=new ModelAndView("admin/serviceTest");
        List<Class<?>> serviceClass=InterfaceScan.getClasses(restPackage,RestBean.class);
        List<String> services=new ArrayList<String>();
        for(Class service:serviceClass){
            services.add(service.getName());
        }
        mv.addObject("services",services);
        return mv;
    }


    @ResponseBody
    @RequestMapping("getMethod")
    public List<String> getMethodAjax(String serviceName){
        List<String> methods=new ArrayList<String>();
        try{
            Class service=Class.forName(serviceName);
            Method[] methodMs=service.getMethods();
            for(Method method:methodMs){
                methods.add(method.getName());
            }
        }catch(Exception e){
            LOGGER.error("get method error,",e);
        }

        return methods;
    }

    private String getServiceImplName(String interfaceName){
        int offset=interfaceName.lastIndexOf('.')+1;
        String shortInterfaceName=interfaceName.substring(offset);
        //tian yuan is special
        if(shortInterfaceName.contains("PoiInfo")||shortInterfaceName.contains("Map")){
            offset=interfaceName.lastIndexOf("service.");
            return interfaceName.substring(0,offset)+"elasticsearch.map.service.impl."+shortInterfaceName.substring(1)+"Impl";
        }
        return interfaceName.substring(0,offset)+"impl."+shortInterfaceName.substring(1)+"Impl";
    }

    private Method getMethodFromInterface(String interfaceName,String methodName) throws Exception{
        Class interfaceClass=Class.forName(interfaceName);
        Method[] methods=interfaceClass.getMethods();
        for(Method method:methods){
            if(method.getName().equals(methodName)){
                return method;
            }
        }
        return null;
    }

    @ResponseBody
    @RequestMapping("getParam")
    public Map<String,Object> getParamAjax(String serviceName,String methodName){
        Map<String,Object> paramsMap=new HashMap<String, Object>();
        try{
            LocalVariableTableParameterNameDiscoverer discover = new LocalVariableTableParameterNameDiscoverer();
            Class serviceImpl=Class.forName(getServiceImplName(serviceName));
            Method method=getMethodFromInterface(serviceName,methodName);
            Method realMethod=serviceImpl.getMethod(method.getName(),method.getParameterTypes());
            Class[] paramsType=realMethod.getParameterTypes();
            String[] params = discover.getParameterNames(realMethod);
            for(int i=0;i<params.length;i++){
                if(!params[i].equals("_userId")){
                    paramsMap.put(params[i], getDefaultBaseTypeValue(paramsType[i]));
                }
            }
        }catch(Exception e){
            LOGGER.error("get param error",e);
        }
        return paramsMap;
    }

    @RequestMapping("invoke")
    @ResponseBody
    public String invokeMethodAjax(String service,String method,String input,String userId,String serviceHost){
        int index=service.lastIndexOf('.');
        if(StringUtils.isEmpty(serviceHost)){
            serviceHost="localhost:8556";
        }
        String url="http://"+serviceHost+"/api/"+service.substring(index+1)+"/"+method;
        Map<String,Object> maps =JSONObject.fromObject(input);
        maps.put("_appId","test");
        maps.put("_password","test");
        maps.put("_userId",userId);
        return  rest.postForObject(url,maps,String.class);
    }


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
