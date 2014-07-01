/**
 * 
 */
package com.dajie.message.elasticsearch.map.utils.spring;

import java.net.URL;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author tianyuan.zhu
 *
 */
@Component("myApplicationContextUtil")
public class MyApplicationContextUtil implements ApplicationContextAware {

	private static ApplicationContext context;
	
//	static{
//		URL logPath = MyApplicationContextUtil.class.getClassLoader().getResource("log4j.properties");
//		PropertyConfigurator.configure(logPath.getPath());
//	}
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		this.context = applicationContext;
	}

	public static ApplicationContext getContext() {
		
		if(context == null)
		{
			context = new ClassPathXmlApplicationContext(
					"classpath:mybatis-connection.xml",
					"classpath:spring/*.xml",
					"classpath:*.xml"
					);
		}
		
		
		return context;
	}
	
	public static Object getObjectByName(String name)
	{
		return getContext().getBean(name);
	}
	
}
