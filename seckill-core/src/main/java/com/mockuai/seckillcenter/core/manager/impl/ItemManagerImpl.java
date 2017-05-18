package com.mockuai.seckillcenter.core.manager.impl;

import com.mockuai.itemcenter.client.ItemClient;
import com.mockuai.itemcenter.client.ItemEfficientClient;
import com.mockuai.itemcenter.client.ItemSkuClient;
import com.mockuai.itemcenter.common.api.Response;
import com.mockuai.itemcenter.common.domain.dto.ItemDTO;
import com.mockuai.itemcenter.common.domain.dto.ItemSkuDTO;
import com.mockuai.itemcenter.common.domain.dto.SkuInfoDTO;
import com.mockuai.itemcenter.common.domain.qto.ItemQTO;
import com.mockuai.itemcenter.common.domain.qto.ItemSkuQTO;
import com.mockuai.seckillcenter.common.constant.ResponseCode;
import com.mockuai.seckillcenter.common.domain.dto.SeckillDTO;
import com.mockuai.seckillcenter.common.domain.dto.SeckillItemDTO;
import com.mockuai.seckillcenter.core.exception.SeckillException;
import com.mockuai.seckillcenter.core.manager.ItemManager;
import com.mockuai.seckillcenter.core.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by edgar.zr on 12/4/15.
 */
