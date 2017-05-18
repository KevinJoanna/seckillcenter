package com.mockuai.seckillcenter.core.manager.impl;

import com.mockuai.seckillcenter.common.constant.ResponseCode;
import com.mockuai.seckillcenter.common.domain.dto.SeckillHistoryDTO;
import com.mockuai.seckillcenter.common.domain.qto.SeckillHistoryQTO;
import com.mockuai.seckillcenter.core.dao.SeckillHistoryDAO;
import com.mockuai.seckillcenter.core.domain.SeckillHistoryDO;
import com.mockuai.seckillcenter.core.exception.SeckillException;
import com.mockuai.seckillcenter.core.manager.SeckillHistoryManager;
import com.mockuai.seckillcenter.core.util.JsonUtil;
import com.mockuai.seckillcenter.core.util.ModelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by edgar.zr on 12/4/15.
 */
public class SeckillHistoryManagerImpl implements SeckillHistoryManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeckillHistoryManagerImpl.class);

    @Autowired
    private SeckillHistoryDAO seckillHistoryDAO;

    @Override
    public List<SeckillHistoryDTO> querySeckillHistory(SeckillHistoryQTO seckillHistoryQTO) throws SeckillException {
        try {
            List<SeckillHistoryDO> seckillHistoryDOs = seckillHistoryDAO.querySeckillHistory(seckillHistoryQTO);
            return ModelUtil.genSeckillHistoryDTOList(seckillHistoryDOs);
        } catch (Exception e) {
            LOGGER.error("error to querySeckillHistory, seckillHistoryQTO : {}",
                    JsonUtil.toJson(seckillHistoryQTO), e);
            throw new SeckillException(ResponseCode.DB_OP_ERROR);
        }
    }

    @Override
    public void addSeckillHistory(SeckillHistoryDO seckillHistoryDO) throws SeckillException {
        try {
            Long id = seckillHistoryDAO.addSeckillHistory(seckillHistoryDO);
            if (id == null) {
                LOGGER.error("error to addSeckillHistory, {}", JsonUtil.toJson(seckillHistoryDO));
                throw new SeckillException(ResponseCode.SERVICE_EXCEPTION);
            }
        } catch (SeckillException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("error to addSeckillHistory, {}",
                    JsonUtil.toJson(seckillHistoryDO), e);
            throw new SeckillException(ResponseCode.DB_OP_ERROR);
        }
    }

    @Override
    public void updateSeckillHistory(Long orderId, Long userId, Long skuId) throws SeckillException {
        try {
            SeckillHistoryDO seckillHistoryDO = new SeckillHistoryDO();
            seckillHistoryDO.setOrderId(orderId);
            seckillHistoryDO.setUserId(userId);
            seckillHistoryDO.setSkuId(skuId);
            int opNum = seckillHistoryDAO.updateSeckillHistory(seckillHistoryDO);
            if (opNum != 1) {
                LOGGER.error("error to updateSeckillHistory, orderId : {}, userId : {}, skuId : {}",
                        orderId, userId, skuId);
                throw new SeckillException(ResponseCode.SERVICE_EXCEPTION);
            }
        } catch (SeckillException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("error to updateSeckillHistory, orderId : {}, userId : {}, skuId : {}",
                    orderId, userId, skuId, e);
        }
    }
}