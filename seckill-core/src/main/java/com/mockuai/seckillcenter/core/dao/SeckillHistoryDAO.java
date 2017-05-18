package com.mockuai.seckillcenter.core.dao;

import com.mockuai.seckillcenter.common.domain.qto.SeckillHistoryQTO;
import com.mockuai.seckillcenter.core.domain.SeckillHistoryDO;

import java.util.List;

/**
 * Created by edgar.zr on 12/4/15.
 */
public interface SeckillHistoryDAO {

    Long addSeckillHistory(SeckillHistoryDO seckillHistoryDO);

    List<SeckillHistoryDO> querySeckillHistory(SeckillHistoryQTO seckillHistoryQTO);

    int updateSeckillHistory(SeckillHistoryDO seckillHistoryDO);
}