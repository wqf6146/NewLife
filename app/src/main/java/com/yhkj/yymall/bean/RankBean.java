package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/21.
 */

public class RankBean {

    /**
     * code : 0
     * msg : 1
     * data : {"head":{"head_ico":"http://q.qlogo.cn/qqapp/1106319912/35953427DD722F0294CA2AC573835B14/100","lv":"V7","num":32790,"word":"你已到达最高等级"},"body":[{"id":7,"group_name":"V6","minexp":5500,"maxexp":5999},{"id":8,"group_name":"V7","minexp":6000,"maxexp":9999},{"id":0,"group_name":"0","minexp":"0","maxexp":0}],"foot":[{"title":"购物6折","tag":"V7可享用","des":"获得V7等级，可享用购物6折优惠","id":8},{"title":"优先发货","tag":"V2及以上可享用","des":"获得V2及以上等级，可获得优先发货特权","id":3}]}
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
         * head : {"head_ico":"http://q.qlogo.cn/qqapp/1106319912/35953427DD722F0294CA2AC573835B14/100","lv":"V7","num":32790,"word":"你已到达最高等级"}
         * body : [{"id":7,"group_name":"V6","minexp":5500,"maxexp":5999},{"id":8,"group_name":"V7","minexp":6000,"maxexp":9999},{"id":0,"group_name":"0","minexp":"0","maxexp":0}]
         * foot : [{"title":"购物6折","tag":"V7可享用","des":"获得V7等级，可享用购物6折优惠","id":8},{"title":"优先发货","tag":"V2及以上可享用","des":"获得V2及以上等级，可获得优先发货特权","id":3}]
         */

        private HeadBean head;
        private List<BodyBean> body;
        private List<FootBean> foot;

        public HeadBean getHead() {
            return head;
        }

        public void setHead(HeadBean head) {
            this.head = head;
        }

        public List<BodyBean> getBody() {
            return body;
        }

        public void setBody(List<BodyBean> body) {
            this.body = body;
        }

        public List<FootBean> getFoot() {
            return foot;
        }

        public void setFoot(List<FootBean> foot) {
            this.foot = foot;
        }

        public static class HeadBean {
            /**
             * head_ico : http://q.qlogo.cn/qqapp/1106319912/35953427DD722F0294CA2AC573835B14/100
             * lv : V7
             * num : 32790
             * word : 你已到达最高等级
             */

            private String head_ico;
            private String lv;
            private int num;
            private String word;

            public String getHead_ico() {
                return head_ico;
            }

            public void setHead_ico(String head_ico) {
                this.head_ico = head_ico;
            }

            public String getLv() {
                return lv;
            }

            public void setLv(String lv) {
                this.lv = lv;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public String getWord() {
                return word;
            }

            public void setWord(String word) {
                this.word = word;
            }
        }

        public static class BodyBean {
            /**
             * id : 7
             * group_name : V6
             * minexp : 5500
             * maxexp : 5999
             */

            private int id;
            private String group_name;
            private int minexp;
            private int maxexp;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getGroup_name() {
                return group_name;
            }

            public void setGroup_name(String group_name) {
                this.group_name = group_name;
            }

            public int getMinexp() {
                return minexp;
            }

            public void setMinexp(int minexp) {
                this.minexp = minexp;
            }

            public int getMaxexp() {
                return maxexp;
            }

            public void setMaxexp(int maxexp) {
                this.maxexp = maxexp;
            }
        }

        public static class FootBean {
            /**
             * title : 购物6折
             * tag : V7可享用
             * des : 获得V7等级，可享用购物6折优惠
             * id : 8
             */

            private String title;
            private String tag;
            private String des;
            private int id;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            public String getDes() {
                return des;
            }

            public void setDes(String des) {
                this.des = des;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
    }
}
