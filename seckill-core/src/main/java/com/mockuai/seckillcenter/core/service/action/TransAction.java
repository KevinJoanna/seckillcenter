package com.mockuai.seckillcenter.core.service.action;

import com.mockuai.seckillcenter.common.api.SeckillResponse;
import com.mockuai.seckillcenter.core.exception.SeckillException;
import com.mockuai.seckillcenter.core.service.RequestContext;
import com.mockuai.seckillcenter.core.util.ResponseUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

@Service
public abstract class TransAction extends BaseAction {

//    private static final Logger log = LoggerFactory.getLogger(TransAction.class);

    @Resource
    TransactionTemplate transactionTemplate;

    protected abstract SeckillResponse doTransaction(RequestContext context)
            throws SeckillException;

    public SeckillResponse execute(final RequestContext context) throws SeckillException {

        return (SeckillResponse) this.transactionTemplate.execute(new TransactionCallback() {

            public Object doInTransaction(TransactionStatus status) {
                try {
                    SeckillResponse marketingResponse = TransAction.this.doTransaction(context);
                    if (marketingResponse.isSuccess() == false) {
//                        log.error("Action [{}] is unsuccess, {}",
//                                context.getRequest().getCommand(), marketingResponse.getMessage());
                        status.setRollbackOnly();
                    }
//                    log.debug("-------------------------------------------------------------");
//                    log.debug("{}, {}", context.getRequest().getCommand(), JsonUtil.toJson(marketingResponse));
//                    log.debug("-------------------------------------------------------------");
                    return marketingResponse;
                } catch (SeckillException e) {
//                    log.error(e.getMessage(), e);
                    status.setRollbackOnly();
                    return ResponseUtil.getResponse(e.getCode(), e.getMessage());
                }
            }

        });
    }
}