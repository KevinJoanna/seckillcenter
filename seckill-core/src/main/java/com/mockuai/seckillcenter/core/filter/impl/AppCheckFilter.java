package com.mockuai.seckillcenter.core.filter.impl;

import com.mockuai.appcenter.common.domain.AppInfoDTO;
import com.mockuai.seckillcenter.core.exception.SeckillException;
import com.mockuai.seckillcenter.core.filter.Filter;
import com.mockuai.seckillcenter.core.manager.AppManager;
import com.mockuai.seckillcenter.core.service.RequestContext;
import com.mockuai.seckillcenter.common.api.SeckillResponse;
import com.mockuai.seckillcenter.common.constant.ResponseCode;
import org.apache.commons.lang.StringUtils;

public class AppCheckFilter implements Filter {

    @Override
    public boolean isAccept(RequestContext ctx) {
        return true;
    }

    @Override
    public SeckillResponse before(RequestContext ctx) throws SeckillException {
        String appKey = (String) ctx.getRequest().getParam("appKey");
        if (StringUtils.isBlank(appKey)) {
            return new SeckillResponse(ResponseCode.PARAMETER_NULL, "appKey is null");
        }

        AppManager appManager = (AppManager) ctx.getAppContext().getBean("appManager");
        AppInfoDTO appInfoDTO = appManager.getAppInfo(appKey);
        if (appInfoDTO == null) {
            return new SeckillResponse(ResponseCode.BIZ_E_APP_NOT_EXIST, "appKey is invalid");
        }

        //将app所属的bizCode塞到context中
        ctx.put("bizCode", appInfoDTO.getBizCode());
        ctx.put("appKey", appInfoDTO.getAppKey());

        return new SeckillResponse(ResponseCode.SUCCESS);
    }

    @Override
    public SeckillResponse after(RequestContext ctx) throws SeckillException {
        return new SeckillResponse(ResponseCode.SUCCESS);
    }
}