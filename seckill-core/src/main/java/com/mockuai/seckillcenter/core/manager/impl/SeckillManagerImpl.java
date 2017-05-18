package com.mockuai.seckillcenter.core.manager.impl;

import com.mockuai.itemcenter.common.constant.DBConst;
import com.mockuai.itemcenter.common.domain.dto.ItemDTO;
import com.mockuai.itemcenter.common.domain.dto.ItemImageDTO;
import com.mockuai.itemcenter.common.domain.dto.ItemLabelDTO;
import com.mockuai.itemcenter.common.domain.dto.ItemSkuDTO;
import com.mockuai.itemcenter.common.domain.dto.LimitEntity;
import com.mockuai.itemcenter.common.domain.dto.SkuPropertyDTO;
import com.mockuai.marketingcenter.common.constant.ActivityCouponStatus;
import com.mockuai.marketingcenter.common.constant.ActivityLifecycle;
import com.mockuai.marketingcenter.common.constant.ActivityStatus;
import com.mockuai.seckillcenter.common.constant.ResponseCode;
import com.mockuai.seckillcenter.common.constant.SeckillLifecycle;
import com.mockuai.seckillcenter.common.constant.SeckillStatus;
import com.mockuai.seckillcenter.common.domain.dto.SeckillDTO;
import com.mockuai.seckillcenter.common.domain.qto.SeckillQTO;
import com.mockuai.seckillcenter.core.dao.SeckillDAO;
import com.mockuai.seckillcenter.core.domain.SeckillDO;
import com.mockuai.seckillcenter.core.exception.SeckillException;
import com.mockuai.seckillcenter.core.manager.ItemManager;
import com.mockuai.seckillcenter.core.manager.SeckillManager;
import com.mockuai.seckillcenter.core.manager.TradeManager;
import com.mockuai.seckillcenter.core.util.DateUtils;
import com.mockuai.seckillcenter.core.util.JsonUtil;
import com.mockuai.seckillcenter.core.util.ModelUtil;
import com.mockuai.tradecenter.common.domain.DataQTO;
import com.mockuai.tradecenter.common.domain.ItemSalesVolumeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by edgar.zr on 12/4/15.
 */
