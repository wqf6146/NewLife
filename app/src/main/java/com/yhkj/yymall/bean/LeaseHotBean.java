package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/13.
 */

public class LeaseHotBean {

    /**
     * code : 0
     * msg : 1
     * data : {"hotlist":[{"id":"16","name":"儿童安全座椅汽车","price":"1.00","rentId":"3","sale":"389","img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/07/20170707101033215.jpg","promotion":["拼团价","限购5件"]},{"id":"17","name":"儿童安全座椅汽车用婴儿宝宝车","price":"23.00","rentId":"4","sale":"456","img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/07/20170707102115754.jpg","promotion":["拼团价","限购5件"]},{"id":"1","name":"夏装日系连帽5五分袖t恤男短袖韩版潮流带帽中袖学生宽松半袖衣服","price":"21.00","rentId":"5","sale":"156","img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/04/20170704134928154.jpg","promotion":["拼团价","限购5件"]},{"id":"18","name":"超级哈利isofix硬接口 竹纤维面料 可换衣设计 高宽躺调节 此商品正在参加聚划算，22小时34","price":"600.00","rentId":"6","sale":"0","img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/07/20170707142443558.jpg","promotion":["拼团价","限购5件"]}]}
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
        private List<HotlistBean> hotlist;

        public List<HotlistBean> getHotlist() {
            return hotlist;
        }

        public void setHotlist(List<HotlistBean> hotlist) {
            this.hotlist = hotlist;
        }

        public static class HotlistBean {
            /**
             * id : 16
             * name : 儿童安全座椅汽车
             * price : 1.00
             * rentId : 3
             * sale : 389
             * img : http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/07/20170707101033215.jpg
             * promotion : ["拼团价","限购5件"]
             */

            private String id;
            private String name;
            private String price;
            private String rentId;
            private String sale;
            private String img;
            private List<String> promotion;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public String getRentId() {
                return rentId;
            }

            public void setRentId(String rentId) {
                this.rentId = rentId;
            }

            public String getSale() {
                return sale;
            }

            public void setSale(String sale) {
                this.sale = sale;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public List<String> getPromotion() {
                return promotion;
            }

            public void setPromotion(List<String> promotion) {
                this.promotion = promotion;
            }
        }
    }
}