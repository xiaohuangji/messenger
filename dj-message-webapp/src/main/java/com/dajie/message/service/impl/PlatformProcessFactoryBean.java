package com.dajie.message.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dajie.message.enums.PlatformEnum;
import com.dajie.message.exception.UnResolvableTypeException;

@Component("platformProcessFactory")
public class PlatformProcessFactoryBean implements IPlatformProcessFactory {

	@Autowired
	private SinaWeiboProcessor sinaWeiboProcessor;

	@Autowired
	private TecentQQProcessor tecentQQProcessor;

	@Autowired
	private RenrenProcessor renrenProcessor;

	@Override
	public PlatformProcessor getPlatformProcessor(PlatformEnum type)
			throws UnResolvableTypeException {
		if (PlatformEnum.SINA_WEIBO == type) {
			return new SinaWeiboProcessor();
		} else if (PlatformEnum.TECENT_QQ == type) {
			return new TecentQQProcessor();
		} else if (PlatformEnum.RENREN == type) {
			return new RenrenProcessor();
		} else {
			throw new UnResolvableTypeException(type.getCode());
		}
	}

}
