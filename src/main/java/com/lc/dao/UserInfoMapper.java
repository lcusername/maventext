package com.lc.dao;

import com.lc.pojo.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_user
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_user
     *
     * @mbg.generated
     */
    int insert(UserInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_user
     *
     * @mbg.generated
     */
    UserInfo selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_user
     *
     * @mbg.generated
     */
    List<UserInfo> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_user
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(UserInfo record);

    //校验用户名是否存在
    int checkUsername(String username);

    //使用用户名和密码进行查找

    UserInfo selectUserInfoByUP(@Param("username") String username,
                                @Param("password") String password);

    //校验邮箱是否存在
    int checkEmail(String email);

    //根据用户名查找密保问题
    String selectQuestionByU(String username);
    //通过三个参数来查询值
    int selectByUQA(@Param("username") String username,
                    @Param("question") String question,
                    @Param("answer") String answer);
    //更改密码
    int updateByPassword(@Param("username") String username,
                         @Param("passwordNew") String passwordNew);
    //登录中状态更新个人信息
    int updateUserByAll(UserInfo userInfo);
}