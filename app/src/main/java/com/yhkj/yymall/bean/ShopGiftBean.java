package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/28.
 */

public class ShopGiftBean {

    /**
     * code : 0
     * msg : 1
     * data : {"info":[{"img":"http://oss.yiyiyaya.cc/upload/2017/09/22/20170922103029196.jpg","name":"红思达挖掘机","price":"168.00","goodsId":176,"status":3,"nums":99}]}
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
        private List<InfoBean> info;

        public List<InfoBean> getInfo() {
            return info;
        }

        public void setInfo(List<InfoBean> info) {
            this.info = info;
        }

        public static class InfoBean {
            /**
             * img : http://oss.yiyiyaya.cc/upload/2017/09/22/20170922103029196.jpg
             * name : 红思达挖掘机
             * price : 168.00
             * goodsId : 176
             * status : 3
             * nums : 99
             */

            private String img;
            private String name;
            private String price;
            private int goodsId;
            private int type;
            private int nums;

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

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public int getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(int goodsId) {
                this.goodsId = goodsId;
            }

            public int getType() {
                return type;
            }

            public void setType(int status) {
                this.type = status;
            }

            public int getNums() {
                return nums;
            }

            public void setNums(int nums) {
                this.nums = nums;
            }
        }
    }
}
