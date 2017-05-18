package com.mockuai.seckillcenter.core.message.consumer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.mockuai.seckillcenter.common.constant.RMQMessageType;
import com.mockuai.seckillcenter.core.exception.SeckillException;
import com.mockuai.seckillcenter.core.manager.AppManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by edgar.zr on 12/15/15.
 */
public class BaseConsumer implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseConsumer.class);

    /**
     * rocketMQ 地址
     */
    private String address;
    /**
     * rocketMQ 端口
     */
    private String port;

    private String consumerGroup;

    @Autowired
    private AppManager appManager;

    @Autowired
    private ListenerHolder listenerHolder;

    @Override
    public void afterPropertiesSet() {

        try {
            final DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
            LOGGER.info("consumer_group : {}, address : {}, port : {}", consumerGroup, address, port);
            consumer.setNamesrvAddr(address + ":" + port);
            // 批量订阅
            String tags;
            for (Map.Entry<String, List<String>> entry : listenerHolder.topicTagMap.entrySet()) {
                tags = getTags(entry.getValue());
                consumer.subscribe(entry.getKey(), tags);
                LOGGER.info("subscribe : {}, {}", entry.getKey(), tags);
            }
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.registerMessageListener(new MessageListenerConcurrently() {

                public String getTopicTag(String topic, String tag) {
                    return topic + "_" + tag;
                }

                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

                    LOGGER.info("Receive {} Messages", msgs.size());
                    LOGGER.info("{}", Arrays.deepToString(msgs.toArray()));

                    try {
                        if (msgs.isEmpty()) return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        MessageExt msg = msgs.get(0);

                        List<Listener> listeners = listenerHolder.getListener(getTopicTag(msg.getTopic(), msg.getTags()));
                        if (listeners == null || listeners.isEmpty()) return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        LOGGER.info("msg topic = {}, tag = {}, body = {}", msg.getTopic(), msg.getTags(), new String(msg.getBody()).toString());

                        List<JSONObject> jsonObjects = new ArrayList<JSONObject>();

                        // 数组消息，拆分成单条消息处理
                        if (msg.getTopic().equals(RMQMessageType.ITEM_STATUS_CHANGE_BATCH.getTopic()) && msg.getTags().equals(RMQMessageType.ITEM_STATUS_CHANGE_BATCH.getTag())) {
                            JSONArray jsonArray = JSONObject.parseArray(new String(msg.getBody()));
                            for (int i = 0; i < jsonArray.size(); i++)
                                jsonObjects.add(jsonArray.getJSONObject(i));
                        } else {
                            jsonObjects.add(JSONObject.parseObject(new String(msg.getBody())));
                        }

                        for (JSONObject jsonObject : jsonObjects) {

                            //  咱不做消息重复处理
//                            if (!rmqMessageManager.addMessage(jsonObject.getLong("id"), msg.getTopic(), msg.getTags())) {
//                                continue;
//                            }

                            String appKey;
                            try {
                                appKey = appManager.getAppKeyByBizCode(jsonObject.getString("bizCode"));
                            } catch (SeckillException e) {
                                LOGGER.error("error to get appKey, {} ", jsonObject.toJSONString());
                                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                            }

                            for (Listener listener : listeners)
                                try {
                                    listener.execute(jsonObject, appKey);
                                } catch (SeckillException e) {
                                    LOGGER.error("error to consume message : {}", jsonObject.toJSONString(), e);
                                }
                        }
                    } catch (Exception e) {
                        LOGGER.error("error to consume message, {}", Arrays.deepToString(msgs.toArray()), e);
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            consumer.start();
            LOGGER.info("appManager in BaseConsumer, {}", appManager);
        } catch (MQClientException e) {
            LOGGER.error("", e);
        }
    }

    public String getTags(List<String> tags) {
        StringBuilder sb = new StringBuilder(tags.remove(0));
        while (!tags.isEmpty())
            sb.append(" || ").append(tags.remove(0));
        return sb.toString();
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }
}