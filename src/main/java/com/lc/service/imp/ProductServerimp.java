package com.lc.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lc.common.ResponseCode;
import com.lc.common.ServerResponse;
import com.lc.dao.CategoryMapper;
import com.lc.dao.ProductMapper;
import com.lc.pojo.Category;
import com.lc.pojo.Product;
import com.lc.service.CategoryServer;
import com.lc.service.ProductServer;
import com.lc.utils.DateUtils;
import com.lc.utils.FTPUtil;
import com.lc.utils.PropertiesUtil;
import com.lc.vo.ProductDetailVo;
import com.lc.vo.ProductListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class ProductServerimp implements ProductServer {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    CategoryServer categoryServer;

    /*
     * 新增OR更新产品
     * */
    @Override
    public ServerResponse save(Product product) {
//        1非空判断
        if (product == null) {
            return ServerResponse.serverResponseByError("参数不能为空");
        }
//        2设置商品主图 （从子图中获取到第一个就是主图）
        //获得参数中的子图
        String sub = product.getSubImages();
        //判断字符串并将该字符串分隔开
        if (sub != null && !sub.equals("")) {
            String[] substr = sub.split(",");
            //判断该数组非空，并将数组中第一个放入到主图中
            if (substr.length > 0) {
                product.setMainImage(substr[0]);
            }
        }
//        3保存或者更新商品
//        4返回值
        //对id进行判断 空及添加 非空及更新
        if (product.getId() == null) {
            //添加
            int resultadd = productMapper.insert(product);
            if (resultadd > 0) {
                return ServerResponse.serverResponseBySuccess("添加成功");
            } else {
                return ServerResponse.serverResponseByError("添加失败");
            }
        } else {
            //更新
            int resultupdate = productMapper.updateByPrimaryKey(product);
            if (resultupdate > 0) {
                return ServerResponse.serverResponseBySuccess("更新成功");
            } else {
                return ServerResponse.serverResponseByError("更新失败");
            }
        }


    }

    /*
     * 产品上下架
     * */
    @Override
    public ServerResponse set_sale_status(Integer productId, Integer status) {
//        1参数非空判断
        if (productId == null) {
            return ServerResponse.serverResponseByError("id不能为空");
        }
        if (status == null) {
            return ServerResponse.serverResponseByError("状态不能为空");
        }
//      2  更新商品状态
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int resultupadate = productMapper.updateByPrimaryKeytoStatus(product);
//        3返回值
        if (resultupadate > 0) {
            return ServerResponse.serverResponseBySuccess("更新成功");
        }
        return ServerResponse.serverResponseByError("更新失败");
    }

    /*
     * 商品详情
     * */
    @Override
    public ServerResponse detail(Integer productId) {
//        1参数非空检验
        if (productId == null) {
            return ServerResponse.serverResponseByError("id不能为空");
        }
//        2id查询商品对象
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.serverResponseByError("查无此商品");
        }
//        3 将商品转化为vo类型
        ProductDetailVo productDetailVo = transformProduct(product);
//        4 返回值
        return ServerResponse.serverResponseBySuccess(productDetailVo);
    }


    //创建方法将product对象转换为vo
    private ProductDetailVo transformProduct(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setCreateTime(DateUtils.datetostr(product.getCreateTime()));
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setId(product.getId());
        productDetailVo.setName(product.getName());
        productDetailVo.setImageHost(PropertiesUtil.getBykey("ImageHost"));
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setUpdateTime(DateUtils.datetostr(product.getUpdateTime()));
        productDetailVo.setSubtitle(product.getSubtitle());
        //直接从category中获取到parentid
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category != null) {
            productDetailVo.setParentCategoryId(category.getParentId());
        } else {
            productDetailVo.setParentCategoryId(0);
        }
        return productDetailVo;
    }

    /*
     * 后台-分页查看商品列表
     * */
    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {
//        直接查询商品，但是要使用分页插件，必须在查询语句之前使用插件
        PageHelper.startPage(pageNum, pageSize);
        //分页插件会自动在查询语句后添加limit
        List<Product> list = productMapper.selectAll();
        //非空判断，遍历
        List<ProductListVo> ListVo = Lists.newArrayList();
        if (list != null || list.size() > 0) {
            for (Product product : list) {
                ProductListVo productListVo = transformListvo(product);
                ListVo.add(productListVo);
            }
        }
        //使用插件将vo集合封装到同一个对象中
        PageInfo pageInfo = new PageInfo(ListVo);
        return ServerResponse.serverResponseBySuccess(pageInfo);
    }


    //将product转换为Listvo
    private ProductListVo transformListvo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setName(product.getName());
        productListVo.setPrice(product.getPrice());
        productListVo.setStatus(product.getStatus());
        productListVo.setSubtitle(product.getSubtitle());

        return productListVo;
    }

    /*
     * 后台-产品搜索
     * */
    @Override
    public ServerResponse search(Integer productId, String productName, Integer pageNum, Integer pageSize) {
//        1先使用分页插件
        PageHelper.startPage(pageNum, pageSize);
//        2重置name
        if (productName != null && !productName.equals("")) {
            productName = "%" + productName + "%";
        }
//        3查询
        List<Product> list = productMapper.selectByIdOrName(productId, productName);
        //将集合中的product对象转化为vo对象
        List<ProductListVo> ListVo = Lists.newArrayList();
        if (list != null || list.size() > 0) {
            for (Product product : list) {
                ProductListVo productListVo = transformListvo(product);
                ListVo.add(productListVo);
            }
        }
        //使用插件将vo集合封装到同一个对象中
        PageInfo pageInfo = new PageInfo(ListVo);
        return ServerResponse.serverResponseBySuccess(pageInfo);
    }

    /*
     * 图片上传
     * */
    @Override
    public ServerResponse upload(MultipartFile file, String path) {
//        1 参数非空判断
        if (file == null) {
            return ServerResponse.serverResponseByError("请上传图片");
        }
//        2 获取图片名称并获取图片后缀
        String fileName = file.getOriginalFilename();
        String extendName = fileName.substring(fileName.lastIndexOf("."));
//        3为图片生成唯一的名字
        String newFileName = UUID.randomUUID().toString() + extendName;
//        4对路径进行判断是否存在，不存在则创建路径
        File file1 = new File(path);
        if (!file1.exists()) {
            file1.setWritable(true);
            file1.mkdir();
        }
//        5将图片放入到该路径下
        File file2 = new File(path, newFileName);

        try {
            file.transferTo(file2);
            //上传到图片服务器
            FTPUtil.uploadFile(Lists.newArrayList(file2));
            Map<String, String> map = Maps.newHashMap();
            map.put("uri", newFileName);
            map.put("url", PropertiesUtil.getBykey("ImageHostupload") + "/" + newFileName);
            file2.delete();
            return ServerResponse.serverResponseBySuccess(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * 前台商品详情
     * */
    @Override
    public ServerResponse detailportal(Integer productId) {
//        1参数非空校验
        if (productId == null) {
            return ServerResponse.serverResponseByError("id不能为空");
        }
//        2查询商品
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.serverResponseByError("商品不存在");
        }
//        3检查商品状态
        //       封装商品状态
        if (product.getStatus() != ResponseCode.ProductStatue.PRODUCT_ONLINE.getCode()) {
            return ServerResponse.serverResponseByError("商品已被下架或被删除");
        }
//        4获取商品vo对象
        ProductDetailVo productDetailVo = transformProduct(product);
//        5返回值
        return ServerResponse.serverResponseBySuccess(productDetailVo);
    }
    /*
     * 产品搜索及动态排序List
     * */
    @Override
    public ServerResponse listportal(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {

//        1 参数的检验 id和keyword不能同时为空
        if (categoryId ==null && (keyword ==null || keyword.equals(""))){
            return  ServerResponse.serverResponseByError("请给出参数");
        }
//        2根据id查询
        Set<Integer> set = Sets.newHashSet();
        if (categoryId !=null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            PageHelper.startPage(pageNum, pageSize);
            if (category == null && (keyword == null || keyword.equals(""))) {
                List<ProductListVo> list = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(list);
            return ServerResponse.serverResponseBySuccess(pageInfo);
        }

        ServerResponse serverResponse = categoryServer.get_deep_category(categoryId);

        if (serverResponse.isSuccess()) {
            set = (Set<Integer>) serverResponse.getData();
        }
    }

//        3根据keyword查
        if (keyword != null && !keyword.equals("")) {
            keyword = "%" + keyword + "%";
        }
        //判断orderby
        if (orderBy.equals("")){
            PageHelper.startPage(pageNum,pageSize,orderBy);
        }else{
//            对orderby进行处理
            String [] str =orderBy.split("_");
            if (str.length>1){
                PageHelper.startPage(pageNum,pageSize,str[0]+" "+str[1]);
            }else {
                PageHelper.startPage(pageNum,pageSize,orderBy);
            }
        }

//        4将集合商品对象转化为vo对象
        List<Product> list =productMapper.searchProduct(set,keyword);
        List<ProductListVo> listVos =Lists.newArrayList();
        if (list!=null && list.size()>0){
            for (Product product:list){
                ProductListVo productListVo =transformListvo(product);
                listVos.add(productListVo);
            }
        }
//        5分页
            PageInfo pageInfo =new PageInfo(listVos);

//        返回
        return ServerResponse.serverResponseBySuccess(pageInfo);
    }

}
