package com.lc.dao;

import com.lc.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order
     *
     * @mbg.generated
     */
    int insert(Order record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order
     *
     * @mbg.generated
     */
    Order selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order
     *
     * @mbg.generated
     */
    List<Order> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_order
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Order record);

    /*
     * 根据userId和orderNo来查询订单
     * */
    Order selectByUidAndONo(@Param("userId") Integer userId,
                            @Param("orderNo") Long orderNo);
    /*
    * 根据userId查询订单
    * */
    List<Order> selectByUserId(Integer userId);

    Order selectByOrderNo(Long orderNo);
    /*
    * 更新状态和发货时间
    * */
    int updateStatusAndSendtime(Order order);
}