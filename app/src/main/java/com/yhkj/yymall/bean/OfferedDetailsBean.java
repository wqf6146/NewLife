package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/31.
 */

public class OfferedDetailsBean {


    /**
     * code : 0
     * msg : 1
     * data : {"id":52,"number":1,"time":77842,"allowNum":2,"limitnum":1,"status":1,"count":13,"masterico":"http://oss.yiyiyaya.cc/http://q.qlogo.cn/qqapp/1106319912/35953427DD722F0294CA2AC573835B14/100","memberImg":["http://oss.yiyiyaya.cc/http://q.qlogo.cn/qqapp/1106319912/35953427DD722F0294CA2AC573835B14/100"],"goods":{"id":72,"name":"商户儿童自行车","price":"50.00","marketPrice":"299.00","img":"http://oss.yiyiyaya.cc/upload/2017/07/26/20170726175709709.jpg","spec":[{"id":"29","name":"尺寸","type":"1","value":[{"id":"14寸","name":"14寸"},{"id":"16寸","name":"16寸"},{"id":"18寸","name":"18寸"}]},{"id":"30","name":"颜色","type":"1","value":[{"id":"黄色","name":"黄色"},{"id":"粉色","name":"粉色"},{"id":"蓝色","name":"蓝色"}]}]}}
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
         * id : 52
         * number : 1
         * time : 77842
         * allowNum : 2
         * limitnum : 1
         * status : 1
         * count : 13
         * masterico : http://oss.yiyiyaya.cc/http://q.qlogo.cn/qqapp/1106319912/35953427DD722F0294CA2AC573835B14/100
         * memberImg : ["http://oss.yiyiyaya.cc/http://q.qlogo.cn/qqapp/1106319912/35953427DD722F0294CA2AC573835B14/100"]
         * goods : {"id":72,"name":"商户儿童自行车","price":"50.00","marketPrice":"299.00","img":"http://oss.yiyiyaya.cc/upload/2017/07/26/20170726175709709.jpg","spec":[{"id":"29","name":"尺寸","type":"1","value":[{"id":"14寸","name":"14寸"},{"id":"16寸","name":"16寸"},{"id":"18寸","name":"18寸"}]},{"id":"30","name":"颜色","type":"1","value":[{"id":"黄色","name":"黄色"},{"id":"粉色","name":"粉色"},{"id":"蓝色","name":"蓝色"}]}]}
         */

        private int id;
        private int number;
        private int time;
        private int allowNum;
        private int limitnum;
        private int status;
        private int count;
        private String masterico;
        private GoodsBean goods;
        private List<String> memberImg;
        private int isJoin;

        public void setIsJoin(int isJoin) {
            this.isJoin = isJoin;
        }

        public int getIsJoin() {
            return isJoin;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getMasterico() {
            return masterico;
        }

        public void setMasterico(String masterico) {
            this.masterico = masterico;
        }

        public GoodsBean getGoods() {
            return goods;
        }

        public void setGoods(GoodsBean goods) {
            this.goods = goods;
        }

        public List<String> getMemberImg() {
            return memberImg;
        }

        public void setMemberImg(List<String> memberImg) {
            this.memberImg = memberImg;
        }

        public static class GoodsBean {
            /**
             * id : 72
             * name : 商户儿童自行车
             * price : 50.00
             * marketPrice : 299.00
             * img : http://oss.yiyiyaya.cc/upload/2017/07/26/20170726175709709.jpg
             * spec : [{"id":"29","name":"尺寸","type":"1","value":[{"id":"14寸","name":"14寸"},{"id":"16寸","name":"16寸"},{"id":"18寸","name":"18寸"}]},{"id":"30","name":"颜色","type":"1","value":[{"id":"黄色","name":"黄色"},{"id":"粉色","name":"粉色"},{"id":"蓝色","name":"蓝色"}]}]
             */

            private int id;
            private String name;
            private String price;
            private String marketPrice;
            private String img;
            private List<SpecBean> spec;

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

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public List<SpecBean> getSpec() {
                return spec;
            }

            public void setSpec(List<SpecBean> spec) {
                this.spec = spec;
            }

            public static class SpecBean {
                /**
                 * id : 29
                 * name : 尺寸
                 * type : 1
                 * value : [{"id":"14寸","name":"14寸"},{"id":"16寸","name":"16寸"},{"id":"18寸","name":"18寸"}]
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
                     * id : 14寸
                     * name : 14寸
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
        }
    }
}
