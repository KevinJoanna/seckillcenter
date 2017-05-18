package com.mockuai.seckillcenter.core.message.consumer.listener;

import com.alibaba.fastjson.JSONObject;
import com.mockuai.seckillcenter.common.constant.RMQMessageType;
import com.mockuai.seckillcenter.core.domain.SeckillDO;
import com.mockuai.seckillcenter.core.exception.SeckillException;
import com.mockuai.seckillcenter.core.message.consumer.BaseListener;
import com.mockuai.seckillcenter.core.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 商品删除, itemDTO 被删除，关联换购直接失效
 * Created by edgar.zr on 12/24/15.
 */
@Component
public class ItemDeleteSingleListener extends BaseListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemDeleteSingleListener.class);

    @Override
    public void consumeMessage(JSONObject msg, String appKey) throws SeckillException {
        LOGGER.debug("{}, appKey : {}", getName(), appKey);

        // 找到换购中 itemId == itemDTO.id
        // 更新该活动的 itemInvalidTime

        Long itemId = msg.getLong("id");
        String bizCode = msg.getString("bizCode");

        if (itemId == null || StringUtils.isBlank(bizCode)) {
            LOGGER.error("itemId or bizCode is empty");
            return;
        }

        List<SeckillDO> seckillDOs = getSeckills(bizCode);

        SeckillDO toUpdateSeckillDO;
        Date currentDate = new Date();

        for (SeckillDO seckillDO : seckillDOs) {
            if (seckillDO.getItemId().longValue() == itemId) {
                toUpdateSeckillDO = new SeckillDO();
                toUpdateSeckillDO.setId(seckillDO.getId());
                toUpdateSeckillDO.setBizCode(bizCode);
                toUpdateSeckillDO.setSellerId(seckillDO.getSellerId());
                toUpdateSeckillDO.setItemInvalidTime(currentDate);
                try {
                    updateSeckill(toUpdateSeckillDO, appKey);
                } catch (Exception e) {
                    LOGGER.error("error to update seckill, seckillDO : {}",
                            JsonUtil.toJson(toUpdateSeckillDO), e);
                }
            }
        }
    }

    @Override
    public Logger getLogger() {
        return this.LOGGER;
    }

    @Override
    public void init() {

    }

    @Override
    public String getName() {
        return RMQMessageType.ITEM_DELETE_SINGLE.combine();
    }
}