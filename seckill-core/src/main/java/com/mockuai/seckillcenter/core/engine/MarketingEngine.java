//package com.mockuai.groupbuy.core.engine;
//
//import com.mockuai.groupbuy.core.exception.SeckillException;
//import com.mockuai.groupbuy.core.manager.ItemManager;
//import com.mockuai.groupbuy.core.manager.MarketActivityManager;
//import com.mockuai.groupbuy.core.engine.activity.ActivityEngine;
//import com.mockuai.groupbuy.core.util.JsonUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//
//@Service
//public class MarketingEngine implements ApplicationContextAware {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(MarketingEngine.class.getName());
//
//    @Resource
//    private ActivityEngine activityEngine;
//
//    @Autowired
//    private MarketActivityManager marketActivityManager;
//
//    @Autowired
//    private ItemManager itemManager;
//
//    private ApplicationContext applicationContext;
//
//    /**
//     * 计算指定商品满足的优惠信息列表
//     *
//     * @param settlementInfo
//     * @param itemList       FIXME 注意，这里的itemList，里面的marketItemDTO必须是数据完整的对象，包含价格、数量等等数据
//     * @param userId
//     * @param bizCode
//     * @param appKey
//     * @return
//     * @throws SeckillException
//     */
//    public void execute(SettlementInfo settlementInfo, List<MarketItemDTO> itemList, Long userId, String bizCode, String appKey) throws SeckillException {
//
//        //入参检查
//        if ((itemList == null) || (itemList.isEmpty())) {
//            return;
//        }
//
//        for (MarketItemDTO marketItemDTO : itemList) {
//            if (marketItemDTO.getUnitPrice() == null) {
//                throw new SeckillException(ResponseCode.PARAMETER_NULL, "item unit price is null");
//            }
//
//            if (marketItemDTO.getNumber() == null) {
//                throw new SeckillException(ResponseCode.PARAMETER_NULL, "item number is null");
//            }
//        }
//
//        if (userId == null) {
//            throw new SeckillException(ResponseCode.PARAMETER_NULL, "userId is null");
//        }
//
//        Context context = new Context();
//        context.setAttribute("grantedCouponManager", this.applicationContext.getBean("grantedCouponManager"));
//        context.setParam("userId", userId);
//        context.setParam("bizCode", bizCode);
//        context.setParam("appKey", appKey);
//
//        //计算商品总价
//        long itemTotalPrice = itemManager.getItemTotalPrice(itemList);
//
//        List<DiscountInfo> discountInfos = new ArrayList<DiscountInfo>();
//        List<DiscountInfo> validDiscountInfos = new ArrayList<DiscountInfo>();
//
//        //查询指定用户可能参与的营销活动（只查询简单活动或者复合活动的父活动）
////        activityEngine.queryActivity(getItemMap(itemList), itemList, discountInfos, userId, bizCode);
//
//        marketActivityManager.queryActivityWithItemList(itemList, bizCode, userId, discountInfos);
//
//        settlementInfo.setDiscountInfoList(new ArrayList<DiscountInfo>());
//        settlementInfo.setDirectDiscountList(new ArrayList<DiscountInfo>());
//
//        DiscountInfo tempDiscountInfo;
//        // 计算每个活动下的优惠活动
//        for (DiscountInfo discountInfo : discountInfos) {
//            context.setParam("discountInfo", discountInfo);
//
//            // 根据是否为 单品优惠活动 计算相应的优惠门槛
//            if (discountInfo.getItemList().isEmpty()) {
//                context.setParam("itemTotalPrice", itemTotalPrice);
//                context.setParam("itemList", itemList);
//            } else {
//                context.setParam("itemTotalPrice", itemManager.getItemTotalPrice(discountInfo.getItemList()));
//            }
//            LOGGER.debug("discountInfo in execute : {}", JsonUtil.toJson(discountInfo));
//
//            tempDiscountInfo = activityEngine.execute(context);
//
//            if (tempDiscountInfo == null) {
//                continue;
//            }
//            validDiscountInfos.add(tempDiscountInfo);
//            // 分为有券优惠活动和无券优惠活动
//            if (discountInfo.getActivity().getCouponMark().intValue() == 1) { //优惠券
//                settlementInfo.getDiscountInfoList().add(discountInfo);
//            } else {// 满减送
//                settlementInfo.getDirectDiscountList().add(discountInfo);
//                settlementInfo.setDiscountAmount(settlementInfo.getDiscountAmount() + discountInfo.getDiscountAmount());
//                settlementInfo.setSavedPostage(settlementInfo.getSavedPostage() + discountInfo.getSavedPostage());
//                settlementInfo.getGiftList().addAll(discountInfo.getGiftList());
//                settlementInfo.setFreePostage(settlementInfo.isFreePostage() || discountInfo.isFreePostage());
//            }
//        }
//
//        if (settlementInfo.getDiscountInfoList().isEmpty()) {
//            return;
//        }
//
//        //针对所有有券优惠活动，按照优惠力度降序排序
//        Collections.sort(settlementInfo.getDiscountInfoList(), new DiscountInfoComparator());
//
//        tempDiscountInfo = settlementInfo.getDiscountInfoList().get(0);
//        settlementInfo.setDiscountAmount(settlementInfo.getDiscountAmount() + tempDiscountInfo.getDiscountAmount());
//        settlementInfo.setSavedPostage(settlementInfo.getSavedPostage() + tempDiscountInfo.getSavedPostage());
//        settlementInfo.getGiftList().addAll(tempDiscountInfo.getGiftList());
//        settlementInfo.setFreePostage(settlementInfo.isFreePostage() || tempDiscountInfo.isFreePostage());
//    }
//
//
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.applicationContext = applicationContext;
//    }
//
//    private class DiscountInfoComparator implements Comparator<DiscountInfo> {
//
//        @Override
//        public int compare(DiscountInfo o1, DiscountInfo o2) {
//            if (o1.getDiscountAmount().longValue() > o2.getDiscountAmount().longValue()) {
//                return -1;
//            }
//            if (o1.getDiscountAmount().longValue() < o2.getDiscountAmount().longValue()) {
//                return 1;
//            }
//            if (o1.getSavedPostage().longValue() > o2.getSavedPostage().longValue()) {
//                return -1;
//            }
//            if (o1.getSavedPostage().longValue() < o2.getSavedPostage().longValue()) {
//                return 1;
//            }
//            //TODO 目前 赠品的价格没有存入数据库,若要比较需要先获取赠品的单价才行.将价格的填充移入 manager 层
////            if (GroupBuyUtil.getTotalGiftPrice(o1) > GroupBuyUtil.getTotalGiftPrice(o2)) {
////                return -1;
////            }
////            if (GroupBuyUtil.getTotalGiftPrice(o1) < GroupBuyUtil.getTotalGiftPrice(o2)) {
////                return 1;
////            }
//
//            return 0;
//        }
//    }
//}