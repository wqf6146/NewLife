package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */

public class ShopListBean {

    /**
     * code : 0
     * msg : 1
     * data : {"list":[{"id":1,"goodsId":118,"img":"http://oss.yiyiyaya.cc/upload/2017/08/20/20170820102111386.jpg","name":"儿童学习桌椅套装可升降写字台桌椅 S号　3-10岁","price":800,"sale":0},{"id":5,"goodsId":155,"img":"http://oss.yiyiyaya.cc/upload/2017/08/20/20170820102111386.jpg","name":"儿童学习桌椅套装可升降写字台桌椅 S号　3-10岁","price":10,"sale":0}]}
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
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 1
             * goodsId : 118
             * img : http://oss.yiyiyaya.cc/upload/2017/08/20/20170820102111386.jpg
             * name : 儿童学习桌椅套装可升降写字台桌椅 S号　3-10岁
             * price : 800
             * sale : 0
             */

            private int id;
            private int goodsId;
            private String img;
            private String name;
            private double price;
            private int sale;
            private String marketPrice;
            private double discount;

            public double getDiscount() {
                return discount;
            }

            public String getMarketPrice() {
                return marketPrice;
            }

            public void setDiscount(double discount) {
                this.discount = discount;
            }

            public void setMarketPrice(String marketPrice) {
                this.marketPrice = marketPrice;
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

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public int getSale() {
                return sale;
            }

            public void setSale(int sale) {
                this.sale = sale;
            }
        }
    }
}
