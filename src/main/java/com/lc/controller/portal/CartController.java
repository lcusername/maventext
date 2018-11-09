package com.lc.controller.portal;

import com.lc.common.ResponseCode;
import com.lc.common.ServerResponse;
import com.lc.pojo.UserInfo;
import com.lc.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/cart")
public class CartController {
    @Autowired
    CartService cartService;

    /*
     * 购物车添加商品
     * */
    //参数商品id和购买数量
    @RequestMapping(value = "/add.do")
    public ServerResponse add(HttpSession session, Integer productId, Integer count) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
        return cartService.add(userInfo.getId(),productId,count);
    }

    /*
     * 购物车List列表
     * */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
        return cartService.list(userInfo.getId());
    }
    /*
    * 更新购物车某个产品数量
    * */
    @RequestMapping(value = "/update.do")
    public ServerResponse update(HttpSession session,Integer productId, Integer count) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
        return cartService.update(userInfo.getId(),productId,count);
    }

    /*
    * 移除购物车某个产品
    * */
    @RequestMapping(value = "/delete_product.do")
    public ServerResponse delete_product(HttpSession session,String productIds) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
        return cartService.delete_product(userInfo.getId(),productIds);
    }
    /*
     * 购物车选中某个商品
     * 选中操作
     * 共同调用select方法
     * 给出check即可
     * */
    @RequestMapping(value = "/select.do")
    public ServerResponse select(HttpSession session,Integer productId) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
        return cartService.select(userInfo.getId(),productId,ResponseCode.CartCheckStuts.PRODUCT_CHECK.getCode());
    }
    /*
     * 购物车取消选中某个商品
     *
     * */
    @RequestMapping(value = "/un_select.do")
    public ServerResponse un_select(HttpSession session,Integer productId) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
        return cartService.select(userInfo.getId(),productId,ResponseCode.CartCheckStuts.PRODUCT_NOCHECK.getCode());
    }
    /*
     * 购物车全选
     * */
    @RequestMapping(value = "/select_all.do")
    public ServerResponse select_all(HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
        return cartService.select(userInfo.getId(),null,ResponseCode.CartCheckStuts.PRODUCT_CHECK.getCode());
    }

    /*
     * 购物车取消全选
     * */
    @RequestMapping(value = "/un_select_all.do")
    public ServerResponse un_select_all(HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
        return cartService.select(userInfo.getId(),null,ResponseCode.CartCheckStuts.PRODUCT_NOCHECK.getCode());
    }
    /*
     * 查询在购物车里的产品数量
     * */
    @RequestMapping(value = "/get_cart_product_count.do")
    public ServerResponse get_cart_product_count(HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
        return cartService.get_cart_product_count(userInfo.getId());
    }


}
