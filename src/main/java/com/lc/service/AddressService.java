package com.lc.service;

import com.lc.common.ServerResponse;
import com.lc.pojo.Shopping;

public interface AddressService {
    /*
    * 添加地址
    * */
    ServerResponse add(Integer userId,Shopping shopping);
    /*
     * 删除地址
     * */
    ServerResponse del(Integer id, Integer shoppingId);
    /*
     * 登录状态更新地址
     * */
    ServerResponse update(Shopping shopping);
    /*
     * 选中查看具体的地址
     * */
    ServerResponse select(Integer shoppingId);

    /*
     * 地址列表
     * */
    ServerResponse list(Integer pageNum, Integer pageSize);
}
