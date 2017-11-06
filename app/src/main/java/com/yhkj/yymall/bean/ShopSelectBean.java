package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */

public class ShopSelectBean {


    /**
     * code : 0
     * msg : 1
     * data : {"brand":[{"id":"1","name":"阿迪达斯"}],"attrs":[{"id":"3","name":"产地","value":["国产"]}],"list":[{"id":15,"name":"美国babytrend钢骨架 儿童汽车安全座椅宝宝0-12岁isofix接口","pic":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/07/20170707100900852.jpg","price":"1000.00","sale":199,"promotions":["拼团价","限购5件"],"type":2},{"id":16,"name":"儿童安全座椅汽车","pic":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/07/20170707101033215.jpg","price":"300.00","sale":0,"promotions":["拼团价","限购5件"],"type":2},{"id":17,"name":"儿童安全座椅汽车用婴儿宝宝车","pic":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/16/20170716181307479.jpg","price":"250.00","sale":1,"promotions":["拼团价","限购5件"],"type":2},{"id":25,"name":"好孩子儿童自行车","pic":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/18/20170718085603679.jpg","price":"300.00","sale":1,"promotions":["拼团价","限购5件"],"type":0}]}
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
        private List<BrandBean> brand;
        private List<AttrsBean> attrs;
        private List<ListBean> list;

        public List<BrandBean> getBrand() {
            return brand;
        }

        public void setBrand(List<BrandBean> brand) {
            this.brand = brand;
        }

        public List<AttrsBean> getAttrs() {
            return attrs;
        }

        public void setAttrs(List<AttrsBean> attrs) {
            this.attrs = attrs;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class BrandBean {
            /**
             * id : 1
             * name : 阿迪达斯
             */

            private String id;
            private String name;

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
        }

        public static class AttrsBean implements Cloneable {
            /**
             * id : 3
             * name : 产地
             * value : ["国产"]
             */

            private String id;
            private String name;
            private List<String> value;

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

            public List<String> getValue() {
                return value;
            }

            public void setValue(List<String> value) {
                this.value = value;
            }

            @Override
            public Object clone() throws CloneNotSupportedException
            {
                return super.clone();
            }
        }

        public static class ListBean {
            /**
             * id : 15
             * name : 美国babytrend钢骨架 儿童汽车安全座椅宝宝0-12岁isofix接口
             * pic : http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/07/20170707100900852.jpg
             * price : 1000.00
             * sale : 199
             * promotions : ["拼团价","限购5件"]
             * type : 2
             */

            private int id;
            private String name;
            private String pic;
            private String price;
            private int sale;
            private int type;
            private int panicBuyItemId;
            private List<String> promotions;

            public int getPanicBuyItemId() {
                return panicBuyItemId;
            }

            public void setPanicBuyItemId(int panicBuyItemId) {
                this.panicBuyItemId = panicBuyItemId;
            }

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

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public int getSale() {
                return sale;
            }

            public void setSale(int sale) {
                this.sale = sale;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public List<String> getPromotions() {
                return promotions;
            }

            public void setPromotions(List<String> promotions) {
                this.promotions = promotions;
            }
        }
    }
}
