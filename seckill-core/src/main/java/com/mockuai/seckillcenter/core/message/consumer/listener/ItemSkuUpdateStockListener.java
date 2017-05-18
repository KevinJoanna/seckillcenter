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
 * sku 变化
 * <p/>
 * 判断 itemSkuDTO 中商品的数量是否 0, 是则表示商品失效
 * <p/>
 * Created by edgar.zr on 12/24/15.
 */
@Component
public class ItemSkuUpdateStockListener extends BaseListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemSkuUpdateStockListener.class);

    @Override
    public void consumeMessage(JSONObject msg, String appKey) throws SeckillException {
        LOGGER.debug("{}, appKey : {}", getName(), appKey);

        // 过滤所有库存数量为 0 的商品
        // 找到关联 skuId 的换购活动，更新 itemInvalidTime

        Long skuId = msg.getLong("id");
        String bizCode = msg.getString("bizCode");
        Long stockNum = msg.getLong("stockNum");

        if (skuId == null || StringUtils.isBlank(bizCode) || stockNum == null) {
            LOGGER.error("skuId or bizCode is empty or stockNum is null, msg : {}", msg.toJSONString());
            return;
        }

        if (stockNum.intValue() != 0) return;

        List<SeckillDO> seckillDOs = getSeckills(bizCode);


        SeckillDO toUpdateSeckillDO;
        Date currentDate = new Date();

        for (SeckillDO seckillDO : seckillDOs) {
            if (seckillDO.getSkuId().longValue() == skuId.longValue()) {
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
        return RMQMessageType.ITEM_SKU_UPDATE_STOCK.combine();
    }
}