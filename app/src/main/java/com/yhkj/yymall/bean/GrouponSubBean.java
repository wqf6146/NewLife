package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/7/31.
 */

public class GrouponSubBean {

    /**
     * code : 0
     * msg : 111
     * data : {"result":0,"groupUserId":97,"totalPay":50,"goods":{"id":91,"name":"海贼王手办","img":"http://oss.yiyiyaya.cc/upload/2017/07/31/20170731100518799.jpg","price":"50.00","num":"1","spec":"婴幼儿教具：全套"}}
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
         * result : 0
         * groupUserId : 97
         * totalPay : 50
         * goods : {"id":91,"name":"海贼王手办","img":"http://oss.yiyiyaya.cc/upload/2017/07/31/20170731100518799.jpg","price":"50.00","num":"1","spec":"婴幼儿教具：全套"}
         */

        private int result;
        private int groupUserId;
        private float totalPay;
        private GoodsBean goods;

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public int getGroupUserId() {
            return groupUserId;
        }

        public void setGroupUserId(int groupUserId) {
            this.groupUserId = groupUserId;
        }

        public float getTotalPay() {
            return totalPay;
        }

        public void setTotalPay(float totalPay) {
            this.totalPay = totalPay;
        }

        public GoodsBean getGoods() {
            return goods;
        }

        public void setGoods(GoodsBean goods) {
            this.goods = goods;
        }

        public static class GoodsBean {
            /**
             * id : 91
             * name : 海贼王手办
             * img : http://oss.yiyiyaya.cc/upload/2017/07/31/20170731100518799.jpg
             * price : 50.00
             * num : 1
             * spec : 婴幼儿教具：全套
             */

            private int id;
            private String name;
            private String img;
            private String price;
            private String num;
            private String spec;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getSpec() {
                return spec;
            }

            public void setSpec(String spec) {
                this.spec = spec;
            }
        }
    }
}
