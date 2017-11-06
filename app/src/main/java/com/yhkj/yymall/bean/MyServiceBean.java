package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/2.
 */

public class MyServiceBean {

    /**
     * code : 0
     * msg : 111
     * data : {"list":[{"name":"售后问题","cateId":4,"list":[{"title":"test","articleId":5}]},{"name":"物流问题","cateId":3,"list":[{"title":"test","articleId":4}]},{"name":"购物问题","cateId":2,"list":[{"title":"test","articleId":3}]},{"name":"常见问题","cateId":1,"list":[{"title":"华为营收增速和利润下滑，智能手机业务压力大","articleId":1},{"title":"海外投资的\u201c不良动机\u201d，或与遗产税有关","articleId":2}]}],"customer":{"qq":"1234567","phone":"1234567"}}
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
         * list : [{"name":"售后问题","cateId":4,"list":[{"title":"test","articleId":5}]},{"name":"物流问题","cateId":3,"list":[{"title":"test","articleId":4}]},{"name":"购物问题","cateId":2,"list":[{"title":"test","articleId":3}]},{"name":"常见问题","cateId":1,"list":[{"title":"华为营收增速和利润下滑，智能手机业务压力大","articleId":1},{"title":"海外投资的\u201c不良动机\u201d，或与遗产税有关","articleId":2}]}]
         * customer : {"qq":"1234567","phone":"1234567"}
         */

        private CustomerBean customer;
        private List<ListBeanX> list;

        public CustomerBean getCustomer() {
            return customer;
        }

        public void setCustomer(CustomerBean customer) {
            this.customer = customer;
        }

        public List<ListBeanX> getList() {
            return list;
        }

        public void setList(List<ListBeanX> list) {
            this.list = list;
        }

        public static class CustomerBean {
            /**
             * qq : 1234567
             * phone : 1234567
             */

            private String qq;
            private String phone;

            public String getQq() {
                return qq;
            }

            public void setQq(String qq) {
                this.qq = qq;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }
        }

        public static class ListBeanX {
            /**
             * name : 售后问题
             * cateId : 4
             * list : [{"title":"test","articleId":5}]
             */

            private String name;
            private int cateId;
            private List<ListBean> list;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getCateId() {
                return cateId;
            }

            public void setCateId(int cateId) {
                this.cateId = cateId;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class ListBean {
                /**
                 * title : test
                 * articleId : 5
                 */

                private String title;
                private int articleId;

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public int getArticleId() {
                    return articleId;
                }

                public void setArticleId(int articleId) {
                    this.articleId = articleId;
                }
            }
        }
    }
}
