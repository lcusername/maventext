package com.lc.service.imp;

import com.google.common.collect.Sets;
import com.lc.common.ServerResponse;
import com.lc.dao.CategoryMapper;
import com.lc.pojo.Category;
import com.lc.service.CategoryServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/*
 * 获取品类子节点（平级）
 * */
@Service
public class CategpryServerimp implements CategoryServer {

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public ServerResponse get_category(Integer categpryId) {
        //1 非空校验
        if (categpryId == null) {
            return ServerResponse.serverResponseByError("id不能为空");
        }
        //2 根据id来查询类别
        Category category = categoryMapper.selectByPrimaryKey(categpryId);
        if (category == null) {
            return ServerResponse.serverResponseByError("未查询到该类别");
        }
        //3 查询子类别
        List<Category> list = categoryMapper.findCategoryById(categpryId);
        //集合非空判断
        if (list == null || list.size() == 0) {
            return ServerResponse.serverResponseByError("该类别下没有子类别");
        }
        //4 返回结果
        return ServerResponse.serverResponseBySuccess(list);
    }

    /*
     * 增加品类
     * */
    @Override
    public ServerResponse add_category(Integer parentId, String categoryName) {
//        1参数校验
        if (parentId == null) {
            return ServerResponse.serverResponseByError("id不能为空");
        }
        if (categoryName == null || categoryName.equals("")) {
            return ServerResponse.serverResponseByError("品类名不能为空");
        }
        //2对输入的数据进行校验看数据库中是否已经存在该值
        Category isResult = categoryMapper.selectByIdAndName(parentId, categoryName);
        if (isResult != null) {
            return ServerResponse.serverResponseByError("节点已存在，请勿重复增加");
        }
//        3增加节点
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(1);
        int result = categoryMapper.insert(category);
        if (result > 0) {
            return ServerResponse.serverResponseBySuccess("增加成功");
        }
//        4返回结果
        return ServerResponse.serverResponseByError("增加失败");
    }

    /*www
     * 修改品类
     * */
    @Override
    public ServerResponse set_category_name(Integer categoryId, String categoryName) {
//        1参数非空判断
        if (categoryId == null) {
            return ServerResponse.serverResponseByError("id不能为空");
        }
        if (categoryName == null || categoryName.equals("")) {
            return ServerResponse.serverResponseByError("品类名不能为空");
        }
//        2根据id查询
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category == null) {
            return ServerResponse.serverResponseByError("该类别不存在");
        }
//        3修改
//        给对象重新赋值新的名称
        category.setName(categoryName);
        int result = categoryMapper.updateByPrimaryKey(category);
        if (result > 0) {
            return ServerResponse.serverResponseBySuccess("修改成功");
        }
//        4返回结果
        return ServerResponse.serverResponseByError("修改失败");
    }

    /*
     * 获取当前分类ID及递归子节点的categoryId
     * */
    @Override
    public ServerResponse get_deep_category(Integer categoryId) {

//        1参数的非空校验
        if(categoryId == null){
            return ServerResponse.serverResponseByError("id不能为空");
        }
//        2查询
//        创建一个set集合
        Set<Category> categorySet =Sets.newHashSet();
//        得到某个节点需要的所有子节点
        categorySet =findAllChildCategory(categorySet,categoryId);

//        需要返回的是int类型的，再次创建一个set集合
        Set<Integer> integerSet =Sets.newHashSet();

        //将categorySet中的值遍历出来的id放入到integerSet中
        Iterator<Category> categoryIterator =categorySet.iterator();
        while (categoryIterator.hasNext()){
            Category category=categoryIterator.next();
            integerSet.add(category.getId());
        }

//        3返回值
        return ServerResponse.serverResponseBySuccess(integerSet);
    }
    //定义一个方法，该方法返回set集合包含某分类下的所有子节点
    private Set<Category> findAllChildCategory(Set<Category> categorySet,Integer categoryId){
        //查询当前节点的信息放入到集合中
        Category category =categoryMapper.selectByPrimaryKey(categoryId);
        if (category !=null){
            categorySet.add(category);
        }
        //查询当前节点的子节点（平级）使用递归循环
        List<Category> list =categoryMapper.findCategoryById(categoryId);
        //循环遍历集合,让集合中的对象再调用此方法
        if (list!=null || list.size()>0){
            for (Category category1:list){
                findAllChildCategory(categorySet,category1.getId());
            }
        }

        return categorySet;
    }
}
