package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */

public class OrderDetailBean {

    /**
     * code : 0
     * msg : 1
     * data : {"id":528,"orderNo":"20170722181515361367","createTime":"2017-07-22 18:15:54","sellerName":"yiyiyaya自营","goodses":[{"id":793,"goodsId":43,"goodsName":"秋季童鞋","goodsSpec":"颜色：红色|鞋码：16码","realPrice":"59.00","goodsNums":1,"goodsImg":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/22/20170722161237758.jpg","refundmentStatus":"0"}],"trace":{"AcceptTime":"2017-07-25 13:50:17","AcceptStation":"客户 签收人: . 已签收 感谢使用圆通速递，期待再次为您服务"},"payType":0,"status":6,"payStatus":1,"distributionStatus":1,"type":0,"realAmount":"59.00","payableAmount":"59.00","isGroup":1,"isComment":0,"acceptName":"张煜轩","telphone":"","address":"2017-7-22测试地址"}
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
         * id : 528
         * orderNo : 20170722181515361367
         * createTime : 2017-07-22 18:15:54
         * sellerName : yiyiyaya自营
         * goodses : [{"id":793,"goodsId":43,"goodsName":"秋季童鞋","goodsSpec":"颜色：红色|鞋码：16码","realPrice":"59.00","goodsNums":1,"goodsImg":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/22/20170722161237758.jpg","refundmentStatus":"0"}]
         * trace : {"AcceptTime":"2017-07-25 13:50:17","AcceptStation":"客户 签收人: . 已签收 感谢使用圆通速递，期待再次为您服务"}
         * payType : 0
         * status : 6
         * payStatus : 1
         * distributionStatus : 1
         * type : 0
         * realAmount : 59.00
         * payableAmount : 59.00
         * isGroup : 1
         * isComment : 0
         * acceptName : 张煜轩
         * telphone :
         * address : 2017-7-22测试地址
         *
         */

        private int id;
        private String orderNo;
        private String createTime;
        private String sellerName;
        private TraceBean trace;
        private int payType;
        private int status;
        private int payStatus;
        private int distributionStatus;
        private int type;
        private String realAmount;
        private String payableAmount;
        private int isGroup;
        private int isComment;
        private String acceptName;
        private String telphone;
        private String address;
        private int groupUserId;
        private String promotions;
        private String freight;
        private int yaya;
        private List<GoodsesBean> goodses;

        public void setFreight(String freight) {
            this.freight = freight;
        }

        public void setPromotions(String promotions) {
            this.promotions = promotions;
        }

        public void setYaya(int yaya) {
            this.yaya = yaya;
        }

        public int getYaya() {
            return yaya;
        }

        public String getFreight() {
            return freight;
        }

        public String getPromotions() {
            return promotions;
        }

        public int getGroupUserId() {
            return groupUserId;
        }

        public void setGroupUserId(int groupUserId) {
            this.groupUserId = groupUserId;
        }

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

        public String getSellerName() {
            return sellerName;
        }

        public void setSellerName(String sellerName) {
            this.sellerName = sellerName;
        }

        public TraceBean getTrace() {
            return trace;
        }

        public void setTrace(TraceBean trace) {
            this.trace = trace;
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

        public String getRealAmount() {
            return realAmount;
        }

        public void setRealAmount(String realAmount) {
            this.realAmount = realAmount;
        }

        public String getPayableAmount() {
            return payableAmount;
        }

        public void setPayableAmount(String payableAmount) {
            this.payableAmount = payableAmount;
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

        public String getAcceptName() {
            return acceptName;
        }

        public void setAcceptName(String acceptName) {
            this.acceptName = acceptName;
        }

        public String getTelphone() {
            return telphone;
        }

        public void setTelphone(String telphone) {
            this.telphone = telphone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public List<GoodsesBean> getGoodses() {
            return goodses;
        }

        public void setGoodses(List<GoodsesBean> goodses) {
            this.goodses = goodses;
        }

        public static class TraceBean {
            /**
             * AcceptTime : 2017-07-25 13:50:17
             * AcceptStation : 客户 签收人: . 已签收 感谢使用圆通速递，期待再次为您服务
             */

            private String AcceptTime;
            private String AcceptStation;

            public String getAcceptTime() {
                return AcceptTime;
            }

            public void setAcceptTime(String AcceptTime) {
                this.AcceptTime = AcceptTime;
            }

            public String getAcceptStation() {
                return AcceptStation;
            }

            public void setAcceptStation(String AcceptStation) {
                this.AcceptStation = AcceptStation;
            }
        }

        public static class GoodsesBean {
            /**
             * id : 793
             * goodsId : 43
             * goodsName : 秋季童鞋
             * goodsSpec : 颜色：红色|鞋码：16码
             * realPrice : 59.00
             * goodsNums : 1
             * goodsImg : http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/22/20170722161237758.jpg
             * refundmentStatus : 0
             */

            private int id;
            private int goodsId;
            private String goodsName;
            private String goodsSpec;
            private String realPrice;
            private int goodsNums;
            private String goodsImg;
            private int type;
            private String refundmentStatus;
            private int panicBuyItemId;

            public void setPanicBuyItemId(int panicBuyItemId) {
                this.panicBuyItemId = panicBuyItemId;
            }

            public int getPanicBuyItemId() {
                return panicBuyItemId;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
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

            public String getRefundmentStatus() {
                return refundmentStatus;
            }

            public void setRefundmentStatus(String refundmentStatus) {
                this.refundmentStatus = refundmentStatus;
            }
        }
    }
}
