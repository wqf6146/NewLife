package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/27.
 */

public class OrderChildBean {

    /**
     * code : 0
     * msg : 1
     * data : {"orders":[{"id":531,"sellerName":"yiyiyaya自营","goodses":[{"id":796,"goodsId":43,"goodsName":"秋季童鞋","goodsSpec":"颜色：红色|鞋码：16码","realPrice":"59.00","goodsNums":1,"goodsImg":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/22/20170722161237758.jpg"}],"payType":0,"status":5,"payStatus":0,"distributionStatus":0,"type":0,"orderAmount":"59.00","isGroup":1,"isComment":0,"orderGroupId":441},{"id":527,"sellerName":"yiyiyaya自营","goodses":[{"id":792,"goodsId":43,"goodsName":"秋季童鞋","goodsSpec":"颜色：红色|鞋码：16码","realPrice":"50.00","goodsNums":1,"goodsImg":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/22/20170722161237758.jpg"}],"payType":0,"status":5,"payStatus":1,"distributionStatus":1,"type":4,"orderAmount":"50.00","isGroup":1,"isComment":0,"orderGroupId":437}]}
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
        private List<OrdersBean> orders;

        public List<OrdersBean> getOrders() {
            return orders;
        }

        public void setOrders(List<OrdersBean> orders) {
            this.orders = orders;
        }

        public static class OrdersBean {

            /**
             * id : 531
             * sellerName : yiyiyaya自营
             * goodses : [{"id":796,"goodsId":43,"goodsName":"秋季童鞋","goodsSpec":"颜色：红色|鞋码：16码","realPrice":"59.00","goodsNums":1,"goodsImg":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/22/20170722161237758.jpg"}]
             * payType : 0
             * status : 5
             * payStatus : 0
             * distributionStatus : 0
             * type : 0
             * orderAmount : 59.00
             * isGroup : 1
             * isComment : 0
             * orderGroupId : 441
             */



            private int id;
            private String sellerName;
            private int payType;
            private int status;
            private int payStatus;
            private int distributionStatus;
            private int type;
            private String orderAmount;
            private int isGroup;
            private int isComment;
            private int orderGroupId;
            private List<GoodsesBean> goodses;

            @Override
            public boolean equals(Object obj) {
                if (this == obj)
                    return true;
                if (obj == null)
                    return false;
                if (! (obj instanceof OrdersBean))
                    return false;
                OrdersBean bean = (OrdersBean)obj;
                boolean tag = id == bean.getId() && sellerName.equals(bean.getSellerName()) && payType == bean.getPayType()
                        && status == bean.getStatus() && payStatus == bean.getPayStatus() && distributionStatus == bean.getDistributionStatus()
                        && type == bean.getType() && orderAmount.equals(bean.getOrderAmount()) && isGroup == bean.getIsGroup() && isComment == bean.getIsComment()
                        && orderGroupId == bean.getOrderGroupId();
                if (tag){
                    if (goodses.equals(bean.getGoodses()))
                        return true;
                    else
                        return false;
                }else{
                    return false;
                }
            }


            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getSellerName() {
                return sellerName;
            }

            public void setSellerName(String sellerName) {
                this.sellerName = sellerName;
            }

            public int getPayType() {
                return payType;
            }

            public void setPayType(int payType) {
                this.payType = payType;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getPayStatus() {
                return payStatus;
            }

            public void setPayStatus(int payStatus) {
                this.payStatus = payStatus;
            }

            public int getDistributionStatus() {
                return distributionStatus;
            }

            public void setDistributionStatus(int distributionStatus) {
                this.distributionStatus = distributionStatus;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getOrderAmount() {
                return orderAmount;
            }

            public void setOrderAmount(String orderAmount) {
                this.orderAmount = orderAmount;
            }

            public int getIsGroup() {
                return isGroup;
            }

            public void setIsGroup(int isGroup) {
                this.isGroup = isGroup;
            }

            public int getIsComment() {
                return isComment;
            }

            public void setIsComment(int isComment) {
                this.isComment = isComment;
            }

            public int getOrderGroupId() {
                return orderGroupId;
            }

            public void setOrderGroupId(int orderGroupId) {
                this.orderGroupId = orderGroupId;
            }

            public List<GoodsesBean> getGoodses() {
                return goodses;
            }

            public void setGoodses(List<GoodsesBean> goodses) {
                this.goodses = goodses;
            }

            public static class GoodsesBean {
                /**
                 * id : 796
                 * goodsId : 43
                 * goodsName : 秋季童鞋
                 * goodsSpec : 颜色：红色|鞋码：16码
                 * realPrice : 59.00
                 * goodsNums : 1
                 * goodsImg : http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/22/20170722161237758.jpg
                 */

                private int id;
                private int goodsId;
                private String goodsName;
                private String goodsSpec;
                private String realPrice;
                private int goodsNums;
                private String goodsImg;

                @Override
                public boolean equals(Object obj) {
                    if (this==obj) return true;
                    if (!(obj instanceof GoodsesBean)) return false;

                    GoodsesBean bean = (GoodsesBean)obj;
                    boolean tag= id == bean.id && goodsId == bean.getGoodsId() && goodsName.equals(bean.getGoodsName())
                            && goodsSpec.equals(bean.getGoodsSpec()) && realPrice.equals(bean.getRealPrice()) && goodsNums == bean.goodsNums
                            && goodsImg.equals(bean.getGoodsImg());
                    if (tag) return true;
                    return false;
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
            }
        }
    }
}
