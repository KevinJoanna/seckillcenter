package com.mockuai.seckillcenter.core.dao.impl;

import com.mockuai.seckillcenter.common.domain.qto.SeckillHistoryQTO;
import com.mockuai.seckillcenter.core.dao.SeckillHistoryDAO;
import com.mockuai.seckillcenter.core.domain.SeckillHistoryDO;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import java.util.List;

/**
 * Created by edgar.zr on 12/4/15.
 */
public class SeckillHistoryDAOImpl extends SqlMapClientDaoSupport implements SeckillHistoryDAO {

    @Override
    public Long addSeckillHistory(SeckillHistoryDO seckillHistoryDO) {
        return (Long) getSqlMapClientTemplate().insert("seckill_history.addSeckillHistory", seckillHistoryDO);
    }

    @Override
    public List<SeckillHistoryDO> querySeckillHistory(SeckillHistoryQTO seckillHistoryQTO) {
        return getSqlMapClientTemplate().queryForList("seckill_history.querySeckillHistory", seckillHistoryQTO);
    }

    @Override
    public int updateSeckillHistory(SeckillHistoryDO seckillHistoryDO) {
        return getSqlMapClientTemplate().update("seckill_history.updateSeckillHistory", seckillHistoryDO);
    }
}