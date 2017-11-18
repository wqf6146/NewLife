package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/11.
 */

public class PriviledgeBean {

    /**
     * code : 0
     * msg : 1
     * data : {"lists":[{"title":"购物9折","tag":"V1可享用","des":"获得V1等级，可享用购物9折优惠","id":2},{"title":"购物85折","tag":"V2可享用","des":"获得V2等级，可享用购物85折优惠","id":3},{"title":"优先发货","tag":"V2及以上可享用","des":"获得V2及以上等级，可获得优先发货特权","id":3},{"title":"购物8折","tag":"V3可享用","des":"获得V3等级，可享用购物8折优惠","id":4},{"title":"购物75折","tag":"V4可享用","des":"获得V4等级，可享用购物75折优惠","id":5},{"title":"购物7折","tag":"V5可享用","des":"获得V5等级，可享用购物7折优惠","id":6},{"title":"购物65折","tag":"V6可享用","des":"获得V6等级，可享用购物65折优惠","id":7},{"title":"购物6折","tag":"V7可享用","des":"获得V7等级，可享用购物6折优惠","id":8}]}
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
        private List<ListsBean> lists;

        public List<ListsBean> getLists() {
            return lists;
        }

        public void setLists(List<ListsBean> lists) {
            this.lists = lists;
        }

        public static class ListsBean {
            /**
             * title : 购物9折
             * tag : V1可享用
             * des : 获得V1等级，可享用购物9折优惠
             * id : 2
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
