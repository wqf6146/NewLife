package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/5.
 */

public class HomeRecommBean {


    /**
     * code : 0
     * msg : 1
     * data : {"categorys":[{"id":1,"name":"男装","goods":[{"id":12,"name":"高景观婴儿手推车","price":799,"sale":9,"img":"http://test.com/img.jpg","type":0,"promotions":["满减","赠积分"]}],"groupPurchaseNum":1202}]}
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
        private List<CategorysBean> categorys;

        public List<CategorysBean> getCategorys() {
            return categorys;
        }

        public void setCategorys(List<CategorysBean> categorys) {
            this.categorys = categorys;
        }

        public static class CategorysBean {
            /**
             * id : 1
             * name : 男装
             * goods : [{"id":12,"name":"高景观婴儿手推车","price":799,"sale":9,"img":"http://test.com/img.jpg","type":0,"promotions":["满减","赠积分"]}]
             * groupPurchaseNum : 1202
             */

            private int id;
            private String name;
            private int groupPurchaseNum;
            private List<GoodsBean> goods;

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

            public int getGroupPurchaseNum() {
                return groupPurchaseNum;
            }

            public void setGroupPurchaseNum(int groupPurchaseNum) {
                this.groupPurchaseNum = groupPurchaseNum;
            }

            public List<GoodsBean> getGoods() {
                return goods;
            }

            public void setGoods(List<GoodsBean> goods) {
                this.goods = goods;
            }

            public static class GoodsBean {
                /**
                 * id : 12
                 * name : 高景观婴儿手推车
                 * price : 799
                 * sale : 9
                 * img : http://test.com/img.jpg
                 * type : 0
                 * promotions : ["满减","赠积分"]
                 */

                private int id;
                private String name;
                private double price;
                private int sale;
                private String img;
                private int type;
                private List<String> promotions;

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

                public double getPrice() {
                    return price;
                }

                public void setPrice(double price) {
                    this.price = price;
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
}
