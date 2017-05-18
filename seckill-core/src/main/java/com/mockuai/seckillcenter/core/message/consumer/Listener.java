package com.mockuai.seckillcenter.core.message.consumer;


import com.alibaba.fastjson.JSONObject;
import com.mockuai.seckillcenter.core.exception.SeckillException;

public interface Listener {

    void init();

    String getName();

    String execute(JSONObject msg, String appKey) throws SeckillException;
}