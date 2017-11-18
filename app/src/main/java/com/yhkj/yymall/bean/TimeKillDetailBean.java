package com.yhkj.yymall.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2017/7/26.
 */

public class TimeKillDetailBean {

    /**
     * code : 0
     * data : {"panicId":19,"goodsId":41,"name":"ce","price":"22.00","marketPrice":"100.00","sale":0,"storeNum":200,"spec":[{"id":"11","name":"颜色","type":"1","value":[{"id":"红色","name":"红色"},{"id":"绿色","name":"绿色"}]}],"description":"","exp":0,"point":0,"content":"","commentCount":0,"status":0,"promotions":111,"video":{"img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/22/20170722154605263.jpg","url":""},"photo":["http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/23/20170723114057369.jpg","http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/25/20170725101504101.jpg","http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/24/20170724164559355.jpg"],"attr":[{"name":"尺码","value":"xxl"},{"name":"大小","value":"1.2"}],"comment":{"count":0,"type":[{"name":"不错","count":0},{"name":"还可以","count":0}],"list":[{"id":40,"contents":"拖拉机哈哈","user":"未设置","userico":"http://yiyiyaya-oss.oss-cn-zhangjiakou.aliyuncs.com/headImg/1502678874izX1La.jpeg","spec":"颜色：绿色"}]},"time":{"day":7,"hour":23,"min":19,"sec":47},"maxCount":2}
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

    public static class DataBean {
        /**
         * panicId : 19
         * goodsId : 41
         * name : ce
         * price : 22.00
         * marketPrice : 100.00
         * sale : 0
         * storeNum : 200
         * spec : [{"id":"11","name":"颜色","type":"1","value":[{"id":"红色","name":"红色"},{"id":"绿色","name":"绿色"}]}]
         * description :
         * exp : 0
         * point : 0
         * content :
         * commentCount : 0
         * status : 0
         * promotions : 111
         * video : {"img":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/22/20170722154605263.jpg","url":""}
         * photo : ["http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/23/20170723114057369.jpg","http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/25/20170725101504101.jpg","http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/24/20170724164559355.jpg"]
         * attr : [{"name":"尺码","value":"xxl"},{"name":"大小","value":"1.2"}]
         * comment : {"count":0,"type":[{"name":"不错","count":0},{"name":"还可以","count":0}],"list":[{"id":40,"contents":"拖拉机哈哈","user":"未设置","userico":"http://yiyiyaya-oss.oss-cn-zhangjiakou.aliyuncs.com/headImg/1502678874izX1La.jpeg","spec":"颜色：绿色"}]}
         * time : {"day":7,"hour":23,"min":19,"sec":47}
         * maxCount : 2
         */

        private int sellerId;
        private int panicId;
        private int goodsId;
        private String name;
        private String price;
        private String marketPrice;
        private int sale;
        private int storeNum;
        private String description;
        private int exp;
        private int point;
        private String content;
        private int commentCount;
        private int status;
        private int promotions;
        private int deliveryFree;
        private int isGift;
        private int isActivityGift;
        private VideoBean video;
        private CommentBean comment;
        private TimeBean time;
        private int maxCount;
        private int isSale;
        private int allowMaxCount;
        private List<SpecBean> spec;
        private List<String> photo;
        private List<AttrBean> attr;
        private DefaultSpecBean defaultSpec;

        public void setDefaultSpec(DefaultSpecBean defaultSpec) {
            this.defaultSpec = defaultSpec;
        }

        public DefaultSpecBean getDefaultSpec() {
            return defaultSpec;
        }

        public void setAllowMaxCount(int allowMaxCount) {
            this.allowMaxCount = allowMaxCount;
        }

        public int getAllowMaxCount() {
            return allowMaxCount;
        }

        public void setIsSale(int isSale) {
            this.isSale = isSale;
        }

        public int getIsSale() {
            return isSale;
        }

        public void setIsGift(int isGift) {
            this.isGift = isGift;
        }

        public void setIsActivityGift(int isActivityGift) {
            this.isActivityGift = isActivityGift;
        }

        public void setDeliveryFree(int deliveryFree) {
            this.deliveryFree = deliveryFree;
        }

