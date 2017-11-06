package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 */

public class AfterMallListBean {

    /**
     * code : 0
     * msg : 1
     * data : {"orders":[{"orderId":353,"sellerName":"李伟","orderGoods":[{"goodsImg":"http://yiyiyaya-oss.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/08/08/20170808110405257.jpg","goodsName":"小天才手表测试","goodsSpec":"颜色：蓝色","goodsPrice":"199.00","goodsNum":1},{"goodsImg":"http://yiyiyaya-oss.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/08/08/20170808110405257.jpg","goodsName":"小天才手表测试","goodsSpec":"颜色：紫色","goodsPrice":"199.00","goodsNum":1}],"orderStatus":2,"status":0,"amount":"10.00"},{"orderId":353,"sellerName":"李伟","orderGoods":[{"goodsImg":"http://yiyiyaya-oss.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/08/08/20170808110405257.jpg","goodsName":"小天才手表测试","goodsSpec":"颜色：蓝色","goodsPrice":"199.00","goodsNum":1},{"goodsImg":"http://yiyiyaya-oss.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/08/08/20170808110405257.jpg","goodsName":"小天才手表测试","goodsSpec":"颜色：紫色","goodsPrice":"199.00","goodsNum":1}],"orderStatus":2,"status":0,"amount":"10.00"}]}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<OrdersBean> orders;

        public List<OrdersBean> getOrders() {
            return orders;
        }

        public void setOrders(List<OrdersBean> orders) {
            this.orders = orders;
        }

        public static class OrdersBean {
            /**
             * orderId : 353
             * sellerName : 李伟
             * orderGoods : [{"goodsImg":"http://yiyiyaya-oss.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/08/08/20170808110405257.jpg","goodsName":"小天才手表测试","goodsSpec":"颜色：蓝色","goodsPrice":"199.00","goodsNum":1},{"goodsImg":"http://yiyiyaya-oss.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/08/08/20170808110405257.jpg","goodsName":"小天才手表测试","goodsSpec":"颜色：紫色","goodsPrice":"199.00","goodsNum":1}]
             * orderStatus : 2
             * status : 0
             * amount : 10.00
             */

            private int id;
            private int orderId;
            private String sellerName;
            private int orderStatus;
            private int status;
            private String amount;
            private int type;
            private int integral;
            private int orderType;
            private int yaya;
            private List<OrderGoodsBean> orderGoods;

            public void setYaya(int yaya) {
                this.yaya = yaya;
            }

            public int getYaya() {
                return yaya;
            }

            public void setOrderType(int orderType) {
                this.orderType = orderType;
            }

            public int getOrderType() {
                return orderType;
            }

            public void setIntegral(int integral) {
                this.integral = integral;
            }

            public int getIntegral() {
                return integral;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getType() {
                return type;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getId() {
                return id;
            }

            public int getOrderId() {
                return orderId;
            }

            public void setOrderId(int orderId) {
                this.orderId = orderId;
            }

            public String getSellerName() {
                return sellerName;
            }

            public void setSellerName(String sellerName) {
                this.sellerName = sellerName;
            }

            public int getOrderStatus() {
                return orderStatus;
            }

            public void setOrderStatus(int orderStatus) {
                this.orderStatus = orderStatus;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public List<OrderGoodsBean> getOrderGoods() {
                return orderGoods;
            }

            public void setOrderGoods(List<OrderGoodsBean> orderGoods) {
                this.orderGoods = orderGoods;
            }

            public static class OrderGoodsBean {
                /**
                 * goodsImg : http://yiyiyaya-oss.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/08/08/20170808110405257.jpg
                 * goodsName : 小天才手表测试
                 * goodsSpec : 颜色：蓝色
                 * goodsPrice : 199.00
                 * goodsNum : 1
                 */

                private String goodsImg;
                private String goodsName;
                private String goodsSpec;
                private String goodsPrice;
                private int integral;
                private int goodsNum;

                public int getIntegral() {
                    return integral;
                }

                public void setIntegral(int integral) {
                    this.integral = integral;
                }

                public String getGoodsImg() {
                    return goodsImg;
                }

                public void setGoodsImg(String goodsImg) {
                    this.goodsImg = goodsImg;
                }

                public String getGoodsName() {
                    return goodsName;
                }

                public void setGoodsName(String goodsName) {
                    this.goodsName = goodsName;
                }

                public String getGoodsSpec() {
                    return goodsSpec;
                }

                public void setGoodsSpec(String goodsSpec) {
                    this.goodsSpec = goodsSpec;
                }

                public String getGoodsPrice() {
                    return goodsPrice;
                }

                public void setGoodsPrice(String goodsPrice) {
                    this.goodsPrice = goodsPrice;
                }

                public int getGoodsNum() {
                    return goodsNum;
                }

                public void setGoodsNum(int goodsNum) {
                    this.goodsNum = goodsNum;
                }
            }
        }
    }
}
