package com.mockuai.seckillcenter.common.constant;

public enum ResponseCode {

    SUCCESS(10000, "success"),

    //参数错误
    PARAMETER_NULL(20001, "parameter is null"),
    PARAMETER_ERROR(20002, "parameter is error"),
    PARAMETER_MISSING(20003, "some required parameter is missing"),


    //业务异常
    DELETE_ERROR(30001, "delete error"),
    UPDATE_ERROR(30002, "update error"),

    BIZ_E_INVALIDATE_TARGET_ITEM(31001, "秒杀目标商品不合法"),
    BIZ_E_ADD_SECKILL(31002, "创建秒杀失败"),
    BIZ_E_SECKILL_NOT_EXIST(31003, "秒杀不存在"),
    BIZ_E_SKU_ALREADY_EXISTS(31004, "该商品已参加了秒杀"),
    BIZ_E_SECKILL_ENDED(31005, "秒杀活动已经结束"),
    BIZ_E_SECKILL_WITHOUT_PRE_ORDER(31006, "无秒杀结算资格"),
    BIZ_E_SECKILL_NOT_START(31007, "秒杀未开始"),
    BIZ_E_SECKILL_STILL_HAVE_CHANCE(31008, "还有机会"),
    BIZ_E_SECKILL_FAIL_TO_PRE_ORDER(31009, "预下单失败"),
    BIZ_E_SECKILL_PRE_ORDER_ALREADY(31010, "已经秒杀过"),
    BIZ_E_USER_OUT_OF_LIMIT(31011, "用户秒杀数量已达到"),

    /**
     * 指定的app不存在
     */
    BIZ_E_APP_NOT_EXIST(31015, "the specified app is not exist"),

    /**
     * 系统异常
     */
    SERVICE_EXCEPTION(40002, "server exception"),
    REQUEST_FORBIDDEN(40003, "request forbidden"),
    ACTION_NO_EXIST(40004, "action not exist"),
    DB_OP_ERROR(40005, "database operation error"),
    DB_OP_ERROR_OF_DUPLICATE_ENTRY(41001, "duplicate entry for specified key"),;


    private int code;
    private String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public int getCode() {
        return this.code;
    }
}