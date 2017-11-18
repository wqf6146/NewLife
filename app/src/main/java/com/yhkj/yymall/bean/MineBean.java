package com.yhkj.yymall.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/7/15.
 */

public class MineBean {


    /**
     * code : 0
     * msg : 1
     * data : {"info":{"name":"","head_ico":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/","phone":"18330273525","level":2,"state":1},"pending":{"payment":1,"group":2,"receipt":1,"assess":0,"return":1},"wallet":{"balance":"0.00","yab":0,"point":10,"ticket":3}}
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
         * info : {"name":"","head_ico":"http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/","phone":"18330273525","level":2,"state":1}
         * pending : {"payment":1,"group":2,"receipt":1,"assess":0,"return":1}
         * wallet : {"balance":"0.00","yab":0,"point":10,"ticket":3}
         */

        private InfoBean info;
        private PendingBean pending;
        private WalletBean wallet;

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public PendingBean getPending() {
            return pending;
        }

        public void setPending(PendingBean pending) {
            this.pending = pending;
        }

        public WalletBean getWallet() {
            return wallet;
        }

        public void setWallet(WalletBean wallet) {
            this.wallet = wallet;
        }

        public static class InfoBean {
            /**
             * name :
             * head_ico : http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/
             * phone : 18330273525
             * level : 2
             * state : 1
             */

            private String name;
            private String head_ico;
            private String phone;
            private int level;
            private int state;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getHead_ico() {
                return head_ico;
            }

            public void setHead_ico(String head_ico) {
                this.head_ico = head_ico;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }
        }

        public static class PendingBean {
            /**
             * payment : 1
             * group : 2
             * receipt : 1
             * assess : 0
             * return : 1
             */

            private int payment;
            private int group;
            private int receipt;
            private int assess;
            @SerializedName("return")
            private int returnX;

            public int getPayment() {
                return payment;
            }

            public void setPayment(int payment) {
                this.payment = payment;
            }

            public int getGroup() {
                return group;
            }

            public void setGroup(int group) {
                this.group = group;
            }

            public int getReceipt() {
                return receipt;
            }

            public void setReceipt(int receipt) {
                this.receipt = receipt;
            }

            public int getAssess() {
                return assess;
            }

            public void setAssess(int assess) {
                this.assess = assess;
            }

            public int getReturnX() {
                return returnX;
            }

            public void setReturnX(int returnX) {
                this.returnX = returnX;
            }
        }

        public static class WalletBean {
            /**
             * balance : 0.00
             * yab : 0
             * point : 10
             * ticket : 3
             */

            private String balance;
            private int yab;
            private int point;
            private int ticket;

            public String getBalance() {
                return balance;
            }

            public void setBalance(String balance) {
                this.balance = balance;
            }

            public int getYab() {
                return yab;
            }

            public void setYab(int yab) {
                this.yab = yab;
            }

            public int getPoint() {
                return point;
            }

            public void setPoint(int point) {
                this.point = point;
            }

            public int getTicket() {
                return ticket;
            }

            public void setTicket(int ticket) {
                this.ticket = ticket;
            }
        }
    }
}
