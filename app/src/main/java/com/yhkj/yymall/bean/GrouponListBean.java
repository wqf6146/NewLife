package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/31.
 */

public class GrouponListBean {

    /**
     * code : 0
     * msg : 11
     * data : {"list":[{"id":49,"user":null,"userico":"","from":null,"number":2},{"id":51,"user":"两色色 root","userico":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/headImg/76.jpeg","from":null,"number":1},{"id":52,"user":"阿具体开开心心舞台","userico":"","from":null,"number":1},{"id":50,"user":"蔺焕然","userico":"","from":null,"number":2}]}
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
             * id : 49
             * user : null
             * userico :
             * from : null
             * number : 2
             */

            private int id;
            private Object user;
            private String userico;
            private Object from;
            private int number;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public Object getFrom() {
                return from;
            }

            public void setFrom(Object from) {
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
}
