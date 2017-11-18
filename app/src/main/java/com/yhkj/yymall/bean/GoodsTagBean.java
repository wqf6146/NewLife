package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/28.
 */

public class GoodsTagBean {

    /**
     * code : 0
     * msg : 1
     * data : {"impressions":[{"id":41,"goods_id":43,"impression":"很好","total":0},{"id":42,"goods_id":43,"impression":"还可以","total":0},{"id":43,"goods_id":43,"impression":"不好","total":0}],"count":0,"praiseTotal":0,"inTotal":0,"badTotal":0,"imgTotal":0}
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
         * impressions : [{"id":41,"goods_id":43,"impression":"很好","total":0},{"id":42,"goods_id":43,"impression":"还可以","total":0},{"id":43,"goods_id":43,"impression":"不好","total":0}]
         * count : 0
         * praiseTotal : 0
         * inTotal : 0
         * badTotal : 0
         * imgTotal : 0
         */

        private int count;
        private int praiseTotal;
        private int inTotal;
        private int badTotal;
        private int imgTotal;
        private List<ImpressionsBean> impressions;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getPraiseTotal() {
            return praiseTotal;
        }

        public void setPraiseTotal(int praiseTotal) {
            this.praiseTotal = praiseTotal;
        }

        public int getInTotal() {
            return inTotal;
        }

        public void setInTotal(int inTotal) {
            this.inTotal = inTotal;
        }

        public int getBadTotal() {
            return badTotal;
        }

        public void setBadTotal(int badTotal) {
            this.badTotal = badTotal;
        }

        public int getImgTotal() {
            return imgTotal;
        }

        public void setImgTotal(int imgTotal) {
            this.imgTotal = imgTotal;
        }

        public List<ImpressionsBean> getImpressions() {
            return impressions;
        }

        public void setImpressions(List<ImpressionsBean> impressions) {
            this.impressions = impressions;
        }

        public static class ImpressionsBean {
            /**
             * id : 41
             * goods_id : 43
             * impression : 很好
             * total : 0
             */

            private int id;
            private int goods_id;
            private String impression;
            private int total;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(int goods_id) {
                this.goods_id = goods_id;
            }

            public String getImpression() {
                return impression;
            }

            public void setImpression(String impression) {
                this.impression = impression;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }
        }
    }
}
