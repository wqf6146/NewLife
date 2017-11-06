package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/7/24.
 */

public class DaySignBeab {

    /**
     * code : 0
     * msg : 111
     * data : {"data":"签到成功获得7点积分"}
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
         * data : 签到成功获得7点积分
         */

        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
