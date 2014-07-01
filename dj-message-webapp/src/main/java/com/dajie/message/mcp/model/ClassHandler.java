package com.dajie.message.mcp.model;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.dajie.message.util.log.LoggerInformation;

/**
 * 映射了class 内部方法名字和Method间的映射
 * @author xinquan.guan
 *
 */
public class ClassHandler {
	private Object classObject;
	private Class<?> classInterface;
	private Map<String, MethodMeta> methodMapping = new HashMap<String, MethodMeta>();
	private String classSimpleName;
	private String tokenable;
	private static final Set<String> OBJECT_METHODS = new HashSet<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("toString");
			add("getClass");
			add("hashCode");
			add("equals");
			add("wait");
			add("notify");
			add("notifyAll");
		}
	};


	public Object getClassObject() {
		return classObject;
	}

	public void setClassObject(Object classObject,Class<?> clazz) {
		this.classObject = classObject;
		this.classInterface = clazz;
		initMethodMapping();
	}
	

	private void initMethodMapping() {
		Method[] methods = classInterface.getMethods();
		for (Method method : methods) {
			if (!OBJECT_METHODS.contains(method.getName())) {
				Method objectMethod = getMethodByName(classObject.getClass(), method.getName());
				if(objectMethod == null){
					LoggerInformation.LoggerErr("interface no method map with object "+method.getName(), new Exception());
				}
				
				methodMapping.put(method.getName().toLowerCase(),
						new MethodMeta(method,objectMethod));
			}
		}
	}
	
	private Method getMethodByName(Class<?> clazz,String methodName){
		Method[] methods = clazz.getMethods();
		for(Method method : methods){
			if(method.getName().equals(methodName)){
				return method;
			}
		}
		return null;
	}

	public String getClassSimpleName() {
		return classSimpleName;
	}

	public void setClassSimpleName(String classSimpleName) {
		this.classSimpleName = classSimpleName;
	}

	public MethodMeta getMethodMeta(String methodName) {
		return methodMapping.get(methodName);
	}

	public String getTokenable() {
		return tokenable;
	}

	public void setTokenable(String tokenable) {
		this.tokenable = tokenable;
	}
}
