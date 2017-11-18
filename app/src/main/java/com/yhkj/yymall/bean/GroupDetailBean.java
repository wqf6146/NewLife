package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 */

public class GroupDetailBean {


    /**
     * code : 0
     * msg : 1
     * data : {"goodsName":"测试2","createTime":"2017-07-24 10:06:39","orderStatus":2,"headIco":["http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/"],"status":1,"time":1}
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
         * goodsName : 测试2
         * createTime : 2017-07-24 10:06:39
         * orderStatus : 2
         * headIco : ["http://yiyiyaya.oss-cn-zhangjiakou.aliyuncs.com/"]
         * status : 1
         * time : 1
         */

        private String goodsName;
        private String createTime;
        private int orderStatus;
        private int status;
        private int time;
        private List<String> headIco;

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public List<String> getHeadIco() {
            return headIco;
        }

        public void setHeadIco(List<String> headIco) {
            this.headIco = headIco;
        }
    }
}
