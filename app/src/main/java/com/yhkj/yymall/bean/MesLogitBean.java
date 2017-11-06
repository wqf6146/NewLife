package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */

public class MesLogitBean {

    /**
     * code : 0
     * msg : 1
     * data : {"list":[{"type":3,"name":"儿童4~8岁自行车","orderId":514,"img":"http://yiyiyaya-oss.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/25/20170725165748198.jpg","status":1,"time":"2017-08-12 11:56:52","deliveryCode":"1111111","freightCompany":"圆通速递"},{"type":2,"name":"儿童4~8岁自行车","orderId":514,"img":"http://yiyiyaya-oss.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/25/20170725165748198.jpg","status":1,"time":"2017-08-12 11:55:50","deliveryCode":"1111111","freightCompany":"圆通速递"}]}
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
             * type : 3
             * name : 儿童4~8岁自行车
             * orderId : 514
             * img : http://yiyiyaya-oss.oss-cn-zhangjiakou.aliyuncs.com/upload/2017/07/25/20170725165748198.jpg
             * status : 1
             * time : 2017-08-12 11:56:52
             * deliveryCode : 1111111
             * freightCompany : 圆通速递
             */

            private int type;
            private String name;
            private int orderId;
            private String img;
            private int status;
            private String time;
            private String deliveryCode;
            private String freightCompany;

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getOrderId() {
                return orderId;
            }

            public void setOrderId(int orderId) {
                this.orderId = orderId;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getDeliveryCode() {
                return deliveryCode;
            }

            public void setDeliveryCode(String deliveryCode) {
                this.deliveryCode = deliveryCode;
            }

            public String getFreightCompany() {
                return freightCompany;
            }

            public void setFreightCompany(String freightCompany) {
                this.freightCompany = freightCompany;
            }
        }
    }
}
