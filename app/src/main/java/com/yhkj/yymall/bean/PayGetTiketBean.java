package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/7/22.
 */

public class PayGetTiketBean {

    /**
     * code : 0
     * msg : 1
     * data : {"ticket":"OoApecY13khgDlC0S7n6LEOha577UW3d"}
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
         * ticket : OoApecY13khgDlC0S7n6LEOha577UW3d
         */

        private String ticket;

        public String getTicket() {
            return ticket;
        }

        public void setTicket(String ticket) {
            this.ticket = ticket;
        }
    }
}
