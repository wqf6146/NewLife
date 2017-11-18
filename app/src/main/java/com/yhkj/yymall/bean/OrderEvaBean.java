package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/28.
 */

public class OrderEvaBean {

    /**
     * code : 0
     * msg : 1
     * data : {"id":713,"orderNo":"20170727152343164297","createTime":"2017-07-27 15:23:43","sellerId":0,"sellerName":"yiyiyaya自营","goodses":[{"id":1013,"goodsId":63,"goodsName":"儿童安全座椅","goodsSpec":"颜色：红色","realPrice":"2666.00","goodsNums":1,"goodsImg":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/26/20170726134525151.jpg","impression":[{"id":52,"goods_id":63,"impression":"质量好"},{"id":53,"goods_id":63,"impression":"快递给力"},{"id":54,"goods_id":63,"impression":"性价比高"}]}]}
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
         * id : 713
         * orderNo : 20170727152343164297
         * createTime : 2017-07-27 15:23:43
         * sellerId : 0
         * sellerName : yiyiyaya自营
         * goodses : [{"id":1013,"goodsId":63,"goodsName":"儿童安全座椅","goodsSpec":"颜色：红色","realPrice":"2666.00","goodsNums":1,"goodsImg":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/26/20170726134525151.jpg","impression":[{"id":52,"goods_id":63,"impression":"质量好"},{"id":53,"goods_id":63,"impression":"快递给力"},{"id":54,"goods_id":63,"impression":"性价比高"}]}]
         */

        private int id;
        private String orderNo;
        private String createTime;
        private int sellerId;
        private String sellerName;
        private List<GoodsesBean> goodses;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getSellerId() {
            return sellerId;
        }

        public void setSellerId(int sellerId) {
            this.sellerId = sellerId;
        }

        public String getSellerName() {
            return sellerName;
        }

        public void setSellerName(String sellerName) {
            this.sellerName = sellerName;
        }

        public List<GoodsesBean> getGoodses() {
            return goodses;
        }

        public void setGoodses(List<GoodsesBean> goodses) {
            this.goodses = goodses;
        }

        public static class GoodsesBean {
            /**
             * id : 1013
             * goodsId : 63
             * goodsName : 儿童安全座椅
             * goodsSpec : 颜色：红色
             * realPrice : 2666.00
             * goodsNums : 1
             * goodsImg : http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/26/20170726134525151.jpg
             * impression : [{"id":52,"goods_id":63,"impression":"质量好"},{"id":53,"goods_id":63,"impression":"快递给力"},{"id":54,"goods_id":63,"impression":"性价比高"}]
             */

            private int id;
            private int goodsId;
            private String goodsName;
            private String goodsSpec;
            private String realPrice;
            private int goodsNums;
            private String goodsImg;
            private List<ImpressionBean> impression;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(int goodsId) {
                this.goodsId = goodsId;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public String getGoodsSpec() {
                return goodsSpec;
            }

            public void setGoodsSpec(String goodsSpec) {
                this.goodsSpec = goodsSpec;
            }

            public String getRealPrice() {
                return realPrice;
            }

            public void setRealPrice(String realPrice) {
                this.realPrice = realPrice;
            }

            public int getGoodsNums() {
                return goodsNums;
            }

            public void setGoodsNums(int goodsNums) {
                this.goodsNums = goodsNums;
            }

            public String getGoodsImg() {
                return goodsImg;
            }

            public void setGoodsImg(String goodsImg) {
                this.goodsImg = goodsImg;
            }

            public List<ImpressionBean> getImpression() {
                return impression;
            }

            public void setImpression(List<ImpressionBean> impression) {
                this.impression = impression;
            }

            public static class ImpressionBean {
                /**
                 * id : 52
                 * goods_id : 63
                 * impression : 质量好
                 */

                private int id;
                private int goods_id;
                private String impression;

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
            }
        }
    }
}
