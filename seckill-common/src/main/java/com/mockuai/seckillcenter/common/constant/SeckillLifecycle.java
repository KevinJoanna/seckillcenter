package com.mockuai.seckillcenter.common.constant;

/**
 * Created by edgar.zr on 12/4/15.
 */
public enum SeckillLifecycle {
    /**
     * 未开始
     */
    LIFECYCLE_NOT_BEGIN(1),
    /**
     * 进行中
     */
    LIFECYCLE_IN_PROGRESS(2),
    /**
     * 已结束
     */
    LIFECYCLE_ENDED(3);

    private int value;

    SeckillLifecycle(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}