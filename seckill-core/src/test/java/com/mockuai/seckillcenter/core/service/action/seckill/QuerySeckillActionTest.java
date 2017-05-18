package com.mockuai.seckillcenter.core.service.action.seckill;

import com.mockuai.seckillcenter.common.constant.ActionEnum;
import com.mockuai.seckillcenter.common.domain.qto.SeckillQTO;
import com.mockuai.seckillcenter.core.BaseActionTest;
import org.testng.annotations.Test;

/**
 * Created by edgar.zr on 12/13/15.
 */
public class QuerySeckillActionTest extends BaseActionTest {

    public QuerySeckillActionTest() {
        super(QuerySeckillActionTest.class.getName());
    }

    @Override
    protected String getCommand() {
        return ActionEnum.QUERY_SECKILL.getActionName();
    }

    @Test
    public void test() {

        SeckillQTO seckillQTO = new SeckillQTO();
        seckillQTO.setLifecycle(0);
        seckillQTO.setOffset(0);
        seckillQTO.setCount(100);
        request.setParam("seckillQTO", seckillQTO);
        doExecute();

    }
}