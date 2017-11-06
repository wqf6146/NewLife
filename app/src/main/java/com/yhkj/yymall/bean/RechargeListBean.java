package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/25.
 */

public class RechargeListBean {

    /**
     * code : 0
     * msg : 1
     * data : {"list":[{"pay_name":"中国银联","recharge_no":"20170807072606732153","time":"2017-08-07 07:26:06","account":"10.00","status":1},{"pay_name":"中国银联","recharge_no":"20170807081200396548","time":"2017-08-07 08:12:00","account":"10.00","status":0}]}
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
             * pay_name : 中国银联
             * recharge_no : 20170807072606732153
             * time : 2017-08-07 07:26:06
             * account : 10.00
             * status : 1
             */

            private int id;
            private String pay_name;
            private String recharge_no;
            private String time;
            private String account;
            private int status;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getPay_name() {
                return pay_name;
            }

            public void setPay_name(String pay_name) {
                this.pay_name = pay_name;
            }

            public String getRecharge_no() {
                return recharge_no;
            }

            public void setRecharge_no(String recharge_no) {
                this.recharge_no = recharge_no;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
