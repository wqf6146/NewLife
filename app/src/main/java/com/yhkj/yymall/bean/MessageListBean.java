package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/24.
 */

public class MessageListBean {
    /**
     * code : 0
     * msg : 111
     * data : {"list":[{"id":1,"title":"哈哈哈","content":"test3","time":"2017-07-24 14:43:50"},{"id":2,"title":"哈哈哈","content":"test2","time":"2017-07-17 14:43:56"},{"id":3,"title":"哈哈哈","content":"test1","time":"2017-07-13 14:44:53"}]}
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
             * id : 1
             * title : 哈哈哈
             * content : test3
             * time : 2017-07-24 14:43:50
             */

            private int id;
            private String title;
            private String content;
            private String time;
            private String status;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }
    }
}
