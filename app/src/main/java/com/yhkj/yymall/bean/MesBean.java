package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/12/14.
 */

public class MesBean {

    /**
     * code : 0
     * msg : 1
     * data : {"info":{"title":"发货消息","content":"您的订单由已由第三方卖家发货，运单号为[88881474]，请您保持电话通畅。","time":"2017-12-08 10:04:46"}}
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
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
