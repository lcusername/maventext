package com.lc.utils;

import java.math.BigDecimal;

/*
* 价格计算工具类
* */
public class BigDecimalUtils {

    /*
    * 加法计算
    * */
    public static BigDecimal add(double double1,double double2){
        BigDecimal bigDecimal =new BigDecimal(String.valueOf(double1));
        BigDecimal bigDecimal2 =new BigDecimal(String.valueOf(double2));
        return bigDecimal.add(bigDecimal2);
    }

    /*
     * 减法计算
     * */
    public static BigDecimal sub(double double1,double double2){
        BigDecimal bigDecimal =new BigDecimal(String.valueOf(double1));
        BigDecimal bigDecimal2 =new BigDecimal(String.valueOf(double2));
        return bigDecimal.subtract(bigDecimal2);
    }

    /*
     * 乘法计算
     * */
    public static BigDecimal mul(double double1,double double2){
        BigDecimal bigDecimal =new BigDecimal(String.valueOf(double1));
        BigDecimal bigDecimal2 =new BigDecimal(String.valueOf(double2));
        return bigDecimal.multiply(bigDecimal2);
    }
    /*
     * 除法计算
     * */
    public static BigDecimal div(double double1,double double2){
        BigDecimal bigDecimal =new BigDecimal(String.valueOf(double1));
        BigDecimal bigDecimal2 =new BigDecimal(String.valueOf(double2));
        //保留两位小数，四舍五入
        return bigDecimal.divide(bigDecimal2,2,BigDecimal.ROUND_HALF_UP);
    }
}
