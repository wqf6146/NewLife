package com.yhkj.yymall.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/7/14.
 */

public class CheckOutOkBean {

    /**
     * code : 0
     * “msg” : 111
     * data : {"orderNo":"20170630175655259136","total":1024}
     */

    private int code;
    @SerializedName("“msg”")
    private String _$Msg144; // FIXME check this code
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String get_$Msg144() {
        return _$Msg144;
    }

    public void set_$Msg144(String _$Msg144) {
        this._$Msg144 = _$Msg144;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * orderNo : 20170630175655259136
         * total : 1024
         */

        private String orderNo;
        private float total;
        private int integral;
        private int pay;

        public int getPay() {
            return pay;
        }

        public void setPay(int pay) {
            this.pay = pay;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
        }

        public int getIntegral() {
            return integral;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public float getTotal() {
            return total;
        }

        public void setTotal(float total) {
            this.total = total;
        }
    }
}
