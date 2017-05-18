package com.mockuai.seckillcenter.common.domain.qto;

import java.io.Serializable;

/**
 * Created by edgar.zr on 12/4/15.
 */
public class SeckillHistoryQTO extends PageQTO implements Serializable {

    private Long id;
    private String bizCode;
    private Long seckillId;
    private Long sellerId;
    private Long orderId;
    private Long userId;
    private Integer status;
    private Integer notStatus;

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

    public Long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Long seckillId) {
        this.seckillId = seckillId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getNotStatus() {
        return notStatus;
    }

    public void setNotStatus(Integer notStatus) {
        this.notStatus = notStatus;
    }
}