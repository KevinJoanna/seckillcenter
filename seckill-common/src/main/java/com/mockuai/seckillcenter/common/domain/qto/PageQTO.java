package com.mockuai.seckillcenter.common.domain.qto;

public class PageQTO {

    public static final Integer DEFAULT_OFFSET = 0;
    public static final Integer DEFAULT_COUNT = 20;

    private Integer offset;
    private Integer count;
    private Integer totalCount;

    public Integer getOffset() {
        return this.offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}