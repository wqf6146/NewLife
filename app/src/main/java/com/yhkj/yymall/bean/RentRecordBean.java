package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/31.
 */

public class RentRecordBean {

    /**
     * code : 0
     * msg : 1
     * data : {"rentals":[{"goodses":[{"id":957,"goodsId":46,"goodsName":"贝亲（Pigeon）宽口径玻璃奶瓶","goodsSpec":"大小：160ml|颜色：黄色","realPrice":70,"goodsImg":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/22/20170722170941880.jpg","goodsNums":1}]},{"goodses":[{"id":956,"goodsId":46,"goodsName":"贝亲（Pigeon）宽口径玻璃奶瓶","goodsSpec":"大小：160ml|颜色：黄色","realPrice":70,"goodsImg":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/22/20170722170941880.jpg","goodsNums":1}]},{"goodses":[{"id":953,"goodsId":46,"goodsName":"贝亲（Pigeon）宽口径玻璃奶瓶","goodsSpec":"大小：160ml|颜色：黄色","realPrice":70,"goodsImg":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/22/20170722170941880.jpg","goodsNums":1}]}]}
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
        private List<RentalsBean> rentals;

        public List<RentalsBean> getRentals() {
            return rentals;
        }

        public void setRentals(List<RentalsBean> rentals) {
            this.rentals = rentals;
        }

        public static class RentalsBean {
            private List<GoodsesBean> goodses;

            public List<GoodsesBean> getGoodses() {
                return goodses;
            }

            public void setGoodses(List<GoodsesBean> goodses) {
                this.goodses = goodses;
            }

            public static class GoodsesBean {
                /**
                 * id : 957
                 * goodsId : 46
                 * goodsName : 贝亲（Pigeon）宽口径玻璃奶瓶
                 * goodsSpec : 大小：160ml|颜色：黄色
                 * realPrice : 70
                 * goodsImg : http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/22/20170722170941880.jpg
                 * goodsNums : 1
                 */

                private int id;
                private int goodsId;
                private int orderId;
                private String goodsName;
                private String goodsSpec;
                private double realPrice;
                private String goodsImg;
                private int goodsNums;

                public void setOrderId(int orderId) {
                    this.orderId = orderId;
                }

                public int getOrderId() {
                    return orderId;
                }

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

                public double getRealPrice() {
                    return realPrice;
                }

                public void setRealPrice(double realPrice) {
                    this.realPrice = realPrice;
                }

                public String getGoodsImg() {
                    return goodsImg;
                }

                public void setGoodsImg(String goodsImg) {
                    this.goodsImg = goodsImg;
                }

                public int getGoodsNums() {
                    return goodsNums;
                }

                public void setGoodsNums(int goodsNums) {
                    this.goodsNums = goodsNums;
                }
            }
        }
    }
}
