package com.lc.common;

//维护状态码
public class ResponseCode {
    //成功的状态码
    public static final int SUCCESS = 0;
    //失败的状态码
    public static final int ERROR = 100;
    //登录接口常量
    public static final String Const = "login";

    //设置角色属性(使用枚举)
    public enum Roleenum {
        ROLE_ADMIN(0, "管理员"),
        ROLE_CUSTOMER(1, "普通用户");
        private int code;
        private String desc;

        private Roleenum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    //品类的错误代码
    public enum CategoryStatue {

        NEED_LOGIN(2, "请登录"),
        NO_POVERS(3, "权限不足");
        private int code;
        private String desc;

        private CategoryStatue(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    // 商品状态封装
    public enum ProductStatue {

        PRODUCT_ONLINE(1, "在售"),
        PRODUCT_OFFLINE(2, "下架"),
        PRODUCT_DELETE(3, "删除");
        private int code;
        private String desc;

        private ProductStatue(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    // 购物车选中状态
    public enum CartCheckStuts {

        PRODUCT_CHECK(1, "已选中"),
        PRODUCT_NOCHECK(0, "未选中");

        private int code;
        private String desc;

        private CartCheckStuts(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    // 订单状态
    public enum OrderStuts {
//0-取消  10-未付款  20-已付款  40-已发货  50-交易成功 60 -交易关闭
        ORDER_CANCEL(0,"已取消"),
        ORDER_UNPAY(10,"未付款"),
        ORDER_PAY(20,"已付款"),
        ORDER_SEND(40,"已发货"),
        ORDER_SUCCESS(50,"交易成功"),
        ORDER_CLOSE(60,"交易关闭")
        ;
        private int code;
        private String desc;

        private OrderStuts(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public static OrderStuts codeOf(Integer code){
            for (OrderStuts orderStuts:values()){
                if(code==orderStuts.getCode()){
                    return orderStuts;
                }
            }
            return null;
        }
    }

    // 订单支付方式状态
    public enum OrderPayStuts {

        PAY_ONLINE(1, "在线支付");

        private int code;
        private String desc;

        private OrderPayStuts(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public static OrderPayStuts codeOf(Integer code){
            for (OrderPayStuts orderPayStuts:values()){
                if(code==orderPayStuts.getCode()){
                    return orderPayStuts;
                }
            }
            return null;
        }
    }
}
