package com.dajie.message.util.type;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

public class BeanCopy {

	public static void copyProperties(Object dest,Object src){
		try {
			if(dest == null || src == null)
				return;
			BeanUtils.copyProperties(dest, src);
		} catch (IllegalAccessException e) {
			//e.printStackTrace();
		} catch (InvocationTargetException e) {
			//e.printStackTrace();
		}
	}
	
}
