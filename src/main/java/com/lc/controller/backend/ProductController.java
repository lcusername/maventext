package com.lc.controller.backend;

import com.lc.common.ResponseCode;
import com.lc.common.ServerResponse;
import com.lc.pojo.Product;
import com.lc.pojo.UserInfo;
import com.lc.service.ProductServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/*
* 后台商品控制器
* */
@RestController
@RequestMapping(value = "/manage/product")
public class ProductController {
    @Autowired
    ProductServer productServer;
    /*
    * 新增OR更新产品
    * */
    @RequestMapping(value = "/save.do")
    public ServerResponse save(HttpSession session,Product product){
        UserInfo userInfo =(UserInfo)session.getAttribute(ResponseCode.Const);
//        非空判断
        if(userInfo ==null){
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
//        权限校验
        if(userInfo.getRole() !=ResponseCode.Roleenum.ROLE_ADMIN.getCode()){
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NO_POVERS.getCode(),
                    ResponseCode.CategoryStatue.NO_POVERS.getDesc());
        }

        return productServer.save(product);
    }

    /*
     * 产品上下架
     * */
    @RequestMapping(value = "/set_sale_status.do")
    public ServerResponse set_sale_status(HttpSession session,Integer productId,Integer status){
        UserInfo userInfo =(UserInfo)session.getAttribute(ResponseCode.Const);
//        非空判断
        if(userInfo ==null){
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
//        权限校验
        if(userInfo.getRole() !=ResponseCode.Roleenum.ROLE_ADMIN.getCode()){
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NO_POVERS.getCode(),
                    ResponseCode.CategoryStatue.NO_POVERS.getDesc());
        }

        return productServer.set_sale_status(productId,status);
    }

    /*
    * 产品详情
    * */
    @RequestMapping(value = "/detail.do")
    public ServerResponse detail(HttpSession session,Integer productId){
        UserInfo userInfo =(UserInfo)session.getAttribute(ResponseCode.Const);
//        非空判断
        if(userInfo ==null){
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
//        权限校验
        if(userInfo.getRole() !=ResponseCode.Roleenum.ROLE_ADMIN.getCode()){
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NO_POVERS.getCode(),
                    ResponseCode.CategoryStatue.NO_POVERS.getDesc());
        }

        return productServer.detail(productId);
    }

    /*
    * 后台-分页查看商品列表
    * */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(value = "pageNum",required =false,defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize){
        UserInfo userInfo =(UserInfo)session.getAttribute(ResponseCode.Const);
//        非空判断
        if(userInfo ==null){
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
//        权限校验
        if(userInfo.getRole() !=ResponseCode.Roleenum.ROLE_ADMIN.getCode()){
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NO_POVERS.getCode(),
                    ResponseCode.CategoryStatue.NO_POVERS.getDesc());
        }

        return productServer.list(pageNum,pageSize);
    }

    /*
     * 后台-产品搜索
     * */
    @RequestMapping(value = "/search.do")
    public ServerResponse search(HttpSession session,
                                 @RequestParam(value = "productName",required =false)String productName,
                                 @RequestParam(value = "productId",required =false)Integer productId,
                               @RequestParam(value = "pageNum",required =false,defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize
                               ){
        UserInfo userInfo =(UserInfo)session.getAttribute(ResponseCode.Const);
//        非空判断
        if(userInfo ==null){
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
//        权限校验
        if(userInfo.getRole() !=ResponseCode.Roleenum.ROLE_ADMIN.getCode()){
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NO_POVERS.getCode(),
                    ResponseCode.CategoryStatue.NO_POVERS.getDesc());
        }

        return productServer.search(productId,productName,pageNum,pageSize);
    }
    /*
     * 后台-图片上传
     * */

}
