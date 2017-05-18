package com.mockuai.seckillcenter.core.domain;

import java.util.Date;

/**
 * Created by edgar.zr on 12/4/15.
 */
public class SeckillDO {

    private Long id;
    private String bizCode;
    private Long sellerId;
    private Long itemSellerId;
    private Long itemId;
    private Long skuId;
    private String content;
    private Date startTime;
    private Date endTime;
    /**
     * 商品售空的时间
     */
    private Date itemInvalidTime;
    private Integer status;
    private Integer deleteMark;
    private Date gmtCreated;
    private Date gmtModified;

    public SeckillDO() {
    }

    public SeckillDO(Long id, Long sellerId, String bizCode) {
        this.id = id;
        this.sellerId = sellerId;
        this.bizCode = bizCode;
    }

    public SeckillDO(Long id, String bizCode, Long sellerId, Integer status) {
        this.id = id;
        this.bizCode = bizCode;
        this.sellerId = sellerId;
        this.status = status;
    }

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

    public Long getItemSellerId() {
        return itemSellerId;
    }

    public void setItemSellerId(Long itemSellerId) {
        this.itemSellerId = itemSellerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getItemInvalidTime() {
        return itemInvalidTime;
    }

    public void setItemInvalidTime(Date itemInvalidTime) {
        this.itemInvalidTime = itemInvalidTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeleteMark() {
        return deleteMark;
    }

    public void setDeleteMark(Integer deleteMark) {
        this.deleteMark = deleteMark;
    }

    public Date getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(Date gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}