public class SeckillManagerImpl implements SeckillManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeckillManagerImpl.class);

    @Autowired
    private SeckillDAO seckillDAO;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private TradeManager tradeManager;

    @Override
    public void addSeckill(SeckillDTO seckillDTO, String appKey) throws SeckillException {
        try {

            SeckillQTO seckillQTO = new SeckillQTO();
            seckillQTO.setBizCode(seckillDTO.getBizCode());
            seckillQTO.setSellerId(seckillDTO.getSellerId());
            seckillQTO.setStatus(SeckillStatus.NORMAL.getValue());
            seckillQTO.setSkuId(seckillDTO.getSkuId());
            List<SeckillDO> groupBuyDOs = seckillDAO.querySeckill(seckillQTO);
            if (groupBuyDOs.size() != 0) {
                Date current = new Date();
                for (SeckillDO seckillDO : groupBuyDOs) {
                    if (seckillDO.getEndTime().after(current)
                            && DateUtils.isOverlappingDates(seckillDO.getStartTime(), seckillDO.getEndTime(), seckillDTO.getStartTime(), seckillDTO.getEndTime())) {
                        throw new SeckillException(ResponseCode.BIZ_E_SKU_ALREADY_EXISTS);
                    }
                }
            }

            ItemDTO itemDTO;
            ItemSkuDTO itemSkuDTO = null;
            Long itemSellerId;
            try {
                itemSellerId = seckillDTO.getItemSellerId() == null ? seckillDTO.getSellerId() : seckillDTO.getItemSellerId();
                itemDTO = itemManager.getItem(seckillDTO.getItemId(), itemSellerId, appKey);
                if (itemDTO == null) {
                    LOGGER.error("invalidate item, itemId : {}, sellerId : {}, appKey : {}", itemSellerId, seckillDTO.getSellerId(), appKey);
                    throw new SeckillException(ResponseCode.SERVICE_EXCEPTION);
                }
                for (ItemSkuDTO temp : itemDTO.getItemSkuDTOList()) {
                    if (temp.getId().longValue() == seckillDTO.getSkuId().longValue()) {
                        itemSkuDTO = temp;
                    }
                }
//                itemSkuDTO = itemManager.getItemSku(seckillDTO.getSkuId(), itemSellerId, appKey);
                seckillDTO.setItemSellerId(itemDTO.getSellerId());
                if (itemSkuDTO == null || itemSkuDTO.getStockNum().longValue() < 1) {
                    LOGGER.error("invalidate sku, skuId : {}, sellerId : {}, appKey : {}", seckillDTO.getSkuId(), itemSellerId, appKey);
                    throw new SeckillException(ResponseCode.SERVICE_EXCEPTION);
                }
            } catch (SeckillException e) {
                LOGGER.error("error to validate targetItem in addSeckill, seckillDTO : {}", JsonUtil.toJson(seckillDTO));
                throw new SeckillException(ResponseCode.BIZ_E_INVALIDATE_TARGET_ITEM);
            }

            // 通知商品平台创建团购商品
            // 目标商品、参团数量、团购价格、团购售出数量、活动开始时间、活动结束时间、直接存储于商品平台
            try {
                itemDTO.setItemType(DBConst.SECKILL_ITEM.getCode());
                itemDTO.setSaleBegin(null);
                itemDTO.setSaleEnd(null);
                itemDTO.setCreateTime(null);
                if (itemDTO.getItemImageDTOList() != null) {
                    for (ItemImageDTO imageDTO : itemDTO.getItemImageDTOList()) {
                        imageDTO.setId(null);
                    }
                }
                if (itemDTO.getItemLabelDTOList() != null) {
                    for (ItemLabelDTO itemLabelDTO : itemDTO.getItemLabelDTOList()) {
                        itemLabelDTO.setId(null);
                    }
                }
                itemDTO.setItemPropertyList(null);
                itemDTO.setCompositeItem(null);
                // startTime/endTime 不传
                itemDTO.setItemStatus(4);
                itemDTO.setValueAddedServiceTypeDTOList(null);
                List<LimitEntity> limitEntities = new ArrayList<LimitEntity>();
                LimitEntity limitEntity = new LimitEntity();
                limitEntity.setLimitCount(seckillDTO.getLimit());
                limitEntities.add(limitEntity);
                itemDTO.setBuyLimit(limitEntities);
                itemDTO.setSaleMaxNum(seckillDTO.getLimit().intValue() == 0 ? null : seckillDTO.getLimit());
                List<ItemSkuDTO> itemSkuDTOs = new ArrayList<ItemSkuDTO>();
                // 团购价, 原价是 market_price
                itemSkuDTO.setPromotionPrice(seckillDTO.getPrice());
                itemSkuDTO.setWirelessPrice(seckillDTO.getPrice());
                itemDTO.setPromotionPrice(seckillDTO.getPrice());
                itemDTO.setWirelessPrice(seckillDTO.getPrice());
                itemSkuDTO.setStockNum(seckillDTO.getSeckillItem().getStockNum());
                itemSkuDTOs.add(itemSkuDTO);
                itemDTO.setItemSkuDTOList(itemSkuDTOs);
                if (itemDTO.getFreightTemplate() == null && itemDTO.getFreight() == null) {
                    itemDTO.setFreight(0L);
                }
                itemDTO.setId(null);
                itemDTO.getItemSkuDTOList().get(0).setId(null);
                if (itemSkuDTO.getSkuPropertyDTOList() != null) {
                    for (SkuPropertyDTO skuPropertyDTO : itemSkuDTO.getSkuPropertyDTOList()) {
                        skuPropertyDTO.setId(null);
                    }
                }
                itemDTO = itemManager.addItem(itemDTO, appKey);
                if (itemDTO == null) throw new SeckillException(ResponseCode.SERVICE_EXCEPTION);
//                 填充新创建商品的信息
                seckillDTO.setItemId(itemDTO.getId());
                if (itemDTO.getItemSkuDTOList() == null || itemDTO.getItemSkuDTOList().isEmpty())
                    throw new SeckillException(ResponseCode.SERVICE_EXCEPTION);
                seckillDTO.setSkuId(itemDTO.getItemSkuDTOList().get(0).getId());
            } catch (SeckillException e) {
                LOGGER.error("error to add item in addSeckill, seckillDTO : {}", JsonUtil.toJson(seckillDTO), e);
                throw new SeckillException(ResponseCode.BIZ_E_INVALIDATE_TARGET_ITEM);
            }
            seckillDTO.setContent(itemDTO.getItemName());
            seckillDTO.setDeleteMark(0);
            seckillDTO.setStatus(SeckillStatus.NORMAL.getValue());
            Long id = seckillDAO.addSeckill(ModelUtil.genSeckillDO(seckillDTO));
            if (id == null) {
                LOGGER.error("error to addSeckill, seckillDTO : {}", JsonUtil.toJson(seckillDTO));
                throw new SeckillException(ResponseCode.SERVICE_EXCEPTION);
            }
            seckillDTO.setId(id);

        } catch (SeckillException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("error to addSeckill, seckillDTO : {}", JsonUtil.toJson(seckillDTO), e);
            throw new SeckillException(ResponseCode.BIZ_E_ADD_SECKILL);
        }
    }

    @Override
    public SeckillDTO getSeckill(Long id, Long sellerId, String bizCode, String appKey) throws SeckillException {
        try {
            SeckillDO seckillDO = seckillDAO.getSeckill(new SeckillDO(id, sellerId, bizCode));
            if (seckillDO == null) {
                LOGGER.error("error to getSeckill, seckillId : {}, sellerId : {}, bizCode : {}",
                        id, seckillDO, bizCode);
                throw new SeckillException(ResponseCode.BIZ_E_SECKILL_NOT_EXIST);
            }
            SeckillDTO seckillDTO = ModelUtil.genSeckillDTO(seckillDO);
            fillUpSeckillDTO(Arrays.asList(seckillDTO), appKey);
            itemManager.fillUpItem(Arrays.asList(seckillDTO), appKey);
            return seckillDTO;
        } catch (SeckillException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("error to getSeckill, seckillId : {}, sellerId : {}, bizCode : {}",
                    id, sellerId, bizCode, e);
            throw new SeckillException(ResponseCode.BIZ_E_SECKILL_NOT_EXIST);
        }
    }

    @Override
    public int updateSeckill(SeckillDO seckillDO) throws SeckillException {
        int opNum;
        try {
            opNum = seckillDAO.updateSeckill(seckillDO);
            if (opNum != 1) {
                LOGGER.error("error to updateSeckill, seckillDO : {}",
                        JsonUtil.toJson(seckillDO));
                throw new SeckillException(ResponseCode.BIZ_E_SECKILL_NOT_EXIST);
            }
        } catch (SeckillException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("error to updateSeckill, seckillDO : {}",
                    JsonUtil.toJson(seckillDO), e);
            throw new SeckillException(ResponseCode.DB_OP_ERROR);
        }
        return opNum;
    }

    @Override
    public void invalidSeckill(Long seckillId, Long sellerId, String bizCode) throws SeckillException {
        try {
            int opNum = seckillDAO.updateSeckill(new SeckillDO(seckillId, bizCode, sellerId, SeckillStatus.INVALID.getValue()));
            if (opNum != 1) {
                LOGGER.error("error to invalidSeckill, seckillId : {}, sellerId : {}, bizCode : {}",
                        seckillId, sellerId, bizCode);
                throw new SeckillException(ResponseCode.SERVICE_EXCEPTION);
            }
        } catch (SeckillException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("error to invalidSeckill, seckillId : {}, sellerId : {}, bizCode : {}",
                    seckillId, sellerId, bizCode, e);
            throw new SeckillException(ResponseCode.DB_OP_ERROR);
        }
    }

    @Override
    public List<SeckillDTO> querySeckill(SeckillQTO seckillQTO, String appKey) throws SeckillException {

        //根据lifecycle字段，设置时间查询条件
        if (seckillQTO.getLifecycle() != null) {
            Date now = new Date();
            if (seckillQTO.getLifecycle().intValue() == SeckillLifecycle.LIFECYCLE_NOT_BEGIN.getValue()) {
                seckillQTO.setStartTimeLt(now);
            } else if (seckillQTO.getLifecycle().intValue() == ActivityLifecycle.LIFECYCLE_IN_PROGRESS.getValue()) {
                seckillQTO.setStartTimeGe(now);
                seckillQTO.setEndTimeLe(now);
            } else if (seckillQTO.getLifecycle().intValue() == ActivityLifecycle.LIFECYCLE_ENDED.getValue()) {
                seckillQTO.setEndTimeGt(now);
            }

            // 未开始、进行中、已结束 均显示的是有效活动，无效活动只显示在全部
            if (seckillQTO.getLifecycle().intValue() != 0) {
                seckillQTO.setStatus(ActivityCouponStatus.NORMAL.getValue());
            }
        }

        try {
            List<SeckillDTO> seckillDTOs = ModelUtil.genSeckillDTOList(seckillDAO.querySeckill(seckillQTO));
            fillUpSeckillDTO(seckillDTOs, appKey);
            itemManager.fillUpItem(seckillDTOs, appKey);
            return seckillDTOs;
        } catch (SeckillException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("error to querySeckill, seckillQTO : {}", JsonUtil.toJson(seckillQTO), e);
            throw new SeckillException(ResponseCode.DB_OP_ERROR);
        }
    }

    @Override
    public List<SeckillDO> querySeckillSimple(SeckillQTO seckillQTO) throws SeckillException {
        try {
            List<SeckillDO> seckillDOs = seckillDAO.querySeckill(seckillQTO);
            return seckillDOs;
        } catch (Exception e) {
            LOGGER.error("error to querySeckillSimple, seckillQTO : {}", JsonUtil.toJson(seckillQTO), e);
            throw new SeckillException(ResponseCode.DB_OP_ERROR);
        }
    }

    @Override
    public SeckillDTO getSeckillBySkuId(Long sellerId, Long skuId, String bizCode, String appKey) throws SeckillException {
        try {
            SeckillDO seckillDO = new SeckillDO();
            seckillDO.setSkuId(skuId);
            seckillDO.setSellerId(sellerId);
            seckillDO.setBizCode(bizCode);

            seckillDO = seckillDAO.getSeckill(seckillDO);
            if (seckillDO == null) {
                LOGGER.error("error to getSeckillBySkuId, sellerId : {}, skuId : {}, bizCode : {}, appKey : {}",
                        sellerId, skuId, bizCode, appKey);
                throw new SeckillException(ResponseCode.BIZ_E_SECKILL_NOT_EXIST);
            }
            SeckillDTO seckillDTO = ModelUtil.genSeckillDTO(seckillDO);
            itemManager.fillUpItem(Arrays.asList(seckillDTO), appKey);
            fillUpSeckillDTO(Arrays.asList(seckillDTO), appKey);
            seckillDTO.setLifecycle(seckillDTO.getLifecycle().intValue() == 3 ? 4 : seckillDTO.getLifecycle());
            if (seckillDTO.getLifecycle().intValue() == 2) {
                if (seckillDTO.getSeckillItem().getStockNum().longValue() != 0
                        && seckillDTO.getSeckillItem().getStockNum().longValue() == seckillDTO.getSeckillItem().getFrozenNum().longValue())
                    seckillDTO.setLifecycle(3);
                if (seckillDTO.getSeckillItem().getStockNum().longValue() == 0)
                    seckillDTO.setLifecycle(4);
            }
            if (seckillDTO.getStatus().intValue() == SeckillStatus.INVALID.getValue()) seckillDTO.setLifecycle(4);

            return seckillDTO;
        } catch (SeckillException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("error to getSeckillBySkuId, sellerId : {}, skuId : {}, bizCode : {}",
                    sellerId, skuId, bizCode, e);
            throw new SeckillException(ResponseCode.BIZ_E_SECKILL_NOT_EXIST);
        }
    }

    /**
     * 填充 秒杀 数据
     *
     * @param seckillDTOs
     * @param appKey
     */
    private void fillUpSeckillDTO(List<SeckillDTO> seckillDTOs, String appKey) throws SeckillException {
        if (seckillDTOs.isEmpty()) return;
        long currentTime = System.currentTimeMillis();
        Map<Long, SeckillDTO> itemIdKey = new HashMap<>();
        for (SeckillDTO seckillDTO : seckillDTOs) {
            if (currentTime < seckillDTO.getStartTime().getTime()) {
                seckillDTO.setLifecycle(ActivityLifecycle.LIFECYCLE_NOT_BEGIN.getValue());
            } else if (currentTime >= seckillDTO.getStartTime().getTime()
                    && currentTime <= seckillDTO.getEndTime().getTime()) {
                seckillDTO.setLifecycle(ActivityLifecycle.LIFECYCLE_IN_PROGRESS.getValue());
            } else {
                seckillDTO.setLifecycle(ActivityLifecycle.LIFECYCLE_ENDED.getValue());
            }
            if (seckillDTO.getStatus().intValue() == ActivityStatus.INVALID.getValue().intValue()
                    || seckillDTO.getItemInvalidTime() != null) {
                seckillDTO.setLifecycle(ActivityLifecycle.LIFECYCLE_ENDED.getValue());
            }
            itemIdKey.put(seckillDTO.getItemId(), seckillDTO);
            seckillDTO.setSales(0L);
        }
        DataQTO dataQTO = new DataQTO();
        dataQTO.setItemIds(new ArrayList<>(itemIdKey.keySet()));
        List<ItemSalesVolumeDTO> itemSalesVolumeDTOs = tradeManager.queryItemSalesVolume(dataQTO, appKey);
        SeckillDTO seckillDTO;
        for (ItemSalesVolumeDTO itemSalesVolumeDTO : itemSalesVolumeDTOs) {
            seckillDTO = itemIdKey.get(itemSalesVolumeDTO.getItemId());
            if (seckillDTO != null) {
                seckillDTO.setSales(itemSalesVolumeDTO.getItemSalesVolume());
            }
        }
    }
}