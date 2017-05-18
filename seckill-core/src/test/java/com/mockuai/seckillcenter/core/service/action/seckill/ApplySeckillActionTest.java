package com.mockuai.seckillcenter.core.service.action.seckill;

import com.mockuai.seckillcenter.common.constant.ActionEnum;
import com.mockuai.seckillcenter.core.BaseActionTest;
import org.testng.annotations.Test;

/**
 * Created by edgar.zr on 12/15/15.
 */
public class ApplySeckillActionTest extends BaseActionTest {

    public ApplySeckillActionTest() {
        super(ApplySeckillActionTest.class.getName());
    }

    @Override
    protected String getCommand() {
        return ActionEnum.APPLY_SECKILL.getActionName();
    }

    @Test
    public void test() {

        Long seckillId = 9L;
        Long sellerId = 38699L;
        Long skuId = 1039L;
        Long userId = 22L;

        request.setParam("seckillId", seckillId);
        request.setParam("sellerId", sellerId);
        request.setParam("skuId", skuId);
        request.setParam("userId", userId);

//        try {
//            Thread.sleep(1000000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        doExecute();
    }
}