//package com.mockuai.marketingcenter.core.engine.component.impl;
//
//
//import com.mockuai.marketingcenter.core.engine.Context;
//import com.mockuai.marketingcenter.core.engine.component.Component;
//
//import java.util.List;
//
//
//public class ComputeTotalFee
//        implements Component<Long> {
//
//    public void init() {
//
//    }
//
//
//    public Long execute(Context context) {
//
//        List orderItemList = (List) context.getParam("order_item_list");
//
//        long totalFee = 0L;
//
//        for (OrderItemDTO orderItemDTO : orderItemList) {
//
//            totalFee += orderItemDTO.getUnitPrice().longValue() * orderItemDTO.getNumber().intValue();
//
//        }
//
//        return Long.valueOf(totalFee);
//
//    }
//
//}
//
///* Location:           /work/tmp/marketingcenter/WEB-INF/classes/
// * Qualified Name:     com.mockuai.marketingcenter.core.engine.component.impl.ComputeTotalFee
// * JD-Core Version:    0.6.2
// */