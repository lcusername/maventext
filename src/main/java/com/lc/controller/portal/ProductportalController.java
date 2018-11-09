package com.lc.controller.portal;

import com.lc.common.ServerResponse;
import com.lc.service.ProductServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/product")
public class ProductportalController {
    @Autowired
    ProductServer productServer;

    /*
     * 前台商品详情
     * */
    @RequestMapping(value = "/detail.do")
    public ServerResponse detail(Integer productId) {
        return productServer.detailportal(productId);
    }

    /*
     * 产品搜索及动态排序List
     * */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                               @RequestParam(value = "orderBy", required = false, defaultValue = "") String orderBy )
    {
        return productServer.listportal(categoryId,keyword,pageNum,pageSize,orderBy);
    }
}
