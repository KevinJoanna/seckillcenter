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
 * 商品自动上下架
 * <p/>
 * itemDTO 列表中(将列表拆分为单个对象处理)，判断每一个 itemStatus != 4 时，表示商品 无效
 * <p/>
 * Created by edgar.zr on 12/24/15.
 */
@Component
public class ItemStatusChangeBatchListener extends BaseListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemStatusChangeBatchListener.class);

    @Override
    public void consumeMessage(JSONObject msg, String appKey) throws SeckillException {
        LOGGER.debug("{}, appKey : {}", getName(), appKey);

        // 过滤得到 itemStatus != 4 的商品
        // 找到 itemId 关联到的换购活动， 更新 itemInvalidTime

        Long itemId = msg.getLong("id");
        String bizCode = msg.getString("bizCode");
        Integer itemStatus = msg.getInteger("itemStatus");

        if (itemId == null || StringUtils.isBlank(bizCode) || itemStatus == null) {
            LOGGER.error("itemId or bizCode or itemStatus is empty, msg : {}", msg.toJSONString());
            return;
        }

        if (itemStatus.intValue() == 4) return;

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
        return RMQMessageType.ITEM_STATUS_CHANGE_BATCH.combine();
    }
}