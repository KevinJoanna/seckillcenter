package com.mockuai.seckillcenter.core.service.action.seckill;

import com.mockuai.seckillcenter.common.api.SeckillResponse;
import com.mockuai.seckillcenter.common.constant.ActionEnum;
import com.mockuai.seckillcenter.core.domain.SeckillDO;
import com.mockuai.seckillcenter.core.exception.SeckillException;
import com.mockuai.seckillcenter.core.manager.SeckillManager;
import com.mockuai.seckillcenter.core.service.RequestContext;
import com.mockuai.seckillcenter.core.service.action.TransAction;
import com.mockuai.seckillcenter.core.util.SeckillUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by edgar.zr on 12/25/15.
 */
@Service
public class UpdateSeckillAction extends TransAction {

    @Autowired
    private SeckillManager seckillManager;

    @Override
    protected SeckillResponse doTransaction(RequestContext context) throws SeckillException {

        SeckillDO seckillDO = (SeckillDO) context.getRequest().getParam("seckillDO");

        seckillManager.updateSeckill(seckillDO);

        return SeckillUtils.getSuccessResponse();
    }

    @Override
    public String getName() {
        return ActionEnum.UPDATE_SECKILL.getActionName();
    }
}