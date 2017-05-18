package com.mockuai.seckillcenter.core.service.action.seckill;

import com.mockuai.seckillcenter.common.constant.ActionEnum;
import com.mockuai.seckillcenter.common.domain.dto.SeckillDTO;
import com.mockuai.seckillcenter.common.domain.dto.SeckillItemDTO;
import com.mockuai.seckillcenter.core.BaseActionTest;
import org.apache.commons.lang3.time.DateUtils;
import org.testng.annotations.Test;

import java.util.Date;

/**
 * Created by edgar.zr on 12/13/15.
 */
public class AddSeckillActionTest extends BaseActionTest {

    public AddSeckillActionTest() {
        super(AddSeckillActionTest.class.getName());
    }

    @Override
    protected String getCommand() {
        return ActionEnum.ADD_SECKILL.getActionName();
    }

    @Test
    public void test() {

        SeckillDTO seckillDTO = new SeckillDTO();
        seckillDTO.setSellerId(38699L);
        seckillDTO.setSkuId(66213L);
        seckillDTO.setItemId(163716L);
        seckillDTO.setLimit(1);
        seckillDTO.setPrice(100L);
        seckillDTO.setStartTime(DateUtils.addDays(new Date(), 1));
        seckillDTO.setEndTime(DateUtils.addDays(new Date(), 2));
        seckillDTO.setSeckillItem(new SeckillItemDTO());
        seckillDTO.getSeckillItem().setSkuId(66213L);
        seckillDTO.getSeckillItem().setStockNum(10L);
        request.setParam("seckillDTO", seckillDTO);

        doExecute();
    }
}