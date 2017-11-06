package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/8/31.
 */

public class PayDetailBean {

    /**
     * code : 0
     * msg : 1
     * data : {"info":{"payment":"支付宝","event":2,"time":"2017-08-30 18:12:04","amount":"-1.00","amount_log":"9992796.20","order_no":"71"}}
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
         * info : {"payment":"支付宝","event":2,"time":"2017-08-30 18:12:04","amount":"-1.00","amount_log":"9992796.20","order_no":"71"}
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
             * payment : 支付宝
             * event : 2
             * time : 2017-08-30 18:12:04
             * amount : -1.00
             * amount_log : 9992796.20
             * order_no : 71
             */

            private String payment;
            private int event;
            private String time;
            private String amount;
            private String amount_log;
            private String order_no;

            public String getPayment() {
                return payment;
            }

            public void setPayment(String payment) {
                this.payment = payment;
            }

            public int getEvent() {
                return event;
            }

            public void setEvent(int event) {
                this.event = event;
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

            public String getAmount_log() {
                return amount_log;
            }

            public void setAmount_log(String amount_log) {
                this.amount_log = amount_log;
            }

            public String getOrder_no() {
                return order_no;
            }

            public void setOrder_no(String order_no) {
                this.order_no = order_no;
            }
        }
    }
}
