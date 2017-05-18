//package com.mockuai.groupbuy.core.engine.activity;
//
//import com.mockuai.groupbuy.core.engine.Context;
//import com.mockuai.groupbuy.core.engine.tool.Tool;
//import com.mockuai.groupbuy.core.engine.tool.ToolHolder;
//import com.mockuai.groupbuy.core.exception.SeckillException;
//import com.mockuai.groupbuy.core.manager.GrantedCouponManager;
//import com.mockuai.groupbuy.core.manager.MarketActivityManager;
//import com.mockuai.groupbuy.core.util.JsonUtil;
//import com.mockuai.groupbuy.core.util.ModelUtil;
//import com.mockuai.groupbuy.core.manager.ActivityItemManager;
//import com.mockuai.groupbuy.core.manager.PropertyManager;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//@Service
//public class ActivityEngine {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityEngine.class.getName());
//
//    @Resource
//    private MarketActivityManager marketActivityManager;
//
//    @Resource
//    private GrantedCouponManager grantedCouponManager;
//
//    @Resource
//    private PropertyManager propertyManager;
//
//    @Resource
//    private ToolHolder toolHolder;
//
//    @Resource
//    private ActivityItemManager activityItemManager;
//
////    /**
////     * @param itemMap
////     * @param itemList
////     * @param discountInfos
////     * @param userId        userId == null, 非指定用户单应用 在 WHOLE/SPECIAL/SHOP/ITEM 范围下的正在进行的优惠活动, userId != null, 指定用户能参与的优惠活动
////     * @return
////     */
////    public List<MarketActivityDTO> queryActivity(Map<Long, List<Long>> itemMap, List<MarketItemDTO> itemList, List<DiscountInfo> discountInfos, Long userId, String bizCode) {
////
////        List<MarketActivityDTO> marketActivityDTOs = new ArrayList<MarketActivityDTO>();
////        List<MarketActivityDO> totalMatchActivityDOs = new ArrayList<MarketActivityDO>();
////
////        try {
////
////            //1. 查询全场活动
////            MarketActivityQTO marketActivityQTO = new MarketActivityQTO();
////            marketActivityQTO.setBizCode(bizCode);
////            //只查简单活动或者复合活动的父活动
////            marketActivityQTO.setParentId(0L);
////            marketActivityQTO.setScope(ActivityScope.SCOPE_WHOLE.getValue());//全店活动
////            marketActivityQTO.setStatus(ActivityStatus.NORMAL.getValue());
////            List<MarketActivityDO> marketActivityDOs = this.marketActivityManager.queryActivity(marketActivityQTO);
////            totalMatchActivityDOs.addAll(marketActivityDOs);
////
////            //2. TODO 查询专场活动, 目前应该是不做考虑. 至少会涉及到的功能:订单结算、单品优惠、优惠券查询等会进行相应的更改
////
////            //3. 查询全店活动
////            for (Map.Entry<Long, List<Long>> entry : itemMap.entrySet()) {
////                marketActivityQTO = new MarketActivityQTO();
////                marketActivityQTO.setCreatorId(entry.getKey());
////                //只查简单活动或者复合活动的父活动
////                marketActivityQTO.setParentId(0L);
////                marketActivityQTO.setScope(ActivityScope.SCOPE_SHOP.getValue());//全店活动
////                marketActivityQTO.setStatus(ActivityStatus.NORMAL.getValue());
////                marketActivityQTO.setBizCode(bizCode);
////                marketActivityDOs = marketActivityManager.queryActivity(marketActivityQTO);
////                totalMatchActivityDOs.addAll(marketActivityDOs);
////            }
////
////            Map<Long, List<Long>> itemIdMapWithActivityIdKey = new HashMap<Long, List<Long>>();
////
////            //4. 查询单品活动
////            for (Map.Entry<Long, List<Long>> entry : itemMap.entrySet()) {
////                ActivityItemQTO activityItemQTO = new ActivityItemQTO();
////                activityItemQTO.setActivityCreatorId(entry.getKey());
////                //查询符合条件的活动单品列表
////                List<ActivityItemDO> activityItemDOs =
////                        activityItemManager.queryActivityItem(entry.getValue(), entry.getKey());
////                if (activityItemDOs == null || activityItemDOs.isEmpty()) {
////                    break;
////                }
////
////                List<Long> activityIdList = new ArrayList<Long>();
////
////                for (ActivityItemDO activityItemDO : activityItemDOs) {
////                    activityIdList.add(activityItemDO.getActivityId());
////                    if (!itemIdMapWithActivityIdKey.containsKey(activityItemDO.getActivityId())) {
////                        itemIdMapWithActivityIdKey.put(activityItemDO.getActivityId(), new ArrayList<Long>());
////                    }
////                    itemIdMapWithActivityIdKey.get(activityItemDO.getActivityId()).add(activityItemDO.getItemId());
////                }
////
////                marketActivityQTO = new MarketActivityQTO();
////                //只查简单活动或者复合活动的父活动
////                marketActivityQTO.setParentId(0L);
////                marketActivityQTO.setIdList(activityIdList);
////                marketActivityQTO.setCreatorId(entry.getKey());
////                marketActivityQTO.setScope(ActivityScope.SCOPE_ITEM.getValue());//单品活动
////                marketActivityQTO.setStatus(ActivityStatus.NORMAL.getValue());
////                marketActivityQTO.setBizCode(bizCode);
////
////                marketActivityDOs = marketActivityManager.queryActivity(marketActivityQTO);
////                totalMatchActivityDOs.addAll(marketActivityDOs);
////            }
////
////            //根据活动日期，过滤出有效活动
////            List<MarketActivityDO> validMarketActivityList = filterActivityByDate(totalMatchActivityDOs);
////
////            //转换活动对象，并填充每个活动的属性列表
////            List<MarketActivityDTO> validActivityDTOList = ModelUtil.genMarketActivityDTOList(validMarketActivityList);
////            //TODO 性能待重构，重构成批量查询(非本店的活动属性无法进行批量查询，或者以商家进行分组进行批量操作)
////            //填充活动属性列表
////            for (MarketActivityDTO marketActivityDTO : validActivityDTOList) {
////                PropertyQTO propertyQTO = new PropertyQTO();
////                propertyQTO.setOwnerType(PropertyOwnerType.ACTIVITY.getValue());
////                propertyQTO.setOwnerId(marketActivityDTO.getId());
////                propertyQTO.setCreatorId(marketActivityDTO.getCreatorId());
////                List propertyDOs = propertyManager.queryProperty(propertyQTO);
////                marketActivityDTO.setPropertyList(ModelUtil.genPropertyDTOList(propertyDOs));
////            }
////
////            marketActivityDTOs.addAll(validActivityDTOList);
////
////            LOGGER.debug("activities queried : {}", JsonUtil.toJson(marketActivityDTOs));
////
////            if (discountInfos != null) {
////
////                // 同一个商品 itemId 可能会同时选择其下不同的 sku
////                Map<Long, List<MarketItemDTO>> marketItemDTOMapWithItemIdKey = new HashMap<Long, List<MarketItemDTO>>();
////
////                for (MarketItemDTO marketItemDTO : itemList) {
////                    if (!marketItemDTOMapWithItemIdKey.containsKey(marketItemDTO.getItemId())) {
////                        marketItemDTOMapWithItemIdKey.put(marketItemDTO.getItemId(), new ArrayList<MarketItemDTO>());
////                    }
////                    marketItemDTOMapWithItemIdKey.get(marketItemDTO.getItemId()).add(marketItemDTO);
////                }
////
////                DiscountInfo discountInfo;
////                // 关联 MarketItemDTO 到相应的优惠活动上，把满减送排到优惠券前面
////                for (MarketActivityDTO marketActivityDTO : validActivityDTOList) {
////                    discountInfo = genDiscountInfo(marketActivityDTO);
////                    discountInfo.setItemList(new ArrayList<MarketItemDTO>());
////                    if (itemIdMapWithActivityIdKey.containsKey(marketActivityDTO.getId())) {
////                        for (Long itemId : itemIdMapWithActivityIdKey.get(marketActivityDTO.getId())) {
////                            discountInfo.getItemList().addAll(marketItemDTOMapWithItemIdKey.get(itemId));
////                        }
////                    }
////                    if (ToolType.SIMPLE_TOOL.getCode().equals(marketActivityDTO.getToolCode()) && marketActivityDTO.getCouponMark().intValue() == 1) {
////                        discountInfos.add(discountInfo);
////                    } else {
////                        discountInfos.add(0, discountInfo);
////                    }
////                }
////            }
////        } catch (Exception e) {
////            LOGGER.error("error of querying the activities, userId : {}, itemIDMapWithSellerIdKey : {}, discountInfos : {}, bizCode : {}",
////                    userId, JsonUtil.toJson(itemMap), JsonUtil.toJson(discountInfos), bizCode, e);
////            return Collections.emptyList();
////        }
////        if (userId != null) {
////            return filterActivityByUser(marketActivityDTOs, userId);
////        } else {
////            return marketActivityDTOs;
////        }
////    }
////
////    private DiscountInfo genDiscountInfo(MarketActivityDTO marketActivityDTO) {
////
////        DiscountInfo discountInfo = new DiscountInfo();
////        discountInfo.setActivity(marketActivityDTO);
////        return discountInfo;
////    }
////
////    /**
////     * 根据活动日期过滤出有效的营销活动
////     *
////     * @param marketActivityDOs
////     * @return
////     */
////    private List<MarketActivityDO> filterActivityByDate(List<MarketActivityDO> marketActivityDOs) {
////        List<MarketActivityDO> matchedActivities = new ArrayList();
////        try {
////            Date currentDate = new Date();
////            for (MarketActivityDO marketActivityDO : marketActivityDOs) {
////                //只有在可用期范围内的营销活动方可满足需求
////                if (marketActivityDO.getStartTime().before(currentDate)
////                        && marketActivityDO.getEndTime().after(currentDate)) {
////                    matchedActivities.add(marketActivityDO);
////                }
////            }
////        } catch (Exception e) {
////            LOGGER.error("error of comparing the duration and current time, marketActivityDOs : {}",
////                    JsonUtil.toJson(marketActivityDOs), e);
////            return Collections.emptyList();
////        }
////        return matchedActivities;
////    }
////
////    /**
////     * 过滤营销活动，筛选出指定用户满足条件的营销活动
////     *
////     * @param marketActivityDTOs
////     * @param userId
////     * @return
////     */
////    private List<MarketActivityDTO> filterActivityByUser(List<MarketActivityDTO> marketActivityDTOs, Long userId) {
////
////        List<MarketActivityDTO> matchedActivities = new ArrayList();
////        try {
////            for (MarketActivityDTO marketActivityDTO : marketActivityDTOs) {
////                if (marketActivityDTO.getCouponMark().intValue() == 0) {
////                    matchedActivities.add(marketActivityDTO);
////                } else {
////                    //如果为需要优惠券的营销活动，则该用户账户下面需要有相应的优惠券方可满足需求
////                    GrantedCouponQTO grantedCouponQTO = new GrantedCouponQTO();
////                    grantedCouponQTO.setReceiverId(userId);
////                    grantedCouponQTO.setActivityId(marketActivityDTO.getId());
////                    //只查状态为未使用的优惠券
////                    grantedCouponQTO.setStatus(UserCouponStatus.UN_USE.getValue());
////                    List grantedCouponDOs = this.grantedCouponManager.queryGrantedCoupon(grantedCouponQTO);
////
////                    if (!grantedCouponDOs.isEmpty())
////                        matchedActivities.add(marketActivityDTO);
////                }
////            }
////        } catch (Exception e) {
////            LOGGER.error("error of filtering activity by user, userId : {}, marketActivityDTOs : {}",
////                    userId, JsonUtil.toJson(marketActivityDTOs), e);
////            return Collections.emptyList();
////        }
////
////        return matchedActivities;
////    }
//
//    public DiscountInfo execute(Context context) throws SeckillException {
//
//        MarketActivityDTO marketActivityDTO = ((DiscountInfo) context.getParam("discountInfo")).getActivity();
//        long itemTotalPrice = (Long) context.getParam("itemTotalPrice");
//
//        DiscountInfo discountInfo;
//
//        //计算优惠信息。简单活动和复合活动的处理逻辑有所区别
//        if (marketActivityDTO.getToolType() != null
//                && marketActivityDTO.getToolType().intValue() == ToolType.COMPOSITE_TOOL.getValue()) {//复合活动
//            // TODO 包装子活动,放入 MarketActivityManager 中
//            // TODO 将所有的查询条件的对象包装到 manager 层, 减少整体代码量
//            //查询复合活动的子活动
//            MarketActivityQTO marketActivityQTO = new MarketActivityQTO();
//            marketActivityQTO.setCreatorId(marketActivityDTO.getCreatorId());
//            //只查简单活动或者复合活动的父活动
//            marketActivityQTO.setParentId(marketActivityDTO.getId());
//            marketActivityQTO.setBizCode(marketActivityDTO.getBizCode());
//
//            List<MarketActivityDO> marketActivityDOs = this.marketActivityManager.queryActivity(marketActivityQTO);
//
//            /**
//             * TODO 这里的复合活动先简单做，按照需求只需实现多个层级的满减活动（多个层级之间互斥）即可。后续需要实现更灵活的复合活动，
//             * TODO 这里的代码需要重构。add by zengzhangqiang on 2015-07-23
//             */
//
//            List<MarketActivityDTO> subMarketActivityList = ModelUtil.genMarketActivityDTOList(marketActivityDOs);
//            //TODO 性能待重构，重构成批量查询,类同
//            for (MarketActivityDTO subMarketActivity : subMarketActivityList) {
//                PropertyQTO propertyQTO = new PropertyQTO();
//                propertyQTO.setOwnerType(PropertyOwnerType.ACTIVITY.getValue());
//                propertyQTO.setOwnerId(subMarketActivity.getId());
//                List propertyDOs = this.propertyManager.queryProperty(propertyQTO);
//                subMarketActivity.setPropertyList(ModelUtil.genPropertyDTOList(propertyDOs));
//            }
//
//            //根据消费门槛，对子营销活动列表进行降序排列
//            Collections.sort(subMarketActivityList, new Comparator<MarketActivityDTO>() {
//                public int compare(MarketActivityDTO activity1, MarketActivityDTO activity2) {
//                    long consume1 = 0;
//                    List<PropertyDTO> properties = activity1.getPropertyList();
//                    //TODO 在 MarketActivityDTO 中 添加用于比较的属性,避免 for 的频繁使用,最好以 map 存,直接通过键值查询
//                    for (PropertyDTO propertyDTO : properties) {
//                        if ("consume".equals(propertyDTO.getPkey())) {
//                            consume1 = Long.valueOf(propertyDTO.getValue());
//                            break;
//                        }
//                    }
//
//                    long consume2 = 0;
//                    properties = activity2.getPropertyList();
//                    for (PropertyDTO propertyDTO : properties) {
//                        if ("consume".equals(propertyDTO.getPkey())) {
//                            consume2 = Long.valueOf(propertyDTO.getValue());
//                            break;
//                        }
//                    }
//
//                    if (consume1 > consume2) {
//                        return -1;
//                    } else if (consume1 < consume2) {
//                        return 1;
//                    } else {
//                        return 0;
//                    }
//                }
//            });
//
//            LOGGER.debug("sorted subMarketActivity : {}", JsonUtil.toJson(subMarketActivityList));
//            MarketActivityDTO matchActivity = null;
//            //比较商品总价与各子活动的消费门槛，选择最优先满足的门槛最高的活动（默认该活动的优惠力度最高）
//            for (MarketActivityDTO subMarketActivity : subMarketActivityList) {
//                long consume = 0;
//                List<PropertyDTO> properties = subMarketActivity.getPropertyList();
//                for (PropertyDTO propertyDTO : properties) {
//                    if ("consume".equals(propertyDTO.getPkey())) {
//                        consume = Long.valueOf(propertyDTO.getValue());
//                        break;
//                    }
//                }
//                if (itemTotalPrice >= consume) {
//                    matchActivity = subMarketActivity;
//                    break;
//                }
//            }
//
//            //没有满足条件的子优惠活动
//            if (matchActivity == null) {
//                return null;
//            }
//
//            // 目前只需要显示最优的子活动数据
//            marketActivityDTO.setSubMarketActivityList(Arrays.asList(matchActivity));
//
//            // 添加最优的子活动
//            context.setParam("matchActivity", matchActivity);
//        }
//
//        discountInfo = executeActivityTool(marketActivityDTO, context);
//
//        return normalizeDiscountInfo(discountInfo);
//
//    }
//
//    private DiscountInfo normalizeDiscountInfo(DiscountInfo discountInfo) {
//
//        if (discountInfo == null) {
//            return null;
//        }
//
//        if (discountInfo.getSavedPostage() == null) {
//            discountInfo.setSavedPostage(Long.valueOf(0L));
//        }
//
//        if (discountInfo.getGiftList() == null) {
//            discountInfo.setGiftList(Collections.EMPTY_LIST);
//        }
//        return discountInfo;
//    }
//
//    private DiscountInfo executeActivityTool(MarketActivityDTO marketActivityDTO, Context context) {
//
//        if (marketActivityDTO == null) {
//            return null;
//        }
//
//        String toolCode = marketActivityDTO.getToolCode();
//        Tool tool = toolHolder.getTool(toolCode);
//
//        if (tool == null) {
//            LOGGER.error("tool not found, toolCode : {}, bizCode : {}", marketActivityDTO.getToolCode(), context.getParam("bizCode"));
//            return null;
//        }
//
//        try {
//            return tool.execute(context);
//        } catch (SeckillException e) {
//            LOGGER.error("error of executing the tool, toolCode : {}, tool : {}, bizCode : {}",
//                    toolCode, JsonUtil.toJson(tool), context.getParam("bizCode"));
//            return null;
//        }
//    }
//}