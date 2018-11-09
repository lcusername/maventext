package com.lc.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateUtils {
    //将时间转化为字符串
    //使用joda插件

    public static final String FORMAT="yyyy-DD-dd HH:mm:ss";
    public static String datetostr(Date date,String format){
        DateTime dateTime =new DateTime(date);
        String str =dateTime.toString(format);
        return  str;
    }

    public static String datetostr(Date date){
        DateTime dateTime =new DateTime(date);
        String str =dateTime.toString(FORMAT);
        return  str;
    }

    //将字符串转化为data
    public static Date strtodata(String str){
       DateTimeFormatter dateTimeFormatter= DateTimeFormat.forPattern(FORMAT);
      DateTime dateTime= dateTimeFormatter.parseDateTime(str);
      return dateTime.toDate();
    }
}
