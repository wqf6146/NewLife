package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/26.
 */

public class TimeKillShopBean {

    /**
     * code : 0
     * msg : 1
     * data : {"list":[{"paniclBuyItemId":13,"goods_id":41,"name":"ce","marketPrice":"100.00","activePrice":"44.00","sale":1,"storeNum":199,"pic":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/23/20170723144212509.jpg","status":0},{"paniclBuyItemId":14,"goods_id":42,"name":"测试2","marketPrice":"50.00","activePrice":"11.00","sale":5,"storeNum":82,"pic":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/24/20170724152011132.jpg","status":0},{"paniclBuyItemId":15,"goods_id":43,"name":"秋季童鞋","marketPrice":"59.00","activePrice":"66.00","sale":22,"storeNum":576,"pic":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/23/20170723144212509.jpg","status":0},{"paniclBuyItemId":16,"goods_id":44,"name":"男童宝宝0-5岁夏装短袖套装B","marketPrice":"39.00","activePrice":"11.00","sale":6,"storeNum":594,"pic":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/24/20170724152011132.jpg","status":0},{"paniclBuyItemId":17,"goods_id":45,"name":"儿童电子琴钢琴麦克风宝宝益智启蒙玩具","marketPrice":"139.00","activePrice":"44.00","sale":22,"storeNum":78,"pic":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/23/20170723144212509.jpg","status":0}]}
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
             * paniclBuyItemId : 13
             * goods_id : 41
             * name : ce
             * marketPrice : 100.00
             * activePrice : 44.00
             * sale : 1
             * storeNum : 199
             * pic : http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/23/20170723144212509.jpg
             * status : 0
             */

            private int paniclBuyItemId;
            private int goods_id;
            private String name;
            private String marketPrice;
            private String activePrice;
            private int sale;
            private int storeNum;
            private String pic;
            private int status;

            public int getPaniclBuyItemId() {
                return paniclBuyItemId;
            }

            public void setPaniclBuyItemId(int paniclBuyItemId) {
                this.paniclBuyItemId = paniclBuyItemId;
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
