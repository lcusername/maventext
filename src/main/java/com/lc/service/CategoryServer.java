package com.lc.service;

import com.lc.common.ServerResponse;

public interface CategoryServer {
    /*
     * 获取品类子节点（平级）
     * */
    ServerResponse get_category(Integer categpryId);

    /*
     * 增加品类
     * */
    ServerResponse add_category(Integer parentId, String categoryName);

    /*www
     * 修改品类
     * */
    ServerResponse set_category_name(Integer categoryId, String categoryName);

    /*
     * 获取当前分类ID及递归子节点的categoryId
     * */
    ServerResponse get_deep_category(Integer categoryId);
}
