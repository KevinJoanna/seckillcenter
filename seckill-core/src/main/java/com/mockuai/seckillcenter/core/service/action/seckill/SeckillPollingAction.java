package com.mockuai.seckillcenter.core.service.action.seckill;

import com.mockuai.seckillcenter.common.api.SeckillResponse;
import com.mockuai.seckillcenter.common.constant.ActionEnum;
import com.mockuai.seckillcenter.common.constant.ResponseCode;
import com.mockuai.seckillcenter.common.domain.dto.SeckillDTO;
import com.mockuai.seckillcenter.common.domain.dto.SeckillForMopDTO;
import com.mockuai.seckillcenter.core.exception.SeckillException;
import com.mockuai.seckillcenter.core.manager.SeckillManager;
import com.mockuai.seckillcenter.core.manager.TradeManager;
import com.mockuai.seckillcenter.core.service.RequestContext;
import com.mockuai.seckillcenter.core.service.action.TransAction;
import com.mockuai.seckillcenter.core.util.DateUtils;
import com.mockuai.seckillcenter.core.util.SeckillPreconditions;
import com.mockuai.seckillcenter.core.util.SeckillUtils;
import com.mockuai.tradecenter.common.domain.OrderQTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by edgar.zr on 12/15/15.
 */
@Service
public class SeckillPollingAction extends TransAction {

    @Autowired
    private SeckillManager seckillManager;

    @Autowired
    private TradeManager tradeManager;

    @Override
    protected SeckillResponse doTransaction(RequestContext context) throws SeckillException {

        Long seckillId = (Long) context.getRequest().getParam("seckillId");
        Long sellerId = (Long) context.getRequest().getParam("sellerId");
        Long skuId = (Long) context.getRequest().getParam("skuId");
        Long userId = (Long) context.getRequest().getParam("userId");

        String bizCode = (String) context.get("bizCode");
        String appKey = (String) context.get("appKey");

        SeckillPreconditions.checkNotNull(seckillId, "seckillId");
        SeckillPreconditions.checkNotNull(sellerId, "sellerId");
        SeckillPreconditions.checkNotNull(skuId, "skuId");

        SeckillForMopDTO seckillForMopDTO = new SeckillForMopDTO();

        SeckillDTO seckillDTO = seckillManager.getSeckillBySkuId(sellerId, skuId, bizCode, appKey);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("timeInterval", "999999");

        if (seckillDTO.getId().longValue() != seckillId)
            return new SeckillResponse(ResponseCode.BIZ_E_SECKILL_NOT_EXIST);

        seckillForMopDTO.setLifecycle(seckillDTO.getLifecycle());
        seckillForMopDTO.setStartTime(seckillDTO.getStartTime().getTime());
        seckillForMopDTO.setEndTime(seckillDTO.getEndTime().getTime());
        seckillForMopDTO.setSales(seckillDTO.getSales());
        seckillForMopDTO.setSeckillUid(seckillDTO.getSellerId() + "_" + seckillDTO.getId());
        seckillForMopDTO.setStockNum(seckillDTO.getSeckillItem().getStockNum());

        if (userId != null) {
            OrderQTO orderQTO = new OrderQTO();
            orderQTO.setUserId(userId);
            orderQTO.setItemId(seckillDTO.getItemId());
            orderQTO.setItemSkuId(seckillDTO.getSkuId());

            // 如果用户有预下单则继续之前的结算流程，无论活动的状态
            if (tradeManager.queryPreOrder(orderQTO, appKey))
                seckillForMopDTO.setLifecycle(11);
        }

        result.put("seckillDTO", seckillForMopDTO);
        result.put("currentTime", DateUtils.getCurrentDate().getTime());
        result.put("timeInterval", "5000");

        return SeckillUtils.getSuccessResponse(result);
    }

    @Override
    public String getName() {
        return ActionEnum.SECKILL_POLLING.getActionName();
    }
}