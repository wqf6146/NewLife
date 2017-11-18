package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 */

public class TimeKillClassify {

    /**
     * code : 0
     * msg : 1
     * data : {"categorys":[{"id":29,"name":"童车"},{"id":30,"name":"生活家居"},{"id":31,"name":"儿童玩具"},{"id":15,"name":"测试"},{"id":27,"name":"孕婴"},{"id":28,"name":"儿童安全座椅"},{"id":17,"name":"儿童用品"}]}
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
        private List<CategorysBean> categorys;

        public List<CategorysBean> getCategorys() {
            return categorys;
        }

        public void setCategorys(List<CategorysBean> categorys) {
            this.categorys = categorys;
        }

        public static class CategorysBean {
            /**
             * id : 29
             * name : 童车
             */

            private int id;
            private String name;

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
        }
    }
}
