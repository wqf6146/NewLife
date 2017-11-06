package com.yhkj.yymall.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/28.
 */

public class LeaseClassifyBean {

    /**
     * code : 0
     * msg : 1
     * data : {"brand":[{"id":"1","name":"阿迪达斯"}],"attrs":[{"id":"3","name":"产地","value":["国产"]}]}
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
        private List<BrandBean> brand;
        private List<AttrsBean> attrs;

        public List<BrandBean> getBrand() {
            return brand;
        }

        public void setBrand(List<BrandBean> brand) {
            this.brand = brand;
        }

        public List<AttrsBean> getAttrs() {
            return attrs;
        }

        public void setAttrs(List<AttrsBean> attrs) {
            this.attrs = attrs;
        }

        public static class BrandBean {
            /**
             * id : 1
             * name : 阿迪达斯
             */

            private String id;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class AttrsBean {
            /**
             * id : 3
             * name : 产地
             * value : ["国产"]
             */

            private String id;
            private String name;
            private List<String> value;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<String> getValue() {
                return value;
            }

            public void setValue(List<String> value) {
                this.value = value;
            }
        }
    }
}
