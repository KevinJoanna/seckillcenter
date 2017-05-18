package com.mockuai.seckillcenter.core.manager;

import com.mockuai.itemcenter.common.domain.dto.ItemDTO;
import com.mockuai.itemcenter.common.domain.dto.ItemSkuDTO;
import com.mockuai.itemcenter.common.domain.dto.SkuInfoDTO;
import com.mockuai.itemcenter.common.domain.qto.ItemQTO;
import com.mockuai.itemcenter.common.domain.qto.ItemSkuQTO;
import com.mockuai.seckillcenter.common.domain.dto.SeckillDTO;
import com.mockuai.seckillcenter.core.exception.SeckillException;

import java.util.List;

/**
 * Created by edgar.zr on 12/4/15.
 */
public interface ItemManager {

    /**
     * 添加团购新商品
     *
     * @param itemDTO
     * @param appKey
     */
    ItemDTO addItem(ItemDTO itemDTO, String appKey) throws SeckillException;

    /**
     * 查询指定 sku 信息
     *
     * @param skuId
     * @param sellerId
     * @param appKey
     * @return
     * @throws SeckillException
     */
    ItemSkuDTO getItemSku(Long skuId, Long sellerId, String appKey) throws SeckillException;

    /**
     * 查询 item
     *
     * @param itemQTO
     * @param appKey
     * @return
     * @throws SeckillException
     */
    List<ItemDTO> queryItem(ItemQTO itemQTO, String appKey) throws SeckillException;

    /**
     * 查询 itemSku
     *
     * @param itemSkuQTO
     * @param appKey
     * @return
     * @throws SeckillException
     */
    List<ItemSkuDTO> queryItemSku(ItemSkuQTO itemSkuQTO, String appKey) throws SeckillException;

    /**
     * 查询指定 item 信息
     *
     * @param itemId
     * @param sellerId
     * @param appKey
     * @return
     * @throws SeckillException
     */
    ItemDTO getItem(Long itemId, Long sellerId, String appKey) throws SeckillException;

    /**
     * 填充商品信息
     *
     * @param seckillDTOs
     * @param appKey
     * @throws SeckillException
     */
    void fillUpItem(List<SeckillDTO> seckillDTOs, String appKey) throws SeckillException;

    /**
     * 通过 sku 查询完整商品信息
     *
     * @param skuidList
     * @param sellerId
     * @param appKey
     * @return
     * @throws SeckillException
     */
    List<SkuInfoDTO> querySkuInfo(List<Long> skuidList, Long sellerId, String appKey) throws SeckillException;
}