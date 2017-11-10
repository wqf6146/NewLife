package com.yhkj.yymall.bean;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ShopDetailsBean implements Parcelable {


    /**
     * code : 0
     * data : {"id":18,"name":"超级哈利isofix硬接口 竹纤维面料 可换衣设计 高宽躺调节 此商品正在参加聚划算，22小时34","price":"140.00","marketPrice":"500.00","sale":105,"spec":[{"id":"1","name":"颜色","type":"1","value":[{"id":"1","name":"红色"},{"id":"2","name":"绿色"}]},{"id":"4","name":"产地","type":"1","value":[{"id":"1","name":"国产"},{"id":"2","name":"进口"}]}],"description":"","exp":0,"point":0,"content":"阿斯顿发斯蒂芬","commentCount":2,"status":2,"promotions":["拼团价","限购5件"],"video":{"img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/18/20170718111134845.jpg","url":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/video/20170718111128475.mp4"},"photo":["http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/07/20170707142443558.jpg","http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/18/20170718110831240.jpg"],"attr":[{"name":"尺码","value":"l"},{"name":"测试","value":"4"}],"rent":{"deposit":"600.00"},"comment":{"count":2,"type":[{"id":52,"name":"质量很好","count":"50"},{"name":"很划算","count":"15698"},{"name":"态度不错","count":"1656"},{"name":"快递不错","count":"156"},{"name":"性价比高","count":"897"},{"name":"性价比高","count":"897"}],"list":[{"id":1,"contents":"sdfsfsdfd","user":"admin","userico":"pic.cnblogs.com/face/u282019.jpg","spec":"颜色：红色，尺码：xxl"}]}}
     */

    private int code;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {


        /**
         * id : 18
         * name : 超级哈利isofix硬接口 竹纤维面料 可换衣设计 高宽躺调节 此商品正在参加聚划算，22小时34
         * price : 140.00
         * marketPrice : 500.00
         * sale : 105
         * spec : [{"id":"1","name":"颜色","type":"1","value":[{"id":"1","name":"红色"},{"id":"2","name":"绿色"}]},{"id":"4","name":"产地","type":"1","value":[{"id":"1","name":"国产"},{"id":"2","name":"进口"}]}]
         * description :
         * exp : 0
         * point : 0
         * content : 阿斯顿发斯蒂芬
         * commentCount : 2
         * status : 2
         * promotions : ["拼团价","限购5件"]
         * video : {"img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/18/20170718111134845.jpg","url":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/video/20170718111128475.mp4"}
         * photo : ["http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/07/20170707142443558.jpg","http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/18/20170718110831240.jpg"]
         * attr : [{"name":"尺码","value":"l"},{"name":"测试","value":"4"}]
         * rent : {"deposit":"600.00"}
         * comment : {"count":2,"type":[{"id":52,"name":"质量很好","count":"50"},{"name":"很划算","count":"15698"},{"name":"态度不错","count":"1656"},{"name":"快递不错","count":"156"},{"name":"性价比高","count":"897"},{"name":"性价比高","count":"897"}],"list":[{"id":1,"contents":"sdfsfsdfd","user":"admin","userico":"pic.cnblogs.com/face/u282019.jpg","spec":"颜色：红色，尺码：xxl"}]}
         */

        private int sellerId;
        private int id;
        private String cut;
        private String name;
        private String price;
        private String marketPrice;
        private int storeNum;
        private int sale;
        private String description;
        private int exp;
        private int point;
        private String content;
        private int commentCount;
        private int status;
        private int maxYY;
        private int isGift;
        private int isActivityGift;
        private int deliveryFree;
        private int isSale;
        private VideoBean video;
        private RentBean rent;
        private GroupBean group;
        private CommentBean comment;
        private List<SpecBean> spec;
        private List<String> promotions;
        private List<String> photo;
        private List<AttrBean> attr;
        private IntegralBean integral;
        private DiscountBean discount;
        private DailyBean daily;
        private DefaultSpecBean defaultSpec;

        public void setDefaultSpec(DefaultSpecBean defaultSpec) {
            this.defaultSpec = defaultSpec;
        }

        public DefaultSpecBean getDefaultSpec() {
            return defaultSpec;
        }

        public void setDaily(DailyBean daily) {
            this.daily = daily;
        }

        public DailyBean getDaily() {
            return daily;
        }

        public void setCut(String cut) {
            this.cut = cut;
        }

        public String getCut() {
            return cut;
        }

        public void setGroup(GroupBean group) {
            this.group = group;
        }

        public GroupBean getGroup() {
            return group;
        }

        public void setDeliveryFree(int deliveryFree) {
            this.deliveryFree = deliveryFree;
        }

        public void setIsActivityGift(int isActivityGift) {
            this.isActivityGift = isActivityGift;
        }

        public void setIsGift(int isGift) {
            this.isGift = isGift;
        }

        public void setIsSale(int isSale) {
            this.isSale = isSale;
        }

        public int getDeliveryFree() {
            return deliveryFree;
        }

        public int getIsActivityGift() {
            return isActivityGift;
        }

        public int getIsGift() {
            return isGift;
        }

        public int getIsSale() {
            return isSale;
        }

        public void setDiscount(DiscountBean discount) {
            this.discount = discount;
        }

        public DiscountBean getDiscount() {
            return discount;
        }

        public void setIntegral(IntegralBean integral) {
            this.integral = integral;
        }

        public IntegralBean getIntegral() {
            return integral;
        }

        public int getSellerId() {
            return sellerId;
        }

        public void setSellerId(int sellerId) {
            this.sellerId = sellerId;
        }

        public int getStoreNum() {
            return storeNum;
        }

        public void setStoreNum(int storeNum) {
            this.storeNum = storeNum;
        }

        public int getMaxYY() {
            return maxYY;
        }

        public void setMaxYY(int maxYY) {
            this.maxYY = maxYY;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(String marketPrice) {
            this.marketPrice = marketPrice;
        }

        public int getSale() {
            return sale;
        }

        public void setSale(int sale) {
            this.sale = sale;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getExp() {
            return exp;
        }

        public void setExp(int exp) {
            this.exp = exp;
        }

        public int getPoint() {
            return point;
        }

        public void setPoint(int point) {
            this.point = point;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(int commentCount) {
            this.commentCount = commentCount;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public VideoBean getVideo() {
            return video;
        }

        public void setVideo(VideoBean video) {
            this.video = video;
        }

        public RentBean getRent() {
            return rent;
        }

        public void setRent(RentBean rent) {
            this.rent = rent;
        }

        public CommentBean getComment() {
            return comment;
        }

        public void setComment(CommentBean comment) {
            this.comment = comment;
        }

        public List<SpecBean> getSpec() {
            return spec;
        }

        public void setSpec(List<SpecBean> spec) {
            this.spec = spec;
        }

        public List<String> getPromotions() {
            return promotions;
        }

        public void setPromotions(List<String> promotions) {
            this.promotions = promotions;
        }

        public List<String> getPhoto() {
            return photo;
        }

        public void setPhoto(List<String> photo) {
            this.photo = photo;
        }

        public List<AttrBean> getAttr() {
            return attr;
        }

        public void setAttr(List<AttrBean> attr) {
            this.attr = attr;
        }

        public static class VideoBean implements Parcelable {

            /**
             * img : http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/18/20170718111134845.jpg
             * url : http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/video/20170718111128475.mp4
             */

            private String img;
            private String url;

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.img);
                dest.writeString(this.url);
            }

            public VideoBean() {
            }

            protected VideoBean(Parcel in) {
                this.img = in.readString();
                this.url = in.readString();
            }

            public static final Creator<VideoBean> CREATOR = new Creator<VideoBean>() {
                @Override
                public VideoBean createFromParcel(Parcel source) {
                    return new VideoBean(source);
                }

                @Override
                public VideoBean[] newArray(int size) {
                    return new VideoBean[size];
                }
            };
        }

        public static class IntegralBean implements Parcelable {

            private int id;
            private double price;

            public void setId(int id) {
                this.id = id;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public double getPrice() {
                return price;
            }

            public int getId() {
                return id;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeDouble(this.price);
            }

            public IntegralBean() {
            }

            protected IntegralBean(Parcel in) {
                this.id = in.readInt();
                this.price = in.readDouble();
            }

            public static final Creator<IntegralBean> CREATOR = new Creator<IntegralBean>() {
                @Override
                public IntegralBean createFromParcel(Parcel source) {
                    return new IntegralBean(source);
                }

                @Override
                public IntegralBean[] newArray(int size) {
                    return new IntegralBean[size];
                }
            };
        }

        public static class GroupBean implements Parcelable {

            /**
             * id : 9
             * sum : 11
             * allowNum : 3
             * limitnum : 1
             * price : 2666.00
             * time : 133303
             * list : [{"id":49,"user":null,"userico":"","from":null,"number":2},{"id":50,"user":"蔺焕然","userico":"","from":null,"number":2}]
             */

            private int id;
            private int sum;
            private int allowNum;
            private int limitnum;
            private String price;
            private int time;
            private List<GrouponBean.DataBean.GroupBean.ListBeanX> list;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getSum() {
                return sum;
            }

            public void setSum(int sum) {
                this.sum = sum;
            }

            public int getAllowNum() {
                return allowNum;
            }

            public void setAllowNum(int allowNum) {
                this.allowNum = allowNum;
            }

            public int getLimitnum() {
                return limitnum;
            }

            public void setLimitnum(int limitnum) {
                this.limitnum = limitnum;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public int getTime() {
                return time;
            }

            public void setTime(int time) {
                this.time = time;
            }

            public List<GrouponBean.DataBean.GroupBean.ListBeanX> getList() {
                return list;
            }

            public void setList(List<GrouponBean.DataBean.GroupBean.ListBeanX> list) {
                this.list = list;
            }

            public static class ListBeanX {
                /**
                 * id : 49
                 * user : null
                 * userico :
                 * from : null
                 * number : 2
                 */

                private int id;
                private String user;
                private String userico;
                private String from;
                private int number;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getUser() {
                    return user;
                }

                public void setUser(String user) {
                    this.user = user;
                }

                public String getUserico() {
                    return userico;
                }

                public void setUserico(String userico) {
                    this.userico = userico;
                }

                public String getFrom() {
                    return from;
                }

                public void setFrom(String from) {
                    this.from = from;
                }

                public int getNumber() {
                    return number;
                }

                public void setNumber(int number) {
                    this.number = number;
                }
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeInt(this.sum);
                dest.writeInt(this.allowNum);
                dest.writeInt(this.limitnum);
                dest.writeString(this.price);
                dest.writeInt(this.time);
                dest.writeList(this.list);
            }

            public GroupBean() {
            }

            protected GroupBean(Parcel in) {
                this.id = in.readInt();
                this.sum = in.readInt();
                this.allowNum = in.readInt();
                this.limitnum = in.readInt();
                this.price = in.readString();
                this.time = in.readInt();
                this.list = new ArrayList<GrouponBean.DataBean.GroupBean.ListBeanX>();
                in.readList(this.list, GrouponBean.DataBean.GroupBean.ListBeanX.class.getClassLoader());
            }

            public static final Creator<GroupBean> CREATOR = new Creator<GroupBean>() {
                @Override
                public GroupBean createFromParcel(Parcel source) {
                    return new GroupBean(source);
                }

                @Override
                public GroupBean[] newArray(int size) {
                    return new GroupBean[size];
                }
            };
        }

        public static class RentBean implements Parcelable {

            /**
             * deposit : 600.00
             */

            private String deposit;
            private int sumCount;

            public int getSumCount() {
                return sumCount;
            }

            public void setSumCount(int sumCount) {
                this.sumCount = sumCount;
            }

            public String getDeposit() {
                return deposit;
            }

            public void setDeposit(String deposit) {
                this.deposit = deposit;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.deposit);
            }

            public RentBean() {
            }

            protected RentBean(Parcel in) {
                this.deposit = in.readString();
            }

            public static final Creator<RentBean> CREATOR = new Creator<RentBean>() {
                @Override
                public RentBean createFromParcel(Parcel source) {
                    return new RentBean(source);
                }

                @Override
                public RentBean[] newArray(int size) {
                    return new RentBean[size];
                }
            };
        }

        public static class CommentBean implements Parcelable {

            /**
             * count : 2
             * type : [{"id":52,"name":"质量很好","count":"50"},{"name":"很划算","count":"15698"},{"name":"态度不错","count":"1656"},{"name":"快递不错","count":"156"},{"name":"性价比高","count":"897"},{"name":"性价比高","count":"897"}]
             * list : [{"id":1,"contents":"sdfsfsdfd","user":"admin","userico":"pic.cnblogs.com/face/u282019.jpg","spec":"颜色：红色，尺码：xxl"}]
             */

            private int count;
            private List<TypeBean> type;
            private List<ListBean> list;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public List<TypeBean> getType() {
                return type;
            }

            public void setType(List<TypeBean> type) {
                this.type = type;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class TypeBean {
                /**
                 * id : 52
                 * name : 质量很好
                 * count : 50
                 */

                private int id;
                private String name;
                private String count;

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

                public String getCount() {
                    return count;
                }

                public void setCount(String count) {
                    this.count = count;
                }
            }

            public static class ListBean {
                /**
                 * id : 1
                 * contents : sdfsfsdfd
                 * user : admin
                 * userico : pic.cnblogs.com/face/u282019.jpg
                 * spec : 颜色：红色，尺码：xxl
                 */

                private int id;
                private String contents;
                private String user;
                private String userico;
                private String spec;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getContents() {
                    return contents;
                }

                public void setContents(String contents) {
                    this.contents = contents;
                }

                public String getUser() {
                    return user;
                }

                public void setUser(String user) {
                    this.user = user;
                }

                public String getUserico() {
                    return userico;
                }

                public void setUserico(String userico) {
                    this.userico = userico;
                }

                public String getSpec() {
                    return spec;
                }

                public void setSpec(String spec) {
                    this.spec = spec;
                }
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.count);
                dest.writeList(this.type);
                dest.writeList(this.list);
            }

            public CommentBean() {
            }

            protected CommentBean(Parcel in) {
                this.count = in.readInt();
                this.type = new ArrayList<TypeBean>();
                in.readList(this.type, TypeBean.class.getClassLoader());
                this.list = new ArrayList<ListBean>();
                in.readList(this.list, ListBean.class.getClassLoader());
            }

            public static final Creator<CommentBean> CREATOR = new Creator<CommentBean>() {
                @Override
                public CommentBean createFromParcel(Parcel source) {
                    return new CommentBean(source);
                }

                @Override
                public CommentBean[] newArray(int size) {
                    return new CommentBean[size];
                }
            };
        }

        public static class SpecBean implements Parcelable {

            /**
             * id : 1
             * name : 颜色
             * type : 1
             * value : [{"id":"1","name":"红色"},{"id":"2","name":"绿色"}]
             */

            private String id;
            private String name;
            private String type;
            private List<ValueBean> value;
            private int selectdone;

            public void setSelectdone(int selectdone) {
                this.selectdone = selectdone;
            }

            public int getSelectdone() {
                return selectdone;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public List<ValueBean> getValue() {
                return value;
            }

            public void setValue(List<ValueBean> value) {
                this.value = value;
            }

            public static class ValueBean implements Parcelable {

                /**
                 * id : 1
                 * name : 红色
                 */

                private String id;
                private String name;
                private int enable;

                public void setEnable(int enable) {
                    this.enable = enable;
                }

                public int getEnable() {
                    return enable;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.id);
                    dest.writeString(this.name);
                    dest.writeInt(this.enable);
                }

                public ValueBean() {
                }

                protected ValueBean(Parcel in) {
                    this.id = in.readString();
                    this.name = in.readString();
                    this.enable = in.readInt();
                }

                public static final Creator<ValueBean> CREATOR = new Creator<ValueBean>() {
                    @Override
                    public ValueBean createFromParcel(Parcel source) {
                        return new ValueBean(source);
                    }

                    @Override
                    public ValueBean[] newArray(int size) {
                        return new ValueBean[size];
                    }
                };
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.id);
                dest.writeString(this.name);
                dest.writeString(this.type);
                dest.writeInt(this.selectdone);
                dest.writeList(this.value);
            }

            public SpecBean() {
            }

            protected SpecBean(Parcel in) {
                this.id = in.readString();
                this.name = in.readString();
                this.type = in.readString();
                this.selectdone = in.readInt();
                this.value = new ArrayList<ValueBean>();
                in.readList(this.value, ValueBean.class.getClassLoader());
            }

            public static final Parcelable.Creator<SpecBean> CREATOR = new Parcelable.Creator<SpecBean>() {
                @Override
                public SpecBean createFromParcel(Parcel source) {
                    return new SpecBean(source);
                }

                @Override
                public SpecBean[] newArray(int size) {
                    return new SpecBean[size];
                }
            };
        }

        public static class AttrBean implements Parcelable {

            /**
             * name : 尺码
             * value : l
             */

            private String name;
            private String value;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.name);
                dest.writeString(this.value);
            }

            public AttrBean() {
            }

            protected AttrBean(Parcel in) {
                this.name = in.readString();
                this.value = in.readString();
            }

            public static final Parcelable.Creator<AttrBean> CREATOR = new Parcelable.Creator<AttrBean>() {
                @Override
                public AttrBean createFromParcel(Parcel source) {
                    return new AttrBean(source);
                }

                @Override
                public AttrBean[] newArray(int size) {
                    return new AttrBean[size];
                }
            };
        }

        public static class DefaultSpecBean implements Parcelable {

            private String minPrice;
            private String maxPrice;
            private String nMinPrice;
            private String nMaxPrice;
            private String img;

            public void setnMaxPrice(String nMaxPrice) {
                this.nMaxPrice = nMaxPrice;
            }

            public void setnMinPrice(String nMinPrice) {
                this.nMinPrice = nMinPrice;
            }

            public String getnMaxPrice() {
                return nMaxPrice;
            }

            public String getnMinPrice() {
                return nMinPrice;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public void setMaxPrice(String maxPrice) {
                this.maxPrice = maxPrice;
            }

            public String getImg() {
                return img;
            }

            public String getMaxPrice() {
                return maxPrice;
            }

            public void setMinPrice(String minPrice) {
                this.minPrice = minPrice;
            }

            public String getMinPrice() {
                return minPrice;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.minPrice);
                dest.writeString(this.maxPrice);
                dest.writeString(this.nMaxPrice);
                dest.writeString(this.nMinPrice);
                dest.writeString(this.img);
            }

            public DefaultSpecBean() {
            }

            protected DefaultSpecBean(Parcel in) {
                this.minPrice = in.readString();
                this.maxPrice = in.readString();
                this.nMaxPrice = in.readString();
                this.nMinPrice = in.readString();
                this.img = in.readString();
            }

            public static final Creator<DefaultSpecBean> CREATOR = new Creator<DefaultSpecBean>() {
                @Override
                public DefaultSpecBean createFromParcel(Parcel source) {
                    return new DefaultSpecBean(source);
                }

                @Override
                public DefaultSpecBean[] newArray(int size) {
                    return new DefaultSpecBean[size];
                }
            };
        }
        public static class DailyBean implements Parcelable {

            /**
             *  "id": 3,
             "daily_active_id": 2,
             "price": "5.00"
             */
            private int id;
            private int daily_active_id;
            private double price;

            public void setDaily_active_id(int daily_active_id) {
                this.daily_active_id = daily_active_id;
            }

            public double getPrice() {
                return price;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getDaily_active_id() {
                return daily_active_id;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public int getId() {
                return id;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeInt(this.daily_active_id);
                dest.writeDouble(this.price);
            }

            public DailyBean() {
            }

            protected DailyBean(Parcel in) {
                this.id = in.readInt();
                this.daily_active_id = in.readInt();
                this.price = in.readDouble();
            }

            public static final Creator<DailyBean> CREATOR = new Creator<DailyBean>() {
                @Override
                public DailyBean createFromParcel(Parcel source) {
                    return new DailyBean(source);
                }

                @Override
                public DailyBean[] newArray(int size) {
                    return new DailyBean[size];
                }
            };
        }

        public static class DiscountBean implements Parcelable {

            private int id;
            private double price;
            private double discount;

            public void setPrice(double price) {
                this.price = price;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setDiscount(double discount) {
                this.discount = discount;
            }

            public int getId() {
                return id;
            }

            public double getPrice() {
                return price;
            }

            public double getDiscount() {
                return discount;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeDouble(this.price);
                dest.writeDouble(this.discount);
            }

            public DiscountBean() {
            }

            protected DiscountBean(Parcel in) {
                this.id = in.readInt();
                this.price = in.readDouble();
                this.discount = in.readDouble();
            }

            public static final Creator<DiscountBean> CREATOR = new Creator<DiscountBean>() {
                @Override
                public DiscountBean createFromParcel(Parcel source) {
                    return new DiscountBean(source);
                }

                @Override
                public DiscountBean[] newArray(int size) {
                    return new DiscountBean[size];
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
            dest.writeString(this.name);
            dest.writeString(this.price);
            dest.writeString(this.marketPrice);
            dest.writeInt(this.sale);
            dest.writeString(this.description);
            dest.writeInt(this.exp);
            dest.writeInt(this.point);
            dest.writeString(this.content);
            dest.writeInt(this.commentCount);
            dest.writeInt(this.status);
            dest.writeParcelable(this.video, flags);
            dest.writeParcelable(this.rent, flags);
            dest.writeParcelable(this.comment, flags);
            dest.writeParcelable(this.integral, flags);
            dest.writeParcelable(this.discount, flags);
            dest.writeParcelable(this.daily, flags);
            dest.writeParcelable(this.defaultSpec,flags);
            dest.writeTypedList(this.spec);
            dest.writeStringList(this.promotions);
            dest.writeStringList(this.photo);
            dest.writeTypedList(this.attr);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
            this.price = in.readString();
            this.marketPrice = in.readString();
            this.sale = in.readInt();
            this.description = in.readString();
            this.exp = in.readInt();
            this.point = in.readInt();
            this.content = in.readString();
            this.commentCount = in.readInt();
            this.status = in.readInt();
            this.video = in.readParcelable(VideoBean.class.getClassLoader());
            this.rent = in.readParcelable(RentBean.class.getClassLoader());
            this.comment = in.readParcelable(CommentBean.class.getClassLoader());
            this.integral = in.readParcelable(IntegralBean.class.getClassLoader());
            this.discount = in.readParcelable(DiscountBean.class.getClassLoader());
            this.daily = in.readParcelable(DailyBean.class.getClassLoader());
            this.defaultSpec = in.readParcelable(DefaultSpecBean.class.getClassLoader());
            this.spec = in.createTypedArrayList(SpecBean.CREATOR);
            this.promotions = in.createStringArrayList();
            this.photo = in.createStringArrayList();
            this.attr = in.createTypedArrayList(AttrBean.CREATOR);
        }

        public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeParcelable(this.data, flags);
    }

    public ShopDetailsBean() {
    }

    protected ShopDetailsBean(Parcel in) {
        this.code = in.readInt();
        this.data = in.readParcelable(DataBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ShopDetailsBean> CREATOR = new Parcelable.Creator<ShopDetailsBean>() {
        @Override
        public ShopDetailsBean createFromParcel(Parcel source) {
            return new ShopDetailsBean(source);
        }

        @Override
        public ShopDetailsBean[] newArray(int size) {
            return new ShopDetailsBean[size];
        }
    };
}
