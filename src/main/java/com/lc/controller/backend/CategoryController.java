package com.lc.controller.backend;

import com.lc.common.ResponseCode;
import com.lc.common.ServerResponse;
import com.lc.pojo.UserInfo;
import com.lc.service.CategoryServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

//品类接口控制器
@RestController
@RequestMapping(value = "/manage/category")
public class CategoryController {

    @Autowired
    CategoryServer categoryServer;

    /*
     * 获取品类子节点（平级）
     * */
    @RequestMapping(value = "/get_category.do")
    public ServerResponse get_category(HttpSession session, Integer categoryId) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
        //非空判断
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
        //判断权限
        if (userInfo.getRole() != ResponseCode.Roleenum.ROLE_ADMIN.getCode()) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NO_POVERS.getCode(),
                    ResponseCode.CategoryStatue.NO_POVERS.getDesc());
        }
        return categoryServer.get_category(categoryId);
    }

    /*
     * 增加品类
     * */
    @RequestMapping(value = "/add_category.do")
    public ServerResponse add_category(HttpSession session,
                                       @RequestParam(required = false, defaultValue = "0") Integer parentId,
                                       String categoryName) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
        //非空判断
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
        //判断权限
        if (userInfo.getRole() != ResponseCode.Roleenum.ROLE_ADMIN.getCode()) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NO_POVERS.getCode(),
                    ResponseCode.CategoryStatue.NO_POVERS.getDesc());
        }
        return categoryServer.add_category(parentId, categoryName);
    }

    /*
     * 修改品类
     * */
    @RequestMapping(value = "/set_category_name.do")
    public ServerResponse set_category_name(HttpSession session,
                                            Integer categoryId,
                                            String categoryName) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
        //非空判断
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
        //判断权限
        if (userInfo.getRole() != ResponseCode.Roleenum.ROLE_ADMIN.getCode()) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NO_POVERS.getCode(),
                    ResponseCode.CategoryStatue.NO_POVERS.getDesc());
        }
        return categoryServer.set_category_name(categoryId, categoryName);
    }

    /*
     * 获取当前分类ID及递归子节点的categoryId
     * */
    @RequestMapping(value = "/get_deep_category.do")
    public ServerResponse get_deep_category(HttpSession session, Integer categoryId) {
        UserInfo userInfo = (UserInfo) session.getAttribute(ResponseCode.Const);
        //非空判断
        if (userInfo == null) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NEED_LOGIN.getCode(),
                    ResponseCode.CategoryStatue.NEED_LOGIN.getDesc());
        }
        //判断权限
        if (userInfo.getRole() != ResponseCode.Roleenum.ROLE_ADMIN.getCode()) {
            return ServerResponse.serverResponseByError(ResponseCode.CategoryStatue.NO_POVERS.getCode(),
                    ResponseCode.CategoryStatue.NO_POVERS.getDesc());
        }
        return categoryServer.get_deep_category(categoryId);
    }
}
