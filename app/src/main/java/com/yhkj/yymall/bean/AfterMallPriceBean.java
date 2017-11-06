package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/8/24.
 */

public class AfterMallPriceBean {

    /**
     * code : 0
     * msg : 1
     * data : {"yaya":1,"amount":125.12}
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
         * yaya : 1
         * amount : 125.12
         */

        private int yaya;
        private int yayaGive;
        private int integral;
        private String amount;

        public void setIntegral(int integral) {
            this.integral = integral;
        }

        public void setYayaGive(int yayaGive) {
            this.yayaGive = yayaGive;
        }

        public int getIntegral() {
            return integral;
        }

        public int getYayaGive() {
            return yayaGive;
        }

        public int getYaya() {
            return yaya;
        }

        public void setYaya(int yaya) {
            this.yaya = yaya;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}
