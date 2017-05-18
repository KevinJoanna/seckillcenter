package com.mockuai.seckillcenter.core.message.consumer.listener;

import com.alibaba.fastjson.JSONArray;
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
 * 商品修改，itemDTO 非 4 ，失效，同时判断 itemSKUDTO 是否数量为 0， 为零也失效
 * (itemSKUDTO 在消息中带有)
 * Created by edgar.zr on 12/24/15.
 */
@Component
public class ItemUpdateSingleListener extends BaseListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemUpdateSingleListener.class);

    @Override
    public void consumeMessage(JSONObject msg, String appKey) throws SeckillException {
        LOGGER.debug("{}, appKey : {}", getName(), appKey);

        // 过滤 itemStatus != 4 || skuDTO.stockNum == 0 的商品(把 skuDTO 转换到对应的 itemId)
        // 找到 itemId 关联的换购活动，更新 itemInvalidTime
        Long itemId = msg.getLong("id");
        String bizCode = msg.getString("bizCode");

        if (itemId == null || StringUtils.isBlank(bizCode)) {
            LOGGER.error("itemId or bizCode is empty");
            return;
        }

        try {

            Integer itemStatus = msg.getInteger("itemStatus");

            List<SeckillDO> seckillDOs = getSeckills(bizCode);

            SeckillDO toUpdateSeckillDO;
            Date currentDate = new Date();

            // item 状态非 4
            if (itemStatus != 4) {
                for (SeckillDO seckillDO : seckillDOs) {
                    if (seckillDO.getItemId().longValue() == itemId) {
                        toUpdateSeckillDO = new SeckillDO();
                        toUpdateSeckillDO.setId(seckillDO.getId());
                        toUpdateSeckillDO.setBizCode(bizCode);
                        toUpdateSeckillDO.setItemInvalidTime(currentDate);
                        try {
                            updateSeckill(toUpdateSeckillDO, appKey);
                        } catch (Exception e) {
                            LOGGER.error("error to update seckill, seckillDO : {}",
                                    JsonUtil.toJson(toUpdateSeckillDO), e);
                        }
                    }
                }
                return;
            }
            JSONArray itemSkuDTOList = msg.getJSONArray("itemSkuDTOList");
            Long skuId;

            // sku 的库存量为 0
            for (int i = 0; i < itemSkuDTOList.size(); i++) {
                if (itemSkuDTOList.getJSONObject(i).getLong("stockNum") != 0)
                    continue;
                skuId = itemSkuDTOList.getJSONObject(i).getLong("id");

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
        } catch (Exception e) {
            LOGGER.error("error to consumeMessage, msg : {}", msg.toJSONString(), e);
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
        return RMQMessageType.ITEM_UPDATE_SINGLE.combine();
    }
}