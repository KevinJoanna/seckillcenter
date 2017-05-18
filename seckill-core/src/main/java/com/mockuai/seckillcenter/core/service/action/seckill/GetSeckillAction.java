package com.mockuai.seckillcenter.core.service.action.seckill;

import com.mockuai.seckillcenter.common.api.SeckillResponse;
import com.mockuai.seckillcenter.common.constant.ActionEnum;
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
public class GetSeckillAction extends TransAction {

    @Autowired
    private SeckillManager seckillManager;

    @Override
    protected SeckillResponse doTransaction(RequestContext context) throws SeckillException {
        Long seckillId = (Long) context.getRequest().getParam("seckillId");
        Long sellerId = (Long) context.getRequest().getParam("sellerId");
        String bizCode = (String) context.get("bizCode");
        String appKey = (String) context.get("appKey");

        SeckillPreconditions.checkNotNull(seckillId, "seckillId");
        SeckillPreconditions.checkNotNull(sellerId, "sellerId");

        SeckillDTO seckillDTO = seckillManager.getSeckill(seckillId, sellerId, bizCode, appKey);
        return SeckillUtils.getSuccessResponse(seckillDTO);
    }

    @Override
    public String getName() {
        return ActionEnum.GET_SECKILL.getActionName();
    }
}