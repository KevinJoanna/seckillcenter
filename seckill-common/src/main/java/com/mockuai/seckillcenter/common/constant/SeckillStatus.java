package com.mockuai.seckillcenter.common.constant;

/**
 * Created by edgar.zr on 12/4/15.
 */
public enum SeckillStatus {
    /**
     * 正常状态
     */
    NORMAL(1),
    /**
     * 失效状态
     */
    INVALID(2);

    private Integer value;

    SeckillStatus(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}