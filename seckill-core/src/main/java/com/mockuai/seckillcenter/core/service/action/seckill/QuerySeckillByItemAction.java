package com.mockuai.seckillcenter.core.service.action.seckill;

import com.mockuai.seckillcenter.common.api.SeckillResponse;
import com.mockuai.seckillcenter.common.constant.ActionEnum;
import com.mockuai.seckillcenter.common.domain.dto.SeckillDTO;
import com.mockuai.seckillcenter.common.domain.dto.SeckillForMopDTO;
import com.mockuai.seckillcenter.core.exception.SeckillException;
import com.mockuai.seckillcenter.core.manager.SeckillManager;
import com.mockuai.seckillcenter.core.manager.TradeManager;
import com.mockuai.seckillcenter.core.service.RequestContext;
import com.mockuai.seckillcenter.core.service.action.TransAction;
import com.mockuai.seckillcenter.core.util.ModelUtil;
import com.mockuai.seckillcenter.core.util.SeckillPreconditions;
import com.mockuai.tradecenter.common.domain.OrderQTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by edgar.zr on 12/14/15.
 */
@Service
public class QuerySeckillByItemAction extends TransAction {

    @Autowired
    private TradeManager tradeManager;

    @Autowired
    private SeckillManager seckillManager;

    @Override
    protected SeckillResponse doTransaction(RequestContext context) throws SeckillException {
        Long skuId = (Long) context.getRequest().getParam("skuId");
        Long sellerId = (Long) context.getRequest().getParam("sellerId");
        Long userId = (Long) context.getRequest().getParam("userId");
        String bizCode = (String) context.get("bizCode");
        String appKey = (String) context.get("appKey");

        SeckillPreconditions.checkNotNull(skuId, "skuId");
        SeckillPreconditions.checkNotNull(sellerId, "sellerId");

        SeckillDTO seckillDTO = seckillManager.getSeckillBySkuId(sellerId, skuId, bizCode, appKey);

        SeckillForMopDTO seckillForMopDTO = ModelUtil.genSeckillForMopDTO(seckillDTO);

        if (userId != null) {
            OrderQTO orderQTO = new OrderQTO();
            orderQTO.setUserId(userId);
            orderQTO.setItemId(seckillDTO.getItemId());
            orderQTO.setItemSkuId(seckillDTO.getSkuId());

            // 如果用户有预下单，继续之前的结算流程，无论活动的状态
            if (tradeManager.queryPreOrder(orderQTO, appKey))
                seckillForMopDTO.setLifecycle(11);
        }

        return new SeckillResponse(seckillForMopDTO);
    }

    @Override
    public String getName() {
        return ActionEnum.QUERY_SECKILL_BY_ITEM.getActionName();
    }
}