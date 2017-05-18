package com.mockuai.seckillcenter.common.constant;

/**
 * Created by edgar.zr on 12/4/15.
 */
public enum ActionEnum {

    ADD_SECKILL("addSeckill"),
    INVALIDATE_SECKILL("invalidateSeckill"),
    GET_SECKILL("getSeckill"),
    UPDATE_SECKILL("updateSeckill"),
    QUERY_SECKILL("querySeckill"),
    /**
     * 查询商品对应的秒杀活动
     */
    QUERY_SECKILL_BY_ITEM("querySeckillByItem"),

    /**
     * 验证用户结算
     */
    VALIDATE_FOR_SETTLEMENT("validateSeckillForSettlement"),

    /**
     * 确定秒杀
     */
    APPLY_SECKILL("applySeckill"),

    /**
     * 轮询
     */
    SECKILL_POLLING("seckillPolling"),

    /**
     * 批量查询活动信息
     */
    QUERY_SECKILL_BY_ITEM_BATCH("querySeckillByItemBatch");

    private String actionName;

    ActionEnum(String actionName) {
        this.actionName = actionName;
    }

    public static ActionEnum getActionEnum(String actionName) {
        for (ActionEnum ae : values()) {
            if (ae.actionName.equals(actionName)) {
                return ae;
            }
        }
        return null;
    }

    public String getActionName() {
        return this.actionName;
    }
}