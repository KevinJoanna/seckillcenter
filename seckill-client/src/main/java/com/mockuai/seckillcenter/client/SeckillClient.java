package com.mockuai.seckillcenter.client;


import com.mockuai.seckillcenter.common.api.Response;
import com.mockuai.seckillcenter.common.domain.dto.SeckillDTO;
import com.mockuai.seckillcenter.common.domain.dto.SeckillForMopDTO;
import com.mockuai.seckillcenter.common.domain.qto.SeckillQTO;

import java.util.List;

/**
 * Created by edgar.zr on 12/2/15.
 */
public interface SeckillClient {

    /**
     * 创建秒杀
     *
     * @param seckillDTO
     * @return
     */
    Response<Long> addSeckill(SeckillDTO seckillDTO, String appKey);

    /**
     * 使指定秒杀失效
     *
     * @param seckillId
     * @param creatorId
     * @param appKey
     * @return
     */
    Response<Void> invalidSeckill(Long seckillId, Long creatorId, String appKey);


    /**
     * 获取指定秒杀
     *
     * @param seckillId
     * @param creatorId
     * @param appKey
     * @return
     */
    Response<SeckillDTO> getSeckill(Long seckillId, Long creatorId, String appKey);

    /**
     * 查询秒杀列表
     *
     * @param seckillQTO
     * @param appKey
     * @return
     */
    Response<List<SeckillDTO>> querySeckill(SeckillQTO seckillQTO, String appKey);

    /**
     * 验证结算
     *
     * @param skuId
     * @param userId
     * @param sellerId
     * @param appKey
     * @return
     */
    Response<SeckillDTO> validateForSettlement(Long skuId, Long userId, Long sellerId, String appKey);

    /**
     * 获取秒杀活动信息
     *
     * @param skuId
     * @param userId
     * @param sellerId
     * @param appKey
     * @return
     */
    Response<SeckillForMopDTO> querySeckillByItem(Long skuId, Long userId, Long sellerId, String appKey);

    /**
     * 批量查询秒杀活动信息
     *
     * @param seckillQTO
     * @param appKey
     * @return
     */
    Response<List<SeckillForMopDTO>> querySeckillByItemBatch(SeckillQTO seckillQTO, String appKey);
}