package com.mockuai.seckillcenter.core.dao;

import com.mockuai.seckillcenter.common.domain.qto.SeckillQTO;
import com.mockuai.seckillcenter.core.domain.SeckillDO;

import java.util.List;

/**
 * Created by edgar.zr on 12/4/15.
 */
public interface SeckillDAO {

    Long addSeckill(SeckillDO seckillDO);

    SeckillDO getSeckill(SeckillDO seckillDO);

    List<SeckillDO> querySeckill(SeckillQTO seckillQTO);

    int updateSeckill(SeckillDO seckillDO);
}