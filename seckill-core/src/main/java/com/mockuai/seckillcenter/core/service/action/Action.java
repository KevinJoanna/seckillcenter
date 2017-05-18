package com.mockuai.seckillcenter.core.service.action;

import com.mockuai.seckillcenter.common.api.SeckillResponse;
import com.mockuai.seckillcenter.core.exception.SeckillException;
import com.mockuai.seckillcenter.core.service.RequestContext;
import org.springframework.stereotype.Service;

/**
 * 操作对像基类
 *
 * @author wujin.zzq
 */
@Service
public interface Action<T> {

    SeckillResponse run(RequestContext<T> context) throws SeckillException;

    String getName();
}
