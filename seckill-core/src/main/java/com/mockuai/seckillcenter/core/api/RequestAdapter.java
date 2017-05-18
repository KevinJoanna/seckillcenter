package com.mockuai.seckillcenter.core.api;

import com.mockuai.seckillcenter.core.service.SeckillRequest;
import com.mockuai.seckillcenter.common.api.Request;

import java.util.Set;

public class RequestAdapter extends SeckillRequest {

    private static final long serialVersionUID = 1509004091384793666L;

    private Request request;

    public RequestAdapter(Request request) {

        this.request = request;
    }

    public Long getLong(String key) {

        Object value = this.request.getParam(key);
        return value == null ? null : Long.valueOf(Long.parseLong(value.toString()));
    }

    public Boolean getBoolean(String key) {

        return (Boolean) this.request.getParam(key);
    }


    public Integer getInteger(String key) {

        return (Integer) this.request.getParam(key);
    }


    public Double getDouble(String key) {

        return (Double) this.request.getParam(key);
    }

    public Float getFloat(String key) {

        return (Float) this.request.getParam(key);
    }

    public Object getObject(String key) {

        return this.request.getParam(key);
    }

    public String getString(String key) {

        return (String) this.request.getParam(key);
    }

    public String getCommand() {

        return this.request.getCommand();
    }

    public Set<String> getParamNames() {

        return this.request.getParamNames();
    }

    public Object getParam(String key) {

        return this.request.getParam(key);
    }

    public String[] getStrings(String key) {

        return (String[]) this.request.getParam(key);
    }
}