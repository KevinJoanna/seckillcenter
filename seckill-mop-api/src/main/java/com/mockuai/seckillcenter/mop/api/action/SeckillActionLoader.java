package com.mockuai.seckillcenter.mop.api.action;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.mockuai.seckillcenter.common.api.SeckillService;
import com.mockuai.mop.common.service.action.Action;
import com.mockuai.mop.common.service.action.ActionLoader;
import com.mockuai.mop.common.service.action.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class SeckillActionLoader implements ActionLoader {
    private SeckillService seckillService;

    public static void main(String[] args) {
        SeckillActionLoader tal = new SeckillActionLoader();
        List actions = tal.load();
        System.out.println("size=" + actions.size());
    }

    public void init(Context context) {
        RegistryConfig registryConfig = (RegistryConfig) context.getAttribute("registry_config");

        ApplicationConfig application = new ApplicationConfig();
        application.setName("seckill-mop-api");

        ReferenceConfig reference = new ReferenceConfig();
        reference.setApplication(application);
        reference.setRegistry(registryConfig);
        reference.setRetries(0);
        reference.setInterface(SeckillService.class);
        reference.setCheck(Boolean.valueOf(false));

        this.seckillService = ((SeckillService) reference.get());
    }

    public List<Action> load() {
        List actionList = new ArrayList();

        ServiceLoader<BaseAction> serviceLoader = ServiceLoader.load(BaseAction.class);
        for (BaseAction action : serviceLoader) {
            if (action != null) {
                action.setSeckillService(this.seckillService);
                actionList.add(action);
            }
        }
        return actionList;
    }

    private BaseAction loadAction(String actionClass) {
        try {
            Class baseActionClass = Class.forName(actionClass);
            BaseAction action = (BaseAction) baseActionClass.newInstance();
            action.setSeckillService(this.seckillService);
            return action;
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return null;
    }
}