public class ItemManagerImpl implements ItemManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemManagerImpl.class);

    @Autowired
    private ItemSkuClient itemSkuClient;

    @Autowired
    private ItemClient itemClient;

    @Autowired
    private ItemEfficientClient itemEfficientClient;

    @Override
    public ItemDTO addItem(ItemDTO itemDTO, String appKey) throws SeckillException {
        try {
            Response<ItemDTO> response = itemClient.addItem(itemDTO, appKey);
            if (response.isSuccess()) {
                return response.getModule();
            }
            LOGGER.error("error to addItem, itemDTO : {}, appKey : {}, errCode : {}, errMsg : {}",
                    JsonUtil.toJson(itemDTO), appKey, response.getCode(), response.getMessage());
            throw new SeckillException(ResponseCode.SERVICE_EXCEPTION);
        } catch (SeckillException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("error to addItem, itemDTO : {}, appKey : {}", JsonUtil.toJson(itemDTO), appKey, e);
            throw new SeckillException(ResponseCode.SERVICE_EXCEPTION);
        }
    }

    @Override
    public ItemSkuDTO getItemSku(Long skuId, Long sellerId, String appKey) throws SeckillException {
        try {
            Response<ItemSkuDTO> response = itemSkuClient.getItemSku(skuId, sellerId, appKey);
            if (response.isSuccess()) {
                return response.getModule();
            }
            LOGGER.error("error to getItemSku, skuId : {}, sellerId : {}, appKey : {}, errCode : {}, errMsg : {}",
                    skuId, sellerId, appKey, response.getCode(), response.getMessage());
            throw new SeckillException(ResponseCode.SERVICE_EXCEPTION);
        } catch (SeckillException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("error to getItemSku, skuId : {}, sellerId : {}, appKey : {}", skuId, sellerId, appKey, e);
            throw new SeckillException(ResponseCode.SERVICE_EXCEPTION);
        }
    }

    @Override
    public List<ItemDTO> queryItem(ItemQTO itemQTO, String appKey) throws SeckillException {
        try {
            Response<List<ItemDTO>> response = itemClient.queryItem(itemQTO, appKey);
            if (response.isSuccess()) {
                return response.getModule();
            }
            LOGGER.error("error to queryItem, itemQTO : {}, appKey : {}, errCode : {}, errMsg : {}",
                    JsonUtil.toJson(itemQTO), appKey, response.getCode(), response.getMessage());
            throw new SeckillException(ResponseCode.SERVICE_EXCEPTION);
        } catch (SeckillException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("error to queryItem, itemQTO : {}, appKey : {}", JsonUtil.toJson(itemQTO), appKey, e);
            throw new SeckillException(ResponseCode.SERVICE_EXCEPTION);
        }
    }

    @Override
    public List<ItemSkuDTO> queryItemSku(ItemSkuQTO itemSkuQTO, String appKey) throws SeckillException {
        try {
            Response<List<ItemSkuDTO>> response = itemSkuClient.queryItemSku(itemSkuQTO, appKey);
            if (response.isSuccess()) {
                return response.getModule();
            }
            LOGGER.error("error to queryItemSku, itemSkuQTO : {}, appKey : {}, errCode : {}, errMsg : {}",
                    JsonUtil.toJson(itemSkuQTO), appKey, response.getCode(), response.getMessage());
            throw new SeckillException(ResponseCode.SERVICE_EXCEPTION);
        } catch (SeckillException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("error to queryItemSku, itemSkuQTO : {}, appKey : {}", JsonUtil.toJson(itemSkuQTO), appKey, e);
            throw new SeckillException(ResponseCode.SERVICE_EXCEPTION);
        }
    }

    @Override
    public ItemDTO getItem(Long itemId, Long sellerId, String appKey) throws SeckillException {
        try {
            Response<ItemDTO> response = itemClient.getItem(itemId, sellerId, true, appKey);
            if (response.isSuccess()) {
                return response.getModule();
            }
            LOGGER.error("error to getItem, itemId : {}, sellerId : {}, appKey : {}, errCode : {}, errMsg : {}",
                    itemId, sellerId, appKey, response.getCode(), response.getMessage());
            throw new SeckillException(ResponseCode.BIZ_E_INVALIDATE_TARGET_ITEM);
        } catch (SeckillException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("error to getItem, itemId : {}, sellerId : {}, appKey : {}", itemId, sellerId, appKey, e);
            throw new SeckillException(ResponseCode.BIZ_E_INVALIDATE_TARGET_ITEM);
        }
    }

    @Override
    public void fillUpItem(List<SeckillDTO> seckillDTOs, String appKey) throws SeckillException {
        if (seckillDTOs == null || seckillDTOs.isEmpty()) return;

	    Map<Long, Set<Long>> sellerIdKeySkuIdValue = new HashMap<>();
	    Map<Long, SeckillDTO> skuIdKeySeckillValue = new HashMap<>();

	    for (SeckillDTO seckillDTO : seckillDTOs) {
		    seckillDTO.setSeckillItem(new SeckillItemDTO());
		    seckillDTO.getSeckillItem().setItemId(seckillDTO.getItemId());
		    seckillDTO.getSeckillItem().setSellerId(seckillDTO.getSellerId());

			if (seckillDTO.getItemSellerId() == null){
				seckillDTO.setItemSellerId(seckillDTO.getSellerId());
			}

		    if (!sellerIdKeySkuIdValue.containsKey(seckillDTO.getItemSellerId())) {
			    sellerIdKeySkuIdValue.put(seckillDTO.getItemSellerId(), new HashSet<Long>());
		    }
		    sellerIdKeySkuIdValue.get(seckillDTO.getItemSellerId()).add(seckillDTO.getSkuId());

		    skuIdKeySeckillValue.put(seckillDTO.getSkuId(), seckillDTO);
	    }

	    try {
		    Long sellerId;
		    List<Long> skuIds;
		    List<SkuInfoDTO> skuInfoDTOs;
		    ItemDTO itemDTO;
		    ItemSkuDTO itemSkuDTO;
		    SeckillDTO seckillDTO;
		    for (Map.Entry<Long, Set<Long>> entry : sellerIdKeySkuIdValue.entrySet()) {
			    sellerId = entry.getKey();
			    skuIds = new ArrayList<>(entry.getValue());
			    skuInfoDTOs = this.querySkuInfo(skuIds, sellerId, appKey);
			    if (skuInfoDTOs == null || skuInfoDTOs.size() != skuIds.size()) {
				    LOGGER.error("error to get skuInfoDTOs with skuIds : {}, sellerId : {}, response : {}, appKey : {}"
						    , JsonUtil.toJson(entry.getValue()), entry.getKey(), JsonUtil.toJson(skuInfoDTOs), appKey);
				    throw new SeckillException(ResponseCode.SERVICE_EXCEPTION);
			    }
			    for (SkuInfoDTO skuInfoDTO : skuInfoDTOs) {
				    itemDTO = skuInfoDTO.getItemDTO();
				    itemSkuDTO = skuInfoDTO.getItemSkuDTO();
				    seckillDTO = skuIdKeySeckillValue.get(itemSkuDTO.getId());
				    if (itemDTO.getBuyLimit() == null) {
					    seckillDTO.setLimit(0);
				    } else {
					    seckillDTO.setLimit(itemDTO.getBuyLimit().get(0).getLimitCount());
				    }
				    seckillDTO.getSeckillItem().setName(itemDTO.getItemName());
				    seckillDTO.setPrice(itemSkuDTO.getPromotionPrice());
				    seckillDTO.getSeckillItem().setPrice(itemSkuDTO.getMarketPrice());
				    seckillDTO.getSeckillItem().setSkuSnapshot(itemSkuDTO.getSkuCode());
				    seckillDTO.getSeckillItem().setIconUrl(itemSkuDTO.getImageUrl());
				    seckillDTO.getSeckillItem().setStockNum(itemSkuDTO.getStockNum() != null ? itemSkuDTO.getStockNum() : 0L);
				    seckillDTO.getSeckillItem().setFrozenNum(itemSkuDTO.getFrozenStockNum() != null ? itemSkuDTO.getFrozenStockNum() : 0L);
			    }
		    }
	    } catch (SeckillException e) {
		    throw e;
	    }catch (Exception e){
		    LOGGER.error("error to fillUp seckill", e);
		    throw new SeckillException(ResponseCode.SERVICE_EXCEPTION);
	    }
    }

    @Override
    public List<SkuInfoDTO> querySkuInfo(List<Long> skuIdList, Long sellerId, String appKey) throws SeckillException {
        try {
            Response<List<SkuInfoDTO>> response = itemEfficientClient.querySkuInfo(skuIdList, sellerId, appKey);
            if (response.isSuccess()) {
                return response.getModule();
            }
            LOGGER.error("error to querySkuInfo, skuIdList : {}, sellerId : {}, appKey : {}, errCode : {}, errMsg : {}",
                    Arrays.deepToString(skuIdList.toArray()), sellerId, appKey, response.getCode(), response.getMessage());
            throw new SeckillException(response.getCode(), response.getMessage());
        } catch (SeckillException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("error to querySkuInfo, skuIdList : {}, sellerId : {}, appKey : {}"
                    , Arrays.deepToString(skuIdList.toArray()), sellerId, appKey, e);
            throw new SeckillException(ResponseCode.SERVICE_EXCEPTION);
        }
    }
}