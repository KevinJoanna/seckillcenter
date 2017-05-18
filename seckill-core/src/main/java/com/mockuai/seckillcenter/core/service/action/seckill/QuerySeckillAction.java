package com.mockuai.seckillcenter.core.service.action.seckill;

import com.mockuai.seckillcenter.common.api.SeckillResponse;
import com.mockuai.seckillcenter.common.constant.ActionEnum;
import com.mockuai.seckillcenter.common.domain.dto.SeckillDTO;
import com.mockuai.seckillcenter.common.domain.qto.SeckillQTO;
import com.mockuai.seckillcenter.core.exception.SeckillException;
import com.mockuai.seckillcenter.core.manager.SeckillManager;
import com.mockuai.seckillcenter.core.service.RequestContext;
import com.mockuai.seckillcenter.core.service.action.TransAction;
import com.mockuai.seckillcenter.core.util.SeckillPreconditions;
import com.mockuai.seckillcenter.core.util.SeckillUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by edgar.zr on 12/4/15.
 */
@Service
public class QuerySeckillAction extends TransAction {

    @Autowired
    private SeckillManager seckillManager;

    @Override
    protected SeckillResponse doTransaction(RequestContext context) throws SeckillException {
        SeckillQTO seckillQTO = (SeckillQTO) context.getRequest().getParam("seckillQTO");
        String appKey = (String) context.get("appKey");
        String bizCode = (String) context.get("bizCode");

        SeckillPreconditions.checkNotNull(seckillQTO, "seckillQTO");
        seckillQTO.setBizCode(bizCode);

        List<SeckillDTO> seckillDTOs = seckillManager.querySeckill(seckillQTO, appKey);
        return SeckillUtils.getSuccessResponse(seckillDTOs, seckillQTO.getTotalCount());
    }

    @Override
    public String getName() {
        return ActionEnum.QUERY_SECKILL.getActionName();
    }
}