package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/1.
 */

public class GrouponListBeans {


    /**
     * code : 0
     * msg : 111
     * data : {"list":[{"id":10,"goodsId":52,"name":"初代小黄车","price":"49.00","marketPrice":"100.00","sale":2,"img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/23/20170723144212509.jpg"},{"id":9,"name":"儿童安全座椅","price":"2666.00","marketPrice":"2999.00","sale":11,"img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/25/20170725162614221.jpg"}]}
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
             * id : 10
             * goodsId : 52
             * name : 初代小黄车
             * price : 49.00
             * marketPrice : 100.00
             * sale : 2
             * img : http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/23/20170723144212509.jpg
             */

            private int id;
            private int goodsId;
            private String name;
            private String price;
            private String marketPrice;
            private int sale;
            private String img;

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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getMarketPrice() {
                return marketPrice;
            }

            public void setMarketPrice(String marketPrice) {
                this.marketPrice = marketPrice;
            }

            public int getSale() {
                return sale;
            }

            public void setSale(int sale) {
                this.sale = sale;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }
        }
    }
}
