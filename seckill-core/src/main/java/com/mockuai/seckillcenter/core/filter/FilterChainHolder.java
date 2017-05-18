package com.mockuai.seckillcenter.core.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * filter链管理器
 *
 * @author wujin.zzq
 */
public class FilterChainHolder {
    /**
     * action与filterChain的映射关系
     */
    private Map<String, FilterChain> chains = new HashMap<String, FilterChain>();
    private boolean extendsChain = false;

    public void setChains(Map<String, FilterChain> chains) {
        this.chains = chains;
    }

    public void putChain(String chainName, FilterChain chain) {
        chains.put(chainName, chain);
    }

    public FilterChain getChain(String chainName) {
        FilterChain c = chains.get(chainName);
        return c == null ? new FilterChainImpl(new ArrayList<Filter>(0)) : c;
    }

    public boolean isExtendsChain() {
        return extendsChain;
    }

    public void setExtendsChain(boolean extendsChain) {
        this.extendsChain = extendsChain;
    }
}