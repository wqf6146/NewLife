package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/15.
 */

public class RefundBean {

    /**
     * code : 0
     * msg : 1
     * data : {"id":18,"orderId":456,"sellerName":"yiyiyaya自营","orderGoods":[{"goodsImg":"http://yiyiyaya-oss.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/22/20170722170941880.jpg","goodsName":"贝亲（Pigeon）宽口径玻璃奶瓶","goodsSpec":"大小：240ml|颜色：绿色","goodsPrice":"70.00","goodsNum":1}],"orderStatus":6,"status":0,"amount":"70.00","orderAmount":"70.00","disposeTime":"","disposeIdea":"","content":"gvhhvjvp","time":"2017-08-11 17:00:43","qq":"111111111","phone":"03124555126"}
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
        /**
         * id : 18
         * orderId : 456
         * sellerName : yiyiyaya自营
         * orderGoods : [{"goodsImg":"http://yiyiyaya-oss.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/22/20170722170941880.jpg","goodsName":"贝亲（Pigeon）宽口径玻璃奶瓶","goodsSpec":"大小：240ml|颜色：绿色","goodsPrice":"70.00","goodsNum":1}]
         * orderStatus : 6
         * status : 0
         * amount : 70.00
         * orderAmount : 70.00
         * disposeTime :
         * disposeIdea :
         * content : gvhhvjvp
         * time : 2017-08-11 17:00:43
         * qq : 111111111
         * phone : 03124555126
         */

        private int id;
        private int orderId;
        private String sellerName;
        private int orderStatus;
        private int status;
        private String amount;
        private String orderAmount;
        private String disposeTime;
        private String orderNo;
        private String disposeIdea;
        private String content;
        private String time;
        private String qq;
        private String phone;
        private int type;
        private List<OrderGoodsBean> orderGoods;
        private int integral;
        private int refYaya;
        private int orderType;
        private int yaya;
        private int point;

        public void setPoint(int point) {
            this.point = point;
        }

        public int getPoint() {
            return point;
        }

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

        public void setRefYaya(int refYaya) {
            this.refYaya = refYaya;
        }

        public int getRefYaya() {
            return refYaya;
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

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getOrderAmount() {
            return orderAmount;
        }

        public void setOrderAmount(String orderAmount) {
            this.orderAmount = orderAmount;
        }

        public String getDisposeTime() {
            return disposeTime;
        }

        public void setDisposeTime(String disposeTime) {
            this.disposeTime = disposeTime;
        }

        public String getDisposeIdea() {
            return disposeIdea;
        }

        public void setDisposeIdea(String disposeIdea) {
            this.disposeIdea = disposeIdea;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public List<OrderGoodsBean> getOrderGoods() {
            return orderGoods;
        }

        public void setOrderGoods(List<OrderGoodsBean> orderGoods) {
            this.orderGoods = orderGoods;
        }

        public static class OrderGoodsBean {
            /**
             * goodsImg : http://yiyiyaya-oss.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/22/20170722170941880.jpg
             * goodsName : 贝亲（Pigeon）宽口径玻璃奶瓶
             * goodsSpec : 大小：240ml|颜色：绿色
             * goodsPrice : 70.00
             * goodsNum : 1
             */
            private int goodsType;
            private String goodsImg;
            private String goodsName;
            private String goodsSpec;
            private String goodsPrice;
            private int goodsNum;
            private int goodsId;
            private int panicBuyItemId;
            private int integral;

            public int getIntegral() {
                return integral;
            }

            public void setIntegral(int integral) {
                this.integral = integral;
            }

            public void setPanicBuyItemId(int panicBuyItemId) {
                this.panicBuyItemId = panicBuyItemId;
            }

            public int getPanicBuyItemId() {
                return panicBuyItemId;
            }

            public void setGoodsId(int goodsId) {
                this.goodsId = goodsId;
            }

            public int getGoodsId() {
                return goodsId;
            }

            public int getGoodsType() {
                return goodsType;
            }

            public void setGoodsType(int goodsType) {
                this.goodsType = goodsType;
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
