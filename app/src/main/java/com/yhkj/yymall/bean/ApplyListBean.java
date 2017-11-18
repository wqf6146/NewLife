package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/4.
 */

public class ApplyListBean {

    /**
     * code : 0
     * msg : 1
     * data : {"withdrawList":[{"status":2,"time":"2017-07-25 16:35:23","amount":"30.00","id":1},{"status":-1,"time":"2017-07-25 16:41:38","amount":"30.00","id":2},{"status":2,"time":"2017-07-25 16:42:27","amount":"20.00","id":3},{"status":2,"time":"2017-07-25 16:49:45","amount":"20.00","id":4},{"status":2,"time":"2017-07-25 16:52:51","amount":"11.00","id":5},{"status":2,"time":"2017-07-25 16:55:00","amount":"1.00","id":6},{"status":2,"time":"2017-07-25 16:57:32","amount":"1.00","id":7},{"status":2,"time":"2017-07-26 13:43:03","amount":"1.00","id":8},{"status":0,"time":"2017-07-26 06:21:45","amount":"50.00","id":9},{"status":0,"time":"2017-07-26 06:25:34","amount":"50.00","id":10},{"status":0,"time":"2017-07-26 06:32:28","amount":"50.00","id":11},{"status":0,"time":"2017-07-26 06:48:49","amount":"20.00","id":12},{"status":0,"time":"2017-07-27 01:10:08","amount":"10.00","id":13},{"status":0,"time":"2017-07-27 01:11:06","amount":"10.00","id":14}]}
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
        private List<WithdrawListBean> withdrawList;

        public List<WithdrawListBean> getWithdrawList() {
            return withdrawList;
        }

        public void setWithdrawList(List<WithdrawListBean> withdrawList) {
            this.withdrawList = withdrawList;
        }

        public static class WithdrawListBean {
            /**
             * status : 2
             * time : 2017-07-25 16:35:23
             * amount : 30.00
             * id : 1
             */

            private int status;
            private String time;
            private String amount;
            private int id;

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

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
    }
}
