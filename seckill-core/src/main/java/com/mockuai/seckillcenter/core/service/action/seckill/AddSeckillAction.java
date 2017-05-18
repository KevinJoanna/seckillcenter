package com.mockuai.seckillcenter.core.service.action.seckill;

import com.mockuai.seckillcenter.common.api.SeckillResponse;
import com.mockuai.seckillcenter.common.constant.ActionEnum;
import com.mockuai.seckillcenter.common.constant.ResponseCode;
import com.mockuai.seckillcenter.common.domain.dto.SeckillDTO;
import com.mockuai.seckillcenter.core.exception.SeckillException;
import com.mockuai.seckillcenter.core.manager.SeckillManager;
import com.mockuai.seckillcenter.core.service.RequestContext;
import com.mockuai.seckillcenter.core.service.action.TransAction;
import com.mockuai.seckillcenter.core.util.SeckillPreconditions;
import com.mockuai.seckillcenter.core.util.SeckillUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by edgar.zr on 12/4/15.
 */
@Service
public class AddSeckillAction extends TransAction {

    @Autowired
    private SeckillManager seckillManager;

    @Override
    protected SeckillResponse doTransaction(RequestContext context) throws SeckillException {
        SeckillDTO seckillDTO = (SeckillDTO) context.getRequest().getParam("seckillDTO");
        String appKey = (String) context.get("appKey");
        String bizCode = (String) context.get("bizCode");
        SeckillPreconditions.checkNotNull(seckillDTO, "seckillDTO");

        seckillDTO.setBizCode(bizCode);
        SeckillPreconditions.checkNotNull(seckillDTO.getSellerId(), "sellerId");
        SeckillPreconditions.checkNotNull(seckillDTO.getItemId(), "itemId");
        SeckillPreconditions.checkNotNull(seckillDTO.getSkuId(), "skuId");
        SeckillPreconditions.checkNotNull(seckillDTO.getStartTime(), "startTime");
        SeckillPreconditions.checkNotNull(seckillDTO.getEndTime(), "endTime");
        SeckillPreconditions.checkNotNull(seckillDTO.getSeckillItem(), "seckillItem");
        if (seckillDTO.getEndTime().before(seckillDTO.getStartTime())) {
            return new SeckillResponse(ResponseCode.PARAMETER_ERROR, "活动时间不合法");
        }

        seckillManager.addSeckill(seckillDTO, appKey);
        return SeckillUtils.getSuccessResponse(seckillDTO.getId());
    }

    @Override
    public String getName() {
        return ActionEnum.ADD_SECKILL.getActionName();
    }
}