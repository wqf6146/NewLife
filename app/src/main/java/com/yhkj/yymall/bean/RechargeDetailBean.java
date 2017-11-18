package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/8/31.
 */

public class RechargeDetailBean {

    /**
     * code : 0
     * msg : 1
     * data : {"info":{"id":10,"pay_name":"支付宝","recharge_no":"20170830133318399848","time":"2017-08-30 13:33:18","account":"0.10","status":1,"telphone":"181****6332"}}
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
         * info : {"id":10,"pay_name":"支付宝","recharge_no":"20170830133318399848","time":"2017-08-30 13:33:18","account":"0.10","status":1,"telphone":"181****6332"}
         */

        private InfoBean info;

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public static class InfoBean {
            /**
             * id : 10
             * pay_name : 支付宝
             * recharge_no : 20170830133318399848
             * time : 2017-08-30 13:33:18
             * account : 0.10
             * status : 1
             * telphone : 181****6332
             */

            private int id;
            private String pay_name;
            private String recharge_no;
            private String time;
            private String account;
            private int status;
            private String telphone;

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

            public String getTelphone() {
                return telphone;
            }

            public void setTelphone(String telphone) {
                this.telphone = telphone;
            }
        }
    }
}
