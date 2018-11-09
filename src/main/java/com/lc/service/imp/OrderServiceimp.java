package com.lc.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.lc.common.ResponseCode;
import com.lc.common.ServerResponse;
import com.lc.dao.*;
import com.lc.pojo.*;
import com.lc.service.OrderService;
import com.lc.utils.BigDecimalUtils;
import com.lc.utils.DateUtils;
import com.lc.utils.PropertiesUtil;
import com.lc.vo.CartOrderItemVo;
import com.lc.vo.OrderItemVo;
import com.lc.vo.OrderVo;
import com.lc.vo.ShoppingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class OrderServiceimp implements OrderService {
    @Autowired
    CartMapper cartMapper;//查询购物车选中商品
    @Autowired
    ProductMapper productMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    ShoppingMapper shoppingMapper;

    /*
     * 创建订单
     * */
    @Override
    public ServerResponse create(Integer userId, Integer shoppingId) {
//        1参数非空校验
        if (shoppingId == null) {
            return ServerResponse.serverResponseByError("参数不能为空");
        }
        //根据shoppingId查询shopping中是否有该地址
        Shopping selectShopping = shoppingMapper.selectByPrimaryKey(shoppingId);
        if (selectShopping == null) {
            return ServerResponse.serverResponseByError("地址不存在，请添加地址");
        }
//        2根据userid查询购物车中已选中的商品 list<cart>
        List<Cart> cartList = cartMapper.findCartByUidAndChecked(userId);

//        3将list<cart>转化为List<OrderItem>
        ServerResponse serverResponse = transformCartOrderList(userId, cartList);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
//        4创建订单order并保存到数据库中
        //计算订单总价格
        BigDecimal orderTotalPrice = new BigDecimal(0);
        List<OrderItem> orderItemList = (List<OrderItem>) serverResponse.getData();
        if (orderItemList == null || orderItemList.size() == 0) {
            return ServerResponse.serverResponseByError("购物车没有选中商品");
        }
        orderTotalPrice = getOrderPrice(orderItemList);
        Order order = createOrder(userId, shoppingId, orderTotalPrice);
//        5将List<OrderItem>放入到数据库中
        if (order == null) {
            return ServerResponse.serverResponseByError("订单创建失败");
        }
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderNo(order.getOrderNo());
        }
        //mybatis批量插入
        int result = orderItemMapper.insertBatch(orderItemList);
        if (result == 0) {
            return ServerResponse.serverResponseByError("插入失败");
        }
//        6将购买后的商品库存扣除
        reduceProductStock(orderItemList);
//        7购物车清空已下单的商品
        cleanCheckedCart(cartList);
//        8返回结果ordervo
        OrderVo orderVo = transformOrderVo(order, orderItemList, shoppingId);

        return ServerResponse.serverResponseBySuccess(orderVo);
    }


    //order和orderItem转化为orderVo
    private OrderVo transformOrderVo(Order order, List<OrderItem> orderItemList, Integer shoppingId) {
        OrderVo orderVo = new OrderVo();
        //创建一个空List<orderItemVo>集合
        List<OrderItemVo> orderItemVoList = Lists.newArrayList();
        //循环遍历orderitem
        for (OrderItem orderItem : orderItemList) {
            OrderItemVo orderItemVo = transformOrderItemVo(orderItem);
            orderItemVoList.add(orderItemVo);
        }
        //将orderItemVo集合放入到orderVo中
        orderVo.setOrderItemVoList(orderItemVoList);
        orderVo.setImageHost(PropertiesUtil.getBykey("ImageHostupload"));
        orderVo.setShippingId(shoppingId);
        ShoppingVo shoppingVo = transformShoppingVo(shoppingId);
        orderVo.setShoppingVo(shoppingVo);
        orderVo.setReceiverName(shoppingVo.getReceiverName());
        orderVo.setStatus(order.getStatus());
        ResponseCode.OrderStuts orderStuts = ResponseCode.OrderStuts.codeOf(order.getStatus());
        if (orderStuts != null) {
            orderVo.setStatusDesc(orderStuts.getDesc());
        }
        orderVo.setPostage(0);
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        ResponseCode.OrderPayStuts orderPayStuts = ResponseCode.OrderPayStuts.codeOf(order.getPaymentType());
        orderVo.setPaymentTypeDesc(orderPayStuts.getDesc());
        orderVo.setOrderNo(order.getOrderNo());
        return orderVo;
    }

    //shopping转化为shoppingVo
    private ShoppingVo transformShoppingVo(Integer shoppingId) {
        ShoppingVo shoppingVo = new ShoppingVo();
        //根据id获取shopping的数据
        Shopping shopping = shoppingMapper.selectByPrimaryKey(shoppingId);
        if (shopping != null) {
            shoppingVo.setReceiverAddress(shopping.getReceiverAddress());
            shoppingVo.setReceiverCity(shopping.getReceiverCity());
            shoppingVo.setReceiverDistrict(shopping.getReceiverDistrict());
            shoppingVo.setReceiverMobile(shopping.getReceiverMobile());
            shoppingVo.setReceiverName(shopping.getReceiverName());
            shoppingVo.setReceiverPhone(shopping.getReceiverPhone());
            shoppingVo.setReceiverProvince(shopping.getReceiverProvince());
            shoppingVo.setReceiverZip(shopping.getReceiverZip());
        }
        return shoppingVo;
    }

    //orderItem转化为orderItemVo
    private OrderItemVo transformOrderItemVo(OrderItem orderItem) {
        OrderItemVo orderItemVo = new OrderItemVo();
        if (orderItemVo != null) {
            orderItemVo.setCreateTime(DateUtils.datetostr(orderItem.getCreateTime()));
            orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
            orderItemVo.setOrderNo(orderItem.getOrderNo());
            orderItemVo.setProductId(orderItem.getProductId());
            orderItemVo.setProductImage(orderItem.getProductImage());
            orderItemVo.setProductName(orderItem.getProductName());
            orderItemVo.setQuantity(orderItem.getQuantity());
            orderItemVo.setTotalPrice(orderItem.getTotalPrice());

        }

        return orderItemVo;
    }

    //清除购物车已下单商品
    private void cleanCheckedCart(List<Cart> cartList) {
        //批量删除
        if (cartList != null && cartList.size() > 0) {
            cartMapper.deleteBatch(cartList);
        }

    }

    //扣除库存
    private void reduceProductStock(List<OrderItem> orderItemList) {
        if (orderItemList != null && orderItemList.size() > 0) {
            for (OrderItem orderItem : orderItemList) {
                Integer productId = orderItem.getProductId();
                Integer quantity = orderItem.getQuantity();
                Product product = productMapper.selectByPrimaryKey(productId);
                product.setStock(product.getStock() - quantity);
                productMapper.updateByPrimaryKey(product);
            }
        }
    }

    //创建一个计算总价格
    private BigDecimal getOrderPrice(List<OrderItem> orderItemList) {
        BigDecimal orderPrice = new BigDecimal(0);
        for (OrderItem orderItem : orderItemList) {
            orderPrice = BigDecimalUtils.add(orderPrice.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }
        return orderPrice;
    }

    //创建一个订单
    private Order createOrder(Integer userId, Integer shoppingId, BigDecimal totalPrice) {
        Order order = new Order();
        //调用方法生成订单编号
        order.setOrderNo(createOrderNo());
        order.setShippingId(shoppingId);
        order.setUserId(userId);
        //定义状态枚举
        order.setStatus(ResponseCode.OrderStuts.ORDER_UNPAY.getCode());
        order.setPayment(totalPrice);
        order.setPaymentType(ResponseCode.OrderPayStuts.PAY_ONLINE.getCode());

        //保存到数据库
        int result = orderMapper.insert(order);
        if (result > 0) {
            return order;
        }
        return null;
    }

    //生成订单编号(唯一)
    private Long createOrderNo() {
        return System.currentTimeMillis() + new Random().nextInt(100);
    }

    //创建方法将List<cart>转化为List<order>
    private ServerResponse transformCartOrderList(Integer userId, List<Cart> cartList) {
        if (cartList == null || cartList.size() == 0) {
            return ServerResponse.serverResponseByError("购物车中没有选中商品");
        }
        //创建一个空的订单明细集合
        List<OrderItem> orderItemList = Lists.newArrayList();
        for (Cart cart : cartList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setUserId(userId);
            //查询商品信息
            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            if (product == null) {//商品不存在在数据库中
                return ServerResponse.serverResponseByError("id为" + cart.getProductId() + "的商品不存在");
            }
            if (product.getStatus() != ResponseCode.ProductStatue.PRODUCT_ONLINE.getCode()) {//商品下架
                return ServerResponse.serverResponseByError("id为" + cart.getProductId() + "的商品已下架");
            }
            if (product.getStock() < cart.getQuantity()) {//商品库存不足
                return ServerResponse.serverResponseByError("id为" + cart.getProductId() + "的商品库存不足");
            }
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setProductId(product.getId());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setProductName(product.getName());
            orderItem.setQuantity(cart.getQuantity());
            //计算总价格
            orderItem.setTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(), cart.getQuantity().doubleValue()));
            orderItemList.add(orderItem);
        }
        return ServerResponse.serverResponseBySuccess(orderItemList);
    }

    /*
     * 获取订单的商品信息
     * */
    @Override
    public ServerResponse get_order_cart_product(Integer userId) {
//        1查询购物车
        List<Cart> cartList = cartMapper.findCartByUidAndChecked(userId);
//        2将list<cart> 转化为 List<orderItem>
        ServerResponse serverResponse = transformCartOrderList(userId, cartList);
        if (!serverResponse.isSuccess()) {
            return serverResponse;
        }
//        3创建新的需要返回的vo
        //创建一个空的OrderItemVo集合
        CartOrderItemVo cartOrderItemVo = new CartOrderItemVo();
        List<OrderItemVo> orderItemVoList = Lists.newArrayList();

        //从serverResponse中获取到OrderItem集合
        List<OrderItem> orderItemList = (List<OrderItem>) serverResponse.getData();
        //遍历
        for (OrderItem orderItem : orderItemList) {
            //将orderItem转化为OrderitemVo
            OrderItemVo orderItemVo = transformOrderItemVo(orderItem);
            orderItemVoList.add(orderItemVo);
        }
        cartOrderItemVo.setOrderItemList(orderItemVoList);
        cartOrderItemVo.setImageHost(PropertiesUtil.getBykey("ImageHostupload"));
        cartOrderItemVo.setProductTotalPrice(getOrderPrice(orderItemList));
//        4返回
        return ServerResponse.serverResponseBySuccess(cartOrderItemVo);
    }

    /*
     * 订单List
     * */
    @Override
    public ServerResponse list(Integer userId, Integer pageNum, Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList=Lists.newArrayList();
        if (userId == null) {
            //查询所有订单
            orderList = orderMapper.selectAll();
        } else {
            //查询该userId的订单
            orderList = orderMapper.selectByUserId(userId);
        }

        if (orderList == null || orderList.size() == 0) {
            return ServerResponse.serverResponseByError("未查询到订单信息");
        }
        List<OrderVo> orderVoList = Lists.newArrayList();
        for (Order order : orderList) {
            List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(order.getOrderNo());
            OrderVo orderVo = transformOrderVo(order, orderItemList, order.getShippingId());
            orderVoList.add(orderVo);
        }
        PageInfo pageInfo = new PageInfo(orderVoList);
        return ServerResponse.serverResponseBySuccess(pageInfo);
    }

    /*
     * 取消订单
     * */
    @Override
    public ServerResponse cancel(Integer userId, Long orderNo) {
//        1参数校验
        if (orderNo == null) {
            return ServerResponse.serverResponseByError("参数不能为空");
        }
//        2根据userId和orderNo来查询订单
        Order order = orderMapper.selectByUidAndONo(userId, orderNo);
        if (order == null) {
            return ServerResponse.serverResponseByError("订单不存在");
        }
//        3判断订单状态并取消
        if (order.getStatus() != ResponseCode.OrderStuts.ORDER_UNPAY.getCode()) {
            return ServerResponse.serverResponseByError("该订单不能取消");
        }
        order.setStatus(ResponseCode.OrderStuts.ORDER_CANCEL.getCode());
//        4取消订单后将商品返回到库存中
        //创建一个根据orderNo返回orderitem的方法
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(orderNo);
        addProductStock(orderItemList);
//        5返回结果
        int result = orderMapper.updateByPrimaryKey(order);
        if (result > 0) {
            return ServerResponse.serverResponseBySuccess("取消成功");
        }
        return ServerResponse.serverResponseByError("取消失败");
    }


    //将商品数量返回到库存中
    private void addProductStock(List<OrderItem> orderItemList) {
        if (orderItemList != null && orderItemList.size() > 0) {
            for (OrderItem orderItem : orderItemList) {
                Integer productId = orderItem.getProductId();
                Integer quantity = orderItem.getQuantity();
                Product product = productMapper.selectByPrimaryKey(productId);
                product.setStock(product.getStock() + quantity);
                productMapper.updateByPrimaryKey(product);
            }
        }
    }

    /*
     * 订单详情detail
     * */
    @Override
    public ServerResponse detail(Long orderNo) {
//        1参数校验
        if (orderNo == null) {
            return ServerResponse.serverResponseByError("参数不能为空");
        }
//        2查询订单
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order==null){
            return ServerResponse.serverResponseByError("订单不存在");
        }
