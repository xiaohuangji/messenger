package com.dajie.message.mcp.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.dajie.message.annotation.rest.RestBean;
import com.dajie.message.mcp.model.ClassHandler;
import com.dajie.message.util.log.LoggerInformation;
import com.dajie.message.util.scan.InterfaceScan;
/**
 * 维护由class 名字（不区分大小写）到spring容器内实体bean间的映射，初始化方法为init方法。
 * @author xinquan.guan
 *
 */
public class InterfaceMethodPool implements ApplicationContextAware,InitializingBean  {
	private static final Logger logger = Logger.getLogger(InterfaceMethodPool.class);
	private static final String DEFAULT_MCP_PACKAGE = "com.dajie.mobile.remote.service";
	private ApplicationContext context;
	private List<String> mcpPackages = new ArrayList<String>();
	private Map<String, ClassHandler> handlerMapping = new ConcurrentHashMap<String, ClassHandler>();
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.context = context;
	}
	

	public ClassHandler getClassHandler(String interfaceName){
		return handlerMapping.get(interfaceName);
	}
	
	public void init() {
		if(mcpPackages.size() < 1){//配置默认package，可以通过设置mcpPackages字段来修改默认配置
			LoggerInformation.LoggerErr(logger, "InterfaceMethodPool property mcpPackages should not be null or empty", new Exception());
			mcpPackages.add(DEFAULT_MCP_PACKAGE);
		}
		List<Class<?>> classes = InterfaceScan.getClasses(mcpPackages,RestBean.class);//获取package包下的所有类（不包含子包下的类）
		for (Class<?> c : classes) {
			try {
				ClassHandler classHandler = new ClassHandler();
				classHandler.setClassObject(context.getBean(c),c);
				classHandler.setClassSimpleName(c.getSimpleName());
				handlerMapping.put(classHandler.getClassSimpleName()
						.toLowerCase(), classHandler);
				LoggerInformation.LoggerInfo(logger, "add new rest service to spring context : ",c.getName());
			} catch (Exception e) {
				LoggerInformation.LoggerErr(logger, "add new rest service to spring context error", e);
			}
		}
	}
	public List<String> getMcpPackages() {
		return mcpPackages;
	}

	public void setMcpPackages(List<String> mcpPackages) {
		this.mcpPackages = mcpPackages;
	}


	public void afterPropertiesSet() throws Exception {
		init();
	}

}
