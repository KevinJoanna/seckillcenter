package com.mockuai.seckillcenter.mop.api.action;

import com.mockuai.seckillcenter.common.api.SeckillService;
import com.mockuai.mop.common.constant.ResponseFormat;
import com.mockuai.mop.common.service.action.Action;

public abstract class BaseAction implements Action {
    private SeckillService seckillService;

    public SeckillService getSeckillService() {
        return seckillService;
    }

    public void setSeckillService(SeckillService seckillService) {
        this.seckillService = seckillService;
    }

    public ResponseFormat getResponseFormat() {
        return ResponseFormat.STANDARD;
    }
}