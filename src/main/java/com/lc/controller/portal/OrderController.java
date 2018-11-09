package com.lc.controller.portal;

import com.lc.common.ResponseCode;
import com.lc.common.ServerResponse;
import com.lc.pojo.UserInfo;
import com.lc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    /*
    * 创建订单
    * */
    @RequestMapping(value = "/create.do")
    public ServerResponse create(HttpSession session,Integer shoppingId){
        UserInfo userInfo=(UserInfo)session.getAttribute(ResponseCode.Const);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }

        return orderService.create(userInfo.getId(),shoppingId) ;
    }

    /*
     * 获取购物车账单明细
     * */
    @RequestMapping(value = "/get_order_cart_product.do")
    public ServerResponse get_order_cart_product(HttpSession session){
        UserInfo userInfo=(UserInfo)session.getAttribute(ResponseCode.Const);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }

        return orderService.get_order_cart_product(userInfo.getId()) ;
    }

    /*
     * 订单List
     * */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session,
                                @RequestParam(required = false,defaultValue = "1")Integer pageNum,
                               @RequestParam(required = false,defaultValue = "10")Integer pageSize){
        UserInfo userInfo=(UserInfo)session.getAttribute(ResponseCode.Const);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }

        return orderService.list(userInfo.getId(),pageNum,pageSize) ;
    }

    /*
     * 取消订单
     * */
    @RequestMapping(value = "/cancel.do")
    public ServerResponse cancel(HttpSession session,Long orderNo){
        UserInfo userInfo=(UserInfo)session.getAttribute(ResponseCode.Const);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }

        return orderService.cancel(userInfo.getId(),orderNo) ;
    }

    /*
     * 订单详情detail
     * */
    @RequestMapping(value = "/detail.do")
    public ServerResponse detail(HttpSession session,Long orderNo){
        UserInfo userInfo=(UserInfo)session.getAttribute(ResponseCode.Const);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }

        return orderService.detail(orderNo) ;
    }
}
