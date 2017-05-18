package com.mockuai.seckillcenter.mop.api.action;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.mockuai.mop.common.constant.ActionAuthLevel;
import com.mockuai.mop.common.constant.HttpMethodLimit;
import com.mockuai.mop.common.constant.MopRespCode;
import com.mockuai.mop.common.service.action.MopResponse;
import com.mockuai.seckillcenter.common.api.BaseRequest;
import com.mockuai.seckillcenter.common.api.Request;
import com.mockuai.seckillcenter.common.api.Response;
import com.mockuai.seckillcenter.common.constant.ActionEnum;
import com.mockuai.seckillcenter.mop.api.domain.SeckillUidDTO;
import com.mockuai.seckillcenter.mop.api.domain.SkuUidDTO;
import com.mockuai.seckillcenter.mop.api.util.MopApiUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by edgar.zr on 12/15/15.
 */
public class SeckillPolling extends BaseAction {

    public MopResponse execute(com.mockuai.mop.common.service.action.Request request) {

        String seckillUid = (String) request.getParam("seckill_uid");
        String skuUid = (String) request.getParam("sku_uid");
        Long userId = (Long) request.getAttribute("user_id");
        String appKey = (String) request.getParam("app_key");

        if (StringUtils.isBlank(seckillUid)) {
            return new MopResponse(MopRespCode.P_E_PARAM_ISNULL, "seckill_uid is null");
        }
        if (StringUtils.isBlank(skuUid)) {
            return new MopResponse(MopRespCode.P_E_PARAM_ISNULL, "sku_uid is null");
        }

        Long seckillId = null;
        Long skuId = null;
        Long sellerId = null;

        try {
            SkuUidDTO skuUidDTO = MopApiUtil.parseSkuUid(skuUid);
            if (skuUidDTO != null) {
                skuId = skuUidDTO.getSkuId();
                sellerId = skuUidDTO.getSellerId();
            }
            SeckillUidDTO seckillUidDTO = MopApiUtil.parseSeckillUid(seckillUid);
            if (seckillUidDTO != null) {
                seckillId = seckillUidDTO.getSeckillId();
            }
        } catch (Exception e) {
            return new MopResponse(MopRespCode.P_E_PARAM_FORMAT_INVALID, "sku_uid/seckill_uid format is invalid");
        }

        Request marketReq = new BaseRequest();
        marketReq.setCommand(ActionEnum.SECKILL_POLLING.getActionName());
        marketReq.setParam("seckillId", seckillId);
        marketReq.setParam("sellerId", sellerId);
        marketReq.setParam("skuId", skuId);
        marketReq.setParam("userId", userId);
        marketReq.setParam("appKey", appKey);
        Response<Map<String, Object>> marketResp = getSeckillService().execute(marketReq);
        MopResponse response = null;
        if (marketResp.isSuccess()) {
            Map<String, Object> result = marketResp.getModule();
            Map<String, Object> data = new HashMap<String, Object>();

            data.put("seckill", result.get("seckillDTO"));
            data.put("current_time", result.get("currentTime"));
            data.put("time_interval", result.get("timeInterval"));
            response = new MopResponse(data);
        } else {
            response = new MopResponse(marketResp.getResCode(), marketResp.getMessage());
        }

        return response;
    }

    public String getName() {
        return "/seckill/status";
    }

    public ActionAuthLevel getAuthLevel() {
        return ActionAuthLevel.NO_AUTH;
    }

    public HttpMethodLimit getMethodLimit() {
        return HttpMethodLimit.ONLY_GET;
    }
}