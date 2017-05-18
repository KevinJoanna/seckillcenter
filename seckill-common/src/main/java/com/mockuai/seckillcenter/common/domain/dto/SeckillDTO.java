package com.mockuai.seckillcenter.common.domain.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by edgar.zr on 12/4/15.
 */
public class SeckillDTO implements Serializable {

    private Long id;
    private String bizCode;
    private Long sellerId;
    /**
     * 多店铺商品卖家 id
     */
    private Long itemSellerId;
    private Long itemId;
    private Long skuId;
    private String content;
    private Date startTime;
    private Date endTime;

    private Integer limit;
    private Long price;
    private Long sales;

    private SeckillItemDTO seckillItem;
    private Integer lifecycle;
    /**
     * 商品售空的时间
     */
    private Date itemInvalidTime;
    private Integer status;
    private Integer deleteMark;
    private Date gmtCreated;
    private Date gmtModified;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getSales() {
        return sales;
    }

    public void setSales(Long sales) {
        this.sales = sales;
    }

    public SeckillItemDTO getSeckillItem() {
        return seckillItem;
    }

    public void setSeckillItem(SeckillItemDTO seckillItem) {
        this.seckillItem = seckillItem;
    }

    public Integer getLifecycle() {
        return lifecycle;
    }

    public void setLifecycle(Integer lifecycle) {
        this.lifecycle = lifecycle;
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