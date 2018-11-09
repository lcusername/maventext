package com.lc.controller.backend;

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

@RequestMapping(value = "/manage/order")
public class OrderBackendController {
    @Autowired
    OrderService orderService;
    /*
     * 后台订单list
     * */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                               @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
//        非空判断
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
//        权限校验
        if (userInfo.getRole() != ResponseCode.Roleenum.ROLE_ADMIN.getCode()) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NO_POVERS.getCode(),
                    ResponseCode.CategoryStatue.NO_POVERS.getDesc());
        }
        return orderService.list(null,pageNum,pageSize);
    }

    /*
     * 后台订单详情
     * */
    @RequestMapping(value = "/detail.do")
    public ServerResponse detail(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
//        非空判断
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
//        权限校验
        if (userInfo.getRole() != ResponseCode.Roleenum.ROLE_ADMIN.getCode()) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NO_POVERS.getCode(),
                    ResponseCode.CategoryStatue.NO_POVERS.getDesc());
        }
        return orderService.detail(orderNo);
    }

    /*
     * 后台-订单发货
     * */
    @RequestMapping(value = "/send_goods.do")
    public ServerResponse send_goods(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
//        非空判断
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
//        权限校验
        if (userInfo.getRole() != ResponseCode.Roleenum.ROLE_ADMIN.getCode()) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NO_POVERS.getCode(),
                    ResponseCode.CategoryStatue.NO_POVERS.getDesc());
        }
        return orderService.send_goods(orderNo);
    }

}
