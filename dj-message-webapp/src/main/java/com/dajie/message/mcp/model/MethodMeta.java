package com.dajie.message.mcp.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import com.dajie.message.annotation.mcp.NoTicket;
import com.dajie.message.annotation.mcp.ParamNullable;

/**
 * 方法名字与参数名字的映射（根据方法名，可以获取对应的参数的名字）
 * @author xinquan.guan
 *
 */
public class MethodMeta {
	/**
	 * 代表的当前方法
	 */
	private Method method;
	/**
	 * 参数名字
	 */
	private List<ParamMeta> paramMeta = new ArrayList<ParamMeta>();
	/**
	 * 当前方法是否需要验证t票
	 */
	private boolean isTiket = true;
		
	public boolean isTiket() {
		return isTiket;
	}

	public void setTiket(boolean isTiket) {
		this.isTiket = isTiket;
	}

	public MethodMeta(Method method,Method objectMethod) {
		setMethod(method,objectMethod);
	}

	public Method getMethod() {
		return method;
	}

	public List<ParamMeta> getParamMetas() {
		return paramMeta;
	}

	public void setMethod(Method method,Method objectMethod) {
		this.method = method;
		LocalVariableTableParameterNameDiscoverer discover = new LocalVariableTableParameterNameDiscoverer();
		String[] paramNames = discover.getParameterNames(objectMethod);//获取param names
		
		Annotation token = method.getAnnotation(NoTicket.class);//方法是否需要t票
		if(token != null){
			isTiket = false;
		}
		
		Annotation[][] paramAnnotations = method.getParameterAnnotations();
		boolean isAdded = false;
		for (int i = 0; i < paramNames.length; i++) {//参数名和可否为空
			isAdded = false;
			for (Annotation a : paramAnnotations[i]) {
				if (a instanceof ParamNullable) {//参数是否可以为空
					if(((ParamNullable) a).defaultValue() != null && !"".equals(((ParamNullable) a).defaultValue())){
						//参数默认值
						paramMeta.add(new ParamMeta(paramNames[i],
								((ParamNullable) a).value(),((ParamNullable) a).defaultValue()));
					}else{//可以为空但是没有默认值
						paramMeta.add(new ParamMeta(paramNames[i],
								((ParamNullable) a).value()));
					}
					isAdded = true;
					break;
				}
			}
			if (!isAdded) {
				paramMeta.add(new ParamMeta(paramNames[i], false));
			}

		}
	}
}
