package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/16.
 */

public class CollectListBean {

    /**
     * code : 0
     * msg : 1
     * data : {"list":[{"product_id":43,"name":"秋季童鞋","img":"http://oss.yiyiyaya.cc/upload/2017/07/22/20170722161237758.jpg","sell_price":"59.00","type":0}]}
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
             * product_id : 43
             * name : 秋季童鞋
             * img : http://oss.yiyiyaya.cc/upload/2017/07/22/20170722161237758.jpg
             * sell_price : 59.00
             * type : 0
             */

            private int panicBuyItemId;
            private int product_id;
            private String name;
            private String img;
            private String sell_price;
            private int type;

            public int getPanicBuyItemId() {
                return panicBuyItemId;
            }

            public void setPanicBuyItemId(int panicBuyItemId) {
                this.panicBuyItemId = panicBuyItemId;
            }

            public int getProduct_id() {
                return product_id;
            }

            public void setProduct_id(int product_id) {
                this.product_id = product_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getSell_price() {
                return sell_price;
            }

            public void setSell_price(String sell_price) {
                this.sell_price = sell_price;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
