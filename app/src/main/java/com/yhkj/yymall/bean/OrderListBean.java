package com.yhkj.yymall.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/15.
 */

public class OrderListBean {
    /**
     * code : 0
     * msg : 1
     * data : {"orderGroup":[{"id":826,"status":2,"orderGroupNo":"20170728152237437087","orders":[{"id":911,"sellerName":"yiyiyaya自营","goodses":[{"id":1200,"goodsId":45,"goodsName":"儿童电子琴钢琴麦克风宝宝益智启蒙玩具","goodsSpec":"儿童玩具颜色：粉色","realPrice":"80.00","goodsNums":1,"goodsImg":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/26/20170726105914295.jpg"}],"payType":1,"status":2,"payStatus":1,"distributionStatus":1,"type":4,"isGroup":1,"isComment":0,"totalNums":1,"totalPrice":80}],"groupNums":1,"groupPrice":80}]}
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
        private List<OrderGroupBean> orderGroup;

        public List<OrderGroupBean> getOrderGroup() {
            return orderGroup;
        }

        public void setOrderGroup(List<OrderGroupBean> orderGroup) {
            this.orderGroup = orderGroup;
        }

        public static class OrderGroupBean {
            /**
             * id : 826
             * status : 2
             * orderGroupNo : 20170728152237437087
             * orders : [{"id":911,"sellerName":"yiyiyaya自营","goodses":[{"id":1200,"goodsId":45,"goodsName":"儿童电子琴钢琴麦克风宝宝益智启蒙玩具","goodsSpec":"儿童玩具颜色：粉色","realPrice":"80.00","goodsNums":1,"goodsImg":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/26/20170726105914295.jpg"}],"payType":1,"status":2,"payStatus":1,"distributionStatus":1,"type":4,"isGroup":1,"isComment":0,"totalNums":1,"totalPrice":80}]
             * groupNums : 1
             * groupPrice : 80
             */

            private int id;
            private int status;
            private String orderGroupNo;
            private int groupNums;
            private float groupPrice;
            private int endTime;
            private List<OrdersBean> orders;
            private double payPrice;

            public void setPayPrice(double payPrice) {
                this.payPrice = payPrice;
            }

            public double getPayPrice() {
                return payPrice;
            }

            public int getEndTime() {
                return endTime;
            }

            public void setEndTime(int endTime) {
                this.endTime = endTime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getOrderGroupNo() {
                return orderGroupNo;
            }

            public void setOrderGroupNo(String orderGroupNo) {
                this.orderGroupNo = orderGroupNo;
            }

            public int getGroupNums() {
                return groupNums;
            }

            public void setGroupNums(int groupNums) {
                this.groupNums = groupNums;
            }

            public float getGroupPrice() {
                return groupPrice;
            }

            public void setGroupPrice(float groupPrice) {
                this.groupPrice = groupPrice;
            }

            public List<OrdersBean> getOrders() {
                return orders;
            }

            public void setOrders(List<OrdersBean> orders) {
                this.orders = orders;
            }

            public static class OrdersBean implements Parcelable {

                /**
                 * id : 911
                 * sellerName : yiyiyaya自营
                 * goodses : [{"id":1200,"goodsId":45,"goodsName":"儿童电子琴钢琴麦克风宝宝益智启蒙玩具","goodsSpec":"儿童玩具颜色：粉色","realPrice":"80.00","goodsNums":1,"goodsImg":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/26/20170726105914295.jpg"}]
                 * payType : 1
                 * status : 2
                 * payStatus : 1
                 * distributionStatus : 1
                 * type : 4
                 * isGroup : 1
                 * isComment : 0
                 * totalNums : 1
                 * totalPrice : 80
                 */

                private int id;
                private String sellerName;
                private int payType;
                private int status;
                private int payStatus;
                private int distributionStatus;
                private int type;
                private int isGroup;
                private int isComment;
                private int totalNums;
                private float totalPrice;
                private List<GoodsesBean> goodses;

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

                public int getTotalNums() {
                    return totalNums;
                }

                public void setTotalNums(int totalNums) {
                    this.totalNums = totalNums;
                }

                public float getTotalPrice() {
                    return totalPrice;
                }

                public void setTotalPrice(float totalPrice) {
                    this.totalPrice = totalPrice;
                }

                public List<GoodsesBean> getGoodses() {
                    return goodses;
                }

                public void setGoodses(List<GoodsesBean> goodses) {
                    this.goodses = goodses;
                }

                public static class GoodsesBean implements Parcelable {

                    /**
                     * id : 1200
                     * goodsId : 45
                     * goodsName : 儿童电子琴钢琴麦克风宝宝益智启蒙玩具
                     * goodsSpec : 儿童玩具颜色：粉色
                     * realPrice : 80.00
                     * goodsNums : 1
                     * goodsImg : http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/26/20170726105914295.jpg
                     */

                    private int id;
                    private int goodsId;
                    private String goodsName;
                    private String goodsSpec;
                    private String realPrice;
                    private int goodsNums;
                    private String goodsImg;

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

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeInt(this.id);
                        dest.writeInt(this.goodsId);
                        dest.writeString(this.goodsName);
                        dest.writeString(this.goodsSpec);
                        dest.writeString(this.realPrice);
                        dest.writeInt(this.goodsNums);
                        dest.writeString(this.goodsImg);
                    }

                    public GoodsesBean() {
                    }

                    protected GoodsesBean(Parcel in) {
                        this.id = in.readInt();
                        this.goodsId = in.readInt();
                        this.goodsName = in.readString();
                        this.goodsSpec = in.readString();
                        this.realPrice = in.readString();
                        this.goodsNums = in.readInt();
                        this.goodsImg = in.readString();
                    }

                    public static final Creator<GoodsesBean> CREATOR = new Creator<GoodsesBean>() {
                        @Override
                        public GoodsesBean createFromParcel(Parcel source) {
                            return new GoodsesBean(source);
                        }

                        @Override
                        public GoodsesBean[] newArray(int size) {
                            return new GoodsesBean[size];
                        }
                    };
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(this.id);
                    dest.writeString(this.sellerName);
                    dest.writeInt(this.payType);
                    dest.writeInt(this.status);
                    dest.writeInt(this.payStatus);
                    dest.writeInt(this.distributionStatus);
                    dest.writeInt(this.type);
                    dest.writeInt(this.isGroup);
                    dest.writeInt(this.isComment);
                    dest.writeInt(this.totalNums);
                    dest.writeFloat(this.totalPrice);
                    dest.writeList(this.goodses);
                }

                public OrdersBean() {
                }

                protected OrdersBean(Parcel in) {
                    this.id = in.readInt();
                    this.sellerName = in.readString();
                    this.payType = in.readInt();
                    this.status = in.readInt();
                    this.payStatus = in.readInt();
                    this.distributionStatus = in.readInt();
                    this.type = in.readInt();
                    this.isGroup = in.readInt();
                    this.isComment = in.readInt();
                    this.totalNums = in.readInt();
                    this.totalPrice = in.readFloat();
                    this.goodses = new ArrayList<GoodsesBean>();
                    in.readList(this.goodses, GoodsesBean.class.getClassLoader());
                }

                public static final Parcelable.Creator<OrdersBean> CREATOR = new Parcelable.Creator<OrdersBean>() {
                    @Override
                    public OrdersBean createFromParcel(Parcel source) {
                        return new OrdersBean(source);
                    }

                    @Override
                    public OrdersBean[] newArray(int size) {
                        return new OrdersBean[size];
                    }
                };
            }
        }
    }
}
