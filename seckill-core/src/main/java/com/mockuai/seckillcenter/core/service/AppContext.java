package com.mockuai.seckillcenter.core.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;
import java.util.Map;

/**
 * @author wujin.zzq
 */
public class AppContext extends Context implements ApplicationContextAware {
    private static final long serialVersionUID = 6830942667813319841L;
    private ApplicationContext applicationContext;

    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> clz) {
        Map<String, T> beans = applicationContext.getBeansOfType(clz);

        Collection<T> values = beans.values();

        if (values.size() == 0) {
            return null;
        }
        return values.iterator().next();
    }

    public Object getBean(String beanId) {
        return applicationContext.getBean(beanId);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}