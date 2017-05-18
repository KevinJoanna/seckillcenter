package com.mockuai.seckillcenter.core.manager.impl;

import com.mockuai.seckillcenter.core.BaseTest;
import com.mockuai.seckillcenter.core.manager.SeckillManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Created by edgar.zr on 12/4/15.
 */
public class SeckillManagerImplTest extends BaseTest {

    @Autowired
    private SeckillManager seckillManager;

    @Test
    public void testGetSeckill() throws Exception {
        seckillManager.getSeckill(1L, 38699L, "mockuai_demo", "");
    }
}