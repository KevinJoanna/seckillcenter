//package com.mockuai.groupbuy.core.engine.tool;
//
//import com.mockuai.groupbuy.core.engine.Context;
//import com.mockuai.groupbuy.core.engine.component.ComponentHolder;
//import com.mockuai.groupbuy.core.exception.SeckillException;
//
//public interface Tool<T> {
//    public void init();
//
//    public ComponentHolder getComponentHolder();
//
//    /**
//     * 执行优惠计算
//     *
//     * @param context
//     * @return
//     * @throws SeckillException
//     */
//    public DiscountInfo execute(Context context)
//            throws SeckillException;
//}