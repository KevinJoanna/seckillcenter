package com.mockuai.seckillcenter.core.manager;

import com.mockuai.seckillcenter.core.exception.SeckillException;
import com.mockuai.tradecenter.common.domain.DataDTO;
import com.mockuai.tradecenter.common.domain.DataQTO;
import com.mockuai.tradecenter.common.domain.ItemSalesVolumeDTO;
import com.mockuai.tradecenter.common.domain.OrderDTO;
import com.mockuai.tradecenter.common.domain.OrderQTO;

import java.util.List;

/**
 * Created by edgar.zr on 12/13/15.
 */
public interface TradeManager {

    /**
     * 用户秒杀到后预下单
     * 如果正常返回，当前用户秒杀到，否则秒杀失败
     *
     * @param orderDTO userId, itemId, skuId, itemType
     * @param appKey
     */
    void addPreOrder(OrderDTO orderDTO, String appKey) throws SeckillException;

    /**
     * 查询当前进入秒杀活动的用户是否有未完成的下单的预下单
     *
     * @param orderQTO
     * @param appKey
     * @return true : 有未完成的 false : 无
     */
    boolean queryPreOrder(OrderQTO orderQTO, String appKey) throws SeckillException;

    /**
     * 查询销售数据
     *
     * @param dataQTO
     * @param appKey
     * @return
     */
    Long querySalesRatio(DataQTO dataQTO, String appKey) throws SeckillException;

    /**
     * 查询商品销售数据
     *
     * @param dataQTO
     * @param appKey
     * @return
     */
    List<ItemSalesVolumeDTO> queryItemSalesVolume(DataQTO dataQTO, String appKey) throws SeckillException;

    /**
     * 查询订单数量／订单关联商品数量
     *
     * @param dataQTO
     * @param appKey
     * @return
     */
    DataDTO getData(DataQTO dataQTO, String appKey) throws SeckillException;
}