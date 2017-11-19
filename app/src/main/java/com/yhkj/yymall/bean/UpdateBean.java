package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/10/18.
 */

public class UpdateBean {

    /**
     * code : 0
     * msg : 1
     * data : {"info":{"version":"1.0.1","versionNo":"1","versionDescription":"1","versionUrl":"https://api.yiyiyaya.cc/#1","md5":"08:01:12:E6:4E:FF:9D:4B:9A:6F:CD:73:7A:CD:63:FF","versionLowest":"1.1.4"}}
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
         * info : {"version":"1.0.1","versionNo":"1","versionDescription":"1","versionUrl":"https://api.yiyiyaya.cc/#1","md5":"08:01:12:E6:4E:FF:9D:4B:9A:6F:CD:73:7A:CD:63:FF","versionLowest":"1.1.4"}
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
             * version : 1.0.1
             * versionNo : 1
             * versionDescription : 1
             * versionUrl : https://api.yiyiyaya.cc/#1
             * md5 : 08:01:12:E6:4E:FF:9D:4B:9A:6F:CD:73:7A:CD:63:FF
             * versionLowest : 1.1.4
             */

            private String version;
            private String versionNo;
            private String versionDescription;
            private String versionUrl;
            private String md5;
            private String versionLowest;

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            public String getVersionNo() {
                return versionNo;
            }

            public void setVersionNo(String versionNo) {
                this.versionNo = versionNo;
            }

            public String getVersionDescription() {
                return versionDescription;
            }

            public void setVersionDescription(String versionDescription) {
                this.versionDescription = versionDescription;
            }

            public String getVersionUrl() {
                return versionUrl;
            }

            public void setVersionUrl(String versionUrl) {
                this.versionUrl = versionUrl;
            }

            public String getMd5() {
                return md5;
            }

            public void setMd5(String md5) {
                this.md5 = md5;
            }

            public String getVersionLowest() {
                return versionLowest;
            }

            public void setVersionLowest(String versionLowest) {
                this.versionLowest = versionLowest;
            }
        }
    }
}
