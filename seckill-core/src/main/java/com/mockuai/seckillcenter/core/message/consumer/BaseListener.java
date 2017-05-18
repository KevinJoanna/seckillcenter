package com.mockuai.seckillcenter.core.message.consumer;

import com.alibaba.fastjson.JSONObject;
import com.mockuai.seckillcenter.common.api.BaseRequest;
import com.mockuai.seckillcenter.common.api.Request;
import com.mockuai.seckillcenter.common.api.Response;
import com.mockuai.seckillcenter.common.api.SeckillService;
import com.mockuai.seckillcenter.common.constant.ActionEnum;
import com.mockuai.seckillcenter.common.domain.qto.SeckillQTO;
import com.mockuai.seckillcenter.core.domain.SeckillDO;
import com.mockuai.seckillcenter.core.exception.SeckillException;
import com.mockuai.seckillcenter.core.manager.SeckillManager;
import com.mockuai.seckillcenter.core.util.JsonUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by edgar.zr on 12/15/15.
 */
public abstract class BaseListener implements Listener {

    @Autowired
    protected SeckillService seckillService;

    @Autowired
    protected SeckillManager seckillManager;

    @Override
    public String execute(JSONObject msg, String appKey) throws SeckillException {
        consumeMessage(msg, appKey);
        return null;
    }

    public abstract void consumeMessage(JSONObject msg, String appKey) throws SeckillException;

    public void updateSeckill(SeckillDO toUpdateSeckillDO, String appKey) throws SeckillException {

        Request request = new BaseRequest();
        request.setParam("seckillDO", toUpdateSeckillDO);
        request.setParam("appKey", appKey);
        request.setCommand(ActionEnum.UPDATE_SECKILL.getActionName());
        Response<Boolean> response = seckillService.execute(request);
        if (response.isSuccess()) {
            getLogger().info("update seckill success when item invalid, seckillDO : {}",
                    JsonUtil.toJson(toUpdateSeckillDO));
        } else {

            getLogger().info("update seckill failed when item invalid, seckillDO : {}, errCode : {}, errMsg : {}",
                    JsonUtil.toJson(toUpdateSeckillDO), response.getResCode(), response.getMessage());
        }
    }

    public List<SeckillDO> getSeckills(String bizCode) throws SeckillException {

        SeckillQTO seckillQTO = new SeckillQTO();
        seckillQTO.setBizCode(bizCode);
        return seckillManager.querySeckillSimple(seckillQTO);
    }

    public abstract Logger getLogger();
}