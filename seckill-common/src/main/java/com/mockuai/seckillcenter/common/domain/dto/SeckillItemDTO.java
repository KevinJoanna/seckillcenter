package com.mockuai.seckillcenter.common.domain.dto;

import java.io.Serializable;

/**
 * Created by edgar.zr on 12/4/15.
 */
public class SeckillItemDTO implements Serializable {

    private Long sellerId;
    private String skuSnapshot;
    private Long itemId;
    private Long skuId;
    private String name;
    /**
     * 原价
     */
    private Long price;
    private String iconUrl;
    private Long stockNum;
    /**
     * 冻结数量
     */
    private Long frozenNum;

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getSkuSnapshot() {
        return skuSnapshot;
    }

    public void setSkuSnapshot(String skuSnapshot) {
        this.skuSnapshot = skuSnapshot;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Long getStockNum() {
        return stockNum;
    }

    public void setStockNum(Long stockNum) {
        this.stockNum = stockNum;
    }

    public Long getFrozenNum() {
        return frozenNum;
    }

    public void setFrozenNum(Long frozenNum) {
        this.frozenNum = frozenNum;
    }
}