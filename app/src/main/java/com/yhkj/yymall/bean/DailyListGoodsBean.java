package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/21.
 */

public class DailyListGoodsBean {

    /**
     * code : 0
     * msg : 1
     * data : {"list":[{"dailyActiveGoodsId":3,"goods_id":442,"name":"正版幼儿绘本图书中英双语儿童情绪管理与性格培养","marketPrice":"15.00","activePrice":"5.00","sale":0,"storeNum":50,"pic":"http://oss.yiyiyaya.cc/upload/2017/10/19/20171019120915517.jpg","status":0}]}
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
             * dailyActiveGoodsId : 3
             * goods_id : 442
             * name : 正版幼儿绘本图书中英双语儿童情绪管理与性格培养
             * marketPrice : 15.00
             * activePrice : 5.00
             * sale : 0
             * storeNum : 50
             * pic : http://oss.yiyiyaya.cc/upload/2017/10/19/20171019120915517.jpg
             * status : 0
             */

            private int dailyActiveGoodsId;
            private int goods_id;
            private String name;
            private String marketPrice;
            private String activePrice;
            private int sale;
            private int storeNum;
            private String pic;
            private int status;

            public int getDailyActiveGoodsId() {
                return dailyActiveGoodsId;
            }

            public void setDailyActiveGoodsId(int dailyActiveGoodsId) {
                this.dailyActiveGoodsId = dailyActiveGoodsId;
            }

            public int getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(int goods_id) {
                this.goods_id = goods_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMarketPrice() {
                return marketPrice;
            }

            public void setMarketPrice(String marketPrice) {
                this.marketPrice = marketPrice;
            }

            public String getActivePrice() {
                return activePrice;
            }

            public void setActivePrice(String activePrice) {
                this.activePrice = activePrice;
            }

            public int getSale() {
                return sale;
            }

            public void setSale(int sale) {
                this.sale = sale;
            }

            public int getStoreNum() {
                return storeNum;
            }

            public void setStoreNum(int storeNum) {
                this.storeNum = storeNum;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
