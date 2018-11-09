package com.lc.service;

import com.lc.common.ServerResponse;

public interface CartService {

    /*
     * 购物车添加商品
     * */
    ServerResponse add(Integer userId,Integer productId, Integer count);
    /*
     * 购物车List列表
     * */
    ServerResponse list(Integer id);
    /*
     * 更新购物车某个产品数量
     * */
    ServerResponse update(Integer userId, Integer productId, Integer count);
    /*
     * 移除购物车某个产品
     * */
    ServerResponse delete_product(Integer id, String productIds);
    /*
     * 购物车选中某个商品
     * */
    ServerResponse select(Integer userId, Integer productId,Integer check);
    /*
     * 查询在购物车里的产品数量
     * */
    ServerResponse get_cart_product_count(Integer userId);
}
