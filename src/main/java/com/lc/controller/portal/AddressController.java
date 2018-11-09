package com.lc.controller.portal;

import com.lc.common.ResponseCode;
import com.lc.common.ServerResponse;
import com.lc.pojo.Shopping;
import com.lc.pojo.UserInfo;
import com.lc.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/shipping")
public class AddressController {
    @Autowired
    AddressService addressService;
    /*
     * 添加地址
     * */
    @RequestMapping(value = "add.do")
    public ServerResponse add(HttpSession session, Shopping shopping) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
        return addressService.add(userInfo.getId(),shopping);
    }
    /*
     * 删除地址
     * */
    @RequestMapping(value = "del.do")
    public ServerResponse del(HttpSession session, Integer shoppingId) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
        return addressService.del(userInfo.getId(),shoppingId);
    }
    /*
     * 登录状态更新地址
     * */
    @RequestMapping(value = "update.do")
    public ServerResponse update(HttpSession session, Shopping shopping) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
        shopping.setUserId(userInfo.getId());
        return addressService.update(shopping);
    }

    /*
     * 选中查看具体的地址
     * */
    @RequestMapping(value = "select.do")
    public ServerResponse select(HttpSession session, Integer shoppingId) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
        return addressService.select(shoppingId);
    }

    /*
     * 地址列表
     * */
    @RequestMapping(value = "list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam(required = false,defaultValue = "10")Integer pageSize) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
        return addressService.list(pageNum,pageSize);
    }
}
