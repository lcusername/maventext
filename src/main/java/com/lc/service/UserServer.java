package com.lc.service;

import com.lc.common.ServerResponse;
import com.lc.pojo.UserInfo;

public interface UserServer {
    //定义登录接口
    ServerResponse login(String username, String password);

    //定义注册接口
    ServerResponse register(UserInfo userInfo);

    //定义忘记密码接口
    ServerResponse forget_get_question(String username);

    //提交问题答案接口
    ServerResponse forget_check_answer(String username, String question, String answer);

    //忘记密码的重置密码
    ServerResponse forget_reset_password(String username, String passwordNew, String ftoken);

    //检查用户名或邮箱是否有效
    ServerResponse check_valid(String str, String type);



    //   登录中状态重置密码
    ServerResponse reset_password(String username, String passwordOld, String passwordNew);

    // 登录中状态更新个人信息
    ServerResponse update_information(UserInfo user);

    //使用id获取到用户对象
    UserInfo selectAllById(Integer id);
}
