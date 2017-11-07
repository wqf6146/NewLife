package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */

public class EnableSpecBean {

    /**
     * code : 0
     * msg : 1
     * data : {"list":[{"id":"29","value":[{"id":"16寸","name":"16寸"}]},{"id":"59","value":[{"id":"蓝色","name":"蓝色"},{"id":"黄色","name":"黄色"}]}]}
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
             * id : 29
             * value : [{"id":"16寸","name":"16寸"}]
             */

            private String id;
            private List<ValueBean> value;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public List<ValueBean> getValue() {
                return value;
            }

            public void setValue(List<ValueBean> value) {
                this.value = value;
            }

            public static class ValueBean {
                /**
                 * id : 16寸
                 * name : 16寸
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
