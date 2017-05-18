package com.mockuai.seckillcenter.core.filter.conf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterConfBean {

    private Map<String, List<FilterDescBean>> chainMap = new HashMap<String, List<FilterDescBean>>();
    private boolean extension = false;

    public Map<String, List<FilterDescBean>> getChainMap() {
        return chainMap;
    }

    public void setChainMap(Map<String, List<FilterDescBean>> chainMap) {
        this.chainMap = chainMap;
    }

    public boolean isExtension() {
        return extension;
    }

    public void setExtension(boolean extension) {
        this.extension = extension;
    }
}