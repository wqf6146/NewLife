package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/15.
 */

public class PersonBean {

    /**
     * code : 0
     * msg : 1
     * data : {"name":"sad","head_ico":"http://oss.yiyiyaya.cc/headImg/15030283229nB7FU.png","sex":1,"level":7,"babyInfo":{"count":2,"info":[{"years":"1岁","brithday":"2016-01-01","sex":"男","type":1},{"years":"1岁","brithday":"2018-09-01","sex":"女","type":2}]}}
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
         * name : sad
         * head_ico : http://oss.yiyiyaya.cc/headImg/15030283229nB7FU.png
         * sex : 1
         * level : 7
         * babyInfo : {"count":2,"info":[{"years":"1岁","brithday":"2016-01-01","sex":"男","type":1},{"years":"1岁","brithday":"2018-09-01","sex":"女","type":2}]}
         */

        private String name;
        private String head_ico;
        private int sex;
        private int level;
        private BabyInfoBean babyInfo;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHead_ico() {
            return head_ico;
        }

        public void setHead_ico(String head_ico) {
            this.head_ico = head_ico;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public BabyInfoBean getBabyInfo() {
            return babyInfo;
        }

        public void setBabyInfo(BabyInfoBean babyInfo) {
            this.babyInfo = babyInfo;
        }

        public static class BabyInfoBean {
            /**
             * count : 2
             * info : [{"years":"1岁","brithday":"2016-01-01","sex":"男","type":1},{"years":"1岁","brithday":"2018-09-01","sex":"女","type":2}]
             */

            private int count;
            private List<InfoBean> info;

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public List<InfoBean> getInfo() {
                return info;
            }

            public void setInfo(List<InfoBean> info) {
                this.info = info;
            }

            public static class InfoBean {
                /**
                 * years : 1岁
                 * brithday : 2016-01-01
                 * sex : 男
                 * type : 1
                 */

                private int id;
                private String years;
                private String brithday;
                private String sex;
                private int type;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getYears() {
                    return years;
                }

                public void setYears(String years) {
                    this.years = years;
                }

                public String getBrithday() {
                    return brithday;
                }

                public void setBrithday(String brithday) {
                    this.brithday = brithday;
                }

                public String getSex() {
                    return sex;
                }

                public void setSex(String sex) {
                    this.sex = sex;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }
            }
        }
    }
}
