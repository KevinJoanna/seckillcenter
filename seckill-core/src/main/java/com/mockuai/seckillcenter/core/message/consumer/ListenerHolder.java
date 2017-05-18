package com.mockuai.seckillcenter.core.message.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取当前所有的消息发送器
 * 支持单 topic_tag 组合，多处理器
 */
@Service
public class ListenerHolder implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListenerHolder.class);
    public Map<String, List<String>> topicTagMap;
    public Map<String, List<Listener>> listenerMap;
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        Map<String, Listener> map = applicationContext.getBeansOfType(Listener.class);
        listenerMap = new HashMap<String, List<Listener>>();
        for (Listener act : map.values()) {
            if (!listenerMap.containsKey(act.getName()))
                listenerMap.put(act.getName(), new ArrayList<Listener>());
            listenerMap.get(act.getName()).add(act);
        }
        LOGGER.info("messages to subscribe:");
        topicTagMap = new HashMap<String, List<String>>();
        String[] msg;
        for (String name : listenerMap.keySet()) {
            msg = name.split("_");
            if (msg != null && msg.length == 2) {
                if (!topicTagMap.containsKey(msg[0])) {
                    topicTagMap.put(msg[0], new ArrayList<String>());
                }
                topicTagMap.get(msg[0]).add(msg[1]);
                LOGGER.info("{}, {}", msg[0], msg[1]);
            }
        }
    }

    public List<Listener> getListener(String beanName) {
        return listenerMap.get(beanName);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}