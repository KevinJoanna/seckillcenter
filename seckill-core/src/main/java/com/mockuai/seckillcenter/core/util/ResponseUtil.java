package com.mockuai.seckillcenter.core.util;

import com.mockuai.seckillcenter.common.api.SeckillResponse;
import com.mockuai.seckillcenter.common.constant.ResponseCode;

import java.util.List;

public class ResponseUtil {

    public static SeckillResponse getResponse(int code, String message) {

        SeckillResponse response = new SeckillResponse(code, message);

        return response;
    }

    public static SeckillResponse getResponse(ResponseCode retCodeEnum) {

        SeckillResponse response = new SeckillResponse(retCodeEnum);

        return response;
    }

    public static SeckillResponse getResponse(Object model) {

        SeckillResponse response = new SeckillResponse(model);

        return response;
    }

    public static SeckillResponse getResponse(List modelList, long totalCount) {

        SeckillResponse response = new SeckillResponse(modelList, totalCount);


        return response;
    }
}