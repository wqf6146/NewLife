package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/2.
 */

public class BalanceLogBean {

    /**
     * code : 0
     * msg : 1
     * data : {"list":[{"id":256,"type":0,"datetime":"2017-07-28 15:22:41","value":"-80.00","value_log":"47173.00","note":"使用余额支付购买，订单[20170728152237437087]，金额：80元"},{"id":248,"type":0,"datetime":"2017-07-28 11:01:47","value":"-125.00","value_log":"47298.00","note":"使用余额支付购买，订单[20170728110107631372]，金额：125元"},{"id":236,"type":0,"datetime":"2017-07-27 18:32:39","value":"-25.00","value_log":"47323.00","note":"使用余额支付购买，订单[20170727162735591445]，金额：25元"},{"id":235,"type":0,"datetime":"2017-07-27 18:31:23","value":"-25.00","value_log":"47348.00","note":"使用余额支付购买，订单[20170727162735591445]，金额：25元"},{"id":234,"type":0,"datetime":"2017-07-27 18:28:05","value":"-25.00","value_log":"47373.00","note":"使用余额支付购买，订单[20170727162735591445]，金额：25元"},{"id":233,"type":0,"datetime":"2017-07-27 18:25:55","value":"-25.00","value_log":"47398.00","note":"使用余额支付购买，订单[20170727162735591445]，金额：25元"},{"id":232,"type":0,"datetime":"2017-07-27 18:19:23","value":"-25.00","value_log":"47423.00","note":"使用余额支付购买，订单[20170727162735591445]，金额：25元"},{"id":231,"type":0,"datetime":"2017-07-27 18:15:12","value":"-25.00","value_log":"47448.00","note":"使用余额支付购买，订单[20170727162735591445]，金额：25元"},{"id":194,"type":0,"datetime":"2017-07-26 10:16:52","value":"-5332.00","value_log":"52780.00","note":"使用余额支付购买，订单[20170726101550199457]，金额：5332元"},{"id":193,"type":0,"datetime":"2017-07-26 10:02:07","value":"-2666.00","value_log":"55446.00","note":"使用余额支付购买，订单[20170726095944680805]，金额：2666元"},{"id":174,"type":0,"datetime":"2017-07-25 17:58:01","value":"-25.00","value_log":"55530.00","note":"使用余额支付购买，订单[20170725165359645003]，金额：25元"},{"id":173,"type":0,"datetime":"2017-07-25 17:56:52","value":"-25.00","value_log":"55555.00","note":"使用余额支付购买，订单[20170725165359645003]，金额：25元"},{"id":105,"type":0,"datetime":"2017-07-23 10:52:15","value":"-173.00","value_log":"555036.00","note":"使用余额支付购买，订单[20170723092037404468]，金额：173元"}]}
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
             * id : 256
             * type : 0
             * datetime : 2017-07-28 15:22:41
             * value : -80.00
             * value_log : 47173.00
             * note : 使用余额支付购买，订单[20170728152237437087]，金额：80元
             */

            private int id;
            private int type;
            private String datetime;
            private String value;
            private String value_log;
            private String note;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getDatetime() {
                return datetime;
            }

            public void setDatetime(String datetime) {
                this.datetime = datetime;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getValue_log() {
                return value_log;
            }

            public void setValue_log(String value_log) {
                this.value_log = value_log;
            }

            public String getNote() {
                return note;
            }

            public void setNote(String note) {
                this.note = note;
            }
        }
    }
}
