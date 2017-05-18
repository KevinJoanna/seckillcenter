package com.mockuai.seckillcenter.common.api;

import java.io.Serializable;
import java.util.Set;

public abstract interface Request extends Serializable {

    public abstract String getCommand();

    public abstract void setCommand(String paramString);

    public abstract Set<String> getParamNames();

    public abstract Object getParam(String paramString);

    public abstract void setParam(String paramString, Object paramObject);
}