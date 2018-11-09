package com.lc.controller.portal;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lc.common.ResponseCode;
import com.lc.common.ServerResponse;
import com.lc.pojo.UserInfo;
import com.lc.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/usr")
public class UserController {
    @Autowired
    UserServer userServer;


    //  登录
    @RequestMapping(value = "/login.do")
    //调用service中的login
    public ServerResponse login(HttpSession session, String username, String password) {

        ServerResponse serverResponse = userServer.login(username, password);
        if (serverResponse.isSuccess()) {
            UserInfo userInfo = (UserInfo) serverResponse.getData();
            session.setAttribute(ResponseCode.Const, userInfo);
        }
        return serverResponse;
    }

    /*
    注册模块
    */
    @RequestMapping(value = "/register.do")
//调用service中的register方法
    public ServerResponse register(HttpSession session, UserInfo userInfo) {
        ServerResponse serverResponse = userServer.register(userInfo);
        return serverResponse;
    }

    /*
    忘记密码的获取密保问题
    */
    @RequestMapping(value = "/forget_get_question.do")
//调用service中的forget_get_question方法
    public ServerResponse forget_get_question(String username) {
        ServerResponse serverResponse = userServer.forget_get_question(username);
        return serverResponse;
    }

    /*
  提交问题答案
  */
    @RequestMapping(value = "/forget_check_answer.do")

    public ServerResponse forget_check_answer(String username, String question, String answer) {
        ServerResponse serverResponse = userServer.forget_check_answer(username, question, answer);
        return serverResponse;
    }

    /*
忘记密码的重设密码
 */
    @RequestMapping(value = "/forget_reset_password.do")

    public ServerResponse forget_reset_password(String username, String passwordNew, String ftoken) {
        ServerResponse serverResponse = userServer.forget_reset_password(username, passwordNew, ftoken);
        return serverResponse;
    }

    /*
检查用户名或邮箱是否有效
*/
    @RequestMapping(value = "/check_valid.do")

//    str可以是用户名或邮箱，对应的type是username和email
    public ServerResponse check_valid(String str, String type) {
        ServerResponse serverResponse = userServer.check_valid(str, type);
        return serverResponse;
    }


    /*
获取登录用户信息
*/
    @RequestMapping(value = "/get_user_info.do")
    public ServerResponse get_user_info(HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
//        进行非空判断
        if (userInfo == null) {
            return ServerResponse.serverResponseByError("用户未登录");
        }
       UserInfo info =new UserInfo();
        info.setId(userInfo.getId());
        info.setUsername(userInfo.getUsername());
        info.setEmail(userInfo.getEmail());
        info.setPhone(userInfo.getPhone());
        info.setCreateTime(userInfo.getCreateTime());
        info.setUpdateTime(userInfo.getUpdateTime());
        return ServerResponse.serverResponseBySuccess(info);
    }

    /*
登录中状态重置密码
*/
    @RequestMapping(value = "/reset_password.do")
    public ServerResponse reset_password(HttpSession session, String passwordOld, String passwordNew) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError("用户未登录");
        }
        ServerResponse serverResponse = userServer.reset_password(userInfo.getUsername(), passwordOld, passwordNew);
        return serverResponse;
    }

    /*
登录中状态更新个人信息
*/
    @RequestMapping(value = "/update_information.do")
    public ServerResponse update_information(HttpSession session, UserInfo user) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
        if (userInfo == null) {
            return ServerResponse.serverResponseByError("用户未登录");
        }
        user.setId(userInfo.getId());
        ServerResponse serverResponse = userServer.update_information(user);
        //这里要把更新的用户重新放入session中
        if(serverResponse.isSuccess()){
            UserInfo userInfo1 =userServer.selectAllById(userInfo.getId());
            session.setAttribute(ResponseCode.Const,userInfo1);
        }
        return serverResponse;
    }

    /*
获取登录用户详细信息
*/
    @RequestMapping(value = "/get_inforamtion.do")

    public ServerResponse get_inforamtion(HttpSession session) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
//        进行非空判断
        if (userInfo == null) {
            return ServerResponse.serverResponseByError("用户未登录");
        }
        //对密码进行置空
        userInfo.setPassword(null);
//        返回值
        return ServerResponse.serverResponseBySuccess(userInfo);
    }

    /*
获取退出登录
*/
    @RequestMapping(value = "/logout.do")

    public ServerResponse logout(HttpSession session) {
        session.removeAttribute(ResponseCode.Const);
            return ServerResponse.serverResponseBySuccess("登出成功");
    }




}
