package com.mockuai.seckillcenter.common.api;

public interface SeckillService<T> {
    Response<T> execute(Request request);
}