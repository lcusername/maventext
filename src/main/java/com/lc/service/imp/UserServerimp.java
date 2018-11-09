package com.lc.service.imp;

import com.lc.common.ResponseCode;
import com.lc.common.ServerResponse;
import com.lc.dao.UserInfoMapper;
import com.lc.pojo.UserInfo;
import com.lc.service.UserServer;
import com.lc.utils.MD5Utils;
import com.lc.utils.TokenCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServerimp implements UserServer {
    @Autowired
    UserInfoMapper userInfoMapper;

    //    重写登录方法
    @Override
    public ServerResponse login(String username, String password) {
        //1参数的非空检验
        if (username == null || username.equals("")) {
            return ServerResponse.serverResponseByError("用户名不能为空");
        }
        if (password == null || password.equals("")) {
            return ServerResponse.serverResponseByError("密码不能为空");
        }
        //2检查用户名是否存在
        //调用dao层中的userinfo的接口
        int result = userInfoMapper.checkUsername(username);
        if (result == 0) {
            return ServerResponse.serverResponseByError("用户名不存在");
        }
        //3根据用户名和密码查找信息
        //调用dao层中的userinfo的接口
//        这里也需要对密码进行加密在进行查找
        UserInfo resultUserInfo = userInfoMapper.selectUserInfoByUP(username, MD5Utils.getMD5Code(password));
        if (resultUserInfo == null) {
            return ServerResponse.serverResponseByError("登录失败");
        }
        //4 返回结果
//        登录之后的返回值密码不应该显示，这里对密码进行置空
        resultUserInfo.setPassword(null);
        return ServerResponse.serverResponseBySuccess(resultUserInfo);
    }

    /*
     重写注册方法
     */
    @Override
    public ServerResponse register(UserInfo userInfo) {
//        1参数非空校验
        if (userInfo == null) {
            return ServerResponse.serverResponseByError("必须有参数");
        }
//        2校验用户名（唯一）
        int result = userInfoMapper.checkUsername(userInfo.getUsername());
        if (result > 0) {
            return ServerResponse.serverResponseByError("用户名已存在");
        }
//        3校验邮箱（唯一）
        int resultemail = userInfoMapper.checkEmail(userInfo.getEmail());
        if (resultemail > 0) {
            return ServerResponse.serverResponseByError("邮箱重复");
        }
//        4注册
        //给定用户角色
        userInfo.setRole(ResponseCode.Roleenum.ROLE_CUSTOMER.getCode());
        //使用md5对密码进行加密 是数据库显示为密文
        userInfo.setPassword(MD5Utils.getMD5Code(userInfo.getPassword()));
        int resultRegister = userInfoMapper.insert(userInfo);
        if (resultRegister > 0) {
            return ServerResponse.serverResponseBySuccess("注册成功");
        }
//        4返回结果
        return ServerResponse.serverResponseByError("注册失败");
    }

    /*
   忘记密码的获取密保问题
   */
    @Override
    public ServerResponse forget_get_question(String username) {
        // step1:参数非空校验
        if (username == null || username.equals("")) {
            return ServerResponse.serverResponseByError("用户名不能为空");
        }
        //step2:校验username是否存在
        int result = userInfoMapper.checkUsername(username);
        if (result == 0) {
            return ServerResponse.serverResponseByError("用户名不存在");
        }
        //step3:根据username查询密保问题
        String resultQuestion = userInfoMapper.selectQuestionByU(username);
        //对返回的值进行校验
        if (resultQuestion == null || resultQuestion.equals("")) {
            return ServerResponse.serverResponseByError("密保问题为空");
        }
        //step4:返回数据
        return ServerResponse.serverResponseBySuccess(resultQuestion);
    }

    /*
提交问题答案
*/
    @Override
    public ServerResponse forget_check_answer(String username, String question, String answer) {
        //step1:参数非空校验
        if (username == null || username.equals("")) {
            return ServerResponse.serverResponseByError("用户名不能为空");
        }
        if (question == null || question.equals("")) {
            return ServerResponse.serverResponseByError("密保问题不能为空");
        }
        if (answer == null || answer.equals("")) {
            return ServerResponse.serverResponseByError("答案不能为空");
        }
        //step2:校验答案是否正确（通过三个参数看能否查询到值）
        int result = userInfoMapper.selectByUQA(username, question, answer);
        if (result == 0) {
            //答案错误
            return ServerResponse.serverResponseByError("密保答案错误");
        }
        //step3:为防止横向越权，服务端生成forgetToken保存，并将其返回给客户端
        String ftoken = UUID.randomUUID().toString();
        //使用了guava cache ,保存到服务器端
        TokenCacheUtil.set(username, ftoken);
        //step4:返回结果到客户端
        return ServerResponse.serverResponseBySuccess(ftoken);

    }

    /*
忘记密码的重设密码
*/
    @Override
    public ServerResponse forget_reset_password(String username, String passwordNew, String ftoken) {
        // step1:参数非空校验
        if (username == null || username.equals("")) {
            return ServerResponse.serverResponseByError("用户名不能为空");
        }
        if (passwordNew == null || passwordNew.equals("")) {
            return ServerResponse.serverResponseByError("新密码不能为空");
        }
        if (ftoken == null || ftoken.equals("")) {
            return ServerResponse.serverResponseByError("ftoken不能为空");
        }
        //step2:校验token是否有效
        String token = TokenCacheUtil.get(username);
        //先对token进行非空判断
        if (token == null || token.equals("")) {
            return ServerResponse.serverResponseByError("token不能为空");
        }
        //判断客户端的token和服务端的是否相同
        if (!token.equals(ftoken)) {
            return ServerResponse.serverResponseByError("token无效");
        }
        //step3:修改密码
        int result = userInfoMapper.updateByPassword(username, MD5Utils.getMD5Code(passwordNew));
        if (result > 0) {
            return ServerResponse.serverResponseBySuccess("修改成功");
        }
        //step4:返回结果

        return ServerResponse.serverResponseByError("修改失败");
    }

    /*
    检查用户名或邮箱是否有效
    **/
    @Override
    public ServerResponse check_valid(String str, String type) {
//      1.  参数非空校验
        if (str == null || str.equals("")) {
            return ServerResponse.serverResponseByError("用户名或者邮箱不能为空");
        }
        if (type == null || type.equals("")) {
            return ServerResponse.serverResponseByError("校验的参数类型不能为空");
        }
//       2. 验证用户名是否有效      验证邮箱是否有效
        if (type.equals("username")) {
            int result = userInfoMapper.checkUsername(str);
            if (result > 0) {
//                用户名存在
                return ServerResponse.serverResponseByError("用户名已存在");
            } else {
                return ServerResponse.serverResponseBySuccess("用户名可以使用");
            }

        } else if (type.equals("email")) {
            int result = userInfoMapper.checkEmail(str);
            if (result > 0) {
//                邮箱已存在
                return ServerResponse.serverResponseByError("邮箱已存在");
            } else {
                return ServerResponse.serverResponseBySuccess("邮箱可以使用");
            }
        } else {
            return ServerResponse.serverResponseByError("参数类型错误");
        }
//        3.返回值 已返回
    }



    @Override
    public ServerResponse reset_password(String username,String passwordOld, String passwordNew) {
//      1  校验参数是否为空
        if(passwordNew ==null || passwordNew.equals("")){
            return ServerResponse.serverResponseByError("新密码不能为空");
        }
        if(passwordOld ==null || passwordOld.equals("")){
            return ServerResponse.serverResponseByError("原密码不能为空");
        }
//        2查询根据用户名和passwordOld
        UserInfo userInfo =userInfoMapper.selectUserInfoByUP(username,MD5Utils.getMD5Code(passwordOld));
        if(userInfo==null){
            return ServerResponse.serverResponseByError("旧密码错误");
        }
//       3 修改密码
        userInfo.setPassword(MD5Utils.getMD5Code(passwordNew));
        int result = userInfoMapper.updateByPrimaryKey(userInfo);
        if(result>0){
            return ServerResponse.serverResponseBySuccess("更新成功");
        }
        return ServerResponse.serverResponseByError("更新失败");
    }

    /*
    登录中状态更新个人信息
    */
    @Override
    public ServerResponse update_information(UserInfo user) {
//        1参数校验
            if(user ==null){
                return ServerResponse.serverResponseByError("参数不能为空");
            }
//        2更新信息
        int result =userInfoMapper.updateUserByAll(user);
            if(result>0){
                return ServerResponse.serverResponseBySuccess("更新成功");
            }
//        3返回值
        return ServerResponse.serverResponseByError("更新失败");
    }

    @Override
    public UserInfo selectAllById(Integer id) {
        return userInfoMapper.selectByPrimaryKey(id);

    }


}
