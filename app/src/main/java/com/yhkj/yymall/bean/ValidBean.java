package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/11/17.
 */

public class ValidBean {

    /**
     * code : 0
     * msg : 1
     * data : {"isValid":0}
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
         * isValid : 0
         */

        private int isValid;

        public int getIsValid() {
            return isValid;
        }

        public void setIsValid(int isValid) {
            this.isValid = isValid;
        }
    }
}
