package com.mockuai.seckillcenter.common.constant;

public enum ParamEnum {

    SYS_APP_CODE("appCode"),

    SYS_APP_PASSWORD("appPwd"),

    SYS_ACTION("action");

    private String value;

    ParamEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}