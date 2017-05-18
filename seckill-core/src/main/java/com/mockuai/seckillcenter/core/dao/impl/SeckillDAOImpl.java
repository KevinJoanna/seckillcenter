package com.mockuai.seckillcenter.core.dao.impl;

import com.mockuai.seckillcenter.common.domain.qto.SeckillQTO;
import com.mockuai.seckillcenter.core.dao.SeckillDAO;
import com.mockuai.seckillcenter.core.domain.SeckillDO;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.List;

/**
 * Created by edgar.zr on 12/4/15.
 */
public class SeckillDAOImpl extends SqlMapClientDaoSupport implements SeckillDAO {

    @Override
    public Long addSeckill(SeckillDO seckillDO) {
        return ((Long) getSqlMapClientTemplate().insert("seckill.addSeckill", seckillDO));
    }

    @Override
    public SeckillDO getSeckill(SeckillDO seckillDO) {
        return (SeckillDO) getSqlMapClientTemplate().queryForObject("seckill.getSeckill", seckillDO);
    }

    @Override
    public List<SeckillDO> querySeckill(SeckillQTO seckillQTO) {
        seckillQTO.setTotalCount((Integer) getSqlMapClientTemplate().queryForObject("seckill.countOfQuerySeckill", seckillQTO));
        return getSqlMapClientTemplate().queryForList("seckill.querySeckill", seckillQTO);
    }

    @Override
    public int updateSeckill(SeckillDO seckillDO) {
        return getSqlMapClientTemplate().update("seckill.updateSeckill", seckillDO);
    }
}