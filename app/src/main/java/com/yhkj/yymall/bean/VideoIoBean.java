package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/12/20.
 */

public class VideoIoBean {

    /**
     * code : 0
     * msg : 1
     * data : {"restHandleSecond":-1,"nextHandleSecond":-1,"img_valid":0}
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
         * restHandleSecond : -1
         * nextHandleSecond : -1
         * img_valid : 0
         */

        private int restHandleSecond;
        private int nextHandleSecond;
        private int handleTime;
        private int img_valid = -1;
        private int num;

        public void setNum(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }

        public void setHandleTime(int handleTime) {
            this.handleTime = handleTime;
        }

        public int getHandleTime() {
            return handleTime;
        }

        public int getRestHandleSecond() {
            return restHandleSecond;
        }

        public void setRestHandleSecond(int restHandleSecond) {
            this.restHandleSecond = restHandleSecond;
        }

        public int getNextHandleSecond() {
            return nextHandleSecond;
        }

        public void setNextHandleSecond(int nextHandleSecond) {
            this.nextHandleSecond = nextHandleSecond;
        }

        public int getImg_valid() {
            return img_valid;
        }

        public void setImg_valid(int img_valid) {
            this.img_valid = img_valid;
        }
    }
}
