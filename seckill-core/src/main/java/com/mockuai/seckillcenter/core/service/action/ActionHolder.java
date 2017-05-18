package com.mockuai.seckillcenter.core.service.action;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;


/**
 * 操作容器，运用启动时，就加载进所有的Action<br>
 * 需要开发新功能接口，只要新增相应的具体action即可
 *
 * @author wujin.zzq
 */
public class ActionHolder implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private Map<String, Action> actions;

    /**
     * 取得Spring容器里的Action
     *
     * @param
     * @return
     */
    @SuppressWarnings("unchecked")
    public Action getAction(String actionName) {
        if (null == actions) {
            synchronized (this) {
                if (null == actions) {
                    actions = new HashMap<String, Action>();
                    Map<String, Action> map = applicationContext.getBeansOfType(Action.class);
                    for (Action act : map.values()) {
                        actions.put(act.getName(), act);
                    }
                }

            }
        }
        return actions.get(actionName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}