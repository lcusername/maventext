package com.lc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/*
* 从配置文件中读取数据的工具类
* */
public class PropertiesUtil {
    private  static Properties properties =new Properties();

    static {
        InputStream inputStream=Thread.currentThread().getContextClassLoader().getResourceAsStream("jdbc.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * 读取配置文件
    * */
    public static String getBykey(String key){
        return properties.getProperty(key);
    }
}
