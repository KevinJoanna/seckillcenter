package com.mockuai.seckillcenter.common.domain.dto;

import java.io.Serializable;

/**
 * Created by edgar.zr on 12/15/15.
 */
public class SeckillForMopDTO implements Serializable {

    private String seckillUid;
    private String itemUid;
    private Long startTime;
    private Long endTime;
    private Long sales;
    private Long stockNum;
    private Integer lifecycle;

    public String getSeckillUid() {
        return seckillUid;
    }

    public void setSeckillUid(String seckillUid) {
        this.seckillUid = seckillUid;
    }

    public String getItemUid() {
        return itemUid;
    }

    public void setItemUid(String itemUid) {
        this.itemUid = itemUid;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getSales() {
        return sales;
    }

    public void setSales(Long sales) {
        this.sales = sales;
    }

    public Long getStockNum() {
        return stockNum;
    }

    public void setStockNum(Long stockNum) {
        this.stockNum = stockNum;
    }

    public Integer getLifecycle() {
        return lifecycle;
    }

    public void setLifecycle(Integer lifecycle) {
        this.lifecycle = lifecycle;
    }
}