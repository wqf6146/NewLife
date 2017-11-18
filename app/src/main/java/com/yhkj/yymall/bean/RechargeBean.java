package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/8/25.
 */

public class RechargeBean {

    /**
     * code : 0
     * msg : 1
     * data : {"id":2,"orderNo":"20170807081200396548"}
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
         * id : 2
         * orderNo : 20170807081200396548
         */

        private int id;
        private String orderNo;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }
    }
}
