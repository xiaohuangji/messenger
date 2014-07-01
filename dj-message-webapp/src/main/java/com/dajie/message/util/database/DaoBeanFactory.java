package com.dajie.message.util.database;

import org.mybatis.spring.mapper.MapperFactoryBean;

public class DaoBeanFactory<T> extends MapperFactoryBean<T>{
	public DaoBeanFactory(Class<T> mapperInterface) {
		super.setMapperInterface(mapperInterface);
	}
}
