package com.mockuai.seckillcenter.mop.api.domain;

/**
 * Created by zengzhangqiang on 5/31/15.
 */
public class SeckillUidDTO {
    private Long seckillId;
    private Long sellerId;

    public Long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Long seckillId) {
        this.seckillId = seckillId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }
}