package com.mockuai.seckillcenter.core.message;

import java.util.HashMap;
import java.util.Map;

public class Context {

    private Map<String, Object> paramMap = new HashMap();
    private Map<String, Object> attributeMap = new HashMap();

    public void setParam(String paramName, Object paramValue) {

        this.paramMap.put(paramName, paramValue);
    }

    public Object getParam(String paramName) {

        return this.paramMap.get(paramName);
    }

    public void setAttribute(String attributeName, Object attributeValue) {

        this.attributeMap.put(attributeName, attributeValue);
    }

    public Object getAttribute(String attributeName) {

        return this.attributeMap.get(attributeName);
    }
}