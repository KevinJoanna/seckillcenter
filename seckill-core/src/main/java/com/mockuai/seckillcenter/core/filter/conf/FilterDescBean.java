package com.mockuai.seckillcenter.core.filter.conf;

import java.util.HashMap;
import java.util.Set;

/**
 * 描述filter的配置， FilterFactory 会使用这个描述bean去创建filter的实例 
 */
public class FilterDescBean {
	String intfClass;
	String implClass;
	String methodName;
	String factoryClass;
	HashMap<String, String> attributes = new HashMap<String, String>();
	String exclusion ;
	
	
	public String getIntfClass() {
		return intfClass;
	}
	
	public void setIntfClass(String intfClass) {
		this.intfClass = intfClass;
	}
	
	public String getImplClass() {
		return implClass;
	}
	
	public void setImplClass(String implClass) {
		this.implClass = implClass;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getFactoryClass() {
		return factoryClass;
	}
	
	public void setFactoryClass(String factoryClass) {
		this.factoryClass = factoryClass;
	}
	
	public String getAttribute(String key) {
		return attributes.get(key);
	}
	
	public void setAttribute(String name, String value) {
		attributes.put(name, value);
	}
	
	public Set<String> getAttributeNames() {
		return attributes.keySet();
	}

	public String getExclusion() {
		return exclusion;
	}

	public void setExclusion(String exclusion) {
		this.exclusion = exclusion;
	}
	
	
}
