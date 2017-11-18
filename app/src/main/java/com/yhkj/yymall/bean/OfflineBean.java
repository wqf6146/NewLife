package com.yhkj.yymall.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/11/13.
 */

public class OfflineBean {

    /**
     * code : 0
     * msg : 1
     * data : {"pop":{"img":"http://oss.yiyiyaya.cc/upload/2017/11/09/20171109044952206.jpg","link":""},"float":{"img":"http://oss.yiyiyaya.cc/upload/2017/11/09/20171109044956207.jpg","link":""},"hasJoin":0}
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
         * pop : {"img":"http://oss.yiyiyaya.cc/upload/2017/11/09/20171109044952206.jpg","link":""}
         * float : {"img":"http://oss.yiyiyaya.cc/upload/2017/11/09/20171109044956207.jpg","link":""}
         * hasJoin : 0
         */

        private PopBean pop;
        @SerializedName("float")
        private FloatBean floatX;
        private int hasJoin;

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public PopBean getPop() {
            return pop;
        }

        public void setPop(PopBean pop) {
            this.pop = pop;
        }

        public FloatBean getFloatX() {
            return floatX;
        }

        public void setFloatX(FloatBean floatX) {
            this.floatX = floatX;
        }

        public int getHasJoin() {
            return hasJoin;
        }

        public void setHasJoin(int hasJoin) {
            this.hasJoin = hasJoin;
        }

        public static class PopBean {
            /**
             * img : http://oss.yiyiyaya.cc/upload/2017/11/09/20171109044952206.jpg
             * link :
             */

            private String img;
            private String link;

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }
        }

        public static class FloatBean {
            /**
             * img : http://oss.yiyiyaya.cc/upload/2017/11/09/20171109044956207.jpg
             * link :
             */

            private String img;
            private String link;

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }
        }
    }
}
