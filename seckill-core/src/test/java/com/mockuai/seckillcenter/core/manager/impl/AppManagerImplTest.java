package com.mockuai.seckillcenter.core.manager.impl;

import com.mockuai.appcenter.common.domain.AppInfoDTO;
import com.mockuai.seckillcenter.core.BaseTest;
import com.mockuai.seckillcenter.core.exception.SeckillException;
import com.mockuai.seckillcenter.core.manager.AppManager;
import com.mockuai.seckillcenter.core.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Created by edgar.zr on 12/4/15.
 */
public class AppManagerImplTest extends BaseTest {

    @Autowired
    private AppManager appManager;

    @Test
    public void test() {
        try {
            AppInfoDTO appInfoDTO = appManager.getAppInfo("5b036edd2fe8730db1983368a122fb45");
            System.err.println(JsonUtil.toJson(appInfoDTO));
        } catch (SeckillException e) {
            e.printStackTrace();
        }
    }
}