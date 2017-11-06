package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/8/3.
 */

public class UpdateOrderAddressBean {
    /**
     * code : 0
     * msg : 1
     * data : {"accept_name":"aaa","province":130000,"city":130300,"area":130304,"address":"测试","mobile":"15028267482","contetn":"河北省秦皇岛市北戴河区"}
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
         * accept_name : aaa
         * province : 130000
         * city : 130300
         * area : 130304
         * address : 测试
         * mobile : 15028267482
         * contetn : 河北省秦皇岛市北戴河区
         */

        private String accept_name;
        private int province;
        private int city;
        private int area;
        private String address;
        private String mobile;
        private String content;

        public String getAccept_name() {
            return accept_name;
        }

        public void setAccept_name(String accept_name) {
            this.accept_name = accept_name;
        }

        public int getProvince() {
            return province;
        }

        public void setProvince(int province) {
            this.province = province;
        }

        public int getCity() {
            return city;
        }

        public void setCity(int city) {
            this.city = city;
        }

        public int getArea() {
            return area;
        }

        public void setArea(int area) {
            this.area = area;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getContetn() {
            return content;
        }

        public void setContetn(String content) {
            this.content = content;
        }
    }
}
