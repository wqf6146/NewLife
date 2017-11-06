package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/20.
 */

public class YiYaShopBean {


    /**
     * code : 0
     * msg : 1
     * data : {"fields":[31,30,29],"categroys":[{"id":39,"name":"滑行车","slogan1":"","slogan2":"","pic":"http://oss.yiyiyaya.cc/upload/2017/08/14/20170814083812173.jpg"},{"id":45,"name":"毛绒玩具","slogan1":"","slogan2":"","pic":"http://oss.yiyiyaya.cc/upload/2017/08/14/20170814084030148.jpg"},{"id":20,"name":"儿童服饰","slogan1":"","slogan2":"","pic":"http://oss.yiyiyaya.cc/upload/2017/08/14/20170814084112894.jpg"},{"id":36,"name":"扭扭车","slogan1":"","slogan2":"","pic":"http://oss.yiyiyaya.cc/upload/2017/08/14/20170814084226581.jpg"}],"goods":[{"id":47,"name":"贝亲（Pigeon）自然实感宽口径奶嘴","type":0,"img":"http://oss.yiyiyaya.cc/upload/2017/07/22/20170722171905543.jpg","sale":46,"price":"25.00","promotions":[]},{"id":61,"name":"实木婴儿床","type":0,"img":"http://oss.yiyiyaya.cc/upload/2017/07/25/20170725143045329.jpg","sale":9,"price":"250.00","promotions":[]},{"id":77,"name":"儿童滑行车啦啦嘿嘿嘿嘿额hi额嘿嘿嘿嘿嘿嘿额hi嗯好IEhi额嘿嘿额hi额嘿嘿嘿嘿呀呀呀呀呀呀呀呀呀","type":0,"img":"http://oss.yiyiyaya.cc/upload/2017/07/27/20170727160203666.jpg","sale":17,"price":"5.00","promotions":[]},{"id":91,"name":"海贼王手办","type":0,"img":"http://oss.yiyiyaya.cc/upload/2017/08/01/20170801064032446.jpg","sale":51,"price":"66.60","promotions":[]}]}
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
        private List<Integer> fields;
        private List<CategroysBean> categroys;
        private List<GoodsBean> goods;

        public List<Integer> getFields() {
            return fields;
        }

        public void setFields(List<Integer> fields) {
            this.fields = fields;
        }

        public List<CategroysBean> getCategroys() {
            return categroys;
        }

        public void setCategroys(List<CategroysBean> categroys) {
            this.categroys = categroys;
        }

        public List<GoodsBean> getGoods() {
            return goods;
        }

        public void setGoods(List<GoodsBean> goods) {
            this.goods = goods;
        }

        public static class CategroysBean {
            /**
             * id : 39
             * name : 滑行车
             * slogan1 :
             * slogan2 :
             * pic : http://oss.yiyiyaya.cc/upload/2017/08/14/20170814083812173.jpg
             */

            private int id;
            private String name;
            private String slogan1;
            private String slogan2;
            private String pic;

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

            public String getSlogan1() {
                return slogan1;
            }

            public void setSlogan1(String slogan1) {
                this.slogan1 = slogan1;
            }

            public String getSlogan2() {
                return slogan2;
            }

            public void setSlogan2(String slogan2) {
                this.slogan2 = slogan2;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }
        }

        public static class GoodsBean {
            /**
             * id : 47
             * name : 贝亲（Pigeon）自然实感宽口径奶嘴
             * type : 0
             * img : http://oss.yiyiyaya.cc/upload/2017/07/22/20170722171905543.jpg
             * sale : 46
             * price : 25.00
             * promotions : []
             */

            public int id;
            public String name;
            public int type;
            public String img;
            public int sale;
            public String price;
            public List<?> promotions;
            public int panicBuyItemId;

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

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public int getSale() {
                return sale;
            }

            public void setSale(int sale) {
                this.sale = sale;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public List<?> getPromotions() {
                return promotions;
            }

            public void setPromotions(List<?> promotions) {
                this.promotions = promotions;
            }
        }
    }
}