//        3获取orderVo
        List<OrderItem> orderItemList =orderItemMapper.selectByOrderNo(orderNo);
            OrderVo orderVo=transformOrderVo(order,orderItemList,order.getShippingId());
//        4返回值
        return ServerResponse.serverResponseBySuccess(orderVo);
    }
    /*
     * 后台-订单发货
     * */
    @Override
    public ServerResponse send_goods(Long orderNo) {
//        1参数校验
        if (orderNo==null){
            return ServerResponse.serverResponseByError("参数不能为空");
        }
//        2根据orderNo来查询订单信息
        Order order =orderMapper.selectByOrderNo(orderNo);
        if (order==null){
            return ServerResponse.serverResponseByError("订单不存在");
        }
//        3判断订单状态并发货和更改发货时间
        if (order.getStatus()!=ResponseCode.OrderStuts.ORDER_PAY.getCode()){//必须是已付款
            return ServerResponse.serverResponseByError("该订单不能发货");
        }
        order.setStatus(ResponseCode.OrderStuts.ORDER_SEND.getCode());

//        4返回值
        int result=orderMapper.updateStatusAndSendtime(order);
        if (result>0){
                return ServerResponse.serverResponseBySuccess("发货成功");
        }
        return ServerResponse.serverResponseByError("发货失败");
    }
}
