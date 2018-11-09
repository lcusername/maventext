package com.lc.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.lc.common.ServerResponse;
import com.lc.dao.ShoppingMapper;
import com.lc.pojo.Shopping;
import com.lc.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AddressServiceimp implements AddressService {
    @Autowired
    ShoppingMapper shoppingMapper;

    /*
     * 添加地址
     * */
    @Override
    public ServerResponse add(Integer userId, Shopping shopping) {
//        1参数校验
        if (shopping == null) {
            return ServerResponse.serverResponseByError("参数不能为空");
        }
//        2添加
        shopping.setUserId(userId);
        shoppingMapper.insert(shopping);
//        3返回结果
        Map<String, Integer> map = Maps.newHashMap();
        map.put("shoppingId", shopping.getId());
        return ServerResponse.serverResponseBySuccess(map,"添加成功");
    }

    /*
     * 删除地址
     * */
    @Override
    public ServerResponse del(Integer userId, Integer shoppingId) {
//        1参数校验
        if (shoppingId == null) {
            return ServerResponse.serverResponseByError("参数不能为空");
        }
//        2操作
        int result=shoppingMapper.deleteByUidAndSid(userId,shoppingId);
//        3返回结果
        if(result>0){
            return ServerResponse.serverResponseBySuccess("删除成功");
        }
        return ServerResponse.serverResponseByError("删除失败");
    }
    /*
     * 登录状态更新地址
     * */
    @Override
    public ServerResponse update(Shopping shopping) {
//        1参数校验
        if (shopping ==null){
            return ServerResponse.serverResponseByError("参数不能为空");
        }
//        2更新
        int result=shoppingMapper.updateAll(shopping);
//        3返回结果
        if(result>0){
            return ServerResponse.serverResponseBySuccess("更新成功");
        }
        return ServerResponse.serverResponseByError("更新失败");
    }
    /*
     * 选中查看具体的地址
     * */
    @Override
    public ServerResponse select(Integer shoppingId) {
//        1参数判断
        if (shoppingId ==null){
            return ServerResponse.serverResponseByError("参数不能为空");
        }
//        2查看
        Shopping shopping =shoppingMapper.selectByPrimaryKey(shoppingId);
//        3返回结果
        return ServerResponse.serverResponseBySuccess(shopping);
    }
    /*
     * 地址列表
     * */
    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shopping> list =shoppingMapper.selectAll();
        PageInfo pageInfo =new PageInfo(list);
        return ServerResponse.serverResponseBySuccess(pageInfo);
    }
}
