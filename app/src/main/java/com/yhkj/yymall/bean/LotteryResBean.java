package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/7/13.
 */

public class LotteryResBean {

    /**
     * code : 0
     * msg : 1
     * data : {"result":{"prizeid":7,"type":"empty","point":1086,"info":{"img":"http://oss.yiyiyaya.cc/upload/2017/08/08/20170808044328686.jpg","name":"谢谢参与"}}}
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
         * result : {"prizeid":7,"type":"empty","point":1086,"info":{"img":"http://oss.yiyiyaya.cc/upload/2017/08/08/20170808044328686.jpg","name":"谢谢参与"}}
         */

        private ResultBean result;

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public static class ResultBean {
            /**
             * prizeid : 7
             * type : empty
             * point : 1086
             * info : {"img":"http://oss.yiyiyaya.cc/upload/2017/08/08/20170808044328686.jpg","name":"谢谢参与"}
             */

            private int prizeid;
            private String type;
            private int point;
            private InfoBean info;

            public int getPrizeid() {
                return prizeid;
            }

            public void setPrizeid(int prizeid) {
                this.prizeid = prizeid;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getPoint() {
                return point;
            }

            public void setPoint(int point) {
                this.point = point;
            }

            public InfoBean getInfo() {
                return info;
            }

            public void setInfo(InfoBean info) {
                this.info = info;
            }

            public static class InfoBean {
                /**
                 * img : http://oss.yiyiyaya.cc/upload/2017/08/08/20170808044328686.jpg
                 * name : 谢谢参与
                 */

                private String img;
                private String name;

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }
    }
}
