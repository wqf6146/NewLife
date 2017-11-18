package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */

public class CollectClassifyBean {

    /**
     * code : 0
     * msg : 1
     * data : {"cate_list":[{"cate_name":"儿童玩具","cate_nub":1}]}
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
        private List<CateListBean> cate_list;

        public List<CateListBean> getCate_list() {
            return cate_list;
        }

        public void setCate_list(List<CateListBean> cate_list) {
            this.cate_list = cate_list;
        }

        public static class CateListBean {
            /**
             * cate_name : 儿童玩具
             * cate_nub : 1
             */

            private int id;
            private String cate_name;
            private int cate_nub;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCate_name() {
                return cate_name;
            }

            public void setCate_name(String cate_name) {
                this.cate_name = cate_name;
            }

            public int getCate_nub() {
                return cate_nub;
            }

            public void setCate_nub(int cate_nub) {
                this.cate_nub = cate_nub;
            }
        }
    }
}
