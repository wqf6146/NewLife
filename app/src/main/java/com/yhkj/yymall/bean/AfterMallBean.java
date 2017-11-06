package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/8/14.
 */

public class AfterMallBean {

    /**
     * code : 0
     * msg : 1
     * data : {"orderId":"353","amount":"398.00","wamount":0,"goodsNums":2}
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
         * orderId : 353
         * amount : 398.00
         * wamount : 0
         * goodsNums : 2
         */

        private String orderGoodsId;
        private String amount;
        private String wamount;
        private int goodsNums;
        private int is_send;
        private int yaya;
        private int freightStatus;
        private String freight;
        private int integral;
        private int yayaGive;

        public String getFrieght() {
            return freight;
        }

        public void setFrieght(String frieght) {
            this.freight = frieght;
        }

        public String getOrderGoodsId() {
            return orderGoodsId;
        }

        public void setOrderGoodsId(String orderGoodsId) {
            this.orderGoodsId = orderGoodsId;
        }

        public void setFreightStatus(int freightStatus) {
            this.freightStatus = freightStatus;
        }

        public int getFreightStatus() {
            return freightStatus;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
        }

        public int getIntegral() {
            return integral;
        }

        public void setYayaGive(int yayaGive) {
            this.yayaGive = yayaGive;
        }

        public int getYayaGive() {
            return yayaGive;
        }

        public void setYaya(int yaya) {
            this.yaya = yaya;
        }

        public int getYaya() {
            return yaya;
        }

        public void setIs_send(int is_send) {
            this.is_send = is_send;
        }

        public int getIs_send() {
            return is_send;
        }

        public String getOrderId() {
            return orderGoodsId;
        }

        public void setOrderId(String orderId) {
            this.orderGoodsId = orderId;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getWamount() {
            return wamount;
        }

        public void setWamount(String wamount) {
            this.wamount = wamount;
        }

        public int getGoodsNums() {
            return goodsNums;
        }

        public void setGoodsNums(int goodsNums) {
            this.goodsNums = goodsNums;
        }
    }
}
