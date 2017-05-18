package com.mockuai.seckillcenter.core.service.action;

import com.mockuai.seckillcenter.common.api.SeckillResponse;
import com.mockuai.seckillcenter.common.constant.ResponseCode;
import com.mockuai.seckillcenter.core.exception.SeckillException;
import com.mockuai.seckillcenter.core.service.RequestContext;
import com.mockuai.seckillcenter.core.util.JsonUtil;
import com.mockuai.seckillcenter.core.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by edgar.zr on 12/29/15.
 */
public abstract class BaseAction<T> implements Action {

    public static final Logger LOGGER = LoggerFactory.getLogger(BaseAction.class);

    @Override
    public SeckillResponse run(RequestContext context) throws SeckillException {
        try {
            SeckillResponse seckillBuyResponse = execute(context);
            if (seckillBuyResponse.isSuccess() == false) {
                LOGGER.error("Action [{}] is unsuccess, {}",
                        context.getRequest().getCommand(), seckillBuyResponse.getMessage());
            }
            LOGGER.debug("------------------------------------------------------------------");
            LOGGER.debug("{}, {}", context.getRequest().getCommand(), JsonUtil.toJson(seckillBuyResponse));
            LOGGER.debug("------------------------------------------------------------------");
            return seckillBuyResponse;
        } catch (SeckillException e) {
            LOGGER.error("Action [{}] is unsuccess, {}",
                    context.getRequest().getCommand(), e.getMessage());
            return ResponseUtil.getResponse(e.getCode(), e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Action [{}] is unsuccess, {}",
                    context.getRequest().getCommand(), e.getMessage());
            return ResponseUtil.getResponse(ResponseCode.SERVICE_EXCEPTION);
        }
    }

    public abstract SeckillResponse execute(final RequestContext context) throws SeckillException;
}