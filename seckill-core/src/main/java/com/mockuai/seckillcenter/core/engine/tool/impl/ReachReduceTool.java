//package com.mockuai.groupbuy.core.engine.tool.impl;
//
//import com.mockuai.groupbuy.core.engine.Context;
//import com.mockuai.groupbuy.core.engine.component.ComponentHolder;
//import com.mockuai.groupbuy.core.engine.tool.Tool;
//import com.mockuai.groupbuy.core.exception.SeckillException;
//import com.mockuai.groupbuy.core.manager.GrantedCouponManager;
//import com.mockuai.groupbuy.core.util.ModelUtil;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class ReachReduceTool implements Tool {
//
//    public final static String CONSUME = "consume";
//    public final static String QUOTA = "quota";
//
//    public void init() {
//
//    }
//
//    public ComponentHolder getComponentHolder() {
//        return null;
//    }
//
//    public DiscountInfo execute(Context context) throws SeckillException {
//
//        DiscountInfo discountInfo = (DiscountInfo) context.getParam("discountInfo");
//        MarketActivityDTO marketActivityDTO = discountInfo.getActivity();
//        GrantedCouponManager grantedCouponManager = (GrantedCouponManager) context.getAttribute("grantedCouponManager");
//        Long userId = (Long) context.getParam("userId");
//        Long itemTotalPrice = (Long) context.getParam("itemTotalPrice");
//
//        long consume = 0L;
//        long quota = 0L;
//
//        List<PropertyDTO> propertyDTOs = marketActivityDTO.getPropertyList();
//
//        Map<String, PropertyDTO> propertyMap = new HashMap<String, PropertyDTO>();
//
//        for (PropertyDTO propertyDTO : propertyDTOs) {
//            propertyMap.put(propertyDTO.getPkey(), propertyDTO);
//        }
//
//        if (propertyMap.containsKey(ReachReduceTool.CONSUME)) {
//            String consumeStr = propertyMap.get(ReachReduceTool.CONSUME).getValue();
//            consume = Long.parseLong(consumeStr);
//        }
//
//        // 没有达到此优惠活动的门槛
//        if (itemTotalPrice < consume) {
//            return null;
//        }
//
//        if (propertyMap.containsKey(ReachReduceTool.QUOTA)) {
//            String quotaStr = propertyMap.get(ReachReduceTool.QUOTA).getValue();
//            quota = Long.parseLong(quotaStr);
//        }
//
//        List<GrantedCouponDO> grantedCouponDOs = Collections.emptyList();
//
//        List usableCoupons = new ArrayList();
//
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
//            usableCoupons.addAll(grantedCouponDOs);
//        }
//
//        discountInfo.setFreePostage(false);
//        List<GrantedCouponDTO> availableCoupons = ModelUtil.genGrantedCouponDTOList(usableCoupons, marketActivityDTO.getPropertyList(), marketActivityDTO);
//        StringBuilder sb;
//        int quotaT;
//        for (GrantedCouponDTO grantedCouponDTO : availableCoupons) {
//            //FIXME 显示结算订单详情时的优惠券, content 需要显示未 满XX即可使用
//            for (PropertyDTO propertyDTO : marketActivityDTO.getPropertyList()) {
//                if (ReachReduceTool.CONSUME.equals(propertyDTO.getPkey())) {
//                    if (StringUtils.isBlank(propertyDTO.getValue())) {
//                        quotaT = 0;
//                    } else {
//                        quotaT = Integer.valueOf(propertyDTO.getValue());
//                    }
//                    quotaT = quotaT / 100;
//                    sb = new StringBuilder("满");
//                    sb.append(String.valueOf(quotaT)).append("即可使用");
//                    grantedCouponDTO.setContent(sb.toString());
//                    break;
//                }
//            }
//        }
//        discountInfo.setAvailableCoupons(availableCoupons);
//        discountInfo.setActivity(marketActivityDTO);
//        // 每个订单只能使用一张优惠券
//        discountInfo.setDiscountAmount(Long.valueOf(quota));
//        discountInfo.setConsume(Long.valueOf(consume));
//        return discountInfo;
//    }
//}