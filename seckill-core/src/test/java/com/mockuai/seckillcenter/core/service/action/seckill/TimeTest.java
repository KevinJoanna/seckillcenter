package com.mockuai.seckillcenter.core.service.action.seckill;

import com.mockuai.seckillcenter.core.BaseTest;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by edgar.zr on 12/15/15.
 */
public class TimeTest extends BaseTest {

    @Test
    public void test() {
        try {
            TimeUnit.DAYS.sleep(10L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}