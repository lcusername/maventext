package com.lc.service;

import com.lc.common.ServerResponse;
import com.lc.pojo.Product;
import org.springframework.web.multipart.MultipartFile;

public interface ProductServer {
    /*
     * 新增OR更新产品
     * */
    ServerResponse save(Product product);

    /*
     * 产品上下架
     * */
    ServerResponse set_sale_status(Integer productId, Integer status);

    /*
     * 商品详情
     * */
    ServerResponse detail(Integer productId);

    /*
     * 后台-分页查看商品列表
     * */
    ServerResponse list(Integer pageNum, Integer pageSize);
    /*
     * 后台-产品搜索
     * */
    ServerResponse search(Integer productId, String productName, Integer pageNum, Integer pageSize);
    /*
    * 图片上传
    * */
    ServerResponse upload(MultipartFile file,String path);

    /*
     * 前台商品详情
     * */
    ServerResponse detailportal(Integer productId);
    /*
     * 产品搜索及动态排序List
     * */
    ServerResponse listportal(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy);
}
