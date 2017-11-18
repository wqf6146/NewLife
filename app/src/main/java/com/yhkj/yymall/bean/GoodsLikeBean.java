package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/24.
 */

public class GoodsLikeBean {

    /**
     * code : 0
     * msg : 111
     * data : {"list":[{"id":44,"name":"男童宝宝0-5岁夏装短袖套装B","type":2,"img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/22/20170722162341850.jpg","sale":0,"price":"39.00","promotions":[]},{"id":46,"name":"贝亲（Pigeon）宽口径玻璃奶瓶","type":2,"img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/22/20170722170941880.jpg","sale":0,"price":"89.90","promotions":[]},{"id":43,"name":"秋季童鞋","type":2,"img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/22/20170722161237758.jpg","sale":2,"price":"59.00","promotions":[]},{"id":54,"name":"迪士尼滑板车儿童 2-3-4-5-6岁三两轮摇摆滑轮车幼儿脚踏板车四轮","type":0,"img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/24/20170724190957744.jpg","sale":0,"price":"159.00","promotions":[]}]}
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
             * id : 44
             * name : 男童宝宝0-5岁夏装短袖套装B
             * type : 2
             * img : http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/22/20170722162341850.jpg
             * sale : 0
             * price : 39.00
             * promotions : []
             */

            private int id;
            private String name;
            private int type;
            private String img;
            private int sale;
            private String price;
            private int panicBuyItemId;
            private List<?> promotions;

            public void setPanicBuyItemId(int panicBuyItemId) {
                this.panicBuyItemId = panicBuyItemId;
            }

            public int getPanicBuyItemId() {
                return panicBuyItemId;
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
