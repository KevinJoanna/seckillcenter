package com.mockuai.seckillcenter.core.service.action.seckill;

import com.mockuai.seckillcenter.common.api.SeckillResponse;
import com.mockuai.seckillcenter.common.constant.ActionEnum;
import com.mockuai.seckillcenter.common.constant.ResponseCode;
import com.mockuai.seckillcenter.common.constant.SeckillHistoryStatus;
import com.mockuai.seckillcenter.common.domain.dto.SeckillDTO;
import com.mockuai.seckillcenter.common.domain.qto.SeckillHistoryQTO;
import com.mockuai.seckillcenter.core.exception.SeckillException;
import com.mockuai.seckillcenter.core.manager.SeckillManager;
import com.mockuai.seckillcenter.core.manager.TradeManager;
import com.mockuai.seckillcenter.core.service.RequestContext;
import com.mockuai.seckillcenter.core.service.action.TransAction;
import com.mockuai.seckillcenter.core.util.SeckillPreconditions;
import com.mockuai.seckillcenter.core.util.SeckillUtils;
import com.mockuai.tradecenter.common.domain.DataDTO;
import com.mockuai.tradecenter.common.domain.DataQTO;
import com.mockuai.tradecenter.common.domain.OrderDTO;
import com.mockuai.tradecenter.common.domain.OrderItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by edgar.zr on 12/15/15.
 */
@Service
public class ApplySeckillAction extends TransAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplySeckillAction.class);

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
        SeckillPreconditions.checkNotNull(userId, "userId");

        SeckillDTO seckillDTO = seckillManager.getSeckillBySkuId(sellerId, skuId, bizCode, appKey);

        if (seckillDTO.getLifecycle() == 1)
            return new SeckillResponse(ResponseCode.BIZ_E_SECKILL_NOT_START);
        if (seckillDTO.getLifecycle() == 3)
            return new SeckillResponse(ResponseCode.BIZ_E_SECKILL_STILL_HAVE_CHANCE);
        if (seckillDTO.getLifecycle() == 4)
            return new SeckillResponse(ResponseCode.BIZ_E_SECKILL_ENDED);

        SeckillHistoryQTO seckillHistoryQTO = new SeckillHistoryQTO();
        seckillHistoryQTO.setSeckillId(seckillDTO.getId());
        seckillHistoryQTO.setUserId(userId);
        seckillHistoryQTO.setBizCode(bizCode);
        seckillHistoryQTO.setNotStatus(SeckillHistoryStatus.PAY_CANCEL.getValue());


        try {
            DataQTO dataQTO = new DataQTO();
            dataQTO.setItemSkuId(seckillDTO.getSkuId());
            dataQTO.setUserId(userId);
            // 未支付订单
            dataQTO.setOrderStatus(10);
            dataQTO.setData_type(20);

            DataDTO dataDTO = tradeManager.getData(dataQTO, appKey);

            // 已付款 ＋ 未付款 的订单商品数量
            Long num = dataDTO.getItemCount().longValue() + dataDTO.getPaidItemCount().longValue();

            // 用户团购超过了限制
            if (seckillDTO.getLimit().longValue() != 0 && seckillDTO.getLimit().longValue() <= num)
                throw new SeckillException(ResponseCode.BIZ_E_USER_OUT_OF_LIMIT);

            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setUserId(userId);
            orderDTO.setSellerId(sellerId);

            orderDTO.setOrderItems(new ArrayList<OrderItemDTO>());
            orderDTO.getOrderItems().add(new OrderItemDTO());
            orderDTO.getOrderItems().get(0).setSellerId(sellerId);
            orderDTO.getOrderItems().get(0).setItemSkuId(skuId);
            orderDTO.getOrderItems().get(0).setNumber(1);
            orderDTO.setActivityId(seckillDTO.getId());
            orderDTO.setTimeoutCancelSeconds(900L);
//            orderDTO.setTimeoutCancelSeconds(300L);
            tradeManager.addPreOrder(orderDTO, appKey);
        } catch (SeckillException e) {
            LOGGER.debug("failed to apply seckill, seckillId : {}, sellerId : {}, skuId : {}, userId : {}, bizCode : {}",
                    seckillId, sellerId, skuId, userId, bizCode, e);
            return SeckillUtils.getFailResponse(e.getCode(), e.getMessage());
        }

        return SeckillUtils.getSuccessResponse();
    }

    @Override
    public String getName() {
        return ActionEnum.APPLY_SECKILL.getActionName();
    }
}