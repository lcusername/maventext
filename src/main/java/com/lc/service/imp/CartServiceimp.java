package com.lc.service.imp;

import com.google.common.collect.Lists;
import com.lc.common.ResponseCode;
import com.lc.common.ServerResponse;
import com.lc.dao.CartMapper;
import com.lc.dao.ProductMapper;
import com.lc.pojo.Cart;
import com.lc.pojo.Product;
import com.lc.service.CartService;
import com.lc.utils.BigDecimalUtils;
import com.lc.vo.CartProductVo;
import com.lc.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceimp implements CartService {
    @Autowired
    CartMapper cartMapper;
    @Autowired
    ProductMapper productMapper;

    /*
     * 购物车添加商品
     * */
    @Override
    public ServerResponse add(Integer userId, Integer productId, Integer count) {
//        1参数校验
        if (productId == null || count == null) {
            return ServerResponse.serverResponseByError("参数不能为空");
        }
        Product product =productMapper.selectByPrimaryKey(productId);
        if(product ==null){
            return ServerResponse.serverResponseByError("要添加的商品不存在");
        }
//        2根据商品id和用户id查询商品 若查询为空，说明购物车无该商品则添加，否则有该商品，则更新数量
        Cart cart = cartMapper.selectByUidAndPid(productId, userId);
        if (cart == null) {
            //添加
            Cart cart1 = new Cart();
            cart1.setProductId(productId);
            cart1.setUserId(userId);
            cart1.setQuantity(count);
            cart1.setChecked(ResponseCode.CartCheckStuts.PRODUCT_CHECK.getCode());
            int resultinsert = cartMapper.insert(cart1);
            if (resultinsert == 0) {
                return ServerResponse.serverResponseByError("添加失败");
            }
        } else {
            //更新
            Cart cart2 = new Cart();
            cart2.setId(cart.getId());
            cart2.setUserId(userId);
            cart2.setProductId(productId);
            cart2.setQuantity(count);
            cart2.setChecked(cart.getChecked());
            int result = cartMapper.updateByPrimaryKey(cart2);
            if (result == 0) {
                return ServerResponse.serverResponseByError("更新失败");
            }
        }
        CartVo cartVo =transfromCartVo(userId);
//        3返回值
        return ServerResponse.serverResponseBySuccess(cartVo);
    }



    //创建方法转化为cartvo
    private CartVo transfromCartVo(Integer userId) {
        CartVo cartVo = new CartVo();
        //购物车总价格默认为0
        BigDecimal TotalPrice =new BigDecimal(0);
//        1根据用户id查询商品信息List<cart>
        List<Cart> cartList = cartMapper.selectByUserId(userId);
//        2将List<cart> 转化为 List<CartproductVo>
        List<CartProductVo> cartProductVoList = Lists.newArrayList();
        if (cartList != null && cartList.size() > 0) {
            //增强for遍历cart集合
            for (Cart cart : cartList) {
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cart.getId());
                cartProductVo.setProductChecked(cart.getChecked());
                cartProductVo.setQuantity(cart.getQuantity());
                cartProductVo.setUserId(userId);
                //查询商品
                Product product = productMapper.selectByPrimaryKey(cart.getProductId());
                if (product != null) {
                    cartProductVo.setProductId(cart.getProductId());
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductStock(product.getStock());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    //进行库存和购买数量的判断
                    int stock = product.getStock();
                    int limitcount = 0;
                    if (stock >= cart.getQuantity()) {//库存充足，则可购买数量是库存数
                        limitcount = cart.getQuantity();
                        cartProductVo.setLimitQuantity("LIMIT_NUM_SUCCESS");
                    } else {//库存不足
                        limitcount = stock;
                        //需要更新一下购物车商品数量
                        Cart cart1 = new Cart();
                        cart1.setChecked(cart.getChecked());
                        cart1.setQuantity(stock);
                        cart1.setProductId(cart.getProductId());
                        cart1.setUserId(userId);
                        cart1.setId(cart.getId());
                        cartMapper.updateByPrimaryKey(cart1);

                        cartProductVo.setLimitQuantity("LIMIT_NUM_FAIL ");
                    }
                    cartProductVo.setQuantity(limitcount);
                    //商品价格*数量
                    cartProductVo.setProductTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),Double.valueOf(cartProductVo.getQuantity())));

                }
               // 3计算总价格
                if (cartProductVo.getProductChecked() ==ResponseCode.CartCheckStuts.PRODUCT_CHECK.getCode()){
                    TotalPrice=BigDecimalUtils.add(TotalPrice.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
                }

                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartProductVoList(cartProductVoList);

        cartVo.setCartTotalPrice(TotalPrice);
//        4判断购物车是否全选
            int isAllCheck = cartMapper.isAllCheck(userId);
//            如果返回值大于0则商品未被全选
        if (isAllCheck>0){
            cartVo.setAllChecked(false);
        }else{
            cartVo.setAllChecked(true);
        }
//        5返回结果
        return cartVo;
    }
    /*
     * 购物车List列表
     * */
    @Override
    public ServerResponse list(Integer id) {
        CartVo cartVo =transfromCartVo(id);
        return ServerResponse.serverResponseBySuccess(cartVo);
    }
    /*
     * 更新购物车某个产品数量
     * */
    @Override
    public ServerResponse update(Integer userId, Integer productId, Integer count) {

//        1参数的非空判断
        if(productId==null || count==null){
            return  ServerResponse.serverResponseByError("参数不能为空");
        }
//        2查询购物车中的商品
        Cart cart =cartMapper.selectByUidAndPid(userId,productId);
        if (cart !=null){
            //    3更新数量
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKey(cart);
        }
//        4返回结果
        return ServerResponse.serverResponseBySuccess(transfromCartVo(userId));
    }
    /*
     * 移除购物车某个产品
     * */
    @Override
    public ServerResponse delete_product(Integer userId, String productIds) {
//        1参数判断
        if(productIds==null || productIds.equals("")){
            return ServerResponse.serverResponseByError("参数不能为空");
        }
//        2productIds 转化为集合list<Integer>
            //现将字符串productIds转化为数组
        String [] pIdArr = productIds.split(",");
            //给一个空的集合
        List<Integer> list =Lists.newArrayList();
        if (pIdArr !=null && pIdArr.length>0){
            //遍历集合
            for(String productid :pIdArr){
                Integer pid =Integer.parseInt(productid);
                list.add(pid);
            }
        }
//        3移除操作
      cartMapper.updateByUidAndPid(userId,list);
//        4返回结果
        return ServerResponse.serverResponseBySuccess(transfromCartVo(userId));
    }
    /*
     * 购物车选中操作
     * */
    @Override
    public ServerResponse select(Integer userId, Integer productId,Integer check) {
//        1参数非空判断
//        if(productId ==null){
//            return ServerResponse.serverResponseByError("参数不能为空");
//        }
//        2选中某个商品操作，及更新checked为1
         cartMapper.selectOrNoselect(userId,productId,check);
//        3返回结果
        return ServerResponse.serverResponseBySuccess(transfromCartVo(userId));
    }
    /*
     * 查询在购物车里的产品数量
     * */
    @Override
    public ServerResponse get_cart_product_count(Integer userId) {
    int result=cartMapper.selectSumProduct(userId);
        return ServerResponse.serverResponseBySuccess(result);
    }
}
