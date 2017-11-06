package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/8/4.
 */

public class ApplyDetailBean {

    /**
     * code : 0
     * msg : 1
     * data : {"status":2,"time":"2017-07-25 16:35:23","amount":"30.00","type":1,"account":"","handleTime":"2017-08-04 17:11:11"}
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
         * status : 2
         * time : 2017-07-25 16:35:23
         * amount : 30.00
         * type : 1
         * account :
         * handleTime : 2017-08-04 17:11:11
         */

        private int status;
        private String time;
        private String amount;
        private int type;
        private String account;
        private String handleTime;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getHandleTime() {
            return handleTime;
        }

        public void setHandleTime(String handleTime) {
            this.handleTime = handleTime;
        }
    }
}
