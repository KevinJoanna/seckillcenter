package com.mockuai.seckillcenter.core.message.producer;


import com.mockuai.seckillcenter.core.exception.SeckillException;

/**
 * Created by edgar.zr on 12/16/15.
 */
public interface Producer {

    /**
     * 执行消息发放
     *
     * @param topic
     * @param tag
     * @param key
     * @param obj
     * @throws SeckillException
     */
    void send(String topic, String tag, String key, Object obj) throws SeckillException;
}