package com.dajie.push.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by wills on 5/4/14.
 */
public class SpringBeanFactory {

    private static ApplicationContext context= new ClassPathXmlApplicationContext("applicationContext.xml");

    public static <T> T getBean(Class<T> clazz){
//        String className=clazz.getSimpleName();
//        String springBeanName=className.substring(0,1).toLowerCase()+className.substring(1);
        return (T)context.getBean(clazz);
    }

}
