package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/7/31.
 */

public class RentDetailBean {

    /**
     * code : 0
     * msg : 1
     * data : {"id":518,"goodsId":20,"goodsName":"测试商家商品","goodsSpec":"颜色:1,容量:2","realPrice":"20.00","goodsImg":"upload/2017/07/16/20170716181307479.jpg","orderNo":"20170718135041640829","payTime":"2017-07-18 13:50:45","endTime":"2017-08-18","status":5}
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
         * id : 518
         * goodsId : 20
         * goodsName : 测试商家商品
         * goodsSpec : 颜色:1,容量:2
         * realPrice : 20.00
         * goodsImg : upload/2017/07/16/20170716181307479.jpg
         * orderNo : 20170718135041640829
         * payTime : 2017-07-18 13:50:45
         * endTime : 2017-08-18
         * status : 5
         */

        private int id;
        private int goodsId;
        private String goodsName;
        private String goodsSpec;
        private String realPrice;
        private String goodsImg;
        private String orderNo;
        private String payTime;
        private String endTime;
        private int status;
        private int orderId;
        private int nums;

        public int getNums() {
            return nums;
        }

        public void setNums(int nums) {
            this.nums = nums;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public int getOrderId() {
            return orderId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
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

        public String getRealPrice() {
            return realPrice;
        }

        public void setRealPrice(String realPrice) {
            this.realPrice = realPrice;
        }

        public String getGoodsImg() {
            return goodsImg;
        }

        public void setGoodsImg(String goodsImg) {
            this.goodsImg = goodsImg;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getPayTime() {
            return payTime;
        }

        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
