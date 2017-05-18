package com.mockuai.seckillcenter.core.service;

import com.mockuai.seckillcenter.common.api.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SeckillRequest implements Request {
    private static final long serialVersionUID = -6902403430608462635L;
    private String command;
    private Context context;
    private Map<String, Object> attributes = new HashMap();

    public Context getContext() {

        return this.context;
    }

    public void setContext(Context context) {

        this.context = context;
    }

    public Long getLong(String key) {

        return null;
    }

    public String getString(String key) {

        return null;
    }

    public Boolean getBoolean(String key) {

        return null;
    }

    public Integer getInteger(String key) {

        return null;
    }

    public Double getDouble(String key) {

        return null;
    }

    public Float getFloat(String key) {

        return null;
    }

    public String[] getStrings(String key) {

        return null;
    }

    public Object getObject(String key) {

        return null;
    }

    public String getCommand() {

        return this.command;
    }

    public void setCommand(String command) {

        this.command = command;
    }

    public Set<String> getParamNames() {

        return null;
    }

    public Object getParam(String key) {

        return null;
    }

    public void setParam(String key, Object value) {
    }

    public void setAttribute(String key, Object val) {

        this.attributes.put(key, val);
    }

    public Object getAttribute(String key) {

        return this.attributes.get(key);
    }

    public void addAttributes(Map<String, Object> attributes) {

        this.attributes.putAll(attributes);
    }

    public Map<String, Object> getAttributes() {

        return this.attributes;
    }
}