        public int getIsGift() {
            return isGift;
        }

        public int getIsActivityGift() {
            return isActivityGift;
        }

        public int getDeliveryFree() {
            return deliveryFree;
        }

        public int getSellerId() {
            return sellerId;
        }

        public void setSellerId(int sellerId) {
            this.sellerId = sellerId;
        }

        public int getPanicId() {
            return panicId;
        }

        public void setPanicId(int panicId) {
            this.panicId = panicId;
        }

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
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

        public int getStoreNum() {
            return storeNum;
        }

        public void setStoreNum(int storeNum) {
            this.storeNum = storeNum;
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

        public int getPromotions() {
            return promotions;
        }

        public void setPromotions(int promotions) {
            this.promotions = promotions;
        }

        public VideoBean getVideo() {
            return video;
        }

        public void setVideo(VideoBean video) {
            this.video = video;
        }

        public CommentBean getComment() {
            return comment;
        }

        public void setComment(CommentBean comment) {
            this.comment = comment;
        }

        public TimeBean getTime() {
            return time;
        }

        public void setTime(TimeBean time) {
            this.time = time;
        }

        public int getMaxCount() {
            return maxCount;
        }

        public void setMaxCount(int maxCount) {
            this.maxCount = maxCount;
        }

        public List<SpecBean> getSpec() {
            return spec;
        }

        public void setSpec(List<SpecBean> spec) {
            this.spec = spec;
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

        public static class VideoBean {
            /**
             * img : http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/22/20170722154605263.jpg
             * url :
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
        }

        public static class CommentBean {
            /**
             * count : 0
             * type : [{"name":"不错","count":0},{"name":"还可以","count":0}]
             * list : [{"id":40,"contents":"拖拉机哈哈","user":"未设置","userico":"http://yiyiyaya-oss.oss-cn-zhangjiakou.aliyuncs.com/headImg/1502678874izX1La.jpeg","spec":"颜色：绿色"}]
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
                 * name : 不错
                 * count : 0
                 */

                private String name;
                private int count;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getCount() {
                    return count;
                }

                public void setCount(int count) {
                    this.count = count;
                }
            }

            public static class ListBean {
                /**
                 * id : 40
                 * contents : 拖拉机哈哈
                 * user : 未设置
                 * userico : http://yiyiyaya-oss.oss-cn-zhangjiakou.aliyuncs.com/headImg/1502678874izX1La.jpeg
                 * spec : 颜色：绿色
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
        }

        public static class TimeBean {
            /**
             * day : 7
             * hour : 23
             * min : 19
             * sec : 47
             */

            private int day;
            private int hour;
            private int min;
            private int sec;

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }

            public int getHour() {
                return hour;
            }

            public void setHour(int hour) {
                this.hour = hour;
            }

            public int getMin() {
                return min;
            }

            public void setMin(int min) {
                this.min = min;
            }

            public int getSec() {
                return sec;
            }

            public void setSec(int sec) {
                this.sec = sec;
            }
        }

        public static class SpecBean {
            /**
             * id : 11
             * name : 颜色
             * type : 1
             * value : [{"id":"红色","name":"红色"},{"id":"绿色","name":"绿色"}]
             */

            private String id;
            private String name;
            private String type;
            private List<ValueBean> value;

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

            public static class ValueBean {
                /**
                 * id : 红色
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
            }
        }
        public static class DefaultSpecBean {

            private String minPrice;
            private String maxPrice;
            private String nMinPrice;
            private String nMaxPrice;
            private String img;

            public void setnMinPrice(String nMinPrice) {
                this.nMinPrice = nMinPrice;
            }

            public void setnMaxPrice(String nMaxPrice) {
                this.nMaxPrice = nMaxPrice;
            }

            public String getnMinPrice() {
                return nMinPrice;
            }

            public String getnMaxPrice() {
                return nMaxPrice;
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


            public DefaultSpecBean() {
            }

            protected DefaultSpecBean(Parcel in) {
                this.minPrice = in.readString();
                this.maxPrice = in.readString();
                this.img = in.readString();
            }
        }
        public static class AttrBean {
            /**
             * name : 尺码
             * value : xxl
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
        }
    }
}
