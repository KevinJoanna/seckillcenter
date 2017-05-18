package com.mockuai.seckillcenter.core.exception;

import com.mockuai.seckillcenter.common.constant.ResponseCode;

public class SeckillException extends Exception {

    private static final long serialVersionUID = -2453984659537756900L;
    private int code;

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(Throwable cause) {
        super(cause);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }

    public SeckillException(int code, String message) {
        super(message);
        this.code = code;
    }

    public SeckillException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.code = responseCode.getCode();
    }

    public SeckillException(ResponseCode responseCode, String message) {
        super(message);
        this.code = responseCode.getCode();
    }

    public int getCode() {
        return this.code;
    }
}