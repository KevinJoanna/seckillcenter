package com.mockuai.seckillcenter.core.manager.impl;

import com.mockuai.seckillcenter.common.constant.ResponseCode;
import com.mockuai.seckillcenter.core.exception.SeckillException;
import com.mockuai.seckillcenter.core.manager.TradeManager;
import com.mockuai.seckillcenter.core.util.JsonUtil;
import com.mockuai.tradecenter.client.OrderClient;
import com.mockuai.tradecenter.client.PreOrderClient;
import com.mockuai.tradecenter.client.TradeDataReportClient;
import com.mockuai.tradecenter.common.api.Response;
import com.mockuai.tradecenter.common.domain.DataDTO;
import com.mockuai.tradecenter.common.domain.DataQTO;
import com.mockuai.tradecenter.common.domain.ItemSalesVolumeDTO;
import com.mockuai.tradecenter.common.domain.OrderDTO;
import com.mockuai.tradecenter.common.domain.OrderQTO;
import com.mockuai.tradecenter.common.domain.SalesTotalDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

/**
 * Created by edgar.zr on 12/13/15.
 */
public class TradeManagerImpl implements TradeManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeManagerImpl.class);

    @Autowired
    private PreOrderClient preOrderClient;

    @Autowired
    private TradeDataReportClient tradeDataReportClient;

    @Autowired
    private OrderClient orderClient;

    @Override
    public void addPreOrder(OrderDTO orderDTO, String appKey) throws SeckillException {
        try {
            Response response = preOrderClient.addPreOrder(orderDTO, appKey);
            if (response.isSuccess()) {
                return;
            }
            LOGGER.error("error to addPreOrder, orderDTO : {}, appKey : {}, errCode : {}, errMsg : {}",
                    JsonUtil.toJson(orderDTO), appKey, response.getCode(), response.getMessage());
            throw new SeckillException(ResponseCode.BIZ_E_SECKILL_PRE_ORDER_ALREADY);
        } catch (SeckillException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("error to addPreOrder, orderDTO : {}, appKey : {}", JsonUtil.toJson(orderDTO), appKey, e);
            throw new SeckillException(ResponseCode.BIZ_E_SECKILL_FAIL_TO_PRE_ORDER);
        }
    }

    @Override
    public boolean queryPreOrder(OrderQTO orderQTO, String appKey) throws SeckillException {
        try {
            Response response = preOrderClient.queryPreOrder(orderQTO, appKey);
            if (response.isSuccess()) {
                return response.getModule() != null;
            }
            if (response.getCode() == 30001)
                LOGGER.info("preOrder doesn't exits, orderQTO : {}, appKey : {}, errCode : {}, errMsg : {}",
                        JsonUtil.toJson(orderQTO), appKey, response.getCode(), response.getMessage());
            else
                LOGGER.error("error to queryPreOrder, orderQTO : {}, appKey : {}, errCode : {}, errMsg : {}",
                        JsonUtil.toJson(orderQTO), appKey, response.getCode(), response.getMessage());
            return false;
        } catch (Exception e) {
            LOGGER.error("error to queryPreOrder, orderQTO : {}, appKey : {}", JsonUtil.toJson(orderQTO), appKey, e);
            return false;
        }
    }

    public Long querySalesRatio(DataQTO dataQTO, String appKey) throws SeckillException {
        try {
            Response<SalesTotalDTO> response = tradeDataReportClient.querySalesRatio(dataQTO, appKey);
            if (response.isSuccess()) {
                if (response.getModule() == null)
                    return 0L;
                else if (response.getModule().getSalesVolumes() == null)
                    return 0L;
                else
                    return Long.valueOf(response.getModule().getSalesVolumes());
            }
            LOGGER.error("error to querySalesRatio, dataQTO : {}, errCode : {}, errMsg : {}, appKey :{}",
                    JsonUtil.toJson(dataQTO), response.getCode(), response.getMessage(), appKey);
            throw new SeckillException(ResponseCode.SERVICE_EXCEPTION, response.getMessage());
        } catch (SeckillException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("error to querySalesRatio, dataQTO : {}, appKey :{}",
                    JsonUtil.toJson(dataQTO), appKey, e);
            throw new SeckillException(ResponseCode.SERVICE_EXCEPTION);
        }
    }

    @Override
    public List<ItemSalesVolumeDTO> queryItemSalesVolume(DataQTO dataQTO, String appKey) throws SeckillException {
        try {
            Response<List<ItemSalesVolumeDTO>> response = tradeDataReportClient.queryItemSalesVolume(dataQTO, appKey);
            if (response.isSuccess()) {
                return response.getModule() != null ? response.getModule() : Collections.EMPTY_LIST;
            }
            LOGGER.error("error to queryItemSalesVolume, dataQTO : {}, errCode : {}, errMsg : {}, appKey :{}",
                    JsonUtil.toJson(dataQTO), response.getCode(), response.getMessage(), appKey);
            throw new SeckillException(ResponseCode.SERVICE_EXCEPTION, response.getMessage());
        } catch (SeckillException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("error to queryItemSalesVolume, dataQTO : {}, appKey :{}",
                    JsonUtil.toJson(dataQTO), appKey, e);
            throw new SeckillException(ResponseCode.SERVICE_EXCEPTION);
        }
    }

    @Override
    public DataDTO getData(DataQTO dataQTO, String appKey) throws SeckillException {
        try {
            Response<DataDTO> response = orderClient.getData(dataQTO, appKey);
            if (response.isSuccess()) {
                return response.getModule();
            }
            LOGGER.error("error to getData, dataQTO : {}, appKey :{}, errCode : {}, errMsg : {}",
                    JsonUtil.toJson(dataQTO), appKey, response.getCode(), response.getMessage());
            throw new SeckillException(ResponseCode.SERVICE_EXCEPTION, response.getMessage());
        } catch (SeckillException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("error to getData, dataQTO : {}, appKey :{}",
                    JsonUtil.toJson(dataQTO), appKey, e);
            throw new SeckillException(ResponseCode.SERVICE_EXCEPTION);
        }
    }
}