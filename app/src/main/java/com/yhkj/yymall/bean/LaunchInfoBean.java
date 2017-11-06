package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/9/13.
 */

public class LaunchInfoBean {

    /**
     * code : 0
     * msg : 1
     * data : {"logo":"http://oss.yiyiyaya.cc/upload/2017/07/03/20170703212655278.jpg","startmap":"http://oss.yiyiyaya.cc/upload/2017/07/08/20170708173548922.jpg"}
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
         * logo : http://oss.yiyiyaya.cc/upload/2017/07/03/20170703212655278.jpg
         * startmap : http://oss.yiyiyaya.cc/upload/2017/07/08/20170708173548922.jpg
         */

        private String logo;
        private String startmap;

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getStartmap() {
            return startmap;
        }

        public void setStartmap(String startmap) {
            this.startmap = startmap;
        }
    }
}
