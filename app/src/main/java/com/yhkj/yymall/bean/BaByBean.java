package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/8/24.
 */

public class BaByBean {

    /**
     * code : 0
     * msg : 1
     * data : {"info":{"id":11,"sex":"男","brithday":"0000-00-00","type":1}}
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
         * info : {"id":11,"sex":"男","brithday":"0000-00-00","type":1}
         */

        private InfoBean info;

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public static class InfoBean {
            /**
             * id : 11
             * sex : 男
             * brithday : 0000-00-00
             * type : 1
             */

            private int id;
            private String sex;
            private String brithday;
            private int type;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getBrithday() {
                return brithday;
            }

            public void setBrithday(String brithday) {
                this.brithday = brithday;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
