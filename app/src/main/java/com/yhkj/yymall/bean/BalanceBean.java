package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/8/4.
 */

public class BalanceBean {

    /**
     * code : 0
     * msg : 1
     * data : {"name":"","account":"ssss","balance":"5962.19"}
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
         * name :
         * account : ssss
         * balance : 5962.19
         */

        private String name;
        private String account;
        private String balance;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }
    }
}
