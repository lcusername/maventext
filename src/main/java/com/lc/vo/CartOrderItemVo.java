package com.lc.vo;

import com.lc.pojo.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public class CartOrderItemVo {
    private List<OrderItemVo> orderItemList;
    private String imageHost;
    private BigDecimal productTotalPrice;


    public List<OrderItemVo> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItemVo> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }
}
