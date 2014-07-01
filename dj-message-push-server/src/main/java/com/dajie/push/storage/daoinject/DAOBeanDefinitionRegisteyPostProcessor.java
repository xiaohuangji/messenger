package com.dajie.push.storage.daoinject;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wills on 3/11/14.
 */
public class DAOBeanDefinitionRegisteyPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private List<String> scanPackage=new ArrayList<String>();

    private SqlSessionFactory sqlSessionFactory;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        List<Class> classes= DaoScan.getClasses(scanPackage, DAO.class);
        for(Class clazz:classes){
            registry.registerBeanDefinition(clazz.getSimpleName(),
                    BeanDefinitionBuilder.rootBeanDefinition(MapperFactoryBean.class).addPropertyValue("mapperInterface", clazz)
                            .addPropertyValue("sqlSessionFactory", sqlSessionFactory).getBeanDefinition());
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    public List<String> getScanPackage() {
        return scanPackage;
    }

    public void setScanPackage(List<String> scanPackage) {
        this.scanPackage = scanPackage;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }
}
