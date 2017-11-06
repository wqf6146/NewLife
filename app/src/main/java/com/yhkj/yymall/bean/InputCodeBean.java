package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/8/14.
 */

public class InputCodeBean {

    /**
     * code : 0
     * msg : 1
     * data : {"desc":"恭喜您获取5个丫丫"}
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
         * desc : 恭喜您获取5个丫丫
         */

        private String desc;
        private int yaya;

        public void setYaya(int yaya) {
            this.yaya = yaya;
        }

        public int getYaya() {
            return yaya;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
