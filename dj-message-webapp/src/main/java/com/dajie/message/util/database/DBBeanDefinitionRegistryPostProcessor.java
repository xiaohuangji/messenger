package com.dajie.message.util.database;

import com.dajie.message.annotation.database.DBBean;
import com.dajie.message.util.log.LoggerInformation;
import com.dajie.message.util.scan.InterfaceScan;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.util.ArrayList;
import java.util.List;

public class DBBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor{
	private Logger logger = Logger.getLogger(DBBeanDefinitionRegistryPostProcessor.class);
	private static final String DEFAULT_DB_PACKAGE = "com.dajie.message.dao";
	private List<String> dbPackages = new ArrayList<String>();
	private SqlSessionFactory sqlSessionFactory ;

	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
	}

	@Override
	public void postProcessBeanDefinitionRegistry(
			BeanDefinitionRegistry registry) throws BeansException {
		if(dbPackages.size() < 1){
			dbPackages.add(DEFAULT_DB_PACKAGE);
			LoggerInformation.LoggerErr(logger, "DBBeanDefinitionRegistryPostProcessor scan package property dbPackages should not null ,check you config for this properties !",new Exception());
		}
		 List<Class<?>> classes = InterfaceScan.getClasses(dbPackages,DBBean.class);
		
		for(Class<?> clazz : classes){		
			registry.registerBeanDefinition(clazz.getSimpleName(), BeanDefinitionBuilder.rootBeanDefinition(DaoBeanFactory.class).addConstructorArgValue(clazz).addPropertyValue("sqlSessionFactory", sqlSessionFactory).getBeanDefinition());
			LoggerInformation.LoggerInfo(logger, "add new dao bean to spring context : ", clazz.getSimpleName());
		}
	}
	public List<String> getDbPackages() {
		return dbPackages;
	}

	public void setDbPackages(List<String> dbPackages) {
		this.dbPackages = dbPackages;
	}

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

}
