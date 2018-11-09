package com.lc.service;

import com.lc.common.ServerResponse;

public interface OrderService {
    /*
    * 创建订单
    * */
    ServerResponse create(Integer userId,Integer shoppingId);
    /*
     * 获取购物车账单明细
     * */
    ServerResponse get_order_cart_product(Integer userId);
    /*
     * 订单List
     * */
    ServerResponse list(Integer userId, Integer pageNum, Integer pageSize);
    /*
     * 取消订单
     * */
    ServerResponse cancel(Integer userId, Long orderNo);
    /*
     * 订单详情detail
     * */
    ServerResponse detail( Long orderNo);
    /*
     * 后台-订单发货
     * */
    ServerResponse send_goods(Long orderNo);
}
