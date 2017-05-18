//package com.mockuai.groupbuy.core.engine.tool.impl;
//
//import com.mockuai.groupbuy.core.engine.Context;
//import com.mockuai.groupbuy.core.engine.component.ComponentHolder;
//import com.mockuai.groupbuy.core.engine.tool.Tool;
//import com.mockuai.groupbuy.core.exception.SeckillException;
//import com.mockuai.groupbuy.core.manager.ActivityHistoryManager;
//import com.mockuai.groupbuy.core.manager.PropertyManager;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by edgar.zr on 12/1/15.
// */
//public class BarterTool implements Tool {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(BarterTool.class.getName());
//
//    @Autowired
//    private PropertyManager propertyManager;
//
//    @Autowired
//    private ActivityHistoryManager activityHistoryManager;
//
//    @Override
//    public void init() {
//
//    }
//
//    @Override
//    public ComponentHolder getComponentHolder() {
//        return null;
//    }
//
//    @Override
//    public DiscountInfo execute(Context context) throws SeckillException {
//        DiscountInfo discountInfo = (DiscountInfo) context.getParam("discountInfo");
//        MarketActivityDTO marketActivityDTO = discountInfo.getActivity();
//        Long userId = (Long) context.getParam("userId");
//        String appKey = (String) context.getParam("appKey");
//        long itemTotalPrice = (Long) context.getParam("itemTotalPrice");
//
//        Map<String, PropertyDTO> propertyDTOMap = new HashMap<String, PropertyDTO>();
//        for (PropertyDTO propertyDTO : marketActivityDTO.getPropertyList()) {
//            propertyDTOMap.put(propertyDTO.getPkey(), propertyDTO);
//        }
//
//        long consume = propertyManager.extractPropertyConsume(propertyDTOMap);
//        //没到消费门槛，显示为灰色的，但是有机会可以换购
//        discountInfo.setIsAvailable(itemTotalPrice >= consume);
//
//        long extra = propertyManager.extractPropertyExtra(propertyDTOMap);
//        discountInfo.setDiscountAmount(extra);
//
//        ActivityItemDTO activityItemDTO = propertyManager.extractPropertySkuId(propertyDTOMap, marketActivityDTO.getCreatorId(), appKey);
//        // 换购商品不存在
//        if (activityItemDTO == null) {
//            LOGGER.error("error to get the targetItem in BarterTool, userId : {}, itemTotalPrice : {}, activityId : {}",
//                    userId, itemTotalPrice, marketActivityDTO.getId());
//            return null;
//        }
//        long limit = propertyManager.extractPropertyLimit(propertyDTOMap);
//        Long num = activityHistoryManager.getNumOfSkuBuyByUser(userId, marketActivityDTO.getId(), activityItemDTO.getItemSkuId(), marketActivityDTO.getBizCode());
//
//        // 用户超购， 或，商品库存为零
//        if (activityItemDTO.getStockNum().longValue() == 0 || limit <= num) {
//            discountInfo.setIsAvailable(false);
//        }
//
//        marketActivityDTO.setTargetItemList(Arrays.asList(activityItemDTO));
//        return discountInfo;
//    }
//}