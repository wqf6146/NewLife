package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/8/9.
 */

public class InviteCodeBean {

    /**
     * code : 0
     * msg : 1
     * data : {"share_code":"321126","nub":1000}
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
         * share_code : 321126
         * nub : 1000
         */

        private String share_code;
        private int nub;

        public String getShare_code() {
            return share_code;
        }

        public void setShare_code(String share_code) {
            this.share_code = share_code;
        }

        public int getNub() {
            return nub;
        }

        public void setNub(int nub) {
            this.nub = nub;
        }
    }
}
