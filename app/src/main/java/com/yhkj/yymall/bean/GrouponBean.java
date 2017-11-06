package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/27.
 */

public class GrouponBean {

    /**
     * code : 0
     * msg : 111
     * data : {"id":63,"name":"儿童安全座椅","price":"2999.00","marketPrice":"2999.00","sale":0,"storeNum":185,"spec":[{"id":"11","name":"颜色","type":"1","value":[{"id":"红色","name":"红色"},{"id":"绿色","name":"绿色"}]}],"description":"","exp":0,"point":0,"content":"","commentCount":0,"status":2,"promotions":[],"video":{"img":"","url":""},"photo":["http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/26/20170726134525144.jpg","http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/26/20170726134525151.jpg"],"attr":[{"name":"品牌","value":"贝亲"},{"name":"产地","value":"日本"},{"name":"适用年龄段","value":"6~12月,1岁~3岁,3岁~6岁"},{"name":"车篮面料","value":"纺丝"}],"comment":{"count":0,"type":[{"name":"质量好","count":0},{"name":"快递给力","count":0},{"name":"性价比高","count":0}],"list":[{"id":32,"contents":"这是一条评论的内容 safjkasfjslkfjsklfjweiofjwoejweiofjeoweiofweiofjo","user":null,"userico":"","spec":"颜色：红色"}]},"group":{"id":9,"sum":11,"allowNum":3,"limitnum":1,"price":"2666.00","time":133303,"list":[{"id":49,"user":null,"userico":"","from":null,"number":2},{"id":50,"user":"蔺焕然","userico":"","from":null,"number":2}]},"rent":{"deposit":"2500.00"}}
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
         * id : 63
         * name : 儿童安全座椅
         * price : 2999.00
         * marketPrice : 2999.00
         * sale : 0
         * storeNum : 185
         * spec : [{"id":"11","name":"颜色","type":"1","value":[{"id":"红色","name":"红色"},{"id":"绿色","name":"绿色"}]}]
         * description :
         * exp : 0
         * point : 0
         * content :
         * commentCount : 0
         * status : 2
         * promotions : []
         * video : {"img":"","url":""}
         * photo : ["http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/26/20170726134525144.jpg","http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/26/20170726134525151.jpg"]
         * attr : [{"name":"品牌","value":"贝亲"},{"name":"产地","value":"日本"},{"name":"适用年龄段","value":"6~12月,1岁~3岁,3岁~6岁"},{"name":"车篮面料","value":"纺丝"}]
         * comment : {"count":0,"type":[{"name":"质量好","count":0},{"name":"快递给力","count":0},{"name":"性价比高","count":0}],"list":[{"id":32,"contents":"这是一条评论的内容 safjkasfjslkfjsklfjweiofjwoejweiofjeoweiofweiofjo","user":null,"userico":"","spec":"颜色：红色"}]}
         * group : {"id":9,"sum":11,"allowNum":3,"limitnum":1,"price":"2666.00","time":133303,"list":[{"id":49,"user":null,"userico":"","from":null,"number":2},{"id":50,"user":"蔺焕然","userico":"","from":null,"number":2}]}
         * rent : {"deposit":"2500.00"}
         */
        private int sellerId;
        private int id;
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
        private VideoBean video;
        private CommentBean comment;
        private GroupBean group;
        private RentBean rent;
        private int maxYY;
        private List<SpecBean> spec;
        private List<?> promotions;
        private List<String> photo;
        private List<AttrBean> attr;

        public void setSellerId(int sellerId) {
            this.sellerId = sellerId;
        }

        public int getSellerId() {
            return sellerId;
        }

        public int getId() {
            return id;
        }

        public int getMaxYY() {
            return maxYY;
        }

        public void setMaxYY(int maxYY) {
            this.maxYY = maxYY;
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

        public GroupBean getGroup() {
            return group;
        }

        public void setGroup(GroupBean group) {
            this.group = group;
        }

        public RentBean getRent() {
            return rent;
        }

        public void setRent(RentBean rent) {
            this.rent = rent;
        }

        public List<SpecBean> getSpec() {
            return spec;
        }

        public void setSpec(List<SpecBean> spec) {
            this.spec = spec;
        }

        public List<?> getPromotions() {
            return promotions;
        }

        public void setPromotions(List<?> promotions) {
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

        public static class VideoBean {
            /**
             * img :
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
             * type : [{"name":"质量好","count":0},{"name":"快递给力","count":0},{"name":"性价比高","count":0}]
             * list : [{"id":32,"contents":"这是一条评论的内容 safjkasfjslkfjsklfjweiofjwoejweiofjeoweiofweiofjo","user":null,"userico":"","spec":"颜色：红色"}]
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
                 * name : 质量好
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
                 * id : 32
                 * contents : 这是一条评论的内容 safjkasfjslkfjsklfjweiofjwoejweiofjeoweiofweiofjo
                 * user : null
                 * userico :
                 * spec : 颜色：红色
                 */

                private int id;
                private String contents;
                private Object user;
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

                public Object getUser() {
                    return user;
                }

                public void setUser(Object user) {
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

        public static class GroupBean {
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
            private List<ListBeanX> list;

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

            public List<ListBeanX> getList() {
                return list;
            }

            public void setList(List<ListBeanX> list) {
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
        }

        public static class RentBean {
            /**
             * deposit : 2500.00
             */

            private String deposit;

            public String getDeposit() {
                return deposit;
            }

            public void setDeposit(String deposit) {
                this.deposit = deposit;
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

        public static class AttrBean {
            /**
             * name : 品牌
             * value : 贝亲
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
