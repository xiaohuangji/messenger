package com.dajie.messageadmin.listener;

import com.dajie.messageadmin.utils.ClassScanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wills on 5/13/14.
 */
public class ServiceDefinitionRegistryPostProcess implements BeanDefinitionRegistryPostProcessor {
    
    private static final Logger LOGGER= LoggerFactory.getLogger(ServiceDefinitionRegistryPostProcess.class);

    private List<String> servicePackages = new ArrayList<String>();

    private List<String> excludeServices=new ArrayList<String>();

    public void setServicePackages(List<String> servicePackages) {
        this.servicePackages = servicePackages;
    }

    public void setExcludeServices(List<String> excludeServices) {
        this.excludeServices = excludeServices;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        List<Class<?>> classes = ClassScanUtil.getClasses(servicePackages, null);

        for(Class<?> clazz : classes){

            Component component=clazz.getAnnotation(Component.class);
            if(component==null){
                continue;
            }
            String beanName=component.value();

            if(excludeServices.contains(beanName)){
                LOGGER.info("admin add service,exclude service:"+clazz.getSimpleName());
                continue;
            }
            BeanDefinitionBuilder builder=BeanDefinitionBuilder.rootBeanDefinition(clazz);
            Field[] fields=clazz.getDeclaredFields();
            for(Field field:fields){
                String fieldName=field.getName();
                if(fieldName.endsWith("DAO")||fieldName.endsWith("service")||fieldName.endsWith("cache")){
                    builder.addPropertyReference(fieldName,fieldName);
                }
            }
            registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
            LOGGER.info("admin add service to springContainer:"+beanName);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
