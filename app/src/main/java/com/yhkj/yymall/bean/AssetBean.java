package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/8/17.
 */

public class AssetBean {

    /**
     * code : 0
     * msg : 1
     * data : {"balance":"24941.20","yaya":10,"point":8358}
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
         * balance : 24941.20
         * yaya : 10
         * point : 8358
         */

        private String balance;
        private int yaya;
        private int point;

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public int getYaya() {
            return yaya;
        }

        public void setYaya(int yaya) {
            this.yaya = yaya;
        }

        public int getPoint() {
            return point;
        }

        public void setPoint(int point) {
            this.point = point;
        }
    }
}
