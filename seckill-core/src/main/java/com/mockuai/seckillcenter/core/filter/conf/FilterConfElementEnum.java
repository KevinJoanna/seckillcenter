package com.mockuai.seckillcenter.core.filter.conf;

public enum FilterConfElementEnum {
    ELEMENT_FILTERS("filters"),
    ELEMENT_COMMAND("command"),
    ELEMENT_CHAIN("chain"),
    ELEMENT_ATTRBUTE_NAME("name"),
    ELEMENT_ATTR_INTERFACE("interface"),
    ELEMENT_ATTR_IMPL_CLASS("implClass"),
    ELEMENT_ATTR_METHOD("methodName"),
    ELEMENT_ATTR_FACTORY("factory"),
    ELEMENT_PARAMETER("param"),
    ELEMENT_ATTR_EXCLUSION("exclusion"),
    ELEMENT_EXTENTION("extends"),
    ELEMENT_REQUIRED_CHAIN("required");

    private String value;

    FilterConfElementEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}