package com.mockuai.seckillcenter.common.domain.qto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by edgar.zr on 12/4/15.
 */
public class SeckillQTO extends PageQTO implements Serializable {

    private Long id;
    private String bizCode;
    private Long sellerId;
    private Long skuId;
    private List<Long> skuIds;
    private Long itemId;
    private List<Long> itemIds;
    private Integer status;
    private Integer lifecycle;
    /**
     * 小于开始时间
     */
    private Date startTimeLt;
    /**
     * 大于等于开始时间
     */
    private Date startTimeGe;
    /**
     * 小于等于结束时间
     */
    private Date endTimeLe;
    /**
     * 大于结束时间
     */
    private Date endTimeGt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public List<Long> getSkuIds() {
        return skuIds;
    }

    public void setSkuIds(List<Long> skuIds) {
        this.skuIds = skuIds;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public List<Long> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Long> itemIds) {
        this.itemIds = itemIds;
    }

    public Integer getLifecycle() {
        return lifecycle;
    }

    public void setLifecycle(Integer lifecycle) {
        this.lifecycle = lifecycle;
    }

    public Date getStartTimeLt() {
        return startTimeLt;
    }

    public void setStartTimeLt(Date startTimeLt) {
        this.startTimeLt = startTimeLt;
    }

    public Date getStartTimeGe() {
        return startTimeGe;
    }

    public void setStartTimeGe(Date startTimeGe) {
        this.startTimeGe = startTimeGe;
    }

    public Date getEndTimeLe() {
        return endTimeLe;
    }

    public void setEndTimeLe(Date endTimeLe) {
        this.endTimeLe = endTimeLe;
    }

    public Date getEndTimeGt() {
        return endTimeGt;
    }

    public void setEndTimeGt(Date endTimeGt) {
        this.endTimeGt = endTimeGt;
    }
}