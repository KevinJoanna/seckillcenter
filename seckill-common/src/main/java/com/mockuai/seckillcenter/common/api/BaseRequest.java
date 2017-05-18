package com.mockuai.seckillcenter.common.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BaseRequest implements Request {

    private static final long serialVersionUID = -6523997865316523454L;

    private final Map<String, Object> attrs = new HashMap();
    private String command;
    private Map<String, Object> params = new HashMap(8);

    public BaseRequest() {
    }

    public BaseRequest(String command) {
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Set<String> getParamNames() {
        return this.params.keySet();
    }

    public Object getParam(String key) {
        return this.params.get(key);
    }

    public void setParam(String key, Object value) {
        this.params.put(key, value);
    }

    public void setAttribute(String key, Object value) {
        this.attrs.put(key, value);
    }

    public Object getAttribte(String key) {
        return this.attrs.get(key);
    }
}