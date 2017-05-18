package com.mockuai.seckillcenter.core.service.action.seckill;

import com.mockuai.seckillcenter.common.constant.ActionEnum;
import com.mockuai.seckillcenter.core.BaseActionTest;
import org.testng.annotations.Test;

/**
 * Created by edgar.zr on 12/13/15.
 */
public class InvalidateSeckillActionTest extends BaseActionTest {

    public InvalidateSeckillActionTest() {
        super(InvalidateSeckillActionTest.class.getName());
    }

    @Override
    protected String getCommand() {
        return ActionEnum.INVALIDATE_SECKILL.getActionName();
    }

    @Test
    public void test() {
        request.setParam("sellerId", 38699L);
        request.setParam("seckillId", 2L);
        doExecute();
    }
}