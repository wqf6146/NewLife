package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/10/21.
 */

public class DailyHeadBean {

    /**
     * code : 0
     * msg : 1
     * data : {"dailyActive":{"id":2,"name":"测试2","home_img":"http://oss.yiyiyaya.cc/upload/2017/10/19/20171019013618974.jpg","active_img":"http://oss.yiyiyaya.cc/upload/2017/10/19/20171019013623959.jpg"}}
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
         * dailyActive : {"id":2,"name":"测试2","home_img":"http://oss.yiyiyaya.cc/upload/2017/10/19/20171019013618974.jpg","active_img":"http://oss.yiyiyaya.cc/upload/2017/10/19/20171019013623959.jpg"}
         */

        private DailyActiveBean dailyActive;

        public DailyActiveBean getDailyActive() {
            return dailyActive;
        }

        public void setDailyActive(DailyActiveBean dailyActive) {
            this.dailyActive = dailyActive;
        }

        public static class DailyActiveBean {
            /**
             * id : 2
             * name : 测试2
             * home_img : http://oss.yiyiyaya.cc/upload/2017/10/19/20171019013618974.jpg
             * active_img : http://oss.yiyiyaya.cc/upload/2017/10/19/20171019013623959.jpg
             */

            private int id;
            private String name;
            private String home_img;
            private String active_img;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getHome_img() {
                return home_img;
            }

            public void setHome_img(String home_img) {
                this.home_img = home_img;
            }

            public String getActive_img() {
                return active_img;
            }

            public void setActive_img(String active_img) {
                this.active_img = active_img;
            }
        }
    }
}
