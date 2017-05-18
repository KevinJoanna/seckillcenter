package com.mockuai.seckillcenter.core.manager;

import com.mockuai.seckillcenter.common.domain.dto.SeckillHistoryDTO;
import com.mockuai.seckillcenter.common.domain.qto.SeckillHistoryQTO;
import com.mockuai.seckillcenter.core.domain.SeckillHistoryDO;
import com.mockuai.seckillcenter.core.exception.SeckillException;

import java.util.List;

/**
 * Created by edgar.zr on 12/4/15.
 */
public interface SeckillHistoryManager {

    /**
     * 查询用户秒杀记录
     *
     * @param seckillHistoryQTO
     * @return
     * @throws SeckillException
     */
    List<SeckillHistoryDTO> querySeckillHistory(SeckillHistoryQTO seckillHistoryQTO) throws SeckillException;

    /**
     * @param seckillHistoryDO
     * @throws SeckillException
     */
    void addSeckillHistory(SeckillHistoryDO seckillHistoryDO) throws SeckillException;

    /**
     * 更新用户秒杀记录
     *
     * @param orderId
     * @param userId
     * @param skuId
     * @throws SeckillException
     */
    void updateSeckillHistory(Long orderId, Long userId, Long skuId) throws SeckillException;
}