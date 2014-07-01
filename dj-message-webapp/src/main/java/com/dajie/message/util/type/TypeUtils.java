package com.dajie.message.util.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.dubbo.common.utils.StringUtils;

public class TypeUtils {

	
	public static boolean isNubType(Class<?> type){
		if (type == Integer.class || type == int.class) {
			return true;
		}else if (type == Long.class || type == long.class) {
			return true;
		} else if (type == Double.class || type == double.class) {
			return true;
		} else if (type == Float.class || type == float.class) {
			return true;
		} else if (type == Short.class || type == short.class) {
			return true;
		}
		return false;
	}
	
	public static boolean isBaseType(Class<?> type){
		if (type == String.class) {
			return true;
		} else if (type == Integer.class || type == int.class) {
			return true;
		} else if (type == Boolean.class || type == boolean.class) {
			return true;
		} else if (type == Long.class || type == long.class) {
			return true;
		} else if (type == Double.class || type == double.class) {
			return true;
		} else if (type == Float.class || type == float.class) {
			return true;
		} else if (type == Short.class || type == short.class) {
			return true;
		} else if (type == Byte.class || type == byte.class) {
			return true;
		}
		return false;
	}
	
	/**
	 * 将值转换成基础类型
	 * @param type
	 * @param value
	 * @return
	 */
	public static Object changeToBaseType(Class<?> type, String value) {
		if (value == null)
			return null;
		if (type == String.class) {
			return value;
		} else if (type == Integer.class || type == int.class) {
			return Integer.valueOf(value);
		} else if (type == Boolean.class || type == boolean.class) {
			return Boolean.valueOf(value);
		} else if (type == Long.class || type == long.class) {
			return Long.valueOf(value);
		} else if (type == Double.class || type == double.class) {
			return Double.valueOf(value);
		} else if (type == Float.class || type == float.class) {
			return Float.valueOf(value);
		} else if (type == Short.class || type == short.class) {
			return Short.valueOf(value);
		} else if (type == Byte.class || type == byte.class) {
			return Byte.valueOf(value);
		}
		return null;
	}
	
	public static <T> List<T> changeMapToList(Map<?, T> map){
		List<T> retList = new ArrayList<T>();
		if(map == null || map.size() < 1)
			return retList;
		
		for(Map.Entry<?, T> m : map.entrySet()){
			retList.add(m.getValue());
		}
		return retList;
	}
	
	public static <T> List<Object> changeListToObjectList(List<T> list){
		List<Object> retList = new ArrayList<Object>();
		if(list == null)
			return retList;
		
		for(T t : list){
			retList.add(t);
		}
		return retList;
	}
	
	public static String convertIntegerListToString(List<Integer> list){
		if(list.size() < 1)
			return "";
		StringBuilder builder = new StringBuilder();
		for(Integer i : list){
			builder.append(String.valueOf(i));
			builder.append(",");
		}
		builder.deleteCharAt(builder.length()-1);
		return builder.toString();
	}

	public  static<T> T choiceNoEmpty(T base,T optional){
		if(base.getClass() == Integer.class || base.getClass() == int.class){
			if(base != null){
				int baseInt = (Integer) base;
				if(baseInt > 0){
					return base;
				}else{
					return optional;
				}
			}
		}else if(base.getClass() == String.class){
			String str = (String) base;
			if(StringUtils.isEmpty(str)){
				return optional;
			}else{
				return base;
			}
		}
		return base;
	}
	
}
