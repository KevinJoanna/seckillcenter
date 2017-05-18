package com.mockuai.seckillcenter.core.message.producer;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.mockuai.seckillcenter.core.exception.SeckillException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * Created by edgar.zr on 12/15/15.
 */
public class BaseProducer implements Producer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseProducer.class);

    /**
     * rocketMQ 地址
     */
    private String address;
    /**
     * rocketMQ 端口
     */
    private String port;

    private String producerGroupName = "MarketingProducerGroup";
    private String instanceName = "BaseProducer";

    private DefaultMQProducer producer;

    public void send(String topic, String tag, String key, Object obj) throws SeckillException {
        try {
            Message msg = new Message(topic, // topic
                    tag, // tag
                    key, // key
                    JSONObject.toJSONString(obj).getBytes());// body
            SendResult sendResult = producer.send(msg);
            LOGGER.debug("sendMsg : {}", sendResult);
        } catch (Exception e) {
            LOGGER.error("mq send message error", e);
            throw new SeckillException("mq send message error");
        }
    }

    @PostConstruct
    private void init() {
        producer = new DefaultMQProducer(producerGroupName);
        producer.setInstanceName(instanceName);
        producer.setNamesrvAddr(address + ":" + port);
        try {
            producer.start();
        } catch (MQClientException e) {
            LOGGER.error("error to start producer, producerGroupName : {}, instanceName : {}, namesrvAddr : {}",
                    producer.getProducerGroup(), producer.getInstanceName(), producer.getNamesrvAddr());
        }
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPort(String port) {
        this.port = port;
    }
}