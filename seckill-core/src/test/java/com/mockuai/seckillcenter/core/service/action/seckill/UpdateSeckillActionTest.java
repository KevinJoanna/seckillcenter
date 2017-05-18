package com.mockuai.seckillcenter.core.service.action.seckill;

import com.mockuai.seckillcenter.common.constant.ActionEnum;
import com.mockuai.seckillcenter.core.BaseActionTest;
import com.mockuai.seckillcenter.core.domain.SeckillDO;
import org.testng.annotations.Test;

import java.util.Date;

/**
 * Created by edgar.zr on 12/25/15.
 */
public class UpdateSeckillActionTest extends BaseActionTest {

    public UpdateSeckillActionTest() {
        super(UpdateSeckillActionTest.class.getName());
    }

    @Override
    protected String getCommand() {
        return ActionEnum.UPDATE_SECKILL.getActionName();
    }

    @Test
    public void test() {
        SeckillDO seckillDO = new SeckillDO();
        seckillDO.setId(117L);
        seckillDO.setBizCode("mockuai_demo");
        seckillDO.setSellerId(38699L);
        seckillDO.setItemInvalidTime(new Date());
        request.setParam("seckillDO", seckillDO);
        doExecute();
    }
}