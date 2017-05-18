package com.mockuai.seckillcenter.core.api.impl;

import com.mockuai.seckillcenter.common.api.SeckillService;
import com.mockuai.seckillcenter.core.api.RequestAdapter;
import com.mockuai.seckillcenter.core.service.RequestDispatcher;
import com.mockuai.seckillcenter.common.api.Request;
import com.mockuai.seckillcenter.common.api.Response;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SeckillServiceImpl<T> implements SeckillService<T> {

    @Resource
    private RequestDispatcher requestDispatcher;

    public Response<T> execute(Request request) {

        return this.requestDispatcher.dispatch(new RequestAdapter(request));
    }
}