package com.dajie.message.service.impl;

import com.dajie.message.enums.PlatformEnum;
import com.dajie.message.exception.UnResolvableTypeException;

public interface IPlatformProcessFactory {
	PlatformProcessor getPlatformProcessor(PlatformEnum type) throws UnResolvableTypeException;
}
