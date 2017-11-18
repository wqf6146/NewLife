package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/7/18.
 */

public class NormsBean {

    /**
     * code : 0
     * msg : 11
     * data : {"id":5,"storeNum":95,"price":23,"img":"http://test.com/img.jpg"}
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
         * id : 5
         * storeNum : 95
         * price : 23.0
         * img : http://test.com/img.jpg
         */

        private int id;
        private int limitnum;
        private int storeNum;
        private double price;
        private String img;
        private int yayaLimit;
        private int allowMaxNum;
        private double normalPrice;

        public void setNormalPrice(double normalPrice) {
            this.normalPrice = normalPrice;
        }

        public double getNormalPrice() {
            return normalPrice;
        }

        public void setAllowMaxNum(int allowMaxNum) {
            this.allowMaxNum = allowMaxNum;
        }

        public int getAllowMaxNum() {
            return allowMaxNum;
        }

        public int getLimitnum() {
            return limitnum;
        }

        public void setLimitnum(int limitnum) {
            this.limitnum = limitnum;
        }

        public int getYayaLimit() {
            return yayaLimit;
        }

        public void setYayaLimit(int yayaLimit) {
            this.yayaLimit = yayaLimit;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStoreNum() {
            return storeNum;
        }

        public void setStoreNum(int storeNum) {
            this.storeNum = storeNum;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}
