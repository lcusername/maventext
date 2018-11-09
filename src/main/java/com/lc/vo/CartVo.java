package com.lc.vo;

import java.math.BigDecimal;
import java.util.List;

public class CartVo {
    //购物车信息
    private List<CartProductVo> cartProductVoList;
    //购物车总价格
    private BigDecimal cartTotalPrice;
    //    购物车是否全选
    private boolean allChecked;

    public List<CartProductVo> getCartProductVoList() {
        return cartProductVoList;
    }

    public void setCartProductVoList(List<CartProductVo> cartProductVoList) {
        this.cartProductVoList = cartProductVoList;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public boolean isAllChecked() {
        return allChecked;
    }

    public void setAllChecked(boolean allChecked) {
        this.allChecked = allChecked;
    }
}
