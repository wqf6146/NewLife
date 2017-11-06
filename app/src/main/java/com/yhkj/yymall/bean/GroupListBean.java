package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/2.
 */

public class GroupListBean {


    /**
     * code : 0
     * data : {"list":[{"id":374,"orderId":897,"name":"海贼王手办","price":"50.00","num":1,"img":"http://yiyiyaya-oss.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/31/20170731100518799.jpg","spec":"婴幼儿教具：全套","personNum":2,"type":1,"time":1}]}
     */

    private int code;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
             * id : 374
             * orderId : 897
             * name : 海贼王手办
             * price : 50.00
             * num : 1
             * img : http://yiyiyaya-oss.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/31/20170731100518799.jpg
             * spec : 婴幼儿教具：全套
             * personNum : 2
             * type : 1
             * time : 1
             */

            private int goodsId;
            private int id;
            private int orderId;
            private String name;
            private String price;
            private int num;
            private String img;
            private String spec;
            private int personNum;
            private Integer type;
            private Integer time;

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

            public void setGoodsId(int goodsId){
                this.goodsId = goodsId;
            }
            public int getGoodsId(){
                return goodsId;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getSpec() {
                return spec;
            }

            public void setSpec(String spec) {
                this.spec = spec;
            }

            public int getPersonNum() {
                return personNum;
            }

            public void setPersonNum(int personNum) {
                this.personNum = personNum;
            }

            public Integer getType() {
                return type;
            }

            public void setType(Integer type) {
                this.type = type;
            }

            public Integer getTime() {
                return time;
            }

            public void setTime(Integer time) {
                this.time = time;
            }
        }
    }
}
