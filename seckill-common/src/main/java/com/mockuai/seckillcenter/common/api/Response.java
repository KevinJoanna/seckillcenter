package com.mockuai.seckillcenter.common.api;

import java.io.Serializable;

public abstract interface Response<T> extends Serializable {

    public abstract T getModule();

    public abstract int getResCode();

    public abstract String getMessage();

    public abstract long getTotalCount();

    public abstract boolean isSuccess();
}