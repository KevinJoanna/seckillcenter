package com.mockuai.seckillcenter.core.manager;

import com.mockuai.seckillcenter.common.domain.dto.SeckillDTO;
import com.mockuai.seckillcenter.common.domain.qto.SeckillQTO;
import com.mockuai.seckillcenter.core.domain.SeckillDO;
import com.mockuai.seckillcenter.core.exception.SeckillException;

import java.util.List;

/**
 * Created by edgar.zr on 12/4/15.
 */
public interface SeckillManager {

    /**
     * 创建 秒杀
     *
     * @param seckillDTO
     * @param appKey
     * @throws SeckillException
     */
    void addSeckill(SeckillDTO seckillDTO, String appKey) throws SeckillException;

    /**
     * @param id
     * @param sellerId
     * @param bizCode
     * @param appKey
     * @return
     * @throws SeckillException
     */
    SeckillDTO getSeckill(Long id, Long sellerId, String bizCode, String appKey) throws SeckillException;

    /**
     * 更新 seckill
     *
     * @param seckillDO
     * @return
     * @throws SeckillException
     */
    int updateSeckill(SeckillDO seckillDO) throws SeckillException;

    /**
     * 使秒杀失效
     *
     * @param SeckillId
     * @param sellerId
     * @param bizCode
     * @throws SeckillException
     */
    void invalidSeckill(Long SeckillId, Long sellerId, String bizCode) throws SeckillException;

    /**
     * 查询秒杀
     *
     * @param seckillQTO
     * @param appKey
     * @return
     * @throws SeckillException
     */
    List<SeckillDTO> querySeckill(SeckillQTO seckillQTO, String appKey) throws SeckillException;

    /**
     * 查询秒杀，不封装
     *
     * @param seckillQTO
     * @return
     * @throws SeckillException
     */
    List<SeckillDO> querySeckillSimple(SeckillQTO seckillQTO) throws SeckillException;

    /**
     * 根据 sku 查询相关活动
     *
     * @param sellerId
     * @param skuId
     * @param bizCode
     * @param appKey
     * @return
     * @throws SeckillException
     */
    SeckillDTO getSeckillBySkuId(Long sellerId, Long skuId, String bizCode, String appKey) throws SeckillException;
}