package com.mockuai.seckillcenter.core.service.action.seckill;

import com.mockuai.seckillcenter.common.api.SeckillResponse;
import com.mockuai.seckillcenter.common.constant.ActionEnum;
import com.mockuai.seckillcenter.common.constant.ResponseCode;
import com.mockuai.seckillcenter.common.domain.dto.SeckillDTO;
import com.mockuai.seckillcenter.core.exception.SeckillException;
import com.mockuai.seckillcenter.core.manager.SeckillManager;
import com.mockuai.seckillcenter.core.manager.TradeManager;
import com.mockuai.seckillcenter.core.service.RequestContext;
import com.mockuai.seckillcenter.core.service.action.TransAction;
import com.mockuai.seckillcenter.core.util.SeckillPreconditions;
import com.mockuai.seckillcenter.core.util.SeckillUtils;
import com.mockuai.tradecenter.common.domain.OrderQTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by edgar.zr on 12/15/15.
 */
@Service
public class ValidateForSettlementAction extends TransAction {

    @Autowired
    private SeckillManager seckillManager;

    @Autowired
    private TradeManager tradeManager;

    @Override
    protected SeckillResponse doTransaction(RequestContext context) throws SeckillException {
        Long skuId = (Long) context.getRequest().getParam("skuId");
        Long sellerId = (Long) context.getRequest().getParam("sellerId");
        Long userId = (Long) context.getRequest().getParam("userId");
        String bizCode = (String) context.get("bizCode");
        String appKey = (String) context.get("appKey");

        SeckillPreconditions.checkNotNull(skuId, "skuId");
        SeckillPreconditions.checkNotNull(sellerId, "sellerId");
        SeckillPreconditions.checkNotNull(userId, "userId");

        SeckillDTO seckillDTO = seckillManager.getSeckillBySkuId(sellerId, skuId, bizCode, appKey);

        // 如果用户有预下单，则不会关注秒杀活动的状态，直接结算通过
        OrderQTO orderQTO = new OrderQTO();
        orderQTO.setUserId(userId);
        orderQTO.setItemId(seckillDTO.getItemId());
        orderQTO.setItemSkuId(seckillDTO.getSkuId());

        if (!tradeManager.queryPreOrder(orderQTO, appKey))
            return SeckillUtils.getFailResponse(ResponseCode.BIZ_E_SECKILL_WITHOUT_PRE_ORDER.getCode(), ResponseCode.BIZ_E_SECKILL_WITHOUT_PRE_ORDER.getMessage());

        // 秒杀结束，时间截止／库存为零
//        if (seckillDTO.getLifecycle().intValue() == 4)
//            return new SeckillResponse(ResponseCode.BIZ_E_SECKILL_ENDED);

        return new SeckillResponse(seckillDTO);
    }

    @Override
    public String getName() {
        return ActionEnum.VALIDATE_FOR_SETTLEMENT.getActionName();
    }
}