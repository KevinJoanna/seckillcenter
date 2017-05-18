//package com.mockuai.groupbuy.core.engine.tool.impl;
//
//import com.mockuai.groupbuy.core.engine.Context;
//import com.mockuai.groupbuy.core.engine.component.ComponentHolder;
//import com.mockuai.groupbuy.core.engine.tool.Tool;
//import com.mockuai.groupbuy.core.exception.SeckillException;
//import com.mockuai.groupbuy.core.manager.GrantedCouponManager;
//import com.mockuai.groupbuy.core.manager.ItemManager;
//import com.mockuai.groupbuy.core.util.JsonUtil;
//import com.mockuai.groupbuy.core.util.ModelUtil;
//import com.mockuai.itemcenter.common.domain.dto.ItemDTO;
//import com.mockuai.itemcenter.common.domain.qto.ItemQTO;
//import com.mockuai.groupbuy.core.manager.PropertyManager;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 计算满减送活动
// * 如果满足需要对相应商品做等比的优惠力度扣减
// * <p/>
// * Created by edgar.zr on 8/8/15.
// */
//@Service
//public class ReachMultipleReduceTool implements Tool {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(ReachMultipleReduceTool.class.getName());
//
//    @Autowired
//    private ItemManager itemManager;
//
//    @Autowired
//    private PropertyManager propertyManager;
//
//    @Override
//    public void init() {
//    }
//
//    @Override
//    public ComponentHolder getComponentHolder() {
//        return null;
//    }
//
//    @Override
//    public DiscountInfo execute(Context context) throws SeckillException {
//
//        DiscountInfo discountInfo = (DiscountInfo) context.getParam("discountInfo");
//        MarketActivityDTO marketActivityDTO = discountInfo.getActivity();
//        GrantedCouponManager grantedCouponManager = (GrantedCouponManager) context.getAttribute("grantedCouponManager");
//        Long userId = (Long) context.getParam("userId");
//        String appKey = (String) context.getParam("appKey");
//        long itemTotalPrice = (Long) context.getParam("itemTotalPrice");
//
//        // 复合活动中最优的子活动
//        MarketActivityDTO matchActivity = (MarketActivityDTO) context.getParam("matchActivity");
//
//        Map<String, PropertyDTO> propertyMap = new HashMap<String, PropertyDTO>();
//        for (PropertyDTO propertyDTO : matchActivity.getPropertyList()) {
//            propertyMap.put(propertyDTO.getPkey(), propertyDTO);
//        }
//
//        // 优惠活动优惠门槛无需判断, activityEngine 已经做了处理
//
//        // 用户目前拥有的有效优惠券列表
//        List<GrantedCouponDO> grantedCouponDOs = Collections.emptyList();
//
//        List usableCoupons = new ArrayList();
//
//        // 如需使用优惠券,需要单独进行判断
//        if (marketActivityDTO.getCouponMark().intValue() == 1) {
//
//            GrantedCouponQTO grantedCouponQTO = new GrantedCouponQTO();
//            grantedCouponQTO.setActivityId(marketActivityDTO.getId());
//            grantedCouponQTO.setReceiverId(userId);
//            grantedCouponDOs = grantedCouponManager.queryGrantedCoupon(grantedCouponQTO);
//
//            if (grantedCouponDOs.isEmpty()) {
//                return null;
//            }
//
//            usableCoupons.addAll(grantedCouponDOs);
//        }
//
//        List<MarketItemDTO> giftItemList = Collections.emptyList();
//
//        propertyManager.extractPropertyGiftItemList(propertyMap, appKey);
//        discountInfo.setAvailableCoupons(ModelUtil.genGrantedCouponDTOList(usableCoupons, marketActivityDTO.getPropertyList(), marketActivityDTO));
//        discountInfo.setActivity(marketActivityDTO);
//        discountInfo.setFreePostage(propertyManager.extractPropertyFreePostage(propertyMap));
//        discountInfo.setDiscountAmount(propertyManager.extractPropertyQuota(propertyMap));
//        discountInfo.setConsume(propertyManager.extractPropertyConsume(propertyMap));
//        discountInfo.setGiftList(giftItemList);
//
//        List<MarketItemDTO> itemDTOs = discountInfo.getItemList();
//        if (itemDTOs == null) {
//            itemDTOs = (List<MarketItemDTO>) context.getParam("itemList");
//        }
//
//        cutByQuota(itemDTOs, itemTotalPrice, discountInfo.getDiscountAmount());
//
//        return discountInfo;
//    }
//
//    /**
//     * 填充赠品相关信息:名字,url
//     *
//     * @param giftItemDTOs
//     * @param appKey
//     */
//    private void fillItemInfo(List<MarketItemDTO> giftItemDTOs, String appKey) {
//
//        Map<Long, List<MarketItemDTO>> simpleItemMap = new HashMap<Long, List<MarketItemDTO>>();
//        for (MarketItemDTO simpleItemDTO : giftItemDTOs) {
//            if (simpleItemMap.containsKey(simpleItemDTO.getItemId()) == false) {
//                simpleItemMap.put(simpleItemDTO.getItemId(), new ArrayList<MarketItemDTO>());
//            }
//            simpleItemMap.get(simpleItemDTO.getItemId()).add(simpleItemDTO);
//        }
//
//        if (giftItemDTOs.isEmpty()) {
//            return;
//        }
//        //查询指定商品列表。如果查询异常，则直接吃掉异常，以保证依赖此逻辑的接口继续可用
//        ItemQTO itemQTO = new ItemQTO();
//        itemQTO.setIdList(new ArrayList<Long>(simpleItemMap.keySet()));
//        itemQTO.setSellerId(giftItemDTOs.get(0).getSellerId());
//
//        try {
//            List<ItemDTO> itemDTOs = itemManager.queryItem(itemQTO, appKey);
//            for (ItemDTO itemDTO : itemDTOs) {
//                List<MarketItemDTO> marketItemDTOs = simpleItemMap.get(itemDTO.getId());
//                for (MarketItemDTO marketItemDTO : marketItemDTOs) {
//                    marketItemDTO.setItemName(itemDTO.getItemName());
//                    marketItemDTO.setIconUrl(itemDTO.getIconUrl());
//                }
//            }
//        } catch (SeckillException e) {
//            LOGGER.error("cannot query for giftItem : {}, ", JsonUtil.toJson(giftItemDTOs), e);
//        }
//    }
//
//    /**
//     * 根据满减送优惠值在相应范围下减少商品价值
//     *
//     * @param itemDTOs
//     * @param itemTotalPrice
//     * @param quota
//     * @throws SeckillException
//     */
//    public void cutByQuota(List<MarketItemDTO> itemDTOs, Long itemTotalPrice, Long quota) throws SeckillException {
//
//        for (MarketItemDTO marketItemDTO : itemDTOs) {
//            marketItemDTO.setTotalPrice(marketItemDTO.getTotalPrice() - (long) (marketItemDTO.getTotalPrice().longValue() * 1.0 / itemTotalPrice.longValue() * quota));
//        }
//    }
//}