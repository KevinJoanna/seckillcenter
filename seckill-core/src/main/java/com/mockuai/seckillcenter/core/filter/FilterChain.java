package com.mockuai.seckillcenter.core.filter;

import com.mockuai.seckillcenter.core.exception.SeckillException;
import com.mockuai.seckillcenter.core.service.RequestContext;
import com.mockuai.seckillcenter.common.api.SeckillResponse;

import java.util.List;

public interface FilterChain {

    /**
     * 在拦截操作方法之前调用，如果所有filter成功调用，则返回true
     * 否则返回false
     *
     * @param ctx 当前请求上下文容器
     * @throws SeckillException 处理异常时抛出
     */
    public SeckillResponse before(RequestContext ctx) throws SeckillException;

    /**
     * 在拦截方法后调用
     * 调用成功返回true， 失败返回false
     *
     * @param ctx 当前请求上下文容器
     * @throws SeckillException 处理异常时抛出
     */
    public SeckillResponse after(RequestContext ctx) throws SeckillException;

    public List<Filter> getFilters() throws SeckillException;
}