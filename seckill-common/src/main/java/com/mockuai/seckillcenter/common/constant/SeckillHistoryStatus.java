package com.mockuai.seckillcenter.common.constant;

/**
 * Created by edgar.zr on 12/15/15.
 */
public enum SeckillHistoryStatus {
    /**
     * 付款中
     */
    PAYING(1),
    /**
     * 已付款
     */
    PAY_DONE(2),
    /**
     * 已退单，最终状态
     */
    PAY_CANCEL(3),
    /**
     * 15 天后订单结束，最终状态
     */
    PAY_FINISH(4);

    private int value;

    SeckillHistoryStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}