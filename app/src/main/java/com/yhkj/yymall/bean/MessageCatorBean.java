package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/15.
 */

public class MessageCatorBean {

    /**
     * code : 0
     * msg : 1
     * data : {"list":[{"name":"系统通知","type":1,"count":15},{"name":"我的资产","type":2,"count":33},{"name":"物流通知","type":3,"count":5}]}
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
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * name : 系统通知
             * type : 1
             * count : 15
             */

            private String name;
            private int type;
            private int count;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }
        }
    }
}
