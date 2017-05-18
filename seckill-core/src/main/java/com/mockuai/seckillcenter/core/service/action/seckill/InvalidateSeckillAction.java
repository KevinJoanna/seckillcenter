package com.mockuai.seckillcenter.core.service.action.seckill;

import com.mockuai.seckillcenter.common.api.SeckillResponse;
import com.mockuai.seckillcenter.common.constant.ActionEnum;
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
public class InvalidateSeckillAction extends TransAction {

    @Autowired
    private SeckillManager seckillManager;

    @Override
    protected SeckillResponse doTransaction(RequestContext context) throws SeckillException {
        Long seckillId = (Long) context.getRequest().getParam("seckillId");
        Long sellerId = (Long) context.getRequest().getParam("sellerId");
        String bizCode = (String) context.get("bizCode");

        SeckillPreconditions.checkNotNull(seckillId, "sellerId");
        SeckillPreconditions.checkNotNull(sellerId, "sellerId");

        seckillManager.invalidSeckill(seckillId, sellerId, bizCode);

        return SeckillUtils.getSuccessResponse();
    }

    @Override
    public String getName() {
        return ActionEnum.INVALIDATE_SECKILL.getActionName();
    }
}