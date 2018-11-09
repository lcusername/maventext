package com.lc.controller.backend;

import com.lc.common.ResponseCode;
import com.lc.common.ServerResponse;
import com.lc.pojo.UserInfo;
import com.lc.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/*
* 后台控制器
* */
@RestController
@RequestMapping(value = "/manage/usr")
public class ManageController {

    @Autowired
    UserServer userServer;
    /*
    * 管理员登录（与前台登录相同）
    * */
    @RequestMapping(value = "/login.do")
    //调用service中的login
    public ServerResponse login(HttpSession session, String username, String password) {

        ServerResponse serverResponse = userServer.login(username, password);
        if (serverResponse.isSuccess()) {
            //登陆成功
            UserInfo userInfo = (UserInfo) serverResponse.getData();
            //进行权限校验
            if(userInfo.getRole() == ResponseCode.Roleenum.ROLE_CUSTOMER.getCode()){
                return ServerResponse.serverResponseByError("权限不足");
            }
            session.setAttribute(ResponseCode.Const, userInfo);
        }
        return serverResponse;
    }